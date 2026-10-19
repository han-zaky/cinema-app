import { useEffect, useState } from "react";
import type { CrudApi } from "../api/crud";

export function useAdminResource<T, Req>(api: CrudApi<T, Req>) {
  const [items, setItems] = useState<T[]>([]);
  const [isLoading, setIsLoading] = useState(true);
  const [error, setError] = useState<string | null>(null);
  const [version, setVersion] = useState(0); // bump to refetch

  useEffect(() => {
    let active = true;
    api
      .list()
      .then((data) => {
        if (!active) return;
        setItems(data);
        setError(null);
      })
      .catch((err) => {
        if (active)
          setError(err instanceof Error ? err.message : "Unknown error");
      })
      .finally(() => {
        if (active) setIsLoading(false);
      });
    return () => {
      active = false;
    };
  }, [api, version]);

  // runs a mutation, refetches the list, returns whether it succeeded
  async function mutate(action: () => Promise<unknown>): Promise<boolean> {
    try {
      await action();
      setVersion((v) => v + 1);
      return true;
    } catch (err) {
      setError(err instanceof Error ? err.message : "Unknown error");
      return false;
    }
  }

  return {
    items,
    isLoading,
    error,
    create: (body: Req) => mutate(() => api.create(body)),
    update: (id: number, body: Req) => mutate(() => api.update(id, body)),
    remove: (id: number) => mutate(() => api.remove(id)),
  };
}

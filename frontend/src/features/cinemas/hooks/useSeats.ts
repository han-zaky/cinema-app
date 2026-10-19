import { useEffect, useState } from "react";
import { seatAdminApi } from "../api/cinemaApi";
import type { GenerateSeatsRequest, Seat } from "../types/cinema";

export function useSeats(hallId: number | null) {
  const [loaded, setLoaded] = useState<{ key: string; seats: Seat[] }>({
    key: "",
    seats: [],
  });
  const [error, setError] = useState<string | null>(null);
  const [version, setVersion] = useState(0); // bump to refetch

  const key = `${hallId}:${version}`;

  useEffect(() => {
    if (hallId === null) return;
    let active = true;
    seatAdminApi
      .listByHall(hallId)
      .then((seats) => {
        if (!active) return;
        setLoaded({ key, seats });
        setError(null);
      })
      .catch((err) => {
        if (!active) return;
        setError(err instanceof Error ? err.message : "Unknown error");
        setLoaded({ key, seats: [] });
      });
    return () => {
      active = false;
    };
  }, [hallId, key]);

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
    seats: hallId === null ? [] : loaded.seats,
    isLoading: hallId !== null && loaded.key !== key,
    error,
    generate: (body: GenerateSeatsRequest) =>
      mutate(() => seatAdminApi.generate(body)),
    remove: (id: number) => mutate(() => seatAdminApi.remove(id)),
  };
}

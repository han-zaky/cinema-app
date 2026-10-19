import { useEffect, useState } from "react";
import { screeningAdminApi } from "../api/screeningApi";
import type { Screening } from "../types/screening";

export function useScreenings() {
  const [screenings, setScreenings] = useState<Screening[]>([]);
  const [isLoading, setIsLoading] = useState<boolean>(true);
  const [error, setError] = useState<string | null>(null);

  useEffect(() => {
    let cancelled = false;

    screeningAdminApi
      .list()
      .then((data) => {
        if (!cancelled) setScreenings(data);
      })
      .catch(() => {
        if (!cancelled) setError("Nepodařilo se načíst promítání");
      })
      .finally(() => {
        if (!cancelled) setIsLoading(false);
      });

    return () => {
      cancelled = true;
    };
  }, []);

  return { screenings, isLoading, error };
}

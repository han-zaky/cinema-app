import { useEffect, useMemo, useState } from "react";
import type { Seat } from "../../cinemas/types/cinema";
import { fetchTakenSeats, fetchSeats } from "../api/screeningApi";

const MAX_SEATS = 8;

export type SeatStatus = "TAKEN" | "SELECTED" | "FREE";

export function useSeatSelect(screeningId: number) {
  const [seats, setSeats] = useState<Seat[]>([]);
  const [taken, setTaken] = useState<number[]>([]);
  const [selected, setSelected] = useState<number[]>([]);
  const [isLoading, setIsLoading] = useState<boolean>(true);
  const [error, setError] = useState<string | null>(null);
  const [reloadKey, setReloadKey] = useState<number>(0);

  useEffect(() => {
    let cancelled = false; //ochrana proti zmizení

    setIsLoading(true);
    setError(null);

    Promise.all([fetchSeats(screeningId), fetchTakenSeats(screeningId)])
      .then(([seatsData, takenIds]) => {
        if (cancelled) return;
        setSeats(seatsData);
        setTaken(takenIds);

        //vyhodí ze selectu ty, které teď někdo jiný rezervoval
        setSelected((prev) => prev.filter((id) => !takenIds.includes(id)));
      })
      .catch(() => {
        if (!cancelled) setError("Nepodařilo se načíst");
      })
      .finally(() => {
        if (!cancelled) setIsLoading(false);
      });

    return () => {
      cancelled = true;
    };
  }, [screeningId, reloadKey]);

  //sestavení gridu

  const takenSet = useMemo(() => new Set(taken), [taken]);
  const selectedSet = useMemo(() => new Set(selected), [selected]);

  const selectedSeats = useMemo(
    () => seats.filter((s) => selectedSet.has(s.id)),
    [seats, selectedSet],
  );

  const gridRows = useMemo(
    () => Math.max(0, ...seats.map((s) => s.gridRow)),
    [seats],
  );
  const gridCols = useMemo(
    () => Math.max(0, ...seats.map((s) => s.gridCol)),
    [seats],
  );

  function toggleSeat(seatId: number) {
    if (takenSet.has(seatId)) return; // někdo zarezervoval

    setSelected((prev) => {
      if (prev.includes(seatId)) return prev.filter((id) => id !== seatId);
      if (prev.length >= MAX_SEATS) return prev;
      return [...prev, seatId];
    });
  }

  function clearSelection() {
    setSelected([]);
  }

  function getStatus(seatId: number): SeatStatus {
    if (takenSet.has(seatId)) return "TAKEN";
    if (selectedSet.has(seatId)) return "SELECTED";
    return "FREE";
  }

  function reload() {
    setReloadKey((k) => k + 1);
  }

  return {
    seats,
    selectedSeats,
    gridRows,
    gridCols,
    isLoading,
    error,
    isMaxReached: selected.length >= MAX_SEATS,
    toggleSeat,
    clearSelection,
    reload,
    getStatus,
  };
}

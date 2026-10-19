import { useEffect } from "react";
import type { Seat } from "../../cinemas/types/cinema";
import { useSeatSelect } from "../hooks/useSeatSelect";

interface SeatSelectProps {
  screeningId: number;
  onSelectionChange?: (seats: Seat[]) => void;
}

const STATUS_LABEL = {
  TAKEN: " (obsazeno)",
  SELECTED: " (vybráno)",
  FREE: "",
};

export function SeatSelect({ screeningId, onSelectionChange }: SeatSelectProps) {
  const {
    seats,
    selectedSeats,
    gridRows,
    gridCols,
    isLoading,
    error,
    isMaxReached,
    toggleSeat,
    clearSelection,
    reload,
    getStatus,
  } = useSeatSelect(screeningId);

  useEffect(() => {
    onSelectionChange?.(selectedSeats);
  }, [selectedSeats, onSelectionChange]);

  if (isLoading) return <p>Načítání…</p>;
  if (error) {
    return (
      <div>
        <p>{error}</p>
        <button type="button" onClick={reload}>
          Zkusit znovu
        </button>
      </div>
    );
  }

  return (
    <div>
      {/* the only styling: place every seat in the hall's grid */}
      <div
        style={{
          display: "grid",
          gridTemplateColumns: `repeat(${gridCols}, auto)`,
          gridTemplateRows: `repeat(${gridRows}, auto)`,
        }}
      >
        {seats.map((seat) => {
          const status = getStatus(seat.id);
          return (
            <button
              key={seat.id}
              type="button"
              disabled={status === "TAKEN" || (isMaxReached && status === "FREE")}
              onClick={() => toggleSeat(seat.id)}
              style={{ gridRow: seat.gridRow, gridColumn: seat.gridCol }}
            >
              {seat.rowLetter}
              {seat.seatNumber} {seat.seatType}
              {STATUS_LABEL[status]}
            </button>
          );
        })}
      </div>

      <p>Vybrané sedačky: {selectedSeats.length}</p>
      {isMaxReached && <p>Dosažen maximální počet sedaček.</p>}
      <button type="button" onClick={clearSelection}>
        Zrušit výběr
      </button>
    </div>
  );
}

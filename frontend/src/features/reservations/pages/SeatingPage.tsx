import { useState } from "react";
import { Link, useNavigate, useParams } from "react-router-dom";
import type { Seat } from "../../cinemas/types/cinema";
import { useScreenings } from "../../screenings/hooks/useScreenings";
import { SeatSelect } from "../../screenings/components/SeatSelect";

export function SeatingPage() {
  const { id } = useParams();
  const screeningId = Number(id);
  const { screenings } = useScreenings();
  const navigate = useNavigate();
  const [selectedSeats, setSelectedSeats] = useState<Seat[]>([]);

  if (!Number.isInteger(screeningId)) return <p>Neplatné promítání.</p>;

  const screening = screenings.find((s) => s.id === screeningId);

  return (
    <div>
      <Link to="/program">← Zpět na program</Link>
      <h1>Výběr sedaček</h1>
      {screening && (
        <p>
          {screening.movieTitle},{" "}
          {new Date(screening.startTime).toLocaleString("cs-CZ")}
        </p>
      )}

      <SeatSelect
        screeningId={screeningId}
        onSelectionChange={setSelectedSeats}
      />

      {screening && selectedSeats.length > 0 && (
        <p>
          {selectedSeats.map((s) => `${s.rowLetter}${s.seatNumber}`).join(", ")}
          {" – "}
          {selectedSeats.length * screening.basePrice} Kč
        </p>
      )}

      <button
        type="button"
        disabled={selectedSeats.length === 0}
        onClick={() =>
          navigate(`/screenings/${screeningId}/order`, {
            state: { seats: selectedSeats },
          })
        }
      >
        Pokračovat
      </button>
    </div>
  );
}

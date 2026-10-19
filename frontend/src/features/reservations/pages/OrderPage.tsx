import { Link, Navigate, useLocation, useParams } from "react-router-dom";
import type { Seat } from "../../cinemas/types/cinema";
import { useScreenings } from "../../screenings/hooks/useScreenings";
import type { Screening } from "../../screenings/types/screening";
import { useOrder } from "../hooks/useOrder";
import { TICKET_TYPES, ticketPrice, type TicketType } from "../types/ticket";

interface OrderListProps {
  screening: Screening;
  initialSeats: Seat[];
}

function OrderList({ screening, initialSeats }: OrderListProps) {
  const { seats, getTicketType, setTicketType, removeSeat, total } = useOrder(
    initialSeats,
    screening.basePrice,
  );

  if (seats.length === 0) {
    return (
      <p>
        Žádné vybrané sedačky.{" "}
        <Link to={`/screenings/${screening.id}/seats`}>Vybrat sedačky</Link>
      </p>
    );
  }

  return (
    <div>
      <ul>
        {seats.map((seat) => {
          const type = getTicketType(seat.id);
          return (
            <li key={seat.id}>
              <strong>
                {seat.rowLetter}
                {seat.seatNumber}
              </strong>{" "}
              <select
                value={type}
                onChange={(e) =>
                  setTicketType(seat.id, e.target.value as TicketType)
                }
              >
                {(Object.keys(TICKET_TYPES) as TicketType[]).map((t) => (
                  <option key={t} value={t}>
                    {TICKET_TYPES[t].label} (
                    {ticketPrice(screening.basePrice, t)} Kč)
                  </option>
                ))}
              </select>{" "}
              {ticketPrice(screening.basePrice, type)} Kč{" "}
              <button type="button" onClick={() => removeSeat(seat.id)}>
                Odebrat
              </button>
            </li>
          );
        })}
      </ul>

      <p>
        <strong>Celkem: {total} Kč</strong>
      </p>
      {/* next step (login / guest reservation / payment) comes later */}
    </div>
  );
}

export function OrderPage() {
  const { id } = useParams();
  const screeningId = Number(id);
  const location = useLocation();
  const { screenings, isLoading, error } = useScreenings();

  const seats = (location.state as { seats?: Seat[] } | null)?.seats;

  // opened directly (no selection) -> pick seats first
  if (!seats) return <Navigate to={`/screenings/${id}/seats`} replace />;

  const screening = screenings.find((s) => s.id === screeningId);

  return (
    <div>
      <Link to={`/screenings/${id}/seats`}>← Zpět na výběr sedaček</Link>
      <h1>Objednávka</h1>

      {isLoading && <p>Načítání…</p>}
      {error && <p>{error}</p>}
      {screening && (
        <>
          <p>
            {screening.movieTitle},{" "}
            {new Date(screening.startTime).toLocaleString("cs-CZ")}
          </p>
          <OrderList screening={screening} initialSeats={seats} />
        </>
      )}
    </div>
  );
}

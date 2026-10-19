import { useMemo, useState } from "react";
import type { Seat } from "../../cinemas/types/cinema";
import { ticketPrice, type TicketType } from "../types/ticket";

export function useOrder(initialSeats: Seat[], basePrice: number) {
  const [seats, setSeats] = useState<Seat[]>(initialSeats);
  const [ticketTypes, setTicketTypes] = useState<Record<number, TicketType>>(
    {},
  );

  const getTicketType = (seatId: number): TicketType =>
    ticketTypes[seatId] ?? "ADULT";

  function setTicketType(seatId: number, type: TicketType) {
    setTicketTypes((prev) => ({ ...prev, [seatId]: type }));
  }

  function removeSeat(seatId: number) {
    setSeats((prev) => prev.filter((s) => s.id !== seatId));
  }

  const total = useMemo(
    () =>
      seats.reduce(
        (sum, s) => sum + ticketPrice(basePrice, ticketTypes[s.id] ?? "ADULT"),
        0,
      ),
    [seats, ticketTypes, basePrice],
  );

  return { seats, getTicketType, setTicketType, removeSeat, total };
}

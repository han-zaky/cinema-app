// mirrors backend TicketType multipliers
export type TicketType = "ADULT" | "STUDENT" | "CHILD" | "SENIOR";

export const TICKET_TYPES: Record<
  TicketType,
  { label: string; multiplier: number }
> = {
  ADULT: { label: "Dospělý", multiplier: 1 },
  STUDENT: { label: "Student", multiplier: 0.75 },
  CHILD: { label: "Dítě", multiplier: 0.5 },
  SENIOR: { label: "Senior", multiplier: 0.75 },
};

export function ticketPrice(basePrice: number, type: TicketType): number {
  return Math.round(basePrice * TICKET_TYPES[type].multiplier * 100) / 100;
}

import { apiFetchJson } from "../../../shared/api/client";
import { crud } from "../../../shared/api/crud";
import type { Seat } from "../../cinemas/types/cinema";
import type { Screening, ScreeningRequest } from "../types/screening";

export async function fetchTakenSeats(screeningId: number): Promise<number[]> {
  const res = await apiFetchJson<{ seats: number[] }>(
    `/api/screenings/${screeningId}/taken-seats`,
  );
  return res.seats;
}

// all seats of the hall this screening runs in
export function fetchSeats(screeningId: number): Promise<Seat[]> {
  return apiFetchJson<Seat[]>(`/api/screenings/${screeningId}/seats`);
}

// backend has no screening update endpoint -> update is unused
export const screeningAdminApi = crud<Screening, ScreeningRequest>(
  "/api/screenings",
  "/api/admin/screenings",
);

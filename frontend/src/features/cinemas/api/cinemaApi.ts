import { crud, request } from "../../../shared/api/crud";
import type {
  Cinema,
  CinemaRequest,
  GenerateSeatsRequest,
  Hall,
  HallRequest,
  Seat,
} from "../types/cinema";

export const cinemaAdminApi = crud<Cinema, CinemaRequest>(
  "/api/cinemas",
  "/api/admin/cinemas",
);
export const hallAdminApi = crud<Hall, HallRequest>(
  "/api/halls",
  "/api/admin/halls",
);

export const seatAdminApi = {
  listByHall: (hallId: number) =>
    request<Seat[]>(`/api/seats?hallId=${hallId}`, "GET"),
  generate: (body: GenerateSeatsRequest) =>
    request<Seat[]>("/api/admin/seats/generate", "POST", body),
  remove: (id: number) => request<void>(`/api/admin/seats/${id}`, "DELETE"),
};

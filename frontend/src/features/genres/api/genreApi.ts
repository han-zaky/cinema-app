import { crud } from "../../../shared/api/crud";
import type { Genre, GenreRequest } from "../types/genre";

export const genreAdminApi = crud<Genre, GenreRequest>(
  "/api/genres",
  "/api/admin/genres",
);

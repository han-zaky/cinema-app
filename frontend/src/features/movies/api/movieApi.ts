import { apiFetchJson } from "../../../shared/api/client";
import { crud } from "../../../shared/api/crud";
import type { Movie, MovieRequest } from "../types/movie";

export async function fetchMovies(): Promise<Movie[]> {
  return apiFetchJson<Movie[]>("/api/movies");
}

export const movieAdminApi = crud<Movie, MovieRequest>(
  "/api/movies",
  "/api/admin/movies",
);

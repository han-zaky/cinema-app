import type { Movie } from "../types/movie";

const API_URL = "http://localhost:8080/api/movies";

export async function fetchMovies(): Promise<Movie[]> {
  const response = await fetch(API_URL);

  if (!response.ok) {
    throw new Error("Couldn't fetch movies.");
  }

  return response.json();
}

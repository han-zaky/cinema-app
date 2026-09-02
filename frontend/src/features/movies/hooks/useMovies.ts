import { fetchMovies } from "../api/movieApi";
import { useState, useEffect } from "react";
import type { Movie } from "../types/movie";

export function useMovies() {
  const [movies, setMovies] = useState<Movie[]>([]);
  const [isLoading, setIsLoading] = useState<boolean>(true);
  const [error, setError] = useState<string | null>(null);

  useEffect(() => {
    let isMounted = true; // Prevence race condition/update unmounted komponenty

    async function loadMovies() {
      try {
        setIsLoading(true);
        const data = await fetchMovies();
        if (isMounted) {
          setMovies(data);
        }
      } catch (err) {
        if (isMounted) {
          setError(err instanceof Error ? err.message : "Neznámá chyba");
        }
      } finally {
        if (isMounted) {
          setIsLoading(false);
        }
      }
    }

    loadMovies();

    return () => {
      isMounted = false; // Cleanup funkce
    };
  }, []);

  return { movies, isLoading, error };
}

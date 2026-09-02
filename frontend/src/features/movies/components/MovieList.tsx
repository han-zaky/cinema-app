// src/features/movies/pages/MoviesPage.tsx
import { useMovies } from "../hooks/useMovies";
import { MovieCard } from "./MovieCard";

export function MoviesList() {
  const { movies, isLoading, error } = useMovies();

  if (isLoading) {
    return <div>Načítám aktuální filmy...</div>;
  }

  if (error) {
    return <div style={{ color: "red" }}>Chyba: {error}</div>;
  }

  return (
    <div style={{ padding: "20px" }}>
      <h1>Aktuálně promítané filmy</h1>
      {movies.map((movie) => (
        <MovieCard key={movie.id} movie={movie} />
      ))}
    </div>
  );
}

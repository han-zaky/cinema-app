// src/features/movies/components/MovieCard.tsx
import type { MovieCardProps } from "../types/movie";

export function MovieCard({ movie }: MovieCardProps) {
  return (
    <div
      style={{
        border: "1px solid #ccc",
        borderRadius: "8px",
        padding: "16px",
        width: "200px",
      }}
    >
      <img
        src={movie.posterUrl}
        alt={movie.title}
        style={{
          width: "100%",
          height: "280px",
          objectFit: "cover",
          borderRadius: "4px",
        }}
      />
      <h3>{movie.title}</h3>
      <p>
        <strong>Délka:</strong> {movie.durationMinutes} min
      </p>
      <p>{movie.description}</p>
    </div>
  );
}

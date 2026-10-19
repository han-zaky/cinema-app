import { useState, type FormEvent } from "react";
import type { Movie } from "../../movies/types/movie";
import type { Hall } from "../../cinemas/types/cinema";
import type { ScreeningRequest } from "../types/screening";

interface Props {
  movies: Movie[];
  halls: Hall[];
  onSubmit: (values: ScreeningRequest) => Promise<boolean>;
  onCancel: () => void;
}

// screenings can only be created/deleted (no update endpoint)
export function ScreeningForm({ movies, halls, onSubmit, onCancel }: Props) {
  const [movieId, setMovieId] = useState("");
  const [hallId, setHallId] = useState("");
  const [startTime, setStartTime] = useState(""); // datetime-local value
  const [basePrice, setBasePrice] = useState("");

  async function handleSubmit(e: FormEvent) {
    e.preventDefault();
    await onSubmit({
      movieId: Number(movieId),
      hallId: Number(hallId),
      startTime: new Date(startTime).toISOString(),
      basePrice: Number(basePrice),
    });
  }

  return (
    <form onSubmit={handleSubmit}>
      <select
        value={movieId}
        onChange={(e) => setMovieId(e.target.value)}
        required
      >
        <option value="">Select movie</option>
        {movies.map((m) => (
          <option key={m.id} value={m.id}>
            {m.title}
          </option>
        ))}
      </select>
      <select
        value={hallId}
        onChange={(e) => setHallId(e.target.value)}
        required
      >
        <option value="">Select hall</option>
        {halls.map((h) => (
          <option key={h.id} value={h.id}>
            {h.name}
          </option>
        ))}
      </select>
      <input
        type="datetime-local"
        value={startTime}
        onChange={(e) => setStartTime(e.target.value)}
        required
      />
      <input
        type="number"
        min={0}
        step="0.01"
        placeholder="Base price"
        value={basePrice}
        onChange={(e) => setBasePrice(e.target.value)}
        required
      />
      <button type="submit">Save</button>
      <button type="button" onClick={onCancel}>
        Cancel
      </button>
    </form>
  );
}

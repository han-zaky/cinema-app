import { useState, type FormEvent } from "react";
import type { Movie } from "../types/movie";
import type { MovieRequest } from "../types/movie";

interface Props {
  initial?: Movie;
  onSubmit: (values: MovieRequest) => Promise<boolean>;
  onCancel: () => void;
}

export function MovieForm({ initial, onSubmit, onCancel }: Props) {
  const [title, setTitle] = useState(initial?.title ?? "");
  const [description, setDescription] = useState(initial?.description ?? "");
  const [duration, setDuration] = useState(
    initial ? String(initial.durationMinutes) : "",
  );
  const [posterUrl, setPosterUrl] = useState(initial?.posterUrl ?? "");
  const [genres, setGenres] = useState((initial?.genres ?? []).join(", "));

  async function handleSubmit(e: FormEvent) {
    e.preventDefault();
    await onSubmit({
      title,
      description,
      durationMinutes: Number(duration),
      posterUrl,
      genres: genres
        .split(",")
        .map((g) => g.trim())
        .filter(Boolean),
    });
  }

  return (
    <form onSubmit={handleSubmit}>
      <input
        placeholder="Title"
        value={title}
        onChange={(e) => setTitle(e.target.value)}
        required
      />
      <input
        placeholder="Description"
        value={description}
        onChange={(e) => setDescription(e.target.value)}
      />
      <input
        type="number"
        min={1}
        placeholder="Duration (min)"
        value={duration}
        onChange={(e) => setDuration(e.target.value)}
        required
      />
      <input
        placeholder="Poster URL"
        value={posterUrl}
        onChange={(e) => setPosterUrl(e.target.value)}
      />
      <input
        placeholder="Genres (comma separated)"
        value={genres}
        onChange={(e) => setGenres(e.target.value)}
      />
      <button type="submit">Save</button>
      <button type="button" onClick={onCancel}>
        Cancel
      </button>
    </form>
  );
}

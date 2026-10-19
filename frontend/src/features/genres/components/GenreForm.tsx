import { useState, type FormEvent } from "react";
import type { Genre, GenreRequest } from "../types/genre";

interface Props {
  initial?: Genre;
  onSubmit: (values: GenreRequest) => Promise<boolean>;
  onCancel: () => void;
}

export function GenreForm({ initial, onSubmit, onCancel }: Props) {
  const [name, setName] = useState(initial?.name ?? "");

  async function handleSubmit(e: FormEvent) {
    e.preventDefault();
    await onSubmit({ name });
  }

  return (
    <form onSubmit={handleSubmit}>
      <input
        placeholder="Name"
        value={name}
        onChange={(e) => setName(e.target.value)}
        required
      />
      <button type="submit">Save</button>
      <button type="button" onClick={onCancel}>
        Cancel
      </button>
    </form>
  );
}

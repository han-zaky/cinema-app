import { useState, type FormEvent } from "react";
import type { Cinema, Hall, HallRequest } from "../types/cinema";

interface Props {
  initial?: Hall;
  cinemas: Cinema[];
  onSubmit: (values: HallRequest) => Promise<boolean>;
  onCancel: () => void;
}

export function HallForm({ initial, cinemas, onSubmit, onCancel }: Props) {
  const [name, setName] = useState(initial?.name ?? "");
  const [cinemaId, setCinemaId] = useState(
    initial ? String(initial.cinemaId) : "",
  );

  async function handleSubmit(e: FormEvent) {
    e.preventDefault();
    await onSubmit({ name, cinemaId: Number(cinemaId) });
  }

  return (
    <form onSubmit={handleSubmit}>
      <input
        placeholder="Name"
        value={name}
        onChange={(e) => setName(e.target.value)}
        required
      />
      <select
        value={cinemaId}
        onChange={(e) => setCinemaId(e.target.value)}
        required
      >
        <option value="">Select cinema</option>
        {cinemas.map((c) => (
          <option key={c.id} value={c.id}>
            {c.name}
          </option>
        ))}
      </select>
      <button type="submit">Save</button>
      <button type="button" onClick={onCancel}>
        Cancel
      </button>
    </form>
  );
}

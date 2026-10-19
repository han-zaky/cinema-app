import { useState, type FormEvent } from "react";
import type { GenerateSeatsRequest } from "../types/cinema";

interface Props {
  hallId: number;
  onSubmit: (values: GenerateSeatsRequest) => Promise<boolean>;
  onCancel: () => void;
}

export function GenerateSeatsForm({ hallId, onSubmit, onCancel }: Props) {
  const [rowCount, setRowCount] = useState("");
  const [seatsPerRow, setSeatsPerRow] = useState("");

  async function handleSubmit(e: FormEvent) {
    e.preventDefault();
    await onSubmit({
      hallId,
      rowCount: Number(rowCount),
      seatsPerRow: Number(seatsPerRow),
    });
  }

  return (
    <form onSubmit={handleSubmit}>
      <input
        type="number"
        min={1}
        placeholder="Rows"
        value={rowCount}
        onChange={(e) => setRowCount(e.target.value)}
        required
      />
      <input
        type="number"
        min={1}
        placeholder="Seats per row"
        value={seatsPerRow}
        onChange={(e) => setSeatsPerRow(e.target.value)}
        required
      />
      <button type="submit">Generate</button>
      <button type="button" onClick={onCancel}>
        Cancel
      </button>
    </form>
  );
}

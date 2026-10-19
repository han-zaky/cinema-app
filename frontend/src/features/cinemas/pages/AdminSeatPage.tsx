import { useState } from "react";
import { hallAdminApi } from "../api/cinemaApi";
import { AdminNav } from "../../../shared/components/AdminNav";
import { AdminTable } from "../../../shared/components/AdminTable";
import { GenerateSeatsForm } from "../components/GenerateSeatsForm";
import { useAdminResource } from "../../../shared/hooks/useAdminResource";
import { useSeats } from "../hooks/useSeats";

// seats are listed per hall; they can only be generated or deleted
export function AdminSeatPage() {
  const halls = useAdminResource(hallAdminApi);
  const [hallId, setHallId] = useState<number | null>(null);
  const [generating, setGenerating] = useState(false);
  const seats = useSeats(hallId);

  return (
    <div>
      <AdminNav />
      <h1>Seats</h1>

      <select
        value={hallId ?? ""}
        onChange={(e) => {
          setHallId(e.target.value ? Number(e.target.value) : null);
          setGenerating(false);
        }}
      >
        <option value="">Select hall</option>
        {halls.items.map((h) => (
          <option key={h.id} value={h.id}>
            {h.name}
          </option>
        ))}
      </select>

      {hallId !== null && (
        <button onClick={() => setGenerating(true)}>Generate seats</button>
      )}

      {hallId !== null && generating && (
        <GenerateSeatsForm
          hallId={hallId}
          onCancel={() => setGenerating(false)}
          onSubmit={async (values) => {
            const ok = await seats.generate(values);
            if (ok) setGenerating(false);
            return ok;
          }}
        />
      )}

      {halls.error && <p>{halls.error}</p>}
      {seats.isLoading && <p>Loading...</p>}
      {seats.error && <p>{seats.error}</p>}

      {hallId !== null && (
        <AdminTable
          rows={seats.seats}
          getKey={(s) => s.id}
          onDelete={(s) => seats.remove(s.id)}
          columns={[
            { header: "ID", render: (s) => s.id },
            { header: "Row", render: (s) => s.rowLetter },
            { header: "Number", render: (s) => s.seatNumber },
            { header: "Type", render: (s) => s.seatType },
          ]}
        />
      )}
    </div>
  );
}

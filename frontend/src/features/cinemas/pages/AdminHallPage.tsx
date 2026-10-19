import { useState } from "react";
import { cinemaAdminApi, hallAdminApi } from "../api/cinemaApi";
import { AdminNav } from "../../../shared/components/AdminNav";
import { AdminTable } from "../../../shared/components/AdminTable";
import { HallForm } from "../components/HallForm";
import { useAdminResource } from "../../../shared/hooks/useAdminResource";
import type { Hall } from "../types/cinema";

export function AdminHallPage() {
  const halls = useAdminResource(hallAdminApi);
  const cinemas = useAdminResource(cinemaAdminApi);
  const [editing, setEditing] = useState<Hall | "new" | null>(null);

  const cinemaName = (id: number) =>
    cinemas.items.find((c) => c.id === id)?.name ?? id;

  return (
    <div>
      <AdminNav />
      <h1>Halls</h1>
      <button onClick={() => setEditing("new")}>Create</button>

      {editing && (
        <HallForm
          key={editing === "new" ? "new" : editing.id}
          initial={editing === "new" ? undefined : editing}
          cinemas={cinemas.items}
          onCancel={() => setEditing(null)}
          onSubmit={async (values) => {
            const ok =
              editing === "new"
                ? await halls.create(values)
                : await halls.update(editing.id, values);
            if (ok) setEditing(null);
            return ok;
          }}
        />
      )}

      {halls.isLoading && <p>Loading...</p>}
      {halls.error && <p>{halls.error}</p>}

      <AdminTable
        rows={halls.items}
        getKey={(h) => h.id}
        onEdit={setEditing}
        onDelete={(h) => halls.remove(h.id)}
        columns={[
          { header: "ID", render: (h) => h.id },
          { header: "Name", render: (h) => h.name },
          { header: "Cinema", render: (h) => cinemaName(h.cinemaId) },
        ]}
      />
    </div>
  );
}

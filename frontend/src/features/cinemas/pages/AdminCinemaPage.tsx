import { useState } from "react";
import { cinemaAdminApi } from "../api/cinemaApi";
import { AdminNav } from "../../../shared/components/AdminNav";
import { AdminTable } from "../../../shared/components/AdminTable";
import { CinemaForm } from "../components/CinemaForm";
import { useAdminResource } from "../../../shared/hooks/useAdminResource";
import type { Cinema } from "../types/cinema";

export function AdminCinemaPage() {
  const cinemas = useAdminResource(cinemaAdminApi);
  const [editing, setEditing] = useState<Cinema | "new" | null>(null);

  return (
    <div>
      <AdminNav />
      <h1>Cinemas</h1>
      <button onClick={() => setEditing("new")}>Create</button>

      {editing && (
        <CinemaForm
          key={editing === "new" ? "new" : editing.id}
          initial={editing === "new" ? undefined : editing}
          onCancel={() => setEditing(null)}
          onSubmit={async (values) => {
            const ok =
              editing === "new"
                ? await cinemas.create(values)
                : await cinemas.update(editing.id, values);
            if (ok) setEditing(null);
            return ok;
          }}
        />
      )}

      {cinemas.isLoading && <p>Loading...</p>}
      {cinemas.error && <p>{cinemas.error}</p>}

      <AdminTable
        rows={cinemas.items}
        getKey={(c) => c.id}
        onEdit={setEditing}
        onDelete={(c) => cinemas.remove(c.id)}
        columns={[
          { header: "ID", render: (c) => c.id },
          { header: "Name", render: (c) => c.name },
          {
            header: "Address",
            render: (c) =>
              `${c.address.street} ${c.address.houseNumber}, ${c.address.zipCode} ${c.address.city}, ${c.address.country}`,
          },
        ]}
      />
    </div>
  );
}

import { useState } from "react";
import { genreAdminApi } from "../api/genreApi";
import { AdminNav } from "../../../shared/components/AdminNav";
import { AdminTable } from "../../../shared/components/AdminTable";
import { GenreForm } from "../components/GenreForm";
import { useAdminResource } from "../../../shared/hooks/useAdminResource";
import type { Genre } from "../types/genre";

export function AdminGenrePage() {
  const genres = useAdminResource(genreAdminApi);
  const [editing, setEditing] = useState<Genre | "new" | null>(null);

  return (
    <div>
      <AdminNav />
      <h1>Genres</h1>
      <button onClick={() => setEditing("new")}>Create</button>

      {editing && (
        <GenreForm
          key={editing === "new" ? "new" : editing.id}
          initial={editing === "new" ? undefined : editing}
          onCancel={() => setEditing(null)}
          onSubmit={async (values) => {
            const ok =
              editing === "new"
                ? await genres.create(values)
                : await genres.update(editing.id, values);
            if (ok) setEditing(null);
            return ok;
          }}
        />
      )}

      {genres.isLoading && <p>Loading...</p>}
      {genres.error && <p>{genres.error}</p>}

      <AdminTable
        rows={genres.items}
        getKey={(g) => g.id}
        onEdit={setEditing}
        onDelete={(g) => genres.remove(g.id)}
        columns={[
          { header: "ID", render: (g) => g.id },
          { header: "Name", render: (g) => g.name },
        ]}
      />
    </div>
  );
}

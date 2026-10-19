import { useState } from "react";
import { movieAdminApi } from "../api/movieApi";
import { AdminNav } from "../../../shared/components/AdminNav";
import { AdminTable } from "../../../shared/components/AdminTable";
import { MovieForm } from "../components/MovieForm";
import { useAdminResource } from "../../../shared/hooks/useAdminResource";
import type { Movie } from "../types/movie";

export function AdminMoviePage() {
  const movies = useAdminResource(movieAdminApi);
  const [editing, setEditing] = useState<Movie | "new" | null>(null);

  return (
    <div>
      <AdminNav />
      <h1>Movies</h1>
      <button onClick={() => setEditing("new")}>Create</button>

      {editing && (
        <MovieForm
          key={editing === "new" ? "new" : editing.id}
          initial={editing === "new" ? undefined : editing}
          onCancel={() => setEditing(null)}
          onSubmit={async (values) => {
            const ok =
              editing === "new"
                ? await movies.create(values)
                : await movies.update(editing.id, values);
            if (ok) setEditing(null);
            return ok;
          }}
        />
      )}

      {movies.isLoading && <p>Loading...</p>}
      {movies.error && <p>{movies.error}</p>}

      <AdminTable
        rows={movies.items}
        getKey={(m) => m.id}
        onEdit={setEditing}
        onDelete={(m) => movies.remove(m.id)}
        columns={[
          { header: "ID", render: (m) => m.id },
          { header: "Title", render: (m) => m.title },
          { header: "Duration", render: (m) => `${m.durationMinutes} min` },
          { header: "Genres", render: (m) => m.genres?.join(", ") },
          { header: "Description", render: (m) => m.description },
        ]}
      />
    </div>
  );
}

import { useState } from "react";
import { hallAdminApi } from "../../cinemas/api/cinemaApi";
import { movieAdminApi } from "../../movies/api/movieApi";
import { screeningAdminApi } from "../api/screeningApi";
import { AdminNav } from "../../../shared/components/AdminNav";
import { AdminTable } from "../../../shared/components/AdminTable";
import { ScreeningForm } from "../components/ScreeningForm";
import { useAdminResource } from "../../../shared/hooks/useAdminResource";

export function AdminScreeningPage() {
  const screenings = useAdminResource(screeningAdminApi);
  const movies = useAdminResource(movieAdminApi);
  const halls = useAdminResource(hallAdminApi);
  const [creating, setCreating] = useState(false);

  const hallName = (id: number) =>
    halls.items.find((h) => h.id === id)?.name ?? id;

  return (
    <div>
      <AdminNav />
      <h1>Screenings</h1>
      <button onClick={() => setCreating(true)}>Create</button>

      {creating && (
        <ScreeningForm
          movies={movies.items}
          halls={halls.items}
          onCancel={() => setCreating(false)}
          onSubmit={async (values) => {
            const ok = await screenings.create(values);
            if (ok) setCreating(false);
            return ok;
          }}
        />
      )}

      {screenings.isLoading && <p>Loading...</p>}
      {screenings.error && <p>{screenings.error}</p>}

      <AdminTable
        rows={screenings.items}
        getKey={(s) => s.id}
        onDelete={(s) => screenings.remove(s.id)}
        columns={[
          { header: "ID", render: (s) => s.id },
          { header: "Movie", render: (s) => s.movieTitle },
          { header: "Hall", render: (s) => hallName(s.hallId) },
          {
            header: "Start",
            render: (s) => new Date(s.startTime).toLocaleString(),
          },
          {
            header: "End",
            render: (s) => new Date(s.endTime).toLocaleString(),
          },
          { header: "Base price", render: (s) => s.basePrice },
        ]}
      />
    </div>
  );
}

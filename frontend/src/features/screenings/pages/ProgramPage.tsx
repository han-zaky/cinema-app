import { useNavigate } from "react-router-dom";
import { useScreenings } from "../hooks/useScreenings";

export function ProgramPage() {
  const { screenings, isLoading, error } = useScreenings();
  const navigate = useNavigate();

  return (
    <div>
      <h1>Program</h1>

      {isLoading && <p>Načítání…</p>}
      {error && <p>{error}</p>}
      {!isLoading && !error && screenings.length === 0 && (
        <p>Žádná promítání.</p>
      )}

      <ul>
        {screenings.map((s) => (
          <li key={s.id}>
            <h3>{s.movieTitle}</h3>
            <p>{new Date(s.startTime).toLocaleString("cs-CZ")}</p>
            <p>Cena: {s.basePrice} Kč</p>
            <button
              type="button"
              onClick={() => navigate(`/screenings/${s.id}/seats`)}
            >
              Rezervovat
            </button>
          </li>
        ))}
      </ul>
    </div>
  );
}

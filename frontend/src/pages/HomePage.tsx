import { MoviesList } from "../features/movies/components/MovieList";

export function HomePage() {
  return (
    <div>
      <h1>Filmy - zatím všechny... budou jen aktuální</h1>
      <div>
        <MoviesList />
      </div>
    </div>
  );
}

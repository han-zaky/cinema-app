import { useEffect, useMemo, useRef, useState } from "react";
import { useMovies } from "../hooks/useMovies";

const PAGE_SIZE = 10;

export function MoviesPage() {
  const { movies, isLoading, error } = useMovies();
  const [search, setSearch] = useState("");
  const [genre, setGenre] = useState("");
  const [visibleCount, setVisibleCount] = useState(PAGE_SIZE);
  const sentinelRef = useRef<HTMLDivElement>(null);

  // all genres that appear in the loaded movies
  const genres = useMemo(
    () => Array.from(new Set(movies.flatMap((m) => m.genres ?? []))).sort(),
    [movies],
  );

  const filtered = useMemo(() => {
    const query = search.trim().toLowerCase();
    return movies.filter(
      (m) =>
        m.title.toLowerCase().includes(query) &&
        (genre === "" || (m.genres ?? []).includes(genre)),
    );
  }, [movies, search, genre]);

  // reset to the first "page" whenever the filters change
  useEffect(() => {
    setVisibleCount(PAGE_SIZE);
  }, [search, genre]);

  const visible = filtered.slice(0, visibleCount);
  const hasMore = visibleCount < filtered.length;

  // load the next 10 when the sentinel at the bottom scrolls into view
  useEffect(() => {
    const el = sentinelRef.current;
    if (!el || !hasMore) return;

    const observer = new IntersectionObserver((entries) => {
      if (entries[0].isIntersecting) {
        setVisibleCount((c) => c + PAGE_SIZE);
      }
    });
    observer.observe(el);
    return () => observer.disconnect();
  }, [hasMore, visibleCount]);

  return (
    <div>
      <h1>Movies</h1>

      <input
        type="text"
        placeholder="Search movies..."
        value={search}
        onChange={(e) => setSearch(e.target.value)}
      />

      <select value={genre} onChange={(e) => setGenre(e.target.value)}>
        <option value="">All genres</option>
        {genres.map((g) => (
          <option key={g} value={g}>
            {g}
          </option>
        ))}
      </select>

      {isLoading && <p>Loading...</p>}
      {error && <p>{error}</p>}
      {!isLoading && !error && filtered.length === 0 && <p>No movies found.</p>}

      <ul>
        {visible.map((movie) => (
          <li key={movie.id}>
            <h3>{movie.title}</h3>
            <p>{movie.genres?.join(", ")}</p>
            <p>{movie.durationMinutes} min</p>
            <p>{movie.description}</p>
          </li>
        ))}
      </ul>

      {hasMore && <div ref={sentinelRef}>Loading more...</div>}
    </div>
  );
}

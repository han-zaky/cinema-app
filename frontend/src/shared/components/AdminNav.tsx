import { Link } from "react-router-dom";

export function AdminNav() {
  return (
    <div>
      <Link to="/admin/movies">Movies</Link>{" "}
      <Link to="/admin/genres">Genres</Link>{" "}
      <Link to="/admin/cinemas">Cinemas</Link>{" "}
      <Link to="/admin/halls">Halls</Link> <Link to="/admin/seats">Seats</Link>{" "}
      <Link to="/admin/screenings">Screenings</Link>
    </div>
  );
}

import { Link } from "react-router-dom";

export function NotFoundPage() {
  return (
    <div>
      <h1>404</h1>
      <p>Stránka nebyla nalezena.</p>
      <Link to="/">Zpět na hlavní stránku</Link>
    </div>
  );
}

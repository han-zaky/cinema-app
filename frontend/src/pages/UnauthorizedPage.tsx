import { Link } from "react-router-dom";

export function UnauthorizedPage() {
  return (
    <div>
      <h1>Nemáš přístup</h1>
      <p>Na tuto stránku nemáš oprávnění.</p>
      <Link to="/">Zpět na hlavní stránku</Link>
    </div>
  );
}

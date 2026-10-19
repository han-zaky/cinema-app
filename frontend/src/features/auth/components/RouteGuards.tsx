import { Navigate, Outlet, useLocation } from "react-router-dom";
import { useAuth } from "./AuthContext";

export function RequireRole({ role }: { role: string }) {
  const { user, isLoading } = useAuth();
  const location = useLocation();

  if (isLoading) return <div>Loading…</div>;

  if (!user) {
    return <Navigate to="/login" replace state={{ from: location }} />;
  }

  if (role !== user.role) {
    return <Navigate to={"/unauthorized"} replace />; /* nebo home a pop up */
  }

  return <Outlet />;
}

export function RequireGuest() {
  const { user, isLoading } = useAuth();

  if (isLoading) return <div>Loading…</div>;

  if (user) {
    return (
      <Navigate
        to={user.role === "ADMIN" ? "/admin/movies" : "/profile"}
        replace
      />
    );
  }
  return <Outlet />;
}

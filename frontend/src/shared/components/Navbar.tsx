import { Link, useNavigate } from "react-router-dom";
import { logout } from "../../features/auth/api/client";
import { useAuth } from "../../features/auth/components/AuthContext";

export function Navbar() {
  const navigate = useNavigate();
  const { user, setUser, isLoading } = useAuth();

  const handleLogout = async () => {
    try {
      await logout();
    } catch (err) {
      console.error(err);
    } finally {
      setUser(null);
      navigate("/login");
    }
  };

  return (
    <nav>
      <Link to="/">Home</Link>

      {!isLoading && !user && (
        <>
          <Link to="/login">Login</Link>
          <Link to="/register">Register</Link>
        </>
      )}

      {!isLoading && user && (
        <>
          <Link to="/profile">
            <div>
              <div>
                {user.firstName} {user.lastName}
              </div>
              <div>{user.email}</div>
            </div>
          </Link>
          <button onClick={handleLogout}>Logout</button>
        </>
      )}
    </nav>
  );
}

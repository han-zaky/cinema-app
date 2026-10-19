import { BrowserRouter, Routes, Route } from "react-router-dom";
import { HomePage } from "../pages/HomePage";
import { RegisterPage } from "../features/auth/pages/RegisterPage";
import { LoginPage } from "../features/auth/pages/LoginPage";
import { MoviesPage } from "../features/movies/pages/MoviesPage";
import { AdminMoviePage } from "../features/movies/pages/AdminMoviePage";
import { AdminGenrePage } from "../features/genres/pages/AdminGenrePage";
import { AdminCinemaPage } from "../features/cinemas/pages/AdminCinemaPage";
import { AdminHallPage } from "../features/cinemas/pages/AdminHallPage";
import { AdminSeatPage } from "../features/cinemas/pages/AdminSeatPage";
import { ProgramPage } from "../features/screenings/pages/ProgramPage";
import { SeatingPage } from "../features/reservations/pages/SeatingPage";
import { OrderPage } from "../features/reservations/pages/OrderPage";
import { AdminScreeningPage } from "../features/screenings/pages/AdminScreeningPage";
import { Navbar } from "../shared/components/Navbar";
import { AuthProvider } from "../features/auth/components/AuthContext";
import { ProfilePage } from "../features/profile/pages/ProfilePage";
import { UnauthorizedPage } from "../pages/UnauthorizedPage";
import { NotFoundPage } from "../pages/NotFoundPage";
import {
  RequireGuest,
  RequireRole,
} from "../features/auth/components/RouteGuards";

export function AppRouter() {
  return (
    <AuthProvider>
      <BrowserRouter>
        <Navbar />
        <Routes>
          {/*Pro všechny*/}
          <Route path="/" element={<HomePage />} />
          <Route path="/movies" element={<MoviesPage />} />
          <Route path="/program" element={<ProgramPage />} />
          <Route path="/screenings/:id/seats" element={<SeatingPage />} />
          <Route path="/screenings/:id/order" element={<OrderPage />} />

          {/* Pro guesta only */}
          <Route element={<RequireGuest />}>
            <Route path="/login" element={<LoginPage />} />
            <Route path="/register" element={<RegisterPage />} />
          </Route>

          <Route element={<RequireRole role="USER" />}>
            <Route path="/profile" element={<ProfilePage />} />
          </Route>

          {/* Pro admina only*/}
          <Route path="/admin" element={<RequireRole role="ADMIN" />}>
            <Route path="movies" element={<AdminMoviePage />} />
            <Route path="genres" element={<AdminGenrePage />} />
            <Route path="cinemas" element={<AdminCinemaPage />} />
            <Route path="halls" element={<AdminHallPage />} />
            <Route path="seats" element={<AdminSeatPage />} />
            <Route path="screenings" element={<AdminScreeningPage />} />
          </Route>

          <Route path="/unauthorized" element={<UnauthorizedPage />} />
          <Route path="*" element={<NotFoundPage />} />
        </Routes>
      </BrowserRouter>
    </AuthProvider>
  );
}

export interface Movie {
  id: number;
  title: string;
  description: string;
  durationMinutes: number;
  posterUrl: string;
  genres: string[];
}

export interface MovieCardProps {
  movie: Movie;
}

export interface MovieRequest {
  title: string;
  description: string;
  durationMinutes: number;
  posterUrl: string;
  genres: string[];
}

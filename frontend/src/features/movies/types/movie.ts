export interface Movie {
  id: number;
  title: string;
  description: string;
  durationMinutes: number;
  posterUrl: string;
}

export interface MovieCardProps {
  movie: Movie;
}

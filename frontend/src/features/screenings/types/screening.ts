export interface Screening {
  id: number;
  movieId: number;
  movieTitle: string;
  hallId: number;
  startTime: string;
  endTime: string;
  basePrice: number;
}
export interface ScreeningRequest {
  movieId: number;
  hallId: number;
  startTime: string; // ISO instant
  basePrice: number;
}

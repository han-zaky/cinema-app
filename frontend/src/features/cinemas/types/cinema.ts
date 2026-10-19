export interface Address {
  country: string;
  city: string;
  street: string;
  houseNumber: string;
  zipCode: string;
}
export interface Cinema {
  id: number;
  name: string;
  address: Address;
}
export interface CinemaRequest {
  name: string;
  address: Address;
}
export interface Hall {
  id: number;
  name: string;
  cinemaId: number;
}
export interface HallRequest {
  name: string;
  cinemaId: number;
}
// mirrors backend enum SeatType
export type SeatType = "SINGLE" | "DOUBLE" | "COMFY";

// mirrors backend SeatResponseDto
export interface Seat {
  id: number;
  rowLetter: string;
  seatNumber: number;
  seatType: SeatType;
  hallId: number;
  gridCol: number;
  gridRow: number;
}
export interface GenerateSeatsRequest {
  hallId: number;
  rowCount: number;
  seatsPerRow: number;
}

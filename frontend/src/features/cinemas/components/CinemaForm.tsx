import { useState, type FormEvent } from "react";
import type { Address, Cinema, CinemaRequest } from "../types/cinema";

interface Props {
  initial?: Cinema;
  onSubmit: (values: CinemaRequest) => Promise<boolean>;
  onCancel: () => void;
}

const EMPTY_ADDRESS: Address = {
  country: "",
  city: "",
  street: "",
  houseNumber: "",
  zipCode: "",
};

export function CinemaForm({ initial, onSubmit, onCancel }: Props) {
  const [name, setName] = useState(initial?.name ?? "");
  const [address, setAddress] = useState<Address>(
    initial?.address ?? EMPTY_ADDRESS,
  );

  function field(key: keyof Address, placeholder: string) {
    return (
      <input
        placeholder={placeholder}
        value={address[key]}
        onChange={(e) => setAddress({ ...address, [key]: e.target.value })}
        required
      />
    );
  }

  async function handleSubmit(e: FormEvent) {
    e.preventDefault();
    await onSubmit({ name, address });
  }

  return (
    <form onSubmit={handleSubmit}>
      <input
        placeholder="Name"
        value={name}
        onChange={(e) => setName(e.target.value)}
        required
      />
      {field("country", "Country")}
      {field("city", "City")}
      {field("street", "Street")}
      {field("houseNumber", "House number")}
      {field("zipCode", "Zip code")}
      <button type="submit">Save</button>
      <button type="button" onClick={onCancel}>
        Cancel
      </button>
    </form>
  );
}

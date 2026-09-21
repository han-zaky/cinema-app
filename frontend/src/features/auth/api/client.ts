import type { authResponse, loginRequest } from "../types/auth";

export async function login(data: loginRequest): Promise<authResponse> {
  const res = await fetch("/api/auth/login", {
    method: "POST",
    headers: { "Content-Type": "application/json" },
    credentials: "include",
    body: JSON.stringify(data),
  });

  if (!res.ok) {
    throw new Error("Přihlášení selhalo");
  }

  return res.json();
}

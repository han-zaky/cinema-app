import type {
  AuthResponse,
  LoginRequest,
  RegisterRequest,
} from "../types/auth";

let refreshPromise: Promise<boolean> | null = null;

export async function login(data: LoginRequest): Promise<AuthResponse> {
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

export async function register(data: RegisterRequest): Promise<AuthResponse> {
  const res = await fetch("/api/auth/register", {
    method: "POST",
    headers: { "Content-Type": "application/json" },
    credentials: "include",
    body: JSON.stringify(data),
  });

  if (!res.ok) {
    throw new Error("Registrace selhala");
  }

  return res.json();
}

export async function logout(): Promise<void> {
  const res = await fetch("/api/auth/logout", {
    method: "POST",
    credentials: "include",
  });

  if (!res.ok) throw new Error("Odhlášení selhalo!");
}

export async function refreshAccessToken(): Promise<boolean> {
  if (refreshPromise) return refreshPromise;

  refreshPromise = fetch("/api/auth/refresh", {
    method: "POST",
    credentials: "include",
  })
    .then((res) => res.ok)
    .finally(() => {
      refreshPromise = null;
    });

  return refreshPromise;
}

export async function apiFetch(
  url: string,
  options: RequestInit = {},
  redirectOnAuthFail: boolean = true,
): Promise<Response> {
  let response = await fetch(url, { ...options, credentials: "include" });

  if (response.status === 401) {
    const refreshed = await refreshAccessToken();

    if (refreshed) {
      response = await fetch(url, { ...options, credentials: "include" });
    } else if (redirectOnAuthFail) {
      window.location.href = "/login"; //TODO eventuelně řešit jinak -> handler
    }
  }

  return response;
}

export async function apiFetchJson<T>(
  url: string,
  options: RequestInit = {},
): Promise<T> {
  const res = await apiFetch(url, options);
  if (!res.ok) {
    const errorBody = await res.json().catch(() => null);
    throw new Error(errorBody?.message ?? "Požadavek selhal");
  }
  return res.json();
}

export async function getCurrentUser(): Promise<AuthResponse | null> {
  const res = await apiFetch("/api/users/me", {}, false);

  if (!res.ok) return null;

  return res.json();
}

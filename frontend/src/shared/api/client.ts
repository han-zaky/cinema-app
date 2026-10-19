let refreshPromise: Promise<boolean> | null = null;

export async function refreshAccessToken(): Promise<boolean> {
  if (refreshPromise) return refreshPromise;

  refreshPromise = fetch("/api/auth/refresh", {
    method: "POST",
    credentials: "include",
  })
    .then((res) => res.ok)
    .catch(() => false)
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
    } else if (redirectOnAuthFail && window.location.pathname !== "/login") {
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
  if (res.status === 204) {
    return undefined as T;
  }
  return res.json();
}

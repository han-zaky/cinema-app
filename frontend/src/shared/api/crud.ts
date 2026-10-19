import { apiFetch } from "./client";

// like apiFetchJson, but tolerates empty response bodies (DELETE)
export async function request<T>(
  url: string,
  method: string,
  body?: unknown,
): Promise<T> {
  const res = await apiFetch(url, {
    method,
    headers:
      body === undefined ? undefined : { "Content-Type": "application/json" },
    body: body === undefined ? undefined : JSON.stringify(body),
  });

  if (!res.ok) {
    const err = await res.json().catch(() => null);
    throw new Error(err?.message ?? `Request failed (${res.status})`);
  }

  const text = await res.text();
  return (text ? JSON.parse(text) : undefined) as T;
}

export interface CrudApi<T, Req> {
  list: () => Promise<T[]>;
  create: (body: Req) => Promise<T>;
  update: (id: number, body: Req) => Promise<T>;
  remove: (id: number) => Promise<void>;
}

// reads go to the public controller, writes to the /api/admin one
export function crud<T, Req>(listUrl: string, adminUrl: string): CrudApi<T, Req> {
  return {
    list: () => request<T[]>(listUrl, "GET"),
    create: (body) => request<T>(adminUrl, "POST", body),
    update: (id, body) => request<T>(`${adminUrl}/${id}`, "PATCH", body),
    remove: (id) => request<void>(`${adminUrl}/${id}`, "DELETE"),
  };
}

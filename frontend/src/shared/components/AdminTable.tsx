import type { ReactNode } from "react";

export interface Column<T> {
  header: string;
  render: (row: T) => ReactNode;
}

interface Props<T> {
  columns: Column<T>[];
  rows: T[];
  getKey: (row: T) => number;
  onEdit?: (row: T) => void;
  onDelete: (row: T) => void;
}

export function AdminTable<T>({
  columns,
  rows,
  getKey,
  onEdit,
  onDelete,
}: Props<T>) {
  return (
    <table>
      <thead>
        <tr>
          {columns.map((c) => (
            <th key={c.header}>{c.header}</th>
          ))}
          <th>Actions</th>
        </tr>
      </thead>
      <tbody>
        {rows.map((row) => (
          <tr key={getKey(row)}>
            {columns.map((c) => (
              <td key={c.header}>{c.render(row)}</td>
            ))}
            <td>
              {onEdit && <button onClick={() => onEdit(row)}>Edit</button>}
              <button
                onClick={() => {
                  if (window.confirm("Delete this item?")) onDelete(row);
                }}
              >
                Delete
              </button>
            </td>
          </tr>
        ))}
      </tbody>
    </table>
  );
}

#!/usr/bin/env bash

# Exit immediately if a command exits with a non-zero status
set -e

echo "=========================================="
echo "  🎬 Cinema Project: Full Stack Launcher  "
echo "=========================================="

# 1. Start Docker PostgreSQL container
echo "[1/4] Starting PostgreSQL container..."
docker compose up -d postgres

# 2. Wait until PostgreSQL is actually accepting connections
echo "[2/4] Waiting for PostgreSQL (cinemadb on port 5433) to become ready..."
until docker exec cinema-postgres pg_isready -U cinema -d cinemadb > /dev/null 2>&1; do
    echo "      ...waiting for database to accept connections..."
    sleep 1
done
echo "      ✔ PostgreSQL is ready!"

# Clean shutdown handler:
# Pressing Ctrl+C sends SIGINT, triggering this trap to kill background processes.
trap 'echo ""; echo "Stopping all services..."; kill $(jobs -p) 2>/dev/null; exit' SIGINT SIGTERM EXIT

# 3. Start Backend in background
echo "[3/4] Starting Spring Boot Backend on http://localhost:8080 ..."
(
  cd backend/demo
  ./mvnw spring-boot:run
) &

# Optional short pause to let backend initialize its port before frontend starts
sleep 3

# 4. Start Frontend in background
echo "[4/4] Starting Vite Frontend on http://localhost:5173 ..."
(
  cd frontend
  npm run dev
) &

echo "=========================================="
echo "  🚀 All services started!"
echo "  Press CTRL+C at any time to stop both."
echo "=========================================="

# Wait indefinitely for background jobs so the script stays alive
wait
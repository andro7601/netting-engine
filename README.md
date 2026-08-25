# Netting Engine

**Stack**: Java 21, Spring Boot 4.x, PostgreSQL 16, Flyway, Docker

---

## Build & Run

**Full Docker:**
```bash
docker compose up --build
```

**Local app + Dockerized Postgres:**
```bash
docker compose up -d postgres
./mvnw package -DskipTests
java -Dspring.datasource.password="$(cat secrets/POSTGRES_PASSWORD)" -jar target/netting-engine-0.0.1-SNAPSHOT.jar
```

---

## Schema

**`optimization_requests`**

`id` (PK), `max_margin`, `total_margin_used`, `total_expected_pnl`, `created_at`


**`trades`**

`id` (PK), `trade_name`, `margin_required`, `expected_pnl`, `selected`, `request_id` (FK)

All PKs are indexed automatically. Additional indexes: `idx_trade_fk` on `trades(request_id)`, `idx_trade_selected` on `trades(selected)`.

---

## API

See [`exampleCurls.sh`](exampleCurls.sh):

```bash
./exampleCurls.sh 1              # POST /optimize
./exampleCurls.sh 2 <uuid>       # GET /trades/{requestId}
./exampleCurls.sh 3 <page> <size> # GET /trades?page=&size=
```


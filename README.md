# Spring Boot CRUD Example

This project provides a basic Spring Boot backend with CRUD and CSV import/export capabilities for a PostgreSQL table `cat_kpi`.

## Building

Use Maven wrapper:

```bash
cd backend
./mvnw spring-boot:run
```

By default it connects to PostgreSQL on `localhost:5432` using the `postgres` user and password `postgres`. Update `src/main/resources/application.properties` if needed.

## REST Endpoints

- `GET /api/kpis` – list all records
- `GET /api/kpis/{id}` – get a record by id
- `POST /api/kpis` – create a record
- `PUT /api/kpis/{id}` – update a record
- `DELETE /api/kpis/{id}` – delete a record
- `GET /api/kpis/export` – download records as CSV
- `POST /api/kpis/import` – upload a CSV file to import records

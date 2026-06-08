# Poke Mono Repo

[Leer en Español](README.es.md)

[![Backend CI](https://github.com/Hectort9021/poke-mono-repo/actions/workflows/ci.yml/badge.svg)](https://github.com/OWNER/poke-mono-repo/actions/workflows/ci.yml)
[![Backend Maintenance](https://github.com/Hectort9021/poke-mono-repo/actions/workflows/maintenance.yml/badge.svg)](https://github.com/OWNER/poke-mono-repo/actions/workflows/maintenance.yml)
![Java 17](https://img.shields.io/badge/Java-17-007396?logo=openjdk&logoColor=white)
![Spring Boot 3.3.2](https://img.shields.io/badge/Spring%20Boot-3.3.2-6DB33F?logo=springboot&logoColor=white)
![Maven](https://img.shields.io/badge/Build-Maven-C71A36?logo=apachemaven&logoColor=white)
![H2](https://img.shields.io/badge/DB-H2-1A237E)

> Replace `OWNER` with your GitHub user or organization to activate the workflow badges.

## Phase 1: Spring Boot ingestion backend

The first service was created in `apps/backend/pokemon-ingestion` to consume the PokeAPI and fetch Pokemon data.

> The application uses **in-memory H2** for now, so you do not need to configure an external database.

### Run

```bash
cd apps/backend/pokemon-ingestion
mvn spring-boot:run
```

### Run with Docker

Build and start the backend from the repository root:

```bash
docker build -t pokemon-ingestion .
docker run --rm -p 8080:8080 pokemon-ingestion
```

### Test endpoints

Fetch a subset:

```bash
curl "http://localhost:8080/api/ingestion/pokemon?limit=10"
```

Fetch all Pokemon available in PokeAPI:

```bash
curl "http://localhost:8080/api/ingestion/pokemon?all=true"
```

Download a Pokemon default front sprite:

```bash
curl -OJ "http://localhost:8080/api/ingestion/pokemon/pikachu/sprite"
```

Check how many Pokemon were saved in the in-memory database:

```bash
curl "http://localhost:8080/api/ingestion/pokemon/count"
```

H2 console (optional): `http://localhost:8080/h2-console`

The endpoint queries `https://pokeapi.co/api/v2/pokemon` with pagination, fetches the details for each requested Pokemon, and only stores the `front_default` sprite URL when available; it does not download images during ingestion. The actual PNG download happens only when the sprite endpoint is called, which fetches the Pokemon detail again and returns the file as an attachment. If PokeAPI does not recognize the requested name, the API responds with `404 Not Found` and a `pokemon_not_found` error.

## Database schema

The `db/schema/pokemon_schema.sql` script contains a proposed relational schema (PostgreSQL) for a later phase with an external database: species, Pokemon, types, abilities, stats, moves, and evolution.

### Run script

```bash
psql -d your_database -f db/schema/pokemon_schema.sql
```

## CI/CD (GitHub Actions)

Pipelines were added in `.github/workflows` to maintain the app:

- `ci.yml`: runs on `push` and `pull_request` when there are backend changes. It runs `mvn clean verify` to validate compilation and tests.
- `maintenance.yml`: runs every Monday (and manually) to run regression tests (`mvn test`) and report available dependency/plugin updates with Maven Versions Plugin.

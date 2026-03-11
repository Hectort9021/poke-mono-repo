# Poke Mono Repo

## Fase 1: backend de ingesta con Spring Boot

Se creó un primer servicio en `backend/pokemon-ingestion` para consumir la PokeAPI y traer datos de Pokémon.

> La aplicación usa **H2 en memoria** por ahora, así que no necesitas configurar una base de datos externa.

### Ejecutar

```bash
cd backend/pokemon-ingestion
mvn spring-boot:run
```

### Endpoints de prueba

Traer un subconjunto:

```bash
curl "http://localhost:8080/api/ingestion/pokemon?limit=10"
```

Traer todos los Pokémon disponibles en PokeAPI:

```bash
curl "http://localhost:8080/api/ingestion/pokemon?all=true"
```

Ver cuántos Pokémon quedaron guardados en la BD en memoria:

```bash
curl "http://localhost:8080/api/ingestion/pokemon/count"
```

Consola H2 (opcional): `http://localhost:8080/h2-console`

El endpoint consulta `https://pokeapi.co/api/v2/pokemon` de forma paginada y luego trae el detalle de cada Pokémon solicitado.

## Esquema de base de datos

El script `db/schema/pokemon_schema.sql` contiene una propuesta de esquema relacional (PostgreSQL) para una fase posterior con base externa: especies, pokémon, tipos, habilidades, estadísticas, movimientos y evolución.

### Ejecutar script

```bash
psql -d tu_base -f db/schema/pokemon_schema.sql
```

## CI/CD (GitHub Actions)

Se agregaron pipelines en `.github/workflows` para mantener la app:

- `ci.yml`: se ejecuta en `push` y `pull_request` cuando hay cambios en el backend. Corre `mvn clean verify` para validar compilación y pruebas.
- `maintenance.yml`: se ejecuta cada lunes (y manualmente) para correr regresión (`mvn test`) y reportar actualizaciones disponibles de dependencias/plugins con Maven Versions Plugin.

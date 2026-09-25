# Social Network Backend — Step 1: Auth Foundation

Spring Boot backend para sa social network app mo (feed, messenger, groups, marketplace).
Ito muna ang Auth module — foundation para sa lahat ng susunod na feature.

## Kasama dito
- User entity + PostgreSQL persistence (Spring Data JPA)
- Register / Login endpoints (`/api/auth/register`, `/api/auth/login`)
- Password hashing gamit ang BCrypt
- JWT generation at validation
- Stateless security config (walang session, Bearer token ang gamit)

## Kailangan mo munang i-setup

1. **Java 17+** at **Maven** installed.
2. **PostgreSQL** running locally (o sa Docker). Gumawa ng database:
   ```sql
   CREATE DATABASE social_network;
   ```
3. I-update ang `src/main/resources/application.properties`:
   - `spring.datasource.username` / `password` — ilagay ang totoong credentials mo
   - `app.jwt.secret` — palitan ng sarili mong random 32+ character string

## Pag-run

```bash
mvn spring-boot:run
```

Tatakbo ito sa `http://localhost:8080`.

## Pag-test ng endpoints

**Register:**
```bash
curl -X POST http://localhost:8080/api/auth/register \
  -H "Content-Type: application/json" \
  -d '{"username":"chanak","email":"chanak@example.com","password":"password123","displayName":"Chanak"}'
```

**Login:**
```bash
curl -X POST http://localhost:8080/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{"usernameOrEmail":"chanak","password":"password123"}'
```

Ibabalik nito ang isang JWT token na gagamitin mo sa `Authorization: Bearer <token>`
header para sa lahat ng susunod na protected endpoints (feed, messenger, atbp.)

## Susunod na hakbang

- Ikonekta ang Android app mo gamit ang Retrofit sa `/api/auth/*` endpoints
- Sunod na module: **News Feed API** (Post entity, like/comment endpoints)

## Mahalagang paalala

Ang `app.jwt.secret` na nakalagay dito ay placeholder lang — palitan ito bago mo
i-deploy kahit saan, at gamitin ang environment variable sa production imbes na
i-hardcode sa file.

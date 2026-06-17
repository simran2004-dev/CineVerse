<div align="center">

# 🎬 CineVerse

### Full-Stack Movie Booking Platform

*Microservices · JWT Auth · Distributed Seat Locking · Async Notifications · CI/CD*

[![React](https://img.shields.io/badge/React-19-61DAFB?style=flat-square&logo=react&logoColor=black)](https://react.dev)
[![Spring Boot](https://img.shields.io/badge/Spring_Boot-3.2-6DB33F?style=flat-square&logo=springboot&logoColor=white)](https://spring.io/projects/spring-boot)
[![Vite](https://img.shields.io/badge/Vite-8-646CFF?style=flat-square&logo=vite&logoColor=white)](https://vitejs.dev)
[![Docker](https://img.shields.io/badge/Docker-Compose-2496ED?style=flat-square&logo=docker&logoColor=white)](https://docs.docker.com/compose)
[![License: MIT](https://img.shields.io/badge/License-MIT-yellow.svg?style=flat-square)](LICENSE)

</div>

---

## What is CineVerse?

CineVerse is a production-grade cinema booking system built as a **microservices monorepo**. It covers the full lifecycle: browsing movies, selecting seats, locking them in real time, completing a booking, and receiving an email confirmation — all secured by JWT at the API Gateway level.

---

## Architecture

```
┌──────────────────────────────────────────────┐
│         React SPA  (Vite + Nginx)            │
│              http://localhost:80             │
└──────────────────────┬───────────────────────┘
                       │
          ┌────────────▼────────────┐
          │       API Gateway       │
          │  Spring Cloud Gateway   │
          │  JWT Validation · Logs  │
          │    http://localhost:8080 │
          └───┬──────┬──────────┬───┘
              │      │          │
   ┌──────────▼─┐ ┌──▼───────┐ ┌▼──────────────┐
   │Auth Service│ │Movie Svc │ │ Booking Svc   │
   │ Port 8081  │ │ Port 8082│ │  Port 8083    │
   │PostgreSQL  │ │ MongoDB  │ │PostgreSQL+Redis│
   └────────────┘ └──────────┘ └───────┬────────┘
                                        │
                               ┌────────▼────────┐
                               │    RabbitMQ     │
                               └────────┬────────┘
                                        │
                               ┌────────▼────────┐
                               │Notification Svc │
                               │   Port 8084     │
                               │  Email via SMTP │
                               └─────────────────┘
```

---

## Tech Stack

| Layer | Technology |
|---|---|
| **Frontend** | React 19, Vite, React Router v6, Axios, Vanilla CSS |
| **API Gateway** | Spring Cloud Gateway (WebFlux), JWT filter |
| **Auth Service** | Spring Boot 3, Spring Security, JJWT, BCrypt, PostgreSQL |
| **Movie Service** | Spring Boot 3, MongoDB, multipart file upload |
| **Booking Service** | Spring Boot 3, PostgreSQL, Redis (distributed locks) |
| **Notification Service** | Spring Boot 3, RabbitMQ AMQP, Spring Mail |
| **Infrastructure** | Docker, Docker Compose, Nginx |
| **CI/CD** | GitHub Actions → GHCR, Vercel (frontend) |

---

## Project Structure

```
CineVerse/
├── .github/workflows/
│   ├── ci.yml               # Build + test on every push
│   └── cd.yml               # Push Docker images to GHCR on main
│
├── api-gateway/             # Spring Cloud Gateway + JWT filter
├── backend/
│   ├── auth-service/        # Register · Login · RBAC (USER / ADMIN / THEATRE_OWNER)
│   ├── movie-service/       # Movie CRUD · search · reviews · poster upload
│   ├── booking-service/     # Seat locking · booking · dynamic pricing
│   └── notification-service/# RabbitMQ consumer · email templates
│
├── frontend/                # React SPA
│   ├── src/
│   │   ├── components/      # Navbar, MovieCard, SeatLayout, Button, Input …
│   │   ├── pages/           # Login, Dashboard, MovieCatalog, BookingPage, Admin
│   │   ├── context/         # AuthContext (JWT state)
│   │   ├── routes/          # ProtectedRoute
│   │   └── services/        # Axios API layer (reads VITE_API_BASE_URL)
│   └── vercel.json          # SPA rewrites + security headers
│
├── vercel.json              # ← Root Vercel config (import from GitHub root)
├── docker-compose.yml       # Full-stack local orchestration
└── scripts/
    └── create-multiple-pg-databases.sh
```

---

## 🚀 Deploy to Vercel (one click)

**The frontend is pre-configured for zero-config Vercel deployment.**

1. Push this repo to GitHub
2. Go to [vercel.com/new](https://vercel.com/new) → **Import Git Repository**
3. Select this repo — Vercel reads `vercel.json` at the root automatically
4. Add one **Environment Variable** in the Vercel dashboard:

   | Name | Value |
   |---|---|
   | `VITE_API_BASE_URL` | Your hosted API Gateway URL e.g. `https://api.yourserver.com` |

5. Click **Deploy** ✅

> SPA client-side routing is pre-configured. No extra setup needed.

---

## Running Locally

### Prerequisites
- Docker Desktop **or** Java 17 + Maven 3.9 + Node.js 20

### Docker Compose — full stack in one command

```bash
git clone https://github.com/simran2004-dev/CineVerse.git
cd CineVerse
docker compose up --build
```

| Service | URL |
|---|---|
| Frontend | http://localhost |
| API Gateway | http://localhost:8080 |
| RabbitMQ UI | http://localhost:15672 (guest/guest) |

### Manual — hot reload during development

```bash
# 1. Start infrastructure only
docker compose up postgres mongodb redis rabbitmq -d

# 2. Run services (each in its own terminal)
cd backend/auth-service         && mvn spring-boot:run   # :8081
cd backend/movie-service        && mvn spring-boot:run   # :8082
cd backend/booking-service      && mvn spring-boot:run   # :8083
cd backend/notification-service && mvn spring-boot:run   # :8084
cd api-gateway                  && mvn spring-boot:run   # :8080

# 3. Frontend
cd frontend && npm install && npm run dev   # → http://localhost:5173
```

---

## API Reference

### Auth  `/auth/**`

| Method | Endpoint | Auth | Description |
|---|---|---|---|
| `POST` | `/auth/register` | — | Create account |
| `POST` | `/auth/login` | — | Returns JWT |
| `GET` | `/auth/me` | JWT | Current user profile |
| `GET` | `/auth/users` | ADMIN | List all users |
| `PUT` | `/auth/users/{id}/role` | ADMIN | Change user role |

### Movies  `/api/movies/**`

| Method | Endpoint | Description |
|---|---|---|
| `GET` | `/api/movies?page=0&size=10` | Paginated list |
| `GET` | `/api/movies?title=Dune` | Search by title |
| `GET` | `/api/movies?genre=Sci-Fi` | Filter by genre |
| `POST` | `/api/movies` | Create (multipart/form-data) |
| `PUT` | `/api/movies/{id}` | Update |
| `DELETE` | `/api/movies/{id}` | Delete |
| `POST` | `/api/movies/{id}/reviews` | Add review |

### Bookings  `/api/bookings/** · /api/theatres/**`

| Method | Endpoint | Description |
|---|---|---|
| `GET` | `/api/theatres` | List theatres |
| `GET` | `/api/theatres/{id}/screens` | Screens in theatre |
| `GET` | `/api/theatres/shows/movie/{movieId}` | Shows for a movie |
| `POST` | `/api/bookings` | Create booking |
| `GET` | `/api/bookings/user/{userId}` | Booking history |
| `PUT` | `/api/bookings/{id}/status?status=CONFIRMED` | Update status |

---

## Key Design Decisions

### Distributed Seat Locking (Redis)
When a user selects seats, a per-seat key `seat:lock:{showId}:{seatId}` is set with `SETNX` and a **5-minute TTL**. Abandoned checkouts are released automatically. The database is only checked for permanent conflicts after the lock is acquired — preventing double-bookings under concurrent load.

### Event-Driven Notifications (RabbitMQ)
`CONFIRMED` and `CANCELLED` booking events are published to a durable Topic Exchange. The Notification Service consumes them asynchronously — a slow SMTP server never blocks the booking transaction.

### Embedded Reviews (MongoDB)
Reviews are stored as sub-documents inside each Movie document. No joins, atomic updates, and the average rating recalculates on every write — a natural fit for MongoDB's document model.

### Security Model
All `/api/**` and `/auth/me` routes require `Authorization: Bearer <token>`. The **API Gateway** validates the JWT signature before forwarding — individual services trust the gateway and never re-validate. Tokens expire in 24 hours.

---

## CI/CD

| Trigger | Pipeline | Result |
|---|---|---|
| Any push / PR | `ci.yml` | Maven build + test for all services |
| Push to `main` | `cd.yml` | Docker images pushed to GHCR |
| GitHub import | Vercel | Frontend built and deployed automatically |

---

## Running Tests

```bash
cd backend/auth-service    && mvn test
cd backend/booking-service && mvn test
```

---

## Build Log

| Day | Focus | Deliverable |
|---|---|---|
| 1 | System Design | Monorepo, README, .gitignore |
| 2 | React Frontend | Full SPA: components, pages, auth context, Axios |
| 3 | Auth Service | Spring Security + JWT + BCrypt + RBAC |
| 4 | API Gateway | Spring Cloud Gateway + JWT filter + logging |
| 5 | Movie Service | MongoDB CRUD + search + pagination + file upload + reviews |
| 6 | Booking Service | Entity hierarchy + double-booking prevention + pricing |
| 7 | Redis | Distributed seat locking + @Cacheable on show queries |
| 8 | RabbitMQ | Async event publishing + notification consumer + email |
| 9 | Testing | Mockito unit tests + MockMvc controller tests |
| 10 | Docker | Multi-stage Dockerfiles + Compose + Nginx + Postgres init |
| 11 | CI/CD | GitHub Actions CI + CD to GHCR + Vercel root config |

---

## License

MIT — free to use for learning, portfolio, and personal projects.

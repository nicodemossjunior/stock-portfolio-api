# Fullstack Stock Portfolio Application Proposal

## 1. Goal

Build a new portfolio-ready fullstack application that combines the JWT authentication foundation from `auth-api` with the stock portfolio services currently exposed by `stock-portfolio-api`.

The application should demonstrate the stack used most often in real projects: a Spring Boot REST API, a React frontend, PostgreSQL persistence, JWT-based authentication, Docker-based local execution, and a clean API contract for protected frontend consumption.

The main product goal is to provide stock portfolio management and market-data services through an authenticated frontend experience.

## 2. Source Projects

### `auth-api`

Reusable parts:

- JWT authentication flow with login and registration.
- `User`, `Role`, and `user_roles` persistence model.
- Spring Security stateless configuration.
- JWT filter and token provider.
- Password hashing with BCrypt.
- Default roles (`ROLE_USER`, `ROLE_ADMIN`).
- Protected route examples and React authentication context.

### `stock-portfolio-api`

Reusable services:

- Stock purchase CRUD.
- Purchase search by ticker.
- Portfolio position consolidation by ticker.
- Full portfolio consolidation.
- Market top gainers and top losers through brapi.dev.
- OpenFeign integration with brapi.dev.
- Swagger/OpenAPI documentation.

## 3. Proposed Application

Recommended project name:

```text
stock-portfolio-app
```

Recommended structure:

```text
stock-portfolio-app/
├── backend/
│   ├── src/main/java/com/nicodemos/stockportfolioapp/
│   ├── src/main/resources/
│   ├── pom.xml
│   └── Dockerfile
├── frontend/
│   ├── src/
│   ├── package.json
│   └── Dockerfile
├── docker-compose.yml
├── README.md
└── docs/
    ├── api-contract.md
    ├── architecture.md
    └── implementation-roadmap.md
```

## 4. Architecture

```text
[React Frontend] <--- HTTPS / JWT ---> [Spring Boot API] <--- JPA ---> [PostgreSQL]
                                                |
                                                +--- OpenFeign ---> [brapi.dev]
```

### Backend

- Spring Boot 3.x and Java 17+.
- Spring Security 6.x with stateless JWT authentication.
- Spring Data JPA with PostgreSQL.
- OpenFeign for brapi.dev integration.
- Springdoc OpenAPI for API documentation.
- Flyway or Liquibase for database migrations.
- Dockerfile for containerized execution.

### Frontend

- React 18+ with Vite.
- React Router for public and private routes.
- Axios API client with JWT interceptor.
- Authentication context/provider reused from `auth-api`.
- Protected dashboard and portfolio workflows.
- Responsive UI suitable for a portfolio demo.

### Database

Use PostgreSQL instead of H2 for the integrated application. H2 can remain available only for isolated tests.

Main tables:

- `users`
- `roles`
- `user_roles`
- `stock_purchases`
- Optional: `watchlist_items`
- Optional: `portfolio_snapshots`

## 5. Authentication and Authorization

The new backend should port the authentication model from `auth-api` and make every stock portfolio endpoint user-aware.

Recommended behavior:

- `POST /api/auth/register` creates a user with `ROLE_USER`.
- `POST /api/auth/login` returns a JWT.
- Frontend stores the JWT and sends it as `Authorization: Bearer <token>`.
- Stock purchases belong to the authenticated user.
- Users can only access their own portfolio data.
- Admin-only routes can expose operational or diagnostic data later.

Recommended protected API shape:

| Method | Route | Description | Access |
| :--- | :--- | :--- | :--- |
| `POST` | `/api/auth/register` | Register a new user. | Public |
| `POST` | `/api/auth/login` | Authenticate and return a JWT. | Public |
| `GET` | `/api/purchases` | List authenticated user's purchases. | `ROLE_USER` |
| `GET` | `/api/purchases/{id}` | Get one authenticated user's purchase. | `ROLE_USER` |
| `GET` | `/api/purchases/ticker/{ticker}` | Search authenticated user's purchases by ticker. | `ROLE_USER` |
| `POST` | `/api/purchases` | Create a stock purchase for the authenticated user. | `ROLE_USER` |
| `PUT` | `/api/purchases/{id}` | Update one authenticated user's purchase. | `ROLE_USER` |
| `DELETE` | `/api/purchases/{id}` | Delete one authenticated user's purchase. | `ROLE_USER` |
| `GET` | `/api/portfolio/positions` | Consolidate authenticated user's portfolio. | `ROLE_USER` |
| `GET` | `/api/portfolio/positions/{ticker}` | Consolidate one ticker for the authenticated user. | `ROLE_USER` |
| `GET` | `/api/market/top-gainers` | Fetch market top gainers. | `ROLE_USER` |
| `GET` | `/api/market/top-losers` | Fetch market top losers. | `ROLE_USER` |

## 6. Domain Model Changes

The current `StockPurchase` model should be adapted so records are owned by users.

Recommended fields:

```text
StockPurchase
- id
- user_id
- ticker
- quantity
- unit_price
- purchase_date
- notes
- created_at
- updated_at
```

Recommended rules:

- Normalize tickers to uppercase.
- Validate positive quantity and price.
- Prevent cross-user access to purchases.
- Use DTOs for requests and responses instead of exposing JPA entities directly.
- Return `201 Created` for purchase creation.
- Return `404 Not Found` when a purchase does not exist for the authenticated user.

## 7. Frontend Experience

Recommended screens:

- Login.
- Register.
- Dashboard summary.
- Purchases list.
- Purchase form for create and edit.
- Portfolio positions table.
- Ticker detail page.
- Market movers page with top gainers and top losers.
- Account/logout menu.

Recommended dashboard metrics:

- Total invested amount.
- Number of held assets.
- Average price by ticker.
- Total quantity by ticker.
- Recent purchases.
- Market gainers and losers.

The first screen after login should be the dashboard, not a landing page. The app should feel like a practical financial operations tool: dense, clear, and easy to scan.

## 8. Structural Improvements

Recommended backend improvements:

- Upgrade from Spring Boot 2.5.4 / Java 11 to Spring Boot 3.x / Java 17+.
- Move endpoints under `/api`.
- Introduce request and response DTOs.
- Add Bean Validation to incoming requests.
- Add centralized exception handling.
- Add database migrations with Flyway or Liquibase.
- Replace field injection with constructor injection.
- Add integration tests for authenticated portfolio flows.
- Add service tests for user-scoped portfolio consolidation.
- Add Feign fallback/error mapping for brapi.dev failures.
- Externalize secrets and tokens through environment variables.

Recommended frontend improvements:

- Create a typed API service layer.
- Add authenticated route guards.
- Add loading, empty, and error states.
- Add form validation.
- Add consistent table and form components.
- Add token expiration handling.
- Add responsive layouts for desktop and mobile.

Recommended DevOps improvements:

- Docker Compose with `backend`, `frontend`, and `postgres` services.
- Environment files for local development.
- Health checks for API and database.
- CI pipeline with backend tests and frontend build.
- Optional deployment profile for Render, Railway, Fly.io, or similar platforms.

## 9. Additional Portfolio Features

Features that would make the application stronger as a portfolio project:

- Watchlist for tickers not yet held.
- Real-time or cached quote lookup for held assets.
- Profit/loss calculation when quote data is available.
- Portfolio allocation by ticker.
- Import purchases from CSV.
- Export portfolio report as CSV.
- Transaction history filters by date and ticker.
- Admin dashboard for user count and API health.
- Swagger/OpenAPI secured with JWT support.
- Demo seed data for local portfolio exploration.

## 10. Implementation Roadmap

### Phase 1: Backend Foundation

- Create the new backend project.
- Upgrade to Spring Boot 3.x and Java 17+.
- Configure PostgreSQL.
- Add authentication and authorization from `auth-api`.
- Add database migrations for users, roles, and stock purchases.
- Add OpenAPI documentation.

### Phase 2: Stock Services Migration

- Port stock purchase CRUD.
- Port portfolio consolidation services.
- Port brapi.dev market movers integration.
- Make all portfolio data user-scoped.
- Add DTOs, validation, and exception handling.
- Add tests for authentication and portfolio rules.

### Phase 3: Frontend Foundation

- Create the React/Vite frontend.
- Add routing, authentication context, and private routes.
- Add Axios client with JWT interceptor.
- Implement login, registration, and logout.

### Phase 4: Portfolio UI

- Implement dashboard.
- Implement purchase list and purchase form.
- Implement consolidated portfolio positions.
- Implement ticker detail view.
- Implement top gainers and top losers pages.

### Phase 5: Polish and Delivery

- Add Docker Compose for full local execution.
- Add README with screenshots, architecture, setup, and demo credentials.
- Add CI pipeline.
- Add optional deployment instructions.
- Review UI responsiveness and error states.

## 11. Success Criteria

The project is ready to present when:

- A user can register, log in, and log out.
- Authenticated users can manage only their own stock purchases.
- The frontend consumes all stock portfolio services through protected API calls.
- Portfolio consolidation works per authenticated user.
- Market gainers and losers are available inside the authenticated app.
- The project runs locally with one documented command.
- Tests cover the main authentication and portfolio flows.
- The README clearly explains architecture, setup, API usage, and portfolio relevance.

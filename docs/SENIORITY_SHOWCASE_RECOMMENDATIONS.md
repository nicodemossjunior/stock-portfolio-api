# Seniority Showcase Recommendations

This document lists additional improvements for the proposed fullstack stock portfolio application, with the specific goal of demonstrating senior-level engineering judgment.

The goal is not to add features for volume. The goal is to show strong product thinking, secure architecture, operational maturity, domain modeling, and delivery discipline.

## 1. Secure Multi-User Portfolio Isolation

Make portfolio ownership a first-class part of the domain.

Recommended implementation:

- Every portfolio record belongs to an authenticated user.
- All purchase and portfolio queries are scoped by user.
- Repositories expose methods such as `findByIdAndUserId`, `findByTickerIgnoreCaseAndUserId`, and `findAllByUserId`.
- Services never trust client-provided user IDs for protected data.
- Tests prove that user A cannot read, update, or delete user B's portfolio records.

Why this demonstrates seniority:

- Shows secure multi-tenant thinking.
- Prevents a common authorization bug.
- Demonstrates that authentication and domain ownership are integrated, not bolted on.

## 2. Real Portfolio Performance Calculations

Expand the portfolio from basic consolidation to actual investment analytics.

Recommended calculations:

- Total invested amount.
- Current market value.
- Average purchase price.
- Current price.
- Unrealized gain/loss amount.
- Unrealized gain/loss percentage.
- Allocation by ticker.
- Portfolio total value.

Recommended response example:

```json
{
  "ticker": "PETR4",
  "quantity": 100,
  "averagePrice": 28.50,
  "investedAmount": 2850.00,
  "currentPrice": 32.10,
  "marketValue": 3210.00,
  "unrealizedGainLoss": 360.00,
  "unrealizedGainLossPercentage": 12.63,
  "allocationPercentage": 24.50
}
```

Why this demonstrates seniority:

- Moves the project beyond CRUD.
- Shows domain reasoning.
- Creates a useful product experience.
- Makes external market integration meaningful.

## 3. Transaction-Based Domain Model

Consider evolving from a `StockPurchase` model to a transaction ledger.

Recommended transaction types:

- `BUY`
- `SELL`
- Optional: `DIVIDEND`
- Optional: `SPLIT`
- Optional: `BONUS`

Recommended model:

```text
Transaction
- id
- user_id
- ticker
- type
- quantity
- unit_price
- fees
- transaction_date
- notes
- created_at
- updated_at
```

Why this demonstrates seniority:

- Shows better long-term domain modeling.
- Allows realized gain/loss calculations later.
- Supports a more realistic financial application.
- Avoids locking the application into purchase-only behavior.

## 4. External Market Data Resilience

Treat brapi.dev as an unreliable external dependency.

Recommended improvements:

- Cache quote responses by ticker with a short TTL.
- Cache market movers responses.
- Add timeouts and retries with conservative limits.
- Add fallback behavior for quote failures.
- Return stale cached data with a clear metadata flag when appropriate.
- Map external API errors into consistent internal errors.

Recommended metadata:

```json
{
  "source": "brapi.dev",
  "cached": true,
  "asOf": "2026-06-09T12:00:00Z"
}
```

Why this demonstrates seniority:

- Shows production-minded integration design.
- Protects the application from rate limits and outages.
- Makes failure modes explicit.

## 5. Observability

Add enough observability to operate and debug the application.

Recommended backend additions:

- Spring Boot Actuator.
- Health checks for API, database, and external market provider.
- Structured logs.
- Correlation/request IDs.
- Basic metrics for authentication, portfolio requests, and external API calls.
- Optional Prometheus and Grafana profile in Docker Compose.

Useful metrics:

- Request count by endpoint.
- Request latency.
- Authentication failures.
- brapi.dev call count.
- brapi.dev failure count.
- Cache hit/miss count.

Why this demonstrates seniority:

- Shows operational awareness.
- Demonstrates that the app is designed to be supported, not only developed.
- Gives reviewers confidence in production readiness.

## 6. Stronger Security Posture

Go beyond basic JWT authentication.

Recommended improvements:

- Short-lived access tokens.
- Refresh token flow.
- Refresh token rotation.
- Secure logout/invalidation strategy.
- Rate limiting for login and registration.
- Explicit CORS configuration per environment.
- Secrets loaded only from environment variables or secret managers.
- OpenAPI configured with Bearer Auth.
- Consistent validation and safe error messages.

Why this demonstrates seniority:

- Shows awareness of real authentication risks.
- Makes the security model explainable.
- Improves the application's credibility as a complete system.

## 7. Production-Grade Testing Strategy

Prioritize tests that prove business and security behavior.

Recommended backend tests:

- Unit tests for portfolio calculations.
- Unit tests for ticker normalization and validation.
- Integration tests for authenticated endpoints.
- Integration tests for user data isolation.
- Tests for `401 Unauthorized` and `403 Forbidden`.
- Tests for brapi.dev client failures using mocks or stubs.
- Repository tests for user-scoped queries.

Recommended frontend tests:

- Login flow.
- Registration flow.
- Private route guard.
- Token expiration behavior.
- Portfolio dashboard rendering.
- Form validation.

Why this demonstrates seniority:

- Shows risk-based test prioritization.
- Protects core behavior from regressions.
- Makes the project safer to evolve.

## 8. CI/CD Pipeline

Add a simple but meaningful delivery pipeline.

Recommended GitHub Actions jobs:

- Backend tests.
- Backend package/build.
- Frontend lint.
- Frontend build.
- Docker image build.
- Optional dependency vulnerability scan.

Recommended badges in `README.md`:

- Build status.
- Test status.
- Java version.
- Node version.

Why this demonstrates seniority:

- Shows delivery discipline.
- Makes the project easier to trust.
- Demonstrates automation habits expected in professional teams.

## 9. Complete Docker Compose Environment

Make the full application runnable with one command.

Recommended services:

- `frontend`
- `backend`
- `postgres`
- Optional: `prometheus`
- Optional: `grafana`

Recommended behavior:

- Backend waits for PostgreSQL health.
- Frontend points to backend through environment configuration.
- Database uses a persistent local volume.
- Demo data can be seeded for local exploration.

Example command:

```bash
docker compose up --build
```

Why this demonstrates seniority:

- Reduces reviewer friction.
- Shows environment reproducibility.
- Makes the project feel complete.

## 10. Architecture Documentation and ADRs

Document important choices and trade-offs.

Recommended documentation:

- `docs/architecture.md`
- `docs/security.md`
- `docs/api-contract.md`
- `docs/observability.md`
- `docs/adr/0001-authentication-strategy.md`
- `docs/adr/0002-portfolio-domain-model.md`
- `docs/adr/0003-market-data-integration.md`
- `docs/adr/0004-database-migration-strategy.md`

Recommended ADR topics:

- Why JWT authentication was selected.
- Why portfolio data is user-scoped in every query.
- Why PostgreSQL replaced H2 for the integrated app.
- Why market data is cached.
- Why DTOs are used instead of exposing JPA entities.

Why this demonstrates seniority:

- Shows decision-making, not only implementation.
- Makes trade-offs explicit.
- Helps reviewers understand the reasoning behind the architecture.

## 11. Frontend Product Quality

Build the frontend like a real tool, not a demo shell.

Recommended user experience:

- Dashboard as the first authenticated screen.
- Dense, scannable portfolio tables.
- Clear empty states.
- Clear loading states.
- Clear API error states.
- Responsive layout.
- Form validation before submit.
- Optimistic or deliberate refresh behavior after mutations.
- Account/logout menu.

Recommended dashboard sections:

- Portfolio summary.
- Allocation by ticker.
- Top positions.
- Recent transactions.
- Market movers.
- Watchlist.

Why this demonstrates seniority:

- Shows product empathy.
- Demonstrates frontend architecture and state management.
- Makes the application easier to evaluate visually.

## 12. Priority Order

If time is limited, implement the improvements in this order:

1. Secure multi-user portfolio isolation with tests.
2. Real portfolio performance calculations.
3. Complete Docker Compose environment.
4. Observability with Actuator, health checks, and structured logs.
5. CI pipeline for backend tests and frontend build.
6. Architecture documentation and ADRs.
7. Refresh tokens and login rate limiting.
8. Market data caching and fallback behavior.
9. Transaction-based domain model.
10. Frontend polish with dashboard, error states, and responsive layouts.

## 13. Portfolio Presentation Checklist

The application will demonstrate seniority well when:

- A reviewer can run the full stack locally with one command.
- The README explains the business goal, architecture, setup, and demo flow.
- Authentication protects every portfolio endpoint.
- Tests prove user data isolation.
- Portfolio analytics show real financial value, not only stored records.
- External API failures are handled gracefully.
- Logs, health checks, and metrics are available.
- CI validates the project on every push.
- ADRs explain the most important technical decisions.

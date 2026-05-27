# Stock Portfolio API

Spring Boot REST API for managing stock purchase operations, consolidating portfolio positions, and querying Brazilian stock market movers through brapi.dev.

This project was inspired by a Spring design patterns lab and expanded into a portfolio-ready API using:

- **Singleton**: Spring-managed service and integration components.
- **Strategy/Repository**: service interface plus Spring Data repositories.
- **Facade**: REST controllers exposing a clean API over persistence and external market data.

## Features

- Create, update, delete, and list stock purchases.
- Search purchases by ticker.
- Consolidate the current position of a single ticker.
- Consolidate all assets currently held in the portfolio.
- Fetch top gainers and top losers from the Brazilian stock market using brapi.dev.
- Run locally with Maven or in a container with Docker Compose.

## Endpoints

- `GET /purchases`
- `GET /purchases/{id}`
- `GET /purchases/ticker/{ticker}`
- `POST /purchases`
- `PUT /purchases/{id}`
- `DELETE /purchases/{id}`
- `GET /portfolio/positions`
- `GET /portfolio/positions/{ticker}`
- `GET /market/top-gainers?limit=10`
- `GET /market/top-losers?limit=10`

## Configuration

The brapi.dev token is optional for starting the application. brapi.dev is used to fetch Brazilian stock market data, such as top gainers and top losers.

```properties
brapi.token=
```

When provided, the token is sent as `Authorization: Bearer <token>`.

For Docker Compose, use a `.env` file based on `.env.example`:

```properties
BRAPI_URL=https://brapi.dev
BRAPI_TOKEN=
JAVA_OPTS=
```

## Running Locally

Prerequisites:

- Java 11+
- Maven

Compile and run the tests:

```bash
mvn clean test
```

Start the application locally:

```bash
mvn spring-boot:run
```

The API will be available at:

```text
http://localhost:8080
```

Swagger/OpenAPI:

```text
http://localhost:8080/swagger-ui.html
```

H2 Console:

```text
http://localhost:8080/h2-console
```

H2 connection data:

```properties
JDBC URL=jdbc:h2:mem:stockportfoliodb
User Name=sa
Password=
```

## Running with Docker Compose

Prerequisites:

- Docker
- Docker Compose
- Application jar generated in `target/`

The `Dockerfile` uses the package already generated at `target/stock-portfolio-api-0.0.1-SNAPSHOT.jar`. Generate the jar before building the image:

```bash
mvn clean package
```

Start the application in a container:

```bash
docker compose up --build -d
```

The API will be available at `http://localhost:8080`.

Follow the logs:

```bash
docker compose logs -f stock-portfolio-api
```

Stop and remove the container:

```bash
docker compose down
```

## Usage Examples

Create a purchase:

```bash
curl -X POST http://localhost:8080/purchases \
  -H "Content-Type: application/json" \
  -d '{
    "ticker": "petr4",
    "quantity": 100,
    "price": 28.50,
    "purchaseDate": "2026-05-26"
  }'
```

List purchases:

```bash
curl http://localhost:8080/purchases
```

Search purchases by ticker:

```bash
curl http://localhost:8080/purchases/ticker/PETR4
```

Query the consolidated current position of a ticker:

```bash
curl http://localhost:8080/portfolio/positions/PETR4
```

Query the consolidated list of assets in the portfolio:

```bash
curl http://localhost:8080/portfolio/positions
```

Query top gainers in the Brazilian stock market:

```bash
curl "http://localhost:8080/market/top-gainers?limit=10"
```

Query top losers in the Brazilian stock market:

```bash
curl "http://localhost:8080/market/top-losers?limit=10"
```

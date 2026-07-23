# Account Service

This is the Account Service for the Virtual Bank System. It manages bank accounts for users, including account creation, retrieval, balance management, and fund transfers.

## Prerequisites

- **Java 21**
- **Maven 3.8+** (or use the included `./mvnw` wrapper)
- **PostgreSQL** running locally or via Docker

## Database Setup

We use Docker Compose to spin up an isolated PostgreSQL instance for the service.

1. Start the database by running the following command in the `account-service` directory:
   ```bash
   docker compose up -d
   ```
2. This will start a PostgreSQL container (`account_db`) on port `5432` with the `account_db` database already created. The credentials are `postgres`/`postgres`.

*(If you ever want to stop and remove the database container, run `docker compose down`).*

## How to Build

Navigate to the `account-service` directory and run:

```bash
./mvnw clean package
```

*(This will compile the code, run unit tests, and build the runnable `.jar` file).*

## How to Run

You can run the application directly using the Spring Boot Maven plugin:

```bash
./mvnw spring-boot:run
```

Alternatively, you can run the compiled `.jar` file:

```bash
java -jar target/account-service-0.0.1-SNAPSHOT.jar
```

The service will start by default on `http://localhost:8081`.

## How to Run Tests

To run the unit tests in isolation (no database connection required):

```bash
./mvnw test
```

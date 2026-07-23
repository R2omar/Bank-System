# Transaction Service

This is the Transaction Service for the Virtual Bank System. It handles financial transactions (deposits, withdrawals, transfers), maintains transaction history, and includes a scheduled job to calculate and credit daily interest to active savings accounts.

## Prerequisites

- **Java 21**
- **Maven 3.8+** (or use the included `./mvnw` wrapper)
- **PostgreSQL** running locally or via Docker

## Database Setup

We use Docker Compose to spin up an isolated PostgreSQL instance for the service.

1. Start the database by running the following command in the `transaction-service` directory:
   ```bash
   docker compose up -d
   ```
2. This will start a PostgreSQL container (`transaction_db`) on port `5433` (mapped to `5432` internally) with the `transaction_db` database already created. The credentials are `postgres`/`postgres`.

*(If you ever want to stop and remove the database container, run `docker compose down`).*

By default, it connects to:
- **URL**: `jdbc:postgresql://localhost:5433/transaction_db`
- **Username**: `postgres`
- **Password**: `postgres`

## Service Dependencies

This service communicates with the **Account Service** to execute transfers and fetch active savings accounts for interest payouts. 
By default, the `Account Service` is expected to be running at `http://localhost:8080`.

## How to Build

Navigate to the `transaction-service` directory and run:

```bash
./mvnw clean package -DskipTests
```

*(This will compile the code and build the runnable `.jar` file).*

## How to Run

You can run the application directly using the Spring Boot Maven plugin:

```bash
./mvnw spring-boot:run
```

Alternatively, you can run the compiled `.jar` file:

```bash
java -jar target/transaction-service-0.0.1-SNAPSHOT.jar
```

The service will start by default on `http://localhost:8081` to avoid conflicting with the Account Service.

## Endpoints

- `POST /transactions/transfer/initiation`: Initiates a fund transfer and creates a pending record.
- `POST /transactions/transfer/execution`: Executes an initiated fund transfer by communicating with the Account Service.
- `GET /accounts/{accountId}/transactions`: Retrieves the transaction history for a specific account.

## Scheduled Jobs

- **Daily Interest Processing**: Runs daily at midnight (`0 0 0 * * *`) to fetch all active savings accounts from the Account Service and automatically initiate and execute a 5% interest payout from a virtual bank account.

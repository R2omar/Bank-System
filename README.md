# Virtual Bank System

A distributed banking system built using Spring Boot Microservices, Backend for Frontend (BFF), Apache Kafka, PostgreSQL, Docker, and WSO2 API Manager.

The project demonstrates modern enterprise software architecture by separating banking functionalities into independent microservices while exposing secure APIs through WSO2 API Manager.

---

# Table of Contents

- Project Overview
- Architecture
- Microservices
- Technology Stack
- Features
- API Gateway
- Getting Started

---

# Project Overview

The Virtual Bank System follows a Microservices Architecture where each service is responsible for a single business domain.

Client requests are routed through WSO2 API Manager, which acts as the API Gateway. The Backend for Frontend (BFF) communicates with multiple backend services and returns frontend-optimized responses.

The project consists of:

- User Service
- Account Service
- Transaction Service
- Backend for Frontend (BFF)
- Logging Service
- WSO2 API Manager

---

# Architecture

The system follows a layered microservices architecture.

- The client communicates only with the WSO2 API Manager.
- WSO2 provides authentication, routing, throttling, API publishing, and API management.
- The Backend for Frontend (BFF) aggregates data from multiple microservices into a single response optimized for the frontend.
- Business logic is separated into independent services:
  - User Service
  - Account Service
  - Transaction Service
  - Logging Service
- Each service communicates through REST APIs while Kafka is used for centralized logging.

---

# Microservices

## User Service

Responsible for user management.

### Responsibilities

- User registration
- User authentication
- Password hashing
- User profile management

### Endpoints

```text
POST /users/register

POST /users/login

GET /users/{userId}/profile
```

---

## Account Service

Responsible for bank account management.

### Responsibilities

- Create bank accounts
- Retrieve account information
- Update account balances
- Account transfers
- Scheduled account inactivity process

### Endpoints

```text
POST /accounts

GET /accounts/{accountId}

GET /users/{userId}/accounts

PUT /accounts/transfer
```

---

## Transaction Service

Responsible for financial transactions.

### Responsibilities

- Transfer initiation
- Transfer execution
- Transaction history
- Daily interest calculation

### Endpoints

```text
POST /transactions/transfer/initiation

POST /transactions/transfer/execution

GET /accounts/{accountId}/transactions
```

---

## Backend for Frontend (BFF)

The BFF provides frontend-friendly APIs by aggregating responses from multiple backend services.

### Responsibilities

- Aggregate user profile
- Aggregate user accounts
- Aggregate transaction history
- Reduce frontend network calls

### Endpoint

```text
GET /bff/dashboard/{userId}
```

---

## Logging Service

Responsible for centralized logging using Apache Kafka.

### Responsibilities

- Consume Kafka messages
- Store request logs
- Store response logs
- Persist logs into the database

---

# Technology Stack

| Technology | Purpose |
|------------|---------|
| Java | Programming Language |
| Spring Boot | Microservices Framework |
| Spring Data JPA | Persistence Layer |
| Spring Validation | Input Validation |
| PostgreSQL | Database |
| Apache Kafka | Centralized Logging |
| WSO2 API Manager | API Gateway |
| Docker | Containerization |
| Maven | Build Tool |
| Postman | API Testing |

---

# Features

- Microservices Architecture
- Backend for Frontend Pattern
- RESTful APIs
- WSO2 API Gateway
- OAuth2 Authentication
- API Key Authentication
- API Routing
- Request and Response Logging
- Apache Kafka Integration
- Scheduled Background Jobs
- Global Exception Handling
- Input Validation
- Docker Support

---

# Scheduled Jobs

## Account Service

Runs every hour.

Automatically marks accounts as inactive if they have not had any transactions within the configured period.

---

## Transaction Service

Runs daily.

Calculates interest for active savings accounts and transfers the calculated amount from the virtual bank account.

---

# API Gateway

WSO2 API Manager is used as the single entry point for all external requests.

Responsibilities include:

- API Publishing
- Authentication
- Authorization
- Request Routing
- Request Transformation
- Response Transformation
- Rate Limiting
- Analytics

Published APIs

| API | Backend Endpoint |
|-----|------------------|
| Register | /users/register |
| Login | /users/login |
| Dashboard | /bff/dashboard/{userId} |
| Transactions | /transactions/transfer/* |

API Product

```
vbank
```

---

# Getting Started

## Clone the repository

```bash
git clone https://github.com/R2omar/Bank-System.git
```

---

## Start the infrastructure

```bash
docker-compose up
```

---

## Run each microservice

```bash
mvn spring-boot:run
```

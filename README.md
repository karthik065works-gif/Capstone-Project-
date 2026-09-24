# 🏦 Banking Application

A **monolithic RESTful Banking Application** built using **Spring Boot**, **Spring Data JPA**, and **H2 Database**.

The application provides APIs for managing **customers, bank accounts, deposits, withdrawals, transfers, and transaction history**. It also exposes an automatically generated **OpenAPI specification and Swagger UI** for API documentation and testing.

---

## 🚀 Features

### 👤 Customer Management

* Create a new customer
* Get all customers
* Get customer by ID
* Update customer using PUT
* Partially update customer using PATCH
* Activate customer
* Deactivate customer
* Validate customer information
* Prevent duplicate email addresses
* Prevent duplicate phone numbers
* Automatically generate customer numbers

### 🏦 Account Management

* Create Savings accounts
* Create Current accounts
* Retrieve account details
* Update account details
* Activate accounts
* Deactivate accounts
* Prevent duplicate account types for the same customer
* Validate initial account balance
* Close accounts
* Prevent account closure when the balance is non-zero
* Automatically generate account numbers

### 💰 Transaction Management

* Deposit money
* Withdraw money
* Transfer money between accounts
* Prevent withdrawals with insufficient balance
* Prevent transfers with insufficient balance
* Prevent transfers to the same account
* Maintain transaction history
* Filter transactions by transaction type
* Retrieve transaction using transaction reference
* Maintain balance-after-transaction information
* Generate transaction references
* Generate transfer references

### 📚 API Documentation

The application provides automatically generated OpenAPI documentation using SpringDoc.

**Swagger UI:**

```text
http://localhost:8080/swagger-ui.html
```

**OpenAPI JSON:**

```text
http://localhost:8080/v3/api-docs
```

---

# 🛠️ Technology Stack

| Technology        | Purpose                         |
| ----------------- | ------------------------------- |
| Java 17           | Programming language            |
| Spring Boot 4.0.8 | Application framework           |
| Spring Web MVC    | REST APIs                       |
| Spring Data JPA   | Database access                 |
| Hibernate         | ORM                             |
| H2 Database       | Database                        |
| Maven             | Build and dependency management |
| Spring Validation | Request validation              |
| SpringDoc OpenAPI | API documentation               |
| Swagger UI        | API exploration/testing         |
| Postman           | API testing                     |

---

# 🏗️ Architecture

The project follows a layered monolithic architecture.

```text
                    Client
                      |
                      v
              REST Controllers
                      |
                      v
                   Services
                      |
                      v
                 Repositories
                      |
                      v
                  Hibernate
                      |
                      v
                  H2 Database
```

### Project Structure

```text
src/
└── main/
    ├── java/
    │   └── com/
    │       └── icici/
    │           ├── BankingApplication.java
    │           │
    │           ├── controller/
    │           │   ├── CustomerController.java
    │           │   ├── AccountController.java
    │           │   └── TransactionController.java
    │           │
    │           ├── service/
    │           │   ├── CustomerService.java
    │           │   ├── AccountService.java
    │           │   └── TransactionService.java
    │           │
    │           ├── repository/
    │           │   ├── CustomerRepository.java
    │           │   ├── AccountRepository.java
    │           │   └── TransactionRepository.java
    │           │
    │           ├── model/
    │           │   ├── Customer.java
    │           │   ├── CustomerStatus.java
    │           │   ├── Account.java
    │           │   ├── AccountType.java
    │           │   ├── AccountStatus.java
    │           │   ├── AccountResponse.java
    │           │   ├── Transaction.java
    │           │   └── TransactionType.java
    │           │
    │           ├── dto/
    │           │   ├── CustomerRequest.java
    │           │   ├── CustomerResponse.java
    │           │   ├── CustomerPatchRequest.java
    │           │   ├── AccountRequest.java
    │           │   ├── TransactionRequest.java
    │           │   ├── TransferRequest.java
    │           │   └── TransactionResponse.java
    │           │
    │           └── exception/
    │               ├── CustomerNotFoundException.java
    │               ├── DuplicateCustomerException.java
    │               ├── AccountNotFoundException.java
    │               ├── AccountOperationException.java
    │               ├── TransactionNotFoundException.java
    │               └── GlobalExceptionHandler.java
    │
    └── resources/
        └── application.properties
```

---

# ⚙️ Prerequisites

Make sure the following are installed:

* Java 17 or later
* Maven
* Git
* IntelliJ IDEA / Eclipse / VS Code
* Postman (optional)

Verify Java:

```bash
java -version
```

Verify Maven:

```bash
mvn -version
```

---

# 📥 Installation

Clone the repository:

```bash
git clone <YOUR_GITHUB_REPOSITORY_URL>
```

Move into the project directory:

```bash
cd Banking-application
```

---

# ▶️ Running the Application

### Using IntelliJ IDEA

Open the project and run:

```text
BankingApplication.java
```

### Using Maven

On Windows:

```bash
mvnw.cmd spring-boot:run
```

On Linux/macOS:

```bash
./mvnw spring-boot:run
```

The application will start on:

```text
http://localhost:8080
```

---

# 🗄️ Database

The application uses **H2 Database**.

Database URL:

```text
jdbc:h2:file:./data/bankingdb
```

Username:

```text
sa
```

Password:

```text
(empty)
```

### H2 Console

Open:

```text
http://localhost:8080/h2-console
```

Use:

```text
JDBC URL: jdbc:h2:file:./data/bankingdb
User Name: sa
Password:
```

---

# 📖 API Documentation

After starting the application, Swagger UI can be accessed at:

```text
http://localhost:8080/swagger-ui.html
```

OpenAPI JSON:

```text
http://localhost:8080/v3/api-docs
```

Swagger UI provides an interactive interface for viewing and testing all available REST endpoints.

---

# 🔗 API Endpoints

## Customer APIs

### Create Customer

```http
POST /api/v1/customers
```

Example:

```json
{
  "name": "Arjun Menon",
  "email": "arjun.menon@test.com",
  "phone": "9876543210",
  "address": "Mumbai, Maharashtra",
  "dateOfBirth": "2001-04-15"
}
```

### Get All Customers

```http
GET /api/v1/customers
```

### Get Customer

```http
GET /api/v1/customers/{id}
```

### Full Update

```http
PUT /api/v1/customers/{id}
```

### Partial Update

```http
PATCH /api/v1/customers/{id}
```

### Activate Customer

```http
PATCH /api/v1/customers/{id}/activate
```

### Deactivate Customer

```http
PATCH /api/v1/customers/{id}/deactivate
```

---

# 🏦 Account APIs

### Create Account

```http
POST /api/v1/accounts/customer/{customerId}
```

Example:

```json
{
  "accountType": "SAVINGS",
  "initialBalance": 10000.00
}
```

Supported account types:

```text
SAVINGS
CURRENT
```

### Get Account

```http
GET /api/v1/accounts/{accountNumber}
```

### Update Account

```http
PUT /api/v1/accounts/{accountNumber}
```

### Activate Account

```http
PATCH /api/v1/accounts/{accountNumber}/activate
```

### Deactivate Account

```http
PATCH /api/v1/accounts/{accountNumber}/deactivate
```

### Close Account

```http
DELETE /api/v1/accounts/{accountNumber}
```

An account can only be closed when its balance is zero.

---

# 💰 Transaction APIs

## Deposit

```http
POST /api/v1/transactions/{accountNumber}/deposit
```

Example:

```json
{
  "amount": 2000.00,
  "description": "Salary deposit"
}
```

---

## Withdraw

```http
POST /api/v1/transactions/{accountNumber}/withdraw
```

Example:

```json
{
  "amount": 1000.00,
  "description": "ATM withdrawal"
}
```

---

## Transfer

```http
POST /api/v1/transactions/{accountNumber}/transfer
```

Example:

```json
{
  "destinationAccountNumber": "1000000002",
  "amount": 3000.00,
  "description": "Transfer to another account"
}
```

---

## Get Transaction History

```http
GET /api/v1/transactions/{accountNumber}/transactions
```

---

## Filter Transactions

```http
GET /api/v1/transactions/{accountNumber}/transactions/filter?type=DEPOSIT
```

Supported transaction types:

```text
DEPOSIT
WITHDRAWAL
TRANSFER_DEBIT
TRANSFER_CREDIT
```

---

## Get Transaction by Reference

```http
GET /api/v1/transactions/transactions/{transactionReference}
```

---

# 🔐 Validation & Business Rules

The application implements several validation and business rules.

### Customer

* Name is required
* Email is required and must be valid
* Phone must be a valid 10-digit Indian mobile number
* Date of birth is required
* Date of birth must be in the past
* Email must be unique
* Phone must be unique

### Account

* Account type is required
* Initial balance is required
* Initial balance cannot be negative
* A customer cannot have multiple accounts of the same account type
* Inactive customers cannot open accounts
* An account must have an associated customer

### Transactions

* Transaction amount must be greater than zero
* Withdrawals cannot exceed the available balance
* Transfers cannot exceed the available balance
* Source and destination accounts cannot be the same
* Transactions can only be performed on active accounts
* Accounts cannot be closed while they have a non-zero balance

---

# ❌ Exception Handling

The application uses a centralized exception handler:

```text
GlobalExceptionHandler
```

Examples of application errors include:

```text
CUSTOMER_NOT_FOUND
DUPLICATE_CUSTOMER
ACCOUNT_NOT_FOUND
ACCOUNT_OPERATION_ERROR
TRANSACTION_NOT_FOUND
VALIDATION_ERROR
```

Validation failures return HTTP `400`.

Resource-not-found errors return HTTP `404`.

Business-operation failures are handled using appropriate error responses.

---

# 🧪 Testing

The APIs can be tested using:

* Postman
* Swagger UI
* cURL

A complete manual test flow covers:

```text
Customer Creation
       ↓
Customer Updates
       ↓
Account Creation
       ↓
Account Activation/Deactivation
       ↓
Deposit
       ↓
Withdrawal
       ↓
Transfer
       ↓
Transaction History
       ↓
Transaction Filtering
       ↓
Account Closure
```

Example cURL request:

```bash
curl -X GET http://localhost:8080/api/v1/customers
```

---

# 🔄 Example Banking Flow

### Step 1 — Create Customer

```text
POST /api/v1/customers
```

### Step 2 — Create Savings Account

```text
POST /api/v1/accounts/customer/{customerId}
```

### Step 3 — Deposit Money

```text
POST /api/v1/transactions/{accountNumber}/deposit
```

### Step 4 — Withdraw Money

```text
POST /api/v1/transactions/{accountNumber}/withdraw
```

### Step 5 — Transfer Money

```text
POST /api/v1/transactions/{accountNumber}/transfer
```

### Step 6 — View Transactions

```text
GET /api/v1/transactions/{accountNumber}/transactions
```

### Step 7 — Close Account

The account must first have a zero balance:

```text
DELETE /api/v1/accounts/{accountNumber}
```

---

# 📌 Configuration

Main configuration is located at:

```text
src/main/resources/application.properties
```

Current application configuration includes:

```properties
spring.application.name=Banking-application
server.port=8080

spring.datasource.url=jdbc:h2:file:./data/bankingdb
spring.datasource.driver-class-name=org.h2.Driver
spring.datasource.username=sa
spring.datasource.password=

spring.h2.console.enabled=true
spring.h2.console.path=/h2-console

spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
spring.jpa.properties.hibernate.format_sql=true

springdoc.api-docs.enabled=true
springdoc.api-docs.path=/v3/api-docs
springdoc.swagger-ui.enabled=true
springdoc.swagger-ui.path=/swagger-ui.html
```

---

# 📦 Build

Build the application using Maven:

```bash
mvn clean package
```

Run the generated JAR:

```bash
java -jar target/Banking-application-*.jar
```

---

# 🧑‍💻 Development

The project follows a layered architecture:

```text
Controller
    ↓
Service
    ↓
Repository
    ↓
Database
```

### Controller Layer

Handles:

* HTTP requests
* Request mapping
* Request validation
* HTTP responses

### Service Layer

Contains:

* Business logic
* Account operations
* Transaction operations
* Customer operations
* Business validations

### Repository Layer

Handles:

* Database operations
* Entity persistence
* Custom queries

### Model Layer

Contains JPA entities and enums.

### DTO Layer

Contains request and response objects used by the REST APIs.

### Exception Layer

Contains custom exceptions and centralized exception handling.

---

# 🔮 Future Improvements

Possible future enhancements include:

* Authentication and authorization using Spring Security
* JWT-based authentication
* Role-based access control
* PostgreSQL/MySQL production database
* Docker support
* Unit tests with JUnit and Mockito
* Integration tests
* Transaction pagination
* Account statements
* Beneficiary management
* Scheduled payments
* Notification service
* Email/SMS notifications
* Audit logging
* CI/CD pipeline
* Production monitoring and logging

---

# 📄 License

This project is intended for educational and development purposes.

---

# 👨‍💻 Author

**Karthik S**

Computer Science & Engineering
NIT Trichy

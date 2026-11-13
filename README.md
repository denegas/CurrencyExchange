# Currency Exchange REST API

A REST service for managing currencies, exchange rates, and converting money amounts.
Built on plain Jakarta Servlets without Spring, but with layered architecture.

![Java](https://img.shields.io/badge/Java-21-orange)
![Jakarta Servlet](https://img.shields.io/badge/Jakarta%20Servlet-6.1-blue)
![SQLite](https://img.shields.io/badge/SQLite-3.51-lightgrey)
![Maven](https://img.shields.io/badge/Maven-3.9+-red)
<!-- ![License](https://img.shields.io/badge/License-MIT-green) -->



## About the project

The project demonstrates building a backend application without frameworks:
all infrastructure — DI, JSON serialization, validation,
error handling — is set up manually. The main goal is to solidify
fundamental Java Backend concepts: Servlet API, JDBC, layered architecture,
the HTTP protocol, and REST design.

---

## Features

### Currencies
- Get a list of all currencies
- Find a currency
- Add a new currency

### Exchange rates
- Get a list of all rates
- Get a rate for a currency pair (e.g. `USDEUR`)
- Add a new rate
- Update an existing rate (PATCH)

### Conversion
- Calculate an amount using the direct rate `A → B`
- Calculate using the reverse rate `B → A`
- Calculate using the cross-rate via USD (`rate(USD→B) / rate(USD→A)`)

### Reliability
- Strict validation of input parameters
- Centralized exception handling (unified JSON error format)
- Guaranteed `Content-Type: application/json; charset=UTF-8`

---

## Tech stack

| Technology            | Purpose                                        | Version  |
|-----------------------|------------------------------------------------|----------|
| Java                  | Language                                       | 21       |
| Jakarta Servlet API   | Servlet and filter specification               | 6.1.0    |
| SQLite JDBC           | Relational DB                                  | 3.51.1.0 |
| Jackson Databind      | JSON serialization                             | 2.18.2   |
| MapStruct             | Entity ↔ DTO mapping                           | 1.6.3    |
| JUnit Jupiter         | Unit testing framework                         | 5.11.4   |
| Maven                 | Build (war)                                    | 3.9+     |
| maven-compiler-plugin | Compilation + MapStruct annotation processing	 | 3.11.0   |
| maven-surefire-plugin | Test runner                                    | 3.2.5    |


## Architecture

```
[ HTTP Request ]
       │
       ▼
[ Filters ]
       │
       ▼
[ Servlets (Controller) ] ──▶ [ Request DTOs ]
       │
       ▼
[ Services (Business Logic) ] ──▶ [ Currency / Exchange Logic ]
       │
       ▼
[ DAOs (Data Access) ] ──▶ [ JDBC / SQLite ]
       │
       ▼
[ SQLite Database ]
```

### Key Decisions

* **Manual DI** — singletons (DAOs, Services, Validators, `ObjectMapper`) are created once in a `AppInitializerListener` and stored in the servlet context. Servlets receive their dependencies in `init()`.
* **DTO layer** — HTTP parameters are packed into Java `Record` DTOs; services do not depend on the Servlet API.
* **Centralized error handling** — business exceptions are translated into a unified JSON error format (`{"message": "..."}`).
* **MapStruct mapping** — Entity ↔ DTO conversion is generated at compile time (no reflection at runtime).

## Project Structure

```
src/
├── main/
│ ├── java/com/github/denegas/
│ │ ├── config/ # App configuration, DI, db connection
│ │ ├── controller/ # HTTP layer
│ │ │ ├── filter/ # Request filters (error handling, encoding)
│ │ │ ├── servlet/ # Jakarta Servlets
│ │ │ └── utils/ # Request/Response utilities (param validation, JSON sending)
│ │ ├── exception/ # Custom exceptions and error utils
│ │ └── model/ # Core business models and layers
│ │     ├── dao/ # Data Access Objects (JDBC implementation)
│ │     ├── dto/ # Data Transfer Objects (Request/Response records)
│ │     ├── entity/ # Database entities (POJOs)
│ │     ├── mapper/ # MapStruct mappers (Entity ↔ DTO)
│ │     ├── service/ # Business logic
│ │     ├── utils/ # Domain utilities
│ │     └── validator/ # Input validation logic
│ ├── resources/
│ │ ├── indexes.sql 
│ │ └── schema.sql 
│ └── webapp/ # Web application root (web.xml, static files)
└── test/ # Unit tests
``` 

## Database Schema

```sql
CREATE TABLE IF NOT EXISTS Currencies
(
    id       INTEGER PRIMARY KEY AUTOINCREMENT,
    code     VARCHAR(3) UNIQUE NOT NULL,
    full_name VARCHAR(64),
    sign     VARCHAR(6)
);
CREATE TABLE IF NOT EXISTS ExchangeRates
(
    id               INTEGER PRIMARY KEY AUTOINCREMENT,
    base_currency_id   INT REFERENCES Currencies (id),
    target_currency_id INT REFERENCES Currencies (id),
    rate             DECIMAL(6)
);
```

## REST API

**Base URL:** `http://localhost:8080/`

### Currencies

| Method | Endpoint           | Parameters                   | Description            | Status Codes        |
|:-------|:-------------------|:-----------------------------|:-----------------------|:--------------------|
| `GET`  | `/currencies`      | —                            | List of all currencies | `200`               |
| `GET`  | `/currency/{code}` | path: `{code}`               | Currency by ISO code   | `200`, `400`, `404` |
| `POST` | `/currencies`      | form: `code`, `name`, `sign` | Add a new currency     | `201`, `400`, `409` |

### Exchange Rates

| Method  | Endpoint               | Parameters                                             | Description                | Status Codes               |
|:--------|:-----------------------|:-------------------------------------------------------|:---------------------------|:---------------------------|
| `GET`   | `/exchangeRates`       | —                                                      | List of all exchange rates | `200`                      |
| `GET`   | `/exchangeRate/{pair}` | path: `{pair}` (e.g., `USDEUR`)                        | Rate for a specific pair   | `200`, `400`, `404`        |
| `POST`  | `/exchangeRates`       | form: `baseCurrencyCode`, `targetCurrencyCode`, `rate` | Add a new exchange rate    | `201`, `400`, `404`, `409` |
| `PATCH` | `/exchangeRate/{pair}` | path: `{pair}` + form: `rate`                          | Update an existing rate    | `200`, `400`, `404`        |

### Exchanging

| Method | Endpoint    | Query Parameters       | Description                          | Status Codes        |
|:-------|:------------|:-----------------------|:-------------------------------------|:--------------------|
| `GET`  | `/exchange` | `from`, `to`, `amount` | Convert an amount between currencies | `200`, `400`, `404` |

## Request Examples

### Add a currency

```bash
curl -X POST http://localhost:8080/currencies -H "Content-Type: application/x-www-form-urlencoded" -d 'code=USD&name=US Dollar&sign=$'
```

```json
{
  "id": 1,
  "code": "USD",
  "name": "US Dollar",
  "sign": "$"
}
```

### Get a pair rate

```bash
curl http://localhost:8080/exchangeRate/USDEUR
```

```json
{
  "id": 1,
  "baseCurrency":   { "code": "USD", "name": "US Dollar",  "sign": "$" },
  "targetCurrency": { "code": "EUR", "name": "Euro",       "sign": "€" },
  "rate": 0.92
}
```

### Convert an amount

```bash
curl "http://localhost:8080/exchange?from=USD&to=EUR&amount=100"
```

```json
{
  "baseCurrency":   { "code": "USD", "name": "US Dollar", "sign": "$" },
  "targetCurrency": { "code": "EUR", "name": "Euro",      "sign": "€" },
  "rate": 0.92,
  "amount": 100,
  "convertedAmount": 92.00
}
```

### Error Format

All errors are returned in a unified JSON structure:

```json
{
  "message": "currency not found"
}
```

## Running the Application

You can run this project in two ways: using Docker (the fastest way), or manually building it from source.

### Option 1: Running with Docker

The quickest way to get started. You don't need Java, Maven, or Tomcat installed locally.

**1. Pull the image from Docker Hub:**
```bash
docker pull dannydeploy/currency-exchange:latest
```

**2. Run the container:**
```bash
docker run -d -p 8080:8080 --name currency-exchange dannydeploy/currency-exchange:latest
```

**3. Access the API:**
The service is now running at: http://localhost:8080/

**4. Stop and remove the container:**

```bash
docker stop currency-exchange
docker rm currency-exchange
```

### Option 2: Running Manually

Use this method if you want to modify the code or run it in your own servlet container.

### Requirements
* JDK 21
* Apache Maven 3.9+
* Apache Tomcat 10.1+ (Jakarta EE 10)

**1. Clone the repository**
```bash
git clone https://github.com/denegas/CurrencyExchange.git
cd CurrencyExchange
```
**2. Build the WAR**
```bash
mvn clean package
```

**3. Deploy to Tomcat**

Copy the built .war file to your Tomcat webapps directory and start the server:
```bash
cp target/CurrencyExchange-1.0-SNAPSHOT.war $CATALINA_HOME/webapps/
$CATALINA_HOME/bin/startup.sh   # Use startup.bat on Windows
```
**4. Access the application**

REST API: http://localhost:8080/

## 📄 License

MIT License — see the [LICENSE](LICENSE) file for details.


# Swag Labs – Dockerized Test Automation

Selenium + TestNG UI test suite for [Swag Labs](https://www.saucedemo.com), fully containerized with Docker and integrated into a GitHub Actions CI pipeline.

---

## Project Structure

```
swag-labs-tests/
├── src/test/java/com/swaglabs/
│   ├── base/
│   │   └── BaseTest.java          # WebDriver setup & teardown
│   ├── pages/                     # Page Object Model
│   │   ├── LoginPage.java
│   │   ├── InventoryPage.java
│   │   ├── CartPage.java
│   │   └── CheckoutPage.java
│   └── tests/                     # TestNG test classes
│       ├── LoginTest.java         # 5 login scenarios
│       ├── CartTest.java          # 6 cart scenarios
│       └── CheckoutTest.java      # 7 checkout scenarios
├── testng.xml                     # TestNG suite definition
├── pom.xml                        # Maven dependencies
├── Dockerfile                     # Multi-stage Docker build
├── .dockerignore
└── .github/workflows/
    └── test-pipeline.yml          # GitHub Actions CI pipeline
```

---

## Test Coverage

| Module   | Test Cases |
|----------|-----------|
| Login    | Valid login, invalid credentials, empty username, empty password, locked-out user |
| Cart     | Add item, add multiple items, cart contents, remove item, page title, continue shopping |
| Checkout | Step one loads, empty fields, missing name, step two reached, order total, complete order, back to home |

---

## Prerequisites

- Docker Desktop installed and running
- (Optional) Java 11 + Maven 3.9+ for local runs

---

## Running Tests Locally (without Docker)

```bash
mvn test
```

---

## Running Tests with Docker

### 1. Build the image
```bash
docker build -t swag-labs-tests .
```

### 2. Run the tests
```bash
docker run --rm --shm-size=2gb swag-labs-tests
```

> `--shm-size=2gb` prevents Chrome from crashing due to low shared memory.

### 3. Copy test reports to host (optional)
```bash
docker run --rm \
  --shm-size=2gb \
  -v $(pwd)/test-results:/app/target/surefire-reports \
  swag-labs-tests
```
Reports will appear in `./test-results/` on your machine.

---

## CI Pipeline (GitHub Actions)

The pipeline triggers on every push or pull request to `main`/`master`:

1. **Checkout** – pulls the repository
2. **Docker Buildx** – enables layer caching
3. **Build image** – `docker build`
4. **Run tests** – `docker run` inside CI runner
5. **Upload reports** – TestNG XML + HTML reports saved as workflow artifacts
6. **Log on failure** – prints container logs if tests fail

---

## Key Design Decisions

- **Headless Chrome** – configured via `--headless=new` flag in `BaseTest.java`
- **WebDriverManager** – auto-downloads matching ChromeDriver; no manual driver setup needed
- **Multi-stage Dockerfile** – build stage compiles code; runtime stage installs Chrome, keeping image layers clean
- **Page Object Model** – each page has its own class, keeping tests readable and maintainable

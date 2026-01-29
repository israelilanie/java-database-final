# Retail Management Backend System

Backend services for the Retail Management final project. This repository contains:

- **Spring Boot API** (`back-end/`) for stores, products, inventory, orders, customers.
- **MongoDB reviews** for unstructured customer feedback.
- **Static admin UI** (`front-end/`) used by warehouse staff.

## User Stories
See [docs/USER_STORIES.md](docs/USER_STORIES.md) for the admin-focused user stories that guide the feature set.

## Architecture & Data
- **Database schema:** [docs/DATABASE_SCHEMA.md](docs/DATABASE_SCHEMA.md)
- **REST API overview:** [docs/API.md](docs/API.md)
- **Stored procedures:** [docs/STORED_PROCEDURES.md](docs/STORED_PROCEDURES.md)

## Quick Start (Local)

### Prerequisites
- Java 17+
- Maven 3+
- MySQL 8+
- MongoDB 6+

### Configure the backend
Update `back-end/src/main/resources/application.properties` with your database credentials (or set environment variables):

```properties
spring.datasource.url=jdbc:mysql://localhost:3306/inventory
spring.datasource.username=root
spring.datasource.password=your_password
spring.data.mongodb.uri=mongodb://localhost:27017/reviews
```

### Run the API
```bash
cd back-end
./mvnw spring-boot:run
```

### Load sample data
```bash
mysql -u root -p inventory < insert_data.sql
```

### Open the admin UI
Open `front-end/index.html` in your browser (or serve it with any static file server). Set `apiURL` in `front-end/script.js` to your API base URL.

## Docker
Use Docker Compose to run the full stack locally.

```bash
docker compose up --build
```

See [docs/DOCKER.md](docs/DOCKER.md) for details.

## Project Portfolio Notes
This repository includes the backend, schema documentation, and a full containerized stack to make the project easy to evaluate. See [docs/PORTFOLIO.md](docs/PORTFOLIO.md).

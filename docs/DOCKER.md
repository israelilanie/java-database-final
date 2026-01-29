# Docker Setup

This project ships with a Docker Compose stack that runs:
- MySQL
- MongoDB
- Spring Boot API
- Static frontend (Nginx)

## Run
```bash
docker compose up --build
```

## Services
- API: `http://localhost:8080`
- UI: `http://localhost:3000`
- MySQL: `localhost:3306`
- MongoDB: `localhost:27017`

## Environment Variables
The compose file uses these defaults (override as needed):

- `MYSQL_DATABASE=inventory`
- `MYSQL_USER=inventory_user`
- `MYSQL_PASSWORD=inventory_pass`
- `MYSQL_ROOT_PASSWORD=rootpass`
- `MONGO_INITDB_DATABASE=reviews`

## Notes
- The database schema and sample data are loaded from `insert_data.sql`.
- The API uses environment variables to connect to MySQL and MongoDB.

# Spring Boot Application - Database Demo

This is a **Spring Boot Application** configured to connect to a MySQL database using **Docker Compose**. The application is containerized, leveraging Maven for dependency management and packaging.

---

## Table of Contents
1. [Features](#features)
2. [Technologies Used](#technologies-used)
3. [Requirements](#requirements)
4. [Setup Instructions](#setup-instructions)
    - [Running with Docker Compose](#running-with-docker-compose)
    - [Database Configuration](#database-configuration)
5. [Environment Variables](#environment-variables)
6. [Building and Running Locally](#building-and-running-locally)
7. [Endpoints](#endpoints)
8. [Troubleshooting](#troubleshooting)

---

## Features
- Connects to a **MySQL database**.
- Uses Hibernate ORM for database interactions.
- Includes **JWT-based security**.
- Containerized with Docker for easy deployment.
- Logs SQL queries and enables debug-level logging for security filters.

---

## Technologies Used
- **Spring Boot 3.3.5** (RESTful services, JPA)
- **MySQL 8.0**
- **Hibernate ORM** (DDL auto-updates enabled)
- **JWT for Authentication**
- **Docker Compose** (Multi-container setup)
- **Maven** (Build and dependency management)

---

## Requirements
- **Docker** and **Docker Compose** (for containerized deployment)
- **JDK 17 or later** (for local development)
- **Maven** (for building the application)

---

## Setup Instructions

### Running with Docker Compose
1. **Clone the Repository**:
   ```bash
   git clone <repository-url>
   cd <repository-folder>
   ```

2. **Run the Application**:
   Ensure Docker and Docker Compose are installed, then execute:
   ```bash
   docker-compose up --build
   ```
   This will:
    - Build the `api_service` Docker image.
    - Start the MySQL database (`db` service).
    - Launch the Spring Boot application (`api_service`) on port `8080`.

3. **Access the Application (Postman recommended)**:
    - API: [http://localhost:8080](http://localhost:8080)

---

### Database Configuration
The MySQL database is configured with:
- Database: `basics`
- User: `test`
- Password: `pw`

**Connection URL**: `jdbc:mysql://db:3306/basics?allowPublicKeyRetrieval=true`

---

## Environment Variables

### Application Configuration
These are set in `docker-compose.yml` for the `api_service` container:
| Environment Variable               | Description                                      |
|------------------------------------|--------------------------------------------------|
| `spring.datasource.url`            | Database connection URL.                        |
| `spring.datasource.username`       | Database username.                              |
| `spring.datasource.password`       | Database password.                              |
| `token.secret.key`                 | Secret key for JWT token generation.            |
| `token.expirationms`               | JWT token expiration time in milliseconds.      |

### Database Configuration
These are set in `docker-compose.yml` for the `db` service:
| Environment Variable  | Description              |
|-----------------------|--------------------------|
| `MYSQL_DATABASE`      | Database name.           |
| `MYSQL_USER`          | MySQL user.              |
| `MYSQL_PASSWORD`      | MySQL password.          |
| `MYSQL_ROOT_PASSWORD` | Root password for MySQL. |

---

## Building and Running Locally
1. **Install Dependencies**:
   Ensure Maven is installed, then run:
   ```bash
   mvn clean install
   ```

2. **Run the Application**:
   ```bash
   mvn spring-boot:run
   ```

3. **Access the Application**:
    - API: [http://localhost:8080](http://localhost:8080)

4. **Configure MySQL Locally**:
   Update `application.properties` to point to your local database:
   ```properties
   spring.datasource.url=jdbc:mysql://localhost:3306/basics
   spring.datasource.username=test
   spring.datasource.password=pw
   ```

---

## Endpoints

### Example Endpoints
| Method | Endpoint     | Description         |
|--------|--------------|---------------------|
| `GET`  | `/cars/1`    | Fetch all cars.     |
| `POST` | `/cars/addCar` | Create a new car.   |
| `GET`  | `/cars/{id}` | Fetch a car by ID.  |
| `PUT`  | `/cars/{id}` | Update a car by ID. |
| `DELETE` | `/cars/{id}` | Delete a car by ID. |

Here's a section for your README that explains the process for creating a user, logging in, and using the bearer token for car management endpoints:


### User Registration, Login, and Accessing Car Management Endpoints

To interact with the car management API, you must first **register a user** and **log in** to obtain a **Bearer token** for authentication. This token is required to access the protected car management endpoints.

#### 1. **User Registration (Sign Up)**

To create a new user, you need to send a **POST request** to the `/signup` endpoint with the following fields in the **JSON body**:

- **name**: The name of the user (string)
- **mail**: The email address of the user (string)
- **password**: The password for the user (string)

**Example Request**:
```bash
POST http://localhost:8080/signup
Content-Type: application/json

{
  "name": "John Doe",
  "mail": "john.doe@example.com",
  "password": "securepassword123"
}
```

**Response**:
- Upon successful registration, the response will confirm that the user was created.
- Example Response:
  ```json
  {
    "message": "jwtlikestring"
  }
  ```

#### 2. **User Login (Authentication)**

After registering, you can log in to the application by sending a **POST request** to the `/login` endpoint. You will need to include the **email** and **password** in the **JSON body**.

**Example Request**:
```bash
POST http://localhost:8080/login
Content-Type: application/json

{
  "mail": "john.doe@example.com",
  "password": "securepassword123"
}
```

**Response**:
- Upon successful login, the response will include a **JWT Bearer token** that you will need for authentication in subsequent requests.
- Example Response:
  ```json
  {
    "token": "your-jwt-bearer-token"
  }
  ```

#### 3. **Accessing Protected Car Management Endpoints**

Once you have obtained the **JWT Bearer token** from the login response, you can use it to authenticate requests to protected endpoints (like car management endpoints).

To use the **Bearer token**, you must include it in the **Authorization header** of your requests as follows:

**Example Request to Manage Cars**:
```bash
GET http://localhost:8080/cars
Authorization: Bearer your-jwt-bearer-token
```

- Replace `your-jwt-bearer-token` with the actual token obtained from the login step.

The token will authenticate your request and grant access to the car management endpoints.

---

This process ensures that only authenticated users can interact with car-related data, and the token will be required for every request that interacts with protected resources.

---

## Troubleshooting

1. **MySQL Connection Issues**:
    - Verify that the `db` service is running:
      ```bash
      docker-compose ps
      ```
    - Check logs for errors:
      ```bash
      docker-compose logs db
      ```

2. **Application Fails to Start**:
    - Ensure the `api_service` container can connect to the `db` service. Test using:
      ```bash
      docker exec -it <api_service_container_name> ping db
      ```

3. **Schema Not Created Automatically**:
    - Confirm `spring.jpa.hibernate.ddl-auto=update` is set in `application.properties`.

---


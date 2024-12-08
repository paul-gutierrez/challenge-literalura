# Challenge Literalura

## Description
Challenge Literalura is an application designed to manage books and authors obtained from the public API [Gutendex](https://gutendex.com). The application allows users to perform various operations such as searching for books by title, displaying registered books, listing authors, and filtering books by language.

---

## Main Features
- **Search for books**: Search for books by title using the Gutendex API and save them to a local database.
- **Manage authors and books**: Register books and authors, and view detailed information.
- **Dynamic filtering**: Filter books by language, dynamically showing only those available in the database.
- **User-friendly console interface**: Interact with the system through a simple menu.

---

## Initial Configuration

### Requirements
- **Java 17 or higher**
- **Spring Boot** initialized with [start.spring.io](https://start.spring.io)
- **PostgreSQL** as the database

### Database Configuration
In the `application.properties` file, configure your database connection settings.

#### Environment Variables or Direct Replacement:
1. Configure the following system variables or replace the placeholders directly in the `application.properties` file:
    ```properties
    spring.datasource.url=jdbc:postgresql://${DB_HOST}/challenge_literalura
    spring.datasource.username=${DB_USER}
    spring.datasource.password=${DB_PASSWORD}
    ```
    - **`${DB_HOST}`**: Address of the PostgreSQL server (e.g., `localhost`).
    - **`${DB_USER}`**: Database username.
    - **`${DB_PASSWORD}`**: User password.
2. Replace `challenge_literalura` with the name of your database.

---

## Dependencies
Although the dependencies are already declared in the `pom.xml` file, here is a summary:

```xml
<dependencies>
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-data-jpa</artifactId>
    </dependency>
    <dependency>
        <groupId>org.postgresql</groupId>
        <artifactId>postgresql</artifactId>
        <scope>runtime</scope>
    </dependency>
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-test</artifactId>
        <scope>test</scope>
    </dependency>
    <dependency>
        <groupId>com.fasterxml.jackson.core</groupId>
        <artifactId>jackson-databind</artifactId>
        <version>2.16.0</version>
    </dependency>
    <dependency>
        <groupId>org.json</groupId>
        <artifactId>json</artifactId>
        <version>20210307</version>
    </dependency>
</dependencies>
```
---

## How to Use the Application

### 1. Start the Application
Run the **ChallengeLiteraluraApplication.java** class from your IDE or via the command line:

```bash
./mvnw spring-boot:run
```

### 2. Interactive Menu
Once started, the application will display a menu allowing you to:

- **1 - Search for a book by title**: Look for a book in the Gutendex API.
- **2 - Display registered books**: View books stored in the database.
- **3 - Display registered authors**: List all authors in the database.
- **4 - Show authors alive in a specific year**: Filter authors who were alive in a given year.
- **5 - Display books by language**: Filter and display books by selected language.
- **0 - Exit**: Close the application.

### 3. Filter Books by Language
Select the option **"5 - Display books by language."** A dynamic list of languages based on the registered books will appear. Choose a language to view the available books.

---

## API Used
This application uses the public Gutendex API to retrieve book and author data. More information: [https://gutendex.com](https://gutendex.com).

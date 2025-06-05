## 🐱 KittySecurity Backend
KittySecurity Backend to aplikacja serwerowa stworzona w technologii Spring Boot, stanowiąca część systemu KittySecurity. Projekt ten obsługuje logikę biznesową, zarządzanie użytkownikami oraz integrację z bazą danych.

## 🚀 Technologie
- Java 17

- Spring Boot 3.x

- Maven

- Spring Security

- JWT (JSON Web Tokens)

- PostgreSQL / H2

## 📁 Struktura projektu
```
kittysecurity-backend/
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   └── pl/
│   │   │       └── edu/
│   │   │           └── pk/
│   │   │               └── student/
│   │   │                   └── kittysecurity/
│   │   │                       ├── config/
│   │   │                       ├── controller/
│   │   │                       ├── model/
│   │   │                       ├── repository/
│   │   │                       └── service/
│   │   └── resources/
│   │       ├── application.properties
│   │       └── ...
├── .gitignore
├── mvnw
├── mvnw.cmd
├── pom.xml
└── README.md                           
```
## ⚙️ Konfiguracja
### 1. Klonowanie repozytorium:
```
git clone https://github.com/KittySecurity/kittysecurity-backend.git
cd kittysecurity-backend
```
### 2. Konfiguracja bazy danych:

Upewnij się, że plik application.properties zawiera poprawne dane konfiguracyjne dla Twojej bazy danych:
```
spring.datasource.url=jdbc:postgresql://localhost:5432/kittysecurity
spring.datasource.username=twoja_nazwa_uzytkownika
spring.datasource.password=twoje_haslo
```
### 3. Budowanie projektu:
```
./mvnw clean install
```
### 4. Uruchamianie aplikacji:
```
./mvnw spring-boot:run
```
Aplikacja będzie dostępna pod adresem: http://localhost:8080

### 5. Example dockerfile usage
Create image
```
mvn package
docker image build -t kittysecurity:latest .
```
Run container
```
docker run -e DB_USERNAME=example_username -e DB_PASSWORD=example_password -e JWT_PROP_SECRETKEY=example_password -p 8080:8080 kittysecurity
```

## 🔐 Uwierzytelnianie
Projekt wykorzystuje JWT do uwierzytelniania użytkowników. Po zalogowaniu użytkownik otrzymuje token, który należy dołączać do nagłówków kolejnych żądań:
```
Authorization: Bearer <token>
```
## 🧪 Testowanie
Aby przetestować endpointy, możesz użyć narzędzi takich jak:

- Postman

- curl

Przykład użycia curl:
```
curl -X POST http://localhost:8080/api/auth/login \
     -H "Content-Type: application/json" \
     -d '{"username": "user@example.com", "password": "twoje_haslo"}'
```

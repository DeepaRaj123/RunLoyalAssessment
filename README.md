# 🏆 RunLoyal Assessment API

## 📖 Overview
RunLoyal Assessment is a **Spring Boot** application implementing **OAuth2 authentication** and **MongoDB** as the database.  
It provides **JWT-based authentication**, **role-based access control**, and **Swagger API documentation**.

---

## 🚀 Features
✅ **User Registration & Authentication (JWT)**  
✅ **Role-Based Access Control (Admin & User)**  
✅ **MongoDB Integration**  
✅ **Secure API Endpoints with Spring Security**  
✅ **OpenAPI Documentation (Swagger UI)**  
✅ **Unit Testing with JUnit**  
✅ **Uses `.env` for Environment Variables**  

---

## 📌 Tech Stack
- **Backend**: Java 17, Spring Boot 3.x  
- **Database**: MongoDB  
- **Security**: JWT, Spring Security  
- **API Documentation**: SpringDoc OpenAPI (Swagger)  
- **Build Tool**: Maven  
- **Environment Variables**: dotenv-java  

---

## 🔧 Installation & Setup

### **1️⃣ Clone the Repository**
```sh
git clone https://github.com/DeepaRaj123/RunLoyalAssessment.git
cd Runloyal-Assessment

2️⃣ Create a .env File
Create a .env file in the project root and add:
MONGO_URI=mongodb+srv://your_username:your_password@cluster.mongodb.net/your_dbname
JWT_SECRET=my_super_secret_key
3️⃣ Set Up Environment Variables (Optional)
If not using .env, set them manually:

Linux/macOS
export MONGO_URI="your-mongodb-uri"
export JWT_SECRET="your-jwt-secret"

Windows (PowerShell)
$env:MONGO_URI="your-mongodb-uri"
$env:JWT_SECRET="your-jwt-secret"

4️⃣ Build & Run the Application
Using Maven
mvn clean install
mvn spring-boot:run

Using Docker
docker build -t runloyal-assessment .
docker run -p 8080:8080 --env-file .env runloyal-assessment

🔑 API Authentication
All endpoints require JWT authentication, except for Signup/Login.

1️⃣ Signup

POST /api/auth/signup
Request Body:
{
  "firstName": "John",
  "lastName": "Doe",
  "email": "john@example.com",
  "password": "securepassword"
}
Response:
{
  "status": "success",
  "message": "User registered successfully",
  "token": "jwt-token"
}

2️⃣ Login
POST /api/auth/signin
Request Body:
{
  "email": "john@example.com",
  "password": "securepassword"
}
Response:
{
  "status": "success",
  "message": "User logged in successfully",
  "token": "jwt-token"
}

3️⃣ Fetch All Users (Admin Only)
GET /api/users
Authorization: Bearer jwt-token

4️⃣ Update User Profile
PUT /api/user/update/{id}
Authorization: Bearer jwt-token

📘 API Documentation (Swagger)
Once the application is running, visit:
📌 Swagger UI → http://localhost:8080/swagger-ui.html
📌 API Docs → http://localhost:8080/v3/api-docs

✅ Running Tests
Run all tests using:
mvn test

📂 Project Structure

RunLoyal-Assessment/
│── src/main/java/com/assessment
│   ├── controller/      # API Controllers
│   ├── model/           # Data Models (User)
│   ├── repository/      # MongoDB Repository
│   ├── security/        # JWT, Security Config
│   ├── service/         # Business Logic
│   ├── RunLoyalAssessmentApplication.java  # Main Spring Boot Application
│── src/main/resources/
│   ├── application.yml  # Configurations
│   ├── .env             # Environment Variables (Excluded from Git)
│── pom.xml              # Maven Dependencies
│── README.md            # Project Documentation
│── .gitignore           # Ignore unnecessary files

🛠️ Built With
Spring Boot

MongoDB

Spring Security

SpringDoc OpenAPI

JUnit 5

JWT (Json Web Token)

Maven

📜 License
This project is licensed under the Apache License 2.0.

📩 Contact
👤 Deepa K M
📧 deeparaj096@gmail.com


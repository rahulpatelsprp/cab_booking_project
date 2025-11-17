# 🚖 Cab Booking System (Microservices + React + Spring Boot)

![Java](https://img.shields.io/badge/Java-17-blue)
![Spring Boot](https://img.shields.io/badge/SpringBoot-3.0-green)
![React](https://img.shields.io/badge/React-18-blue)
![Vite](https://img.shields.io/badge/Vite-4.0-yellow)
![MySQL](https://img.shields.io/badge/MySQL-8.0-orange)
![License](https://img.shields.io/badge/License-MIT-lightgrey)

---

## 📌 Overview
The **Cab Booking System** is a full-stack microservices application designed to streamline online cab reservations. It enables users to book rides, manage drivers, make payments, and provide ratings.

**Built with:**
- **Frontend:** React (Vite) for a fast and modern UI
- **Backend:** Spring Boot microservices with Spring Security, Spring Data JPA, and MySQL
- **Architecture:** Microservices with API Gateway and Eureka Service Discovery

---

## 🛠️ Tech Stack
### **Frontend**
- React.js, Vite, React Router, Axios, Tailwind CSS, React-Toastify
- JWT Authentication

### **Backend**
- Spring Boot, Spring Security, Spring Data JPA
- Spring Cloud (API Gateway, Eureka Discovery)
- MySQL Database
- JWT for authentication

---

## 🎯 Features
- **User Service:** Registration, Login, JWT Authentication
- **Ride Service:** Book rides, View history, Track status
- **Driver Service:** Manage drivers, Accept/Reject rides
- **Rating Service:** Feedback, Driver ratings
- **Payment Service:** Fare calculation, Payment processing
- **Gateway & Discovery:** API Gateway routing, Eureka service registry

---

## 🏗️ Architecture
![Architecture Diagram](./docs/architecture.png)  
*(Diagram placeholder – add your architecture image here)*

---

## 📸 Screenshots
### **Login Page**
![Login Page](./assets/Screenshot%202025-11-17%20114356.png)

*(Add more screenshots for Ride Booking, Driver Dashboard, Payment Page)*

---

## ⚙️ Installation & Setup

### **Frontend**
```bash
# Create project
npm create vite@latest cab-booking-frontend

# Move into folder
cd cab-booking-frontend

# Install dependencies
npm install react-router-dom axios react-toastify tailwindcss postcss autoprefixer

# Setup Tailwind CSS
npx tailwindcss init -p

# Run development server
npm run dev
```
Access at: **http://localhost:5173**

---

### **Backend**
#### Dependencies:
- spring-boot-starter-web
- spring-boot-starter-security
- spring-boot-starter-data-jpa
- spring-cloud-starter-netflix-eureka-server
- spring-cloud-starter-gateway
- mysql-connector-java
- jjwt

#### Application Properties (`application.yml`)
```yaml
spring:
  datasource:
    url: jdbc:mysql://localhost:3306/cab_booking
    username: root
    password: yourpassword
  jpa:
    hibernate:
      ddl-auto: update
    show-sql: true
    database-platform: org.hibernate.dialect.MySQL8Dialect

jwt:
  secret: your_jwt_secret_key
```

Run:
- Eureka Server → http://localhost:8761
- API Gateway → http://localhost:8082
- Microservices → Registered with Eureka

---

## 🔑 Authentication Flow
1. User registers/login → **User Service** issues JWT token
2. Token stored in frontend (`localStorage`)
3. Axios attaches token in headers for secured API calls
4. Spring Security validates JWT for protected endpoints

---

## 📚 API Endpoints

### **User Service**
| Method | Endpoint | Description |
|--------|----------|-------------|
| POST   | `/api/users/register` | Register new user |
| POST   | `/api/users/login`    | Login and get JWT |
| GET    | `/api/users/profile`  | Get user profile |

### **Ride Service**
| Method | Endpoint | Description |
|--------|----------|-------------|
| POST   | `/api/rides/book`     | Book a ride |
| GET    | `/api/rides/history`  | Get ride history |

*(Add similar tables for Driver, Rating, Payment services)*

---

## ✅ Future Enhancements
- Docker containerization
- CI/CD pipeline
- Payment gateway integration (Stripe/Razorpay)
- Real-time ride tracking with WebSockets

---

## 🤝 Contributing
1. Fork the repo
2. Create a new branch (`git checkout -b feature-name`)
3. Commit changes (`git commit -m 'Add feature'`)
4. Push to branch (`git push origin feature-name`)
5. Open a Pull Request

---

## 📜 License
This project is licensed under the **MIT License**.

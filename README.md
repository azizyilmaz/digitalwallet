# Digital Wallet Project

## 📚 Table of Contents

1. [About the Project](#about-the-project)  
2. [Packages](#packages)  
3. [Key Features](#key-features)  
4. [Technologies Used](#technologies-used)  
5. [Project Architecture](#project-architecture)  
6. [How to Run](#how-to-run)  
7. [API Endpoints](#api-endpoints)  
8. [Layers and Responsibilities](#layers-and-responsibilities)  
9. [Contact](#contact)  

---

## 🧽 About the Project

**Digital Wallet** is a Java-based application for managing digital wallets and financial transactions. It allows users to securely manage their wallets and perform different financial operations.

---

## 📦 Packages

- `.mvn/wrapper`: Maven wrapper files  
- `common`: Shared utility classes and functions  
- `domain`: Business logic and data models  
- `persistence`: Database access layer  
- `service`: Business services  
- `web`: Web layer and API controllers  

---

## 🌟 Key Features

- **User Management**: Register, login, and manage profiles  
- **Digital Wallet Management**: View wallet balance and transfer money  
- **Transaction History**: Keep records of transactions  
- **Security**: Password encryption and secure sessions  

---

## 🛠️ Technologies Used

- **Java**: Programming language  
- **Maven**: Project management and build tool  
- **Spring Boot**: Application framework  
- **JPA/Hibernate**: Database access and ORM  
- **MySQL/PostgreSQL**: Database system  

---

## 🏗 Project Architecture

- **Web Layer**: Handles HTTP requests and responses  
- **Service Layer**: Contains business logic  
- **Persistence Layer**: Handles database operations  
- **Common Layer**: Shared utility classes  

---

## ▶️ How to Run

1. Clone the project:  
   ```bash
   git clone https://github.com/azizyilmaz/digitalwallet.git
   ```
2. Download Maven dependencies and build the project:  
   ```bash
   mvn clean install
   ```
3. Run the application:  
   ```bash
   mvn spring-boot:run
   ```
4. The application will run on `http://localhost:8080` by default.

---

## 📡 API Endpoints

### **AuthController**
- `POST /api/auth/login`: Log in and get a JWT token  

### **CustomerController**
- `POST /api/customers`: Create new customer information  
- `GET /api/customers/id/{id}`: Get customer information

### **WalletController**
- `POST /api/wallets`: Create new wallet information
- `GET /api/wallets/customer/{customerId}`: Get wallet information

### **TransactionController**
- `POST /api/transactions/deposit`: Create a new deposit transaction  
- `POST /api/transactions/withdraw`: Create a new withdraw transaction
- `GET /api/transactions/wallet/{walletId}`: List all transactions  

---

## 🗼 Layers and Responsibilities

- **Web Layer**: Receives HTTP requests and sends them to service layer  
- **Service Layer**: Implements business rules and logic  
- **Persistence Layer**: Handles database operations using JPA/Hibernate  
- **Common Layer**: Provides utility classes and shared functions  

---

## 📞 Contact

- **GitHub**: [https://github.com/azizyilmaz/digitalwallet](https://github.com/azizyilmaz/digitalwallet)  


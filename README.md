# AI Powered Procurement Platform

A complete, production-ready SaaS application for managing procurement workflows with AI-driven supplier recommendations.

## Tech Stack
- **Frontend**: React (Create React App), TailwindCSS, Recharts, Axios, React Router
- **Backend**: Java 17, Spring Boot 3.2.4, Spring Security (JWT), Spring Data JPA
- **Database**: PostgreSQL

## Features
- **Role-Based Access Control**: Employee, Manager, Admin, Vendor roles with JWT stateless authentication.
- **Dashboard**: Real-time statistics and Recharts integration for spending trends.
- **Purchase Requests**: Employees can create and manage their budget requests.
- **Supplier Directory**: Admins manage approved suppliers.
- **AI Recommendations**: Smart algorithm matching the best supplier for each request based on price, rating, delivery speed, and historical reliability.
- **Order Management**: Managers approve requests into fulfilled orders seamlessly.
- **Commenting System**: Threaded discussions on purchase requests.

## Setup Instructions

### Prerequisites
- Java 17
- Maven
- Node.js & npm
- PostgreSQL running on default port 5432

### 1. Database Setup
1. Open PostgreSQL (pgAdmin or psql)
2. Create a database called `procurement_db`:
   ```sql
   CREATE DATABASE procurement_db;
   ```
   *(Ensure your postgres username/password is `postgres`/`postgres`, or update `application.properties`)*

### 2. Run Backend
1. Navigate to the backend directory:
   ```bash
   cd backend
   ```
2. Run the Spring Boot application using Maven:
   ```bash
   mvn spring-boot:run
   ```
   *The backend will start on `http://localhost:8080`. The database schema (`schema.sql`) will be initialized automatically.*

### 3. Run Frontend
1. Open a new terminal and navigate to the frontend directory:
   ```bash
   cd frontend
   ```
2. Install dependencies:
   ```bash
   npm install
   ```
3. Start the React development server:
   ```bash
   npm start
   ```
   *The frontend will start on `http://localhost:3000` (or 5173).*

## Default Roles & Testing
To fully test the platform, register the following users on the Login screen and assign them their respective roles from the dropdown:
- **Employee User**: Creates requests
- **Admin User**: Adds suppliers 
- **Manager User**: Views AI recommendations and approves orders

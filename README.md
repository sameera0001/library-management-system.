# Library Management System

A Java-based desktop application developed using **Java Swing and MySQL (JDBC)** to manage library operations such as adding books, issuing books, and returning books.

## Features
- Add new books
- View available books
- Issue books to students
- Return books
- Database connectivity using JDBC
- Simple graphical interface using Java Swing

## Technologies Used
- Java (J2SE)
- Java Swing (GUI)
- MySQL Database
- JDBC Connector

## Project Structure
Library-Management-System
│
├── src/ (Java Source Code)
├── lib/ (MySQL Connector Jar)
├── README.md
├── books.jpg
└── logo.jpg

## Database
Database Name: `library_db`

Tables:
- books
- students
- issue_books

## How to Run the Project

1. Install MySQL
2. Create database `library_db`
3. Import tables
4. Add MySQL Connector Jar
5. Compile and run

```bash
javac -cp ".;lib/mysql-connector-java-8.1.0.jar" LibraryManagementSystemFull.java
java -cp ".;lib/mysql-connector-java-8.1.0.jar" LibraryManagementSystemFull

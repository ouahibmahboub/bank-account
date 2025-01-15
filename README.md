# Bank Account Kata


---

### **Key Sections of the README**
1. **Overview**: A brief description of the project and its purpose.
2. **Technologies Used**: Lists the technologies and tools used in the project.
3. **Features**: Highlights the key features of the application.
4. **API Documentation**: Explains how to access and use the Swagger UI.
5. **Setup Instructions**: Provides step-by-step instructions for setting up and running the project.
6. **Testing**: Explains how to run unit tests and test the API.
7. **Project Structure**: Describes the structure of the project.
8. **Dependencies**: Lists the dependencies used in the project.

---
## **Overview**

This project implements a simple **Bank Account** system using **Spring Boot**. It adheres to the following requirements:

- **Deposit and Withdrawal**: Users can deposit and withdraw money from their account.
- **Account Statement**: Users can view their account statement, which includes the date, amount, and balance for each operation.
- **Service API**: The system provides a REST API for interacting with the bank account.
- **No UI**: There is no user interface; the system is purely a backend service.
- **No Persistence**: All data is stored in memory.

---

## **Technologies Used**
- **Spring Boot**: For building the REST API and managing dependencies.
- **Lombok**: For reducing boilerplate code (e.g., getters, setters, constructors).
- **MapStruct**: For mapping between entities and DTOs.
- **Swagger (OpenAPI)**: For API documentation and testing.
- **JUnit 5 & Mockito**: For unit testing.

---

## **Features**
1. **Deposit Money**:
    - Users can deposit money into their account by specifying the amount and date.
2. **Withdraw Money**:
    - Users can withdraw money from their account by specifying the amount and date.
3. **View Account Statement**:
    - Users can view their account statement, which includes all operations with the date, amount, and balance.

---

## **API Documentation**
The API is documented using **Swagger (OpenAPI)**. After running the application, you can access the Swagger UI at:
http://localhost:8080/swagger-ui.html


### **Endpoints**
1. **Deposit Money**:
    - **Method**: `POST`
    - **URL**: `/api/account/deposit`
    - **Parameters**:
        - `amount`: The amount to deposit.
        - `date` : The date of the deposit.
    - **Response**: A success message.

2. **Withdraw Money**:
    - **Method**: `POST`
    - **URL**: `/api/account/withdraw`
    - **Parameters**:
        - `amount`: The amount to withdraw.
        - `date`: The date of the withdrawal.
    - **Response**: A success message.

3. **View Account Statement**:
    - **Method**: `GET`
    - **URL**: `/api/account/statement`
    - **Response**: A `StatementDTO` object containing a list of operations.

---

## **Setup Instructions**

### **Prerequisites**
- **Java 17 or higher**
- **Maven** (for building the project)
- **IDE** (IntelliJ IDEA)

### **Steps**
1. **Clone the Repository**:
   ```bash
   git clone https://github.com/ouahibmahboub/bank-account.git
   cd bank-account

---
## **Dependencies**

The project uses the following dependencies:
- **Spring Boot Starter Web:** For building the REST API.
- **Lombok:** For reducing boilerplate code.
- **MapStruct:** For mapping between entities and DTOs.
- **Springdoc OpenAPI:** For API documentation.
- **JUnit 5 & Mockito:** For unit testing.
- **slf4j:** For logging.

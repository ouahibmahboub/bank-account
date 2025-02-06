# Bank Account API

A simple bank account API that allows to manage operations to a bank account holder:

     *Create account for client
     *Deposit money in client account
     *Withdraw money from client account
     *Print account statement
## Tech Stack and test Coverage

- Java 21
- Spring Boot 3.4.2
- Maven
- JUnit 5
- Mockito
- Lombok


## Prerequisites

- JDK 21 or higher
- Maven 3.8+

## Features

- Create bank account
- Make deposits
- Make withdrawals
- View account statement

## Test Coverage
- Test coverage > 95%

## API Endpoints

### Create Account
POST /api/accounts
```json
{
    "accountNum": "68eec930-e",
    "client": {
        "id": "4ea04f94-6f65-416c-b8bf-8ff99d2e520d",
        "firstName": "Sam",
        "lastName": "Smith",
        "address": "123 Boulevard AAA"
    },
    "creationDate": "2025-02-06",
    "balance": 0,
    "operations": []
}
```
### Make Deposit
POST /api/accounts/{accountNum}/deposit
```json
{
    "balance": 110.00
}
```
### Make Withdrawal
POST /api/accounts/{accountNum}/withdraw
```json
{
    "balance": 50.00
}
```
### Get Statement
GET /api/accounts/{accountNum}/statement
```
----------------------------------------------------------------------------------------------------
Date|Operation|Amount|Balance
06/02/2025|DEPOSIT|+60.00|110.00|
06/02/2025|WITHDRAWAL|-50.00|50.00|
06/02/2025|DEPOSIT|+100.00|100.00|
----------------------------------------------------------------------------------------------------
```
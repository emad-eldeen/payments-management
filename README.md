# Payments Management
A system to manage employees' payments. 
It supports authentication, authentication with security standards, logging security events, and using secured connection

## Technical Requirements
- Authentication:
    - HTTP basic authentication
    - Storing users credentials in a relational database
    - Password requirements: at least **12** characters, and it should not be breached (check against a db table of breached passwords)
- Payment:
    - Storing payments for users for periods (MM-YYYY)
    - payments validation:
        - the user whom the payment belongs to, should be already registered in the system
        - payments (user, period) should be unique
- Employees:
    - Authenticated users can request their payments
- REST APIs:
    - Users:
        - user signup
        - change password
    - Payments:
        - add payments (list of payments)
        - change payment
    - Employee:
        - get payment(s)

## Knowledge Used
- Spring Boot:
    - Web
    - Security
    - JPA
    - Validation
- Lombok
- H2 Database

## API examples
- user signup: POST localhost:8080/api/auth/users
```json
{
  "name": "emad",
  "lastname": "eldeen",
  "password": "test12345678",
  "email": "johndoe@acme.com"
}
```
- change password: POST localhost:8080/api/auth/change-password with basic auth
```json
{
  "new_password": "test123456789"
}
```
- add payments: POST localhost:8080/api/accounting/payments
```json
[
  {"employee":"johndoe@acme.com","period":"01-2021","salary":123456}, 
  {"employee":"johndoe@acme.com","period":"02-2021","salary":123700}
]
```
- change payment: PUT localhost:8080/api/accounting/payments
```json
{
  "employee":"johndoe@acme.com",
  "period":"01-2021",
  "salary":123900
}
```
- get employee salary: GET localhost:8080/api/employee/payment with basic auth


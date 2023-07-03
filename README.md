# Payments Management
A system to manage employees' payments. 
It supports authentication, authentication with security standards, logging security events, and using secured connection

## Technical Requirements
- Authentication:
    - HTTP basic authentication
    - Storing users credentials in a relational database
    - Password requirements: at least **12** characters, and it should not be breached (check against a db table of breached passwords)
- Authorization:
````
  |                                                  | Anonymous | User | Accountant | Admin | Auditor |
  |------------------------------------------------  |-----------|------|------------|-------|---------|
  | POST /api/auth/user                              | +         | +    | +          | +     | +       |
  | DELETE /api/auth/change-password                 | -         | +    | +          | +     | -       |
  | POST, PUT /api/accounting/payments               | -         | -    | +          | -     | -       |
  | GET /api/employee/payment                        | -         | +    | +          | -     | -       |
  | GET, DELETE /api/admin/user                      | -         | -    | -          | +     | -       |
  | PUT /api/admin/user/role                         | -         | -    | -          | +     | -       |
  | GET /api/security/events                         | -         | -    | -          | -     | +       |
````
- Payment:
    - Storing payments for users for periods (MM-YYYY)
    - payments validation:
        - the user whom the payment belongs to, should be already registered in the system
        - payments (user, period) should be unique
- Employees:
    - Authenticated users can request their payments
- Security events:
    - Detecting a brute force attack: If there are more than 5 consecutive attempts to enter an incorrect password, an entry about this should appear in the security events. Also, the user account must be blocked.
    - The service must log information security events. List of events to be logged:
        - CREATE_USER
        - CHANGE_PASSWORD
        - ACCESS_DENIED
        - LOGIN_FAILED
        - GRANT_ROLE
        - REMOVE_ROLE
        - LOCK_USER
        - UNLOCK_USER
        - DELETE_USER
        - BRUTE_FORCE
- REST APIs:
    - Users:
        - user signup
        - change password
    - Payments:
        - add payments (list of payments)
        - change payment
    - Employee:
        - get payment(s)
    - Admin:
        - list users
        - delete user
        - grant/delete user roles
        - lock/unlock user
    - Security:
        - list security events 

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
- list users: GET localhost:8080/api/admin/user
- delete user: DELETE localhost:8080/api/admin/user
```json
{
  "user":"johndoe@acme.com"
}
```
- add user role: PUT localhost:8080/api/admin/user/role
```json
{
  "user":"johndoe@acme.com",
  "operation": "GRANT",
  "role": "ACCOUNTANT"
}
```
- list security events: GET localhost:8080/api/security/events

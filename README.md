# Payment Processing System

This Spring Boot application handles a multi-parent/student payment model, where parents contribute funds to students and balances are updated dynamically with configurable payment rules.

---

## How to Build and Run the Application

### Prerequisites

- Java 17+
- H2 Database

---

### Run Locally

1. **Clone the repository**

   ```
   git clone https://github.com/winnerezy/tredbase-assessment.git
   cd payment-processing-app

    Configure application.properties

    In src/main/resources/application.properties, configure your DB:

    spring.datasource.url=jdbc:h2:mem:db_name
    spring.datasource.username=your_db_user
    spring.datasource.password=your_password
    spring.jpa.hibernate.ddl-auto=update

    spring.sql.init.mode=always
    spring.jpa.show-sql=true

Run the app

With Maven:
```
    ./mvnw spring-boot:run
```

Test the API

Use Postman or cURL to test the API.
```
POST /api/payments

{
  "parentId": 1,
  "studentId": 1,
  "paymentAmount": 100.0
}
```
Response: Balances are updated and a payment record is created.
Security & Design Decisions
Transactional Consistency

    The @Transactional annotation ensures ACID compliance.

    If any step fails (e.g. parent not found), no DB changes are committed.

Multi-Parent Support

    Students may have one or two parents (Many-to-Many).

    When a student has two parents, the payment amount is either:

        Deducted fully from both (current behavior), or

        Split 50/50 (see improvements section).

Data Validation (to be added)

    Use annotations like @NotNull, @Min, @Positive in PaymentRequest.

    Integrate Spring Security or OAuth2 for authentication/authorization.

Arithmetic Logic Explanation
Adjusting the Payment
```
double dynamicRate = 0.05;
double adjustedAmount = request.paymentAmount() * (1 + dynamicRate);

    A 5% dynamic rate is added (can be fee, tax, or incentive).

    If a parent pays 100, the system applies a 5% adjustment → 105.
Student Balance

student.setBalance(student.getBalance() + adjustedAmount);

    The student balance increases by the adjusted amount.
```
Parent Balance Logic
```
if (student.getParents().size() == 2) {
    for (Parent p : student.getParents()) {
        p.setBalance(p.getBalance() - adjustedAmount);
    }
} else {
    parent.setBalance(parent.getBalance() - adjustedAmount);
}

    Two parents? Both are charged the full adjustedAmount (can be updated to split).

    One parent? That parent pays the entire amount.

Optional: Split adjustedAmount between both parents

double splitAmount = adjustedAmount / 2;
for (Parent p : student.getParents()) {
    p.setBalance(p.getBalance() - splitAmount);
}
```

Database Schema
```
Parent
Field	    Type
id      	Long
name    	String
balance 	Double
Student
Field	    Type
id      	Long
name    	String
balance 	Double
Payment
Field	    Type
id      	Long
parentId	Long
studentId	Long
amount  	Double
timestamp	LocalDateTime

parent_students (join table)
parent_id	student_id
```
Sample SQL Data (data.sql)
```
-- Insert Parents
INSERT INTO parent (id, name, balance) VALUES (1, 'Parent A', 1000.0);
INSERT INTO parent (id, name, balance) VALUES (2, 'Parent B', 1200.0);

-- Insert Students
INSERT INTO student (id, name, balance) VALUES (1, 'Student Shared', 100.0);
INSERT INTO student (id, name, balance) VALUES (2, 'Student A', 50.0);
INSERT INTO student (id, name, balance) VALUES (3, 'Student B', 75.0);

-- Student-Parent Relations (Many-to-Many)
INSERT INTO parent_students (student_id, parents_id) VALUES (1, 1);
INSERT INTO parent_students (student_id, parents_id) VALUES (1, 2);
INSERT INTO parent_students (student_id, parents_id) VALUES (2, 1);
INSERT INTO parent_students (student_id, parents_id) VALUES (3, 2);
```
Make sure you enable SQL loading with:

```spring.sql.init.mode=always```
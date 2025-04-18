CREATE TABLE parent (
                        id INT PRIMARY KEY,
                        name VARCHAR(255) NOT NULL,
                        balance DECIMAL(10, 2) NOT NULL
);

CREATE TABLE student (
                         id INT PRIMARY KEY,
                         name VARCHAR(255) NOT NULL,
                         balance DECIMAL(10, 2) NOT NULL
);

CREATE TABLE parent_students (
                                 student_id INT,
                                 parents_id INT,
                                 FOREIGN KEY (student_id) REFERENCES student(id),
                                 FOREIGN KEY (parents_id) REFERENCES parent(id),
                                 PRIMARY KEY (student_id, parents_id)
);
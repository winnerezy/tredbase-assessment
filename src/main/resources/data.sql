

-- Insert Parents
INSERT INTO parent (id, name, balance) VALUES (1, 'Parent A', 1000.0);
INSERT INTO parent (id, name, balance) VALUES (2, 'Parent B', 1200.0);

-- Insert Students
INSERT INTO student (id, name, balance) VALUES (1, 'Student Shared', 100.0);
INSERT INTO student (id, name, balance) VALUES (2, 'Student A', 50.0);
INSERT INTO student (id, name, balance) VALUES (3, 'Student B', 75.0);

-- Insert Student-Parent relations (join table for ManyToMany)
INSERT INTO parent_students (student_id, parents_id) VALUES (1, 1); 
INSERT INTO parent_students (student_id, parents_id) VALUES (1, 2);
INSERT INTO parent_students (student_id, parents_id) VALUES (2, 1);
INSERT INTO parent_students (student_id, parents_id) VALUES (3, 2); 

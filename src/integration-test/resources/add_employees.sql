-- Prepopulate DB for integration tests
INSERT INTO Employee(id,employee_name,employee_salary,employee_age,profile_image)
    VALUES(1,'Toto', 2000, 25, '');
INSERT INTO Employee(id,employee_name,employee_salary,employee_age,profile_image)
    VALUES(2,'Titi', 2000, 52, '');
INSERT INTO Employee(id,employee_name,employee_salary,employee_age,profile_image)
    VALUES(3,'Bubu', 0, 0, '');
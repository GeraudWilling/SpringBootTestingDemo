CREATE TABLE Employee (
   id  integer PRIMARY KEY,
   employee_name  varchar NOT NULL,
   employee_salary  integer,
   employee_age  integer,
   profile_image  varchar
) ;

INSERT INTO Employee(id,employee_name,employee_salary,employee_age,profile_image)
    VALUES(1,'Toto', 2000, 25, '');
INSERT INTO Employee(id,employee_name,employee_salary,employee_age,profile_image)
    VALUES(2,'Titi', 2000, 52, '');
INSERT INTO Employee(id,employee_name,employee_salary,employee_age,profile_image)
    VALUES(3,'Bubu', 0, 0, '');
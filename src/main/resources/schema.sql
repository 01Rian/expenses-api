CREATE TABLE EXPENSE(
    id bigint PRIMARY KEY AUTO_INCREMENT,
    description varchar(100),
    "DATE" date,
    "VALUE" decimal(10,2),
    category varchar(100)
);

INSERT INTO EXPENSE(description, "DATE", "VALUE", category) VALUES('Livro de Spring', '2021-10-15', 56.40, 'STUDY');
INSERT INTO EXPENSE(description, "DATE", "VALUE", category) VALUES('Viagem de férias', '2021-12-20', 1200.00, 'LEISURE');
CREATE TABLE users (
    id VARCHAR(36) PRIMARY KEY NOT NULL,
    name VARCHAR(30) UNIQUE NOT NULL,
    email VARCHAR(50) UNIQUE NOT NULL,
    information VARCHAR(200)  NOT NULL,
    creation_date datetime(6) NOT NULL
) engine=InnoDB default charset=utf8;
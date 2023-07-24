CREATE SCHEMA IF NOT EXISTS canyon;

SET search_path TO canyon;

CREATE TABLE canyon.common_user_info
(
	user_info_id        BIGSERIAL PRIMARY KEY,
	nickname            VARCHAR(255),
	phone_number        VARCHAR(100),
	street_address      VARCHAR(255),
	city                VARCHAR(255),
	state               VARCHAR(255),
	country             VARCHAR(255),
	postal_code         VARCHAR(100),
	profile_picture_url  VARCHAR(255),
	occupation          VARCHAR(255),
	created_at          TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
	updated_at          TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE canyon.common_user
(
	user_id          BIGSERIAL PRIMARY KEY,
	login_id         VARCHAR(255) NOT NULL UNIQUE,
	email            VARCHAR(255) NOT NULL UNIQUE,
	password         VARCHAR(255) NOT NULL,
	name             VARCHAR(255) NOT NULL,
	gender           VARCHAR(100),
	date_of_birth    DATE,
	status           VARCHAR(50),
	account_verified BOOLEAN   DEFAULT FALSE,
	user_info_id     BIGINT REFERENCES canyon.common_user_info (user_info_id),
	created_at       TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
	updated_at       TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);
CREATE INDEX idx_common_user_email ON canyon.common_user (email);
CREATE INDEX idx_common_user_status ON canyon.common_user (status);
CREATE INDEX idx_common_user_gender ON canyon.common_user (gender);

CREATE TABLE canyon.common_role
(
	role_id BIGSERIAL PRIMARY KEY,
	name    character varying(60) NOT NULL
);

CREATE TABLE canyon.common_permission
(
	permission_id BIGSERIAL PRIMARY KEY,
	name          character varying(60) NOT NULL
);

CREATE TABLE canyon.common_user_common_role
(
	user_id BIGSERIAL NOT NULL,
	role_id BIGSERIAL NOT NULL,
	CONSTRAINT user_roles_pkey PRIMARY KEY (user_id, role_id),
	CONSTRAINT fk_user_roles_users FOREIGN KEY (user_id) REFERENCES canyon.common_user (user_id),
	CONSTRAINT fk_user_roles_roles FOREIGN KEY (role_id) REFERENCES canyon.common_role (role_id)
);

CREATE TABLE common_role_common_permission
(
	role_id       BIGSERIAL NOT NULL,
	permission_id BIGSERIAL NOT NULL,
	CONSTRAINT role_permissions_pkey PRIMARY KEY (role_id, permission_id),
	CONSTRAINT fk_role_permissions_roles FOREIGN KEY (role_id) REFERENCES canyon.common_role (role_id),
	CONSTRAINT fk_role_permissions_permissions FOREIGN KEY (permission_id) REFERENCES canyon.common_permission (permission_id)
);

CREATE TABLE canyon.common_user_token
(
	user_id     BIGINT PRIMARY KEY REFERENCES canyon.common_user (user_id),
	token       VARCHAR(255) NOT NULL,
	expiry_date TIMESTAMP WITHOUT TIME ZONE,
	created_at  TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
	updated_at  TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE canyon.common_social_accounts
(
	social_accounts_id BIGSERIAL PRIMARY KEY,
	user_id            BIGINT       NOT NULL REFERENCES canyon.common_user (user_id),
	provider           VARCHAR(255) NOT NULL,
	social_id          VARCHAR(255) NOT NULL,
	access_token       TEXT,
	refresh_token      TEXT,
	last_login         TIMESTAMP,
	created_at         TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
	updated_at         TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);
CREATE INDEX idx_common_social_accounts_user_id ON canyon.common_social_accounts (user_id);

INSERT INTO canyon.common_user_info (nickname, phone_number, street_address, city, state, country, postal_code)
VALUES ('john','010-555-5555', '123 Main St', 'Scranton', 'PA', 'USA', '18503'),
       ('jane','010-555-5556', '124 Main St', 'Scranton', 'PA', 'USA', '18503'),
       ('michael','010-555-5557', '125 Main St', 'Scranton', 'PA', 'USA', '18503'),
       ('jim','010-555-5558', '126 Main St', 'Scranton', 'PA', 'USA', '18503'),
       ('pam','010-555-5559', '127 Main St', 'Scranton', 'PA', 'USA', '18503'),
       ('dwight','010-555-5560', '128 Main St', 'Scranton', 'PA', 'USA', '18503'),
       ('angela','010-555-5561', '129 Main St', 'Scranton', 'PA', 'USA', '18503'),
       ('kevin','010-555-5562', '130 Main St', 'Scranton', 'PA', 'USA', '18503'),
       ('oscar','010-555-5563', '131 Main St', 'Scranton', 'PA', 'USA', '18503'),
       ('toby','010-555-5564', '132 Main St', 'Scranton', 'PA', 'USA', '18503');

INSERT INTO canyon.common_user (login_id, email, name, password, gender, date_of_birth, status, account_verified,
                                user_info_id)
VALUES ('john.doe@gmail.com', 'john.doe@gmail.com', 'John Doe',
        '$2a$04$Cmz9KaFkHbmmlrZodi18buuzrLk27YT7wf6/E06Vbg8m7pqZ1xOCS', 'MAN', '1990-01-01', 'ACTIVE', true, 1),
       ('jane.doe@gmail.com', 'jane.doe@gmail.com', 'Jane Doe',
        '$2a$04$Cmz9KaFkHbmmlrZodi18buuzrLk27YT7wf6/E06Vbg8m7pqZ1xOCS', 'WOMAN', '1991-02-02', 'ACTIVE', true, 2),
       ('michael.scott@gmail.com', 'michael.scott@gmail.com', 'Michael Scott',
        '$2a$04$Cmz9KaFkHbmmlrZodi18buuzrLk27YT7wf6/E06Vbg8m7pqZ1xOCS', 'MAN', '1992-03-03',
        'PAUSED', false, 3),
       ('jim.halpert@gmail.com', 'jim.halpert@gmail.com', 'Jim Halpert',
        '$2a$04$Cmz9KaFkHbmmlrZodi18buuzrLk27YT7wf6/E06Vbg8m7pqZ1xOCS', 'MAN', '1993-04-04', 'ACTIVE',
        true, 4),
       ('pam.beesly@gmail.com', 'pam.beesly@gmail.com', 'Pam Beesly',
        '$2a$04$Cmz9KaFkHbmmlrZodi18buuzrLk27YT7wf6/E06Vbg8m7pqZ1xOCS', 'WOMAN', '1994-05-05', 'PAUSED',
        false, 5),
       ('dwight.schrute@gmail.com', 'dwight.schrute@gmail.com', 'Dwight Schrute',
        '$2a$04$Cmz9KaFkHbmmlrZodi18buuzrLk27YT7wf6/E06Vbg8m7pqZ1xOCS', 'MAN', '1995-06-06',
        'ACTIVE', true, 6),
       ('angela.martin@gmail.com', 'angela.martin@gmail.com', 'Angela Martin',
        '$2a$04$Cmz9KaFkHbmmlrZodi18buuzrLk27YT7wf6/E06Vbg8m7pqZ1xOCS', 'WOMAN', '1996-07-07',
        'PAUSED', false, 7),
       ('kevin.malone@gmail.com', 'kevin.malone@gmail.com', 'Kevin Malone',
        '$2a$04$Cmz9KaFkHbmmlrZodi18buuzrLk27YT7wf6/E06Vbg8m7pqZ1xOCS', 'MAN', '1997-08-08', 'ACTIVE',
        true, 8),
       ('oscar.martinez@gmail.com', 'oscar.martinez@gmail.com', 'Oscar Martinez',
        '$2a$04$Cmz9KaFkHbmmlrZodi18buuzrLk27YT7wf6/E06Vbg8m7pqZ1xOCS', 'MAN', '1998-09-09',
        'PAUSED', false, 9),
       ('toby.flenderson@gmail.com', 'toby.flenderson@gmail.com', 'Toby Flenderson',
        '$2a$04$Cmz9KaFkHbmmlrZodi18buuzrLk27YT7wf6/E06Vbg8m7pqZ1xOCS', 'MAN', '1999-10-10',
        'ACTIVE', true, 10);

INSERT INTO canyon.common_user_token (user_id, token, expiry_date)
VALUES (1, 'token1', '2024-12-31 23:59:59'),
       (2, 'token2', '2024-12-31 23:59:59'),
       (3, 'token3', '2024-12-31 23:59:59'),
       (4, 'token4', '2024-12-31 23:59:59'),
       (5, 'token5', '2024-12-31 23:59:59'),
       (6, 'token6', '2024-12-31 23:59:59'),
       (7, 'token7', '2024-12-31 23:59:59'),
       (8, 'token8', '2024-12-31 23:59:59'),
       (9, 'token9', '2024-12-31 23:59:59'),
       (10, 'token10', '2024-12-31 23:59:59');

INSERT INTO canyon.common_social_accounts (user_id, provider, social_id, access_token, refresh_token, last_login)
VALUES (1, 'Facebook', 'social_id1', 'access_token1', 'refresh_token1', '2023-01-01 00:00:00'),
       (2, 'Facebook', 'social_id2', 'access_token2', 'refresh_token2', '2023-01-02 00:00:00'),
       (3, 'Google', 'social_id3', 'access_token3', 'refresh_token3', '2023-01-03 00:00:00'),
       (4, 'Google', 'social_id4', 'access_token4', 'refresh_token4', '2023-01-04 00:00:00'),
       (5, 'Twitter', 'social_id5', 'access_token5', 'refresh_token5', '2023-01-05 00:00:00'),
       (6, 'Twitter', 'social_id6', 'access_token6', 'refresh_token6', '2023-01-06 00:00:00'),
       (7, 'LinkedIn', 'social_id7', 'access_token7', 'refresh_token7', '2023-01-07 00:00:00'),
       (8, 'LinkedIn', 'social_id8', 'access_token8', 'refresh_token8', '2023-01-08 00:00:00'),
       (9, 'GitHub', 'social_id9', 'access_token9', 'refresh_token9', '2023-01-09 00:00:00'),
       (10, 'GitHub', 'social_id10', 'access_token10', 'refresh_token10', '2023-01-10 00:00:00');

INSERT INTO canyon.common_role(name)
VALUES ('ADMIN'),
       ('USER'),
       ('GUEST'),
       ('CONTENT_MANAGER'),
       ('USER_MANAGER'),
       ('SYSTEM_MANAGER'),
       ('BILLING_MANAGER'),
       ('PRODUCT_MANAGER'),
       ('ORDER_MANAGER'),
       ('REPORT_MANAGER'),
       ('DEVELOPER'),
       ('TESTER'),
       ('SECURITY'),
       ('PARTNER'),
       ('INTERN'),
       ('TEMP_EMPLOYEE');

INSERT INTO canyon.common_permission(name)
VALUES ('READ_ALL'),
       ('WRITE_ALL'),
       ('DELETE_ALL'),
       ('READ_CONTENTS'),
       ('WRITE_CONTENTS'),
       ('DELETE_CONTENTS'),
       ('READ_API'),
       ('WRITE_API'),
       ('DELETE_API');

INSERT INTO canyon.common_user_common_role(user_id, role_id)
VALUES (1, 1),
       (2, 2),
       (3, 3),
       (2, 1);

INSERT INTO canyon.common_role_common_permission(role_id, permission_id)
VALUES (1, 1),
       (1, 2),
       (1, 3),
       (2, 4),
       (2, 5),
       (2, 6),
       (2, 7),
       (2, 8),
       (2, 9),
       (3, 4),
       (3, 5),
       (3, 6);

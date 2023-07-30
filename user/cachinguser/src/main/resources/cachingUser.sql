CREATE SCHEMA IF NOT EXISTS canyon;

SET search_path TO canyon;

CREATE TABLE canyon.caching_user
(
	user_id             BIGSERIAL PRIMARY KEY,
	login_id            VARCHAR(255) NOT NULL UNIQUE,
	email               VARCHAR(255) NOT NULL UNIQUE,
	name                VARCHAR(255),
	gender              VARCHAR(100),
	date_of_birth       DATE,
	status              VARCHAR(50),
	account_verified    BOOLEAN,
	nickname            VARCHAR(255),
	phone_number        VARCHAR(100),
	street_address      VARCHAR(255),
	city                VARCHAR(255),
	state               VARCHAR(255),
	country             VARCHAR(255),
	postal_code         VARCHAR(100),
	profile_picture_url VARCHAR(255)
);
CREATE INDEX idx_common_user_email ON canyon.caching_user (email);
CREATE INDEX idx_common_user_login_id ON canyon.caching_user (login_id);
CREATE INDEX idx_common_user_status ON canyon.caching_user (status);
CREATE INDEX idx_common_user_nickname ON canyon.caching_user (nickname);

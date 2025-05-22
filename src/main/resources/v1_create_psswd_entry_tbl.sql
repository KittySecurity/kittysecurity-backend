CREATE TABLE PasswordEntry (
    entry_id BIGINT PRIMARY KEY,
    user_id BIGINT NOT NULL,
    service_name VARCHAR(255) NOT NULL,
    url VARCHAR(500),
    login VARCHAR(255) NOT NULL,
    password_encrypted VARCHAR(255) NOT NULL,
    created_at TIMESTAMP NOT NULL,
    updated_at TIMESTAMP NOT NULL,
    CONSTRAINT fk_password_user FOREIGN KEY (user_id) REFERENCES User(user_id)
);
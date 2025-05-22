CREATE TABLE Role (
    user_id BIGINT PRIMARY KEY,
    role_id BIGINT NOT NULL,
    role VARCHAR(100) NOT NULL,
    CONSTRAINT fk_role_user FOREIGN KEY (user_id) REFERENCES User(user_id)
);
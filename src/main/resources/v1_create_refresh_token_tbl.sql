CREATE TABLE RefreshToken (
    key_id BIGINT PRIMARY KEY,
    user_id BIGINT NOT NULL,
    prefix VARCHAR(255) NOT NULL,
    hashedToken VARCHAR(255) NOT NULL,
    expiresAt TIMESTAMP NOT NULL,
    createdAt TIMESTAMP NOT NULL,
    CONSTRAINT fk_refresh_user FOREIGN KEY (user_id) REFERENCES User(user_id)
);
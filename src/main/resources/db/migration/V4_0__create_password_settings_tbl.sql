CREATE TABLE PasswordGenSettings (
    user_id BIGINT PRIMARY KEY,
    length INT NOT NULL CHECK (length BETWEEN 1 AND 99),
    minNumOfSpecChars INT NOT NULL CHECK (minNumOfSpecChars BETWEEN 0 AND 99),
    minNumOfDigits INT NOT NULL CHECK (minNumOfDigits BETWEEN 0 AND 99),
    hasLowercase BOOLEAN NOT NULL,
    hasUppercase BOOLEAN NOT NULL,
    hasSpecial BOOLEAN NOT NULL,
    hasDigits BOOLEAN NOT NULL,
    CONSTRAINT fk_settings_user FOREIGN KEY (user_id) REFERENCES users(user_id) ON DELETE CASCADE
);
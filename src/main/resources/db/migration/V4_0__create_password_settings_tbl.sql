CREATE TABLE password_gen_settings (
    user_id BIGINT PRIMARY KEY,
    length INT NOT NULL CHECK (length BETWEEN 1 AND 99),
    min_num_of_spec_chars INT NOT NULL CHECK (min_num_of_spec_chars BETWEEN 0 AND 99),
    min_num_of_digits INT NOT NULL CHECK (min_num_of_digits BETWEEN 0 AND 99),
    has_lowercase BOOLEAN NOT NULL,
    has_uppercase BOOLEAN NOT NULL,
    has_special BOOLEAN NOT NULL,
    has_digits BOOLEAN NOT NULL,
    CONSTRAINT fk_settings_user FOREIGN KEY (user_id) REFERENCES users(user_id) ON DELETE CASCADE
);
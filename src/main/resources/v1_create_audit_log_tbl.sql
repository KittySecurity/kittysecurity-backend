CREATE TABLE AuditLog (
    log_id BIGINT PRIMARY KEY,
    user_id BIGINT NOT NULL,
    action VARCHAR(255) NOT NULL,
    timestamp TIMESTAMP NOT NULL,
    ip_address VARCHAR(45) NOT NULL,
    CONSTRAINT fk_audit_user FOREIGN KEY (user_id) REFERENCES User(user_id)
);
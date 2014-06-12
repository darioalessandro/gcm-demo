# --- !Ups

CREATE TABLE SESSION(
    session_id  VARCHAR(255) NOT NULL UNIQUE,
    gcm_registration_id VARCHAR(255) NOT NULL,
    os_version VARCHAR(255),
    app_version VARCHAR(255) NOT NULL
);

# --- !Downs

DROP TABLE SESSION;
# --- !Ups

CREATE TABLE session(
    id  VARCHAR(255) NOT NULL,
    gcm_registration_id VARCHAR(255) NOT NULL,
    os_version VARCHAR(255),
    app_version VARCHAR(255) NOT NULL
);

# --- !Downs

DROP TABLE session;
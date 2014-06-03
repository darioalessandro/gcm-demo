# --- !Ups

INSERT INTO SESSION(id,gcm_registration_id,os_version,app_version) VALUES('session_1','gcm_1','os version','app version');
INSERT INTO SESSION(id,gcm_registration_id,os_version,app_version) VALUES('session_2','gcm_2','os version','app version');
INSERT INTO SESSION(id,gcm_registration_id,os_version,app_version) VALUES('session_3','gcm_3','os version','app version');
INSERT INTO SESSION(id,gcm_registration_id,os_version,app_version) VALUES('session_4','gcm_4','os version','app version');

# --- !Downs

DELETE FROM SESSION;
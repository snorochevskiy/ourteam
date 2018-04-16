# --- !Ups

create table DB_USER (
    ID VARCHAR(32) NOT NULL PRIMARY KEY,
    EMAIL VARCHAR(128),
    FIRST_NAME VARCHAR(32),
    LAST_NAME VARCHAR(32),
    MIDDLE_NAME  VARCHAR(32),
    AVATAR_URL VARCHAR(256)
);

create table LOGIN_INFO(
    ID INTEGER PRIMARY KEY,
    PROVIDER_ID VARCHAR(32),
    PROVIDER_KEY VARCHAR(32)
);

create table USER_LOGIN_INFO(
    USER_ID VARCHAR(32),
    LOGIN_INFO_ID INTEGER
);

create table PASSWORD_INFO(
    HASHER VARCHAR(32),
    PASSWORD VARCHAR(64),
    SALT VARCHAR(32),
    LOGIN_INFO_ID INTEGER
);

insert into DB_USER(ID, EMAIL, FIRST_NAME, LAST_NAME, MIDDLE_NAME, AVATAR_URL) VALUES
  ('admin', '', '', '', '', ''),
  ('snoro', 'snoro@tpm.com', 'Stas', 'Noro', 'Michael', ''),
  ('vsoko', 'vsoko@tpm.com', 'Vladimir', 'Soko', 'Batkovich', ''),
  ('vkisl', 'vkisl@tpm.com', 'Vladislav', 'Kisl', 'Batkovich', ''),
  ('yzasu', 'yzasu@tpm.com', 'Yuri', 'Zasu', 'Batkovich', '');

insert into LOGIN_INFO(ID, PROVIDER_ID, PROVIDER_KEY) VALUES
  (1, 'credentials', 'admin'),
  (2, 'credentials', 'snoro'),
  (3, 'credentials', 'vsoko'),
  (4, 'credentials', 'vkisl'),
  (5, 'credentials', 'yzasu');

insert into USER_LOGIN_INFO(USER_ID, LOGIN_INFO_ID) VALUES
  ('admin', 1),
  ('snoro', 2),
  ('vsoko', 3),
  ('vkisl', 4),
  ('yzasu', 5);

-- BCryptSha256PasswordHasher: password=1111 logRounds=10

insert into PASSWORD_INFO(HASHER, PASSWORD, SALT, LOGIN_INFO_ID) VALUES
  ('bcrypt-sha256', '$2a$10$4LkxegKhm7vmPJhsPAyN2OMIK06Xq6aLYL7kPjLnQ8sgy78age/vW', NULL, 1),
  ('bcrypt-sha256', '$2a$10$4LkxegKhm7vmPJhsPAyN2OMIK06Xq6aLYL7kPjLnQ8sgy78age/vW', NULL, 2),
  ('bcrypt-sha256', '$2a$10$4LkxegKhm7vmPJhsPAyN2OMIK06Xq6aLYL7kPjLnQ8sgy78age/vW', NULL, 3),
  ('bcrypt-sha256', '$2a$10$4LkxegKhm7vmPJhsPAyN2OMIK06Xq6aLYL7kPjLnQ8sgy78age/vW', NULL, 4),
  ('bcrypt-sha256', '$2a$10$4LkxegKhm7vmPJhsPAyN2OMIK06Xq6aLYL7kPjLnQ8sgy78age/vW', NULL, 5);


# --- !Downs

drop table PASSWORD_INFO;
drop table USER_LOGIN_INFO;
drop table LOGIN_INFO;
drop table DB_USER;
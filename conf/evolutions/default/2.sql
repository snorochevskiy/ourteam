# --- !Ups

create table COMPANY (
    ID VARCHAR(16) NOT NULL PRIMARY KEY,
    NAME VARCHAR(256) NOT NULL
);

create table DEPARTMENT (
    ID VARCHAR(16) NOT NULL PRIMARY KEY,
    NAME VARCHAR(256) NOT NULL,
    DESCRIPTION VARCHAR(4096),
    COMPANY_ID VARCHAR(16)
);

create table PROJECT (
    ID VARCHAR(16) NOT NULL PRIMARY KEY,
    NAME VARCHAR(256) NOT NULL,
    DESCRIPTION VARCHAR(4096),
    DEPARTMENT_ID VARCHAR(16)
);

create table TEAM (
    ID VARCHAR(16) NOT NULL PRIMARY KEY,
    NAME VARCHAR(256) NOT NULL,
    DESCRIPTION VARCHAR(4096),
    PROJECT_ID VARCHAR(16)
);

create table EMPLOYEE (
    ID INTEGER NOT NULL PRIMARY KEY,
    FUNCTIONAL_ROLE VARCHAR(256) NOT NULL,
    USER_ID VARCHAR(16),
    TEAM_ID VARCHAR(16)
);


insert into COMPANY (ID, NAME) VALUES ('tpm', 'The Product Motor');

insert into DEPARTMENT (ID, NAME, COMPANY_ID) VALUES ('cascom', 'Cascom', 'tpm');

insert into PROJECT (ID, NAME, DESCRIPTION, DEPARTMENT_ID) VALUES ('cnt', 'Cascom Network Technologies', '', 'cascom');

insert into TEAM (ID, NAME, DESCRIPTION, PROJECT_ID) VALUES
  ('backend', 'Backend services', '', 'cnt'),
  ('di', 'Device Investigation', '', 'cnt'),
  ('discovery', 'Solutions Discovery', '', 'cnt');

insert into EMPLOYEE (ID, FUNCTIONAL_ROLE, USER_ID, TEAM_ID) VALUES
  (1, 'developer', 'snoro', 'backend'),
  (2, 'di', 'vsoko', 'backend'),
  (3, 'discovery', 'vkisl', 'backend');

# --- !Downs

drop table EMPLOYEE;
drop table TEAM;
drop table PROJECT;
drop table DEPARTMENT;
drop table COMPANY;

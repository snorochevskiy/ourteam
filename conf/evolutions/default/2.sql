# --- !Ups

create table DEPARTMENT (
    ID INTEGER NOT NULL PRIMARY KEY,
    CODE VARCHAR(16) NOT NULL,
    NAME VARCHAR(256) NOT NULL,
    DESCRIPTION VARCHAR(4096)
);

create table PROJECT (
    ID INTEGER NOT NULL PRIMARY KEY,
    CODE VARCHAR(16) NOT NULL,
    NAME VARCHAR(256) NOT NULL,
    DESCRIPTION VARCHAR(4096),
    DEPARTMENT_ID INTEGER
);

create table TEAM (
    ID INTEGER NOT NULL PRIMARY KEY,
    CODE VARCHAR(16) NOT NULL,
    NAME VARCHAR(256) NOT NULL,
    DESCRIPTION VARCHAR(4096),
    PROJECT_ID INTEGER
);

create table EMPLOYEE (
    USER_ID VARCHAR(16),
    FUNCTIONAL_ROLE VARCHAR(256) NOT NULL,
    TEAM_ID INTEGER
);


insert into DEPARTMENT (ID, CODE, NAME, DESCRIPTION) VALUES (1, 'cascom', 'Cascom', 'Most hated company');

insert into PROJECT (ID, CODE, NAME, DESCRIPTION, DEPARTMENT_ID) VALUES (1, 'cnt', 'Cascom Network Technologies', '', 1);

insert into TEAM (ID, CODE, NAME, DESCRIPTION, PROJECT_ID) VALUES
  (1, 'backend', 'Backend services', '', 1),
  (2, 'di', 'Device Investigation', '', 1),
  (3, 'discovery', 'Solutions Discovery', '', 1);

insert into EMPLOYEE (TEAM_ID, USER_ID, FUNCTIONAL_ROLE) VALUES
  (1, 'snoro', 'developer'),
  (2, 'vsoko', 'developer'),
  (3, 'vkisl', 'developer');

# --- !Downs

drop table EMPLOYEE;
drop table TEAM;
drop table PROJECT;
drop table DEPARTMENT;
drop table COMPANY;

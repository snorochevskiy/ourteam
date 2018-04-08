# --- !Ups

create table COMPANY (ID VARCHAR(16) NOT NULL PRIMARY KEY, NAME VARCHAR NOT NULL);

# --- !Downs

drop table COMPANY;
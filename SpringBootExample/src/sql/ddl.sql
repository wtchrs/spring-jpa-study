create user if not exists 'example'@'localhost' identified by 'example';
create user if not exists 'example'@'172.17.0.1' identified by 'example';

create database if not exists example character set = utf8;

grant all privileges on example.* to 'example'@'localhost';
grant all privileges on example.* to 'example'@'172.17.0.1';

create table if not exists example.member
(
    id   bigint auto_increment not null primary key,
    name varchar(255)          not null
) engine = InnoDB
  character set = utf8;
create schema book_service_schema;

create user book_service_user password 'secret';

grant usage on schema book_service_schema to book_service_user;

alter default privileges in schema book_service_schema grant all on tables to book_service_user;

alter user book_service_user set search_path to book_service_schema;
alter user test set search_path to book_service_schema;

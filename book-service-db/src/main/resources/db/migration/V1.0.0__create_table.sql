-- 書籍
create table book_service_schema.books
(
    id             UUID primary key,
    author_id      UUID         not null,
    title          varchar(100) not null,
    title_kana     varchar(100) not null,
    publisher_name varchar(100) not null
);

-- 著者
create table book_service_schema.authors
(
    id         UUID primary key,
    name       varchar(100) not null,
    name_kana  varchar(100) not null,
    birth_date date,
    death_date date
);

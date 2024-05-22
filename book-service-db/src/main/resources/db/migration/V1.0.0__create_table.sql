-- updated_at の更新トリガー
create function book_service_schema.update_updated_at() returns trigger as $$
begin
    new.updated_at = clock_timestamp();
    return new;
end;
$$
language plpgsql;

-- 書籍
create table book_service_schema.books
(
    id             UUID primary key,
    author_id      UUID         not null,
    title          varchar(100) not null,
    title_kana     varchar(100) not null,
    publisher_name varchar(100) not null,
    created_at     timestamptz  not null default clock_timestamp(),
    updated_at     timestamptz  not null default clock_timestamp()
);

create trigger update_books_updated_at
    before update
    on book_service_schema.books
    for each row
    execute procedure book_service_schema.update_updated_at();

-- 著者
create table book_service_schema.authors
(
    id         UUID primary key,
    name       varchar(100) not null,
    name_kana  varchar(100) not null,
    birth_date date,
    death_date date,
    created_at timestamptz  not null default clock_timestamp(),
    updated_at timestamptz  not null default clock_timestamp()
);

create trigger update_authors_updated_at
    before update
    on book_service_schema.authors
    for each row
    execute procedure book_service_schema.update_updated_at();

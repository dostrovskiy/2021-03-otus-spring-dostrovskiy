drop table if exists authors;
create table authors(id bigint identity primary key, name varchar(255));
drop table if exists genres;
create table genres(id bigint identity primary key, name varchar(255));
drop table if exists books;
create table books(id bigint identity primary key, title varchar(255));
drop table if exists books_authors;
create table books_authors(book_id bigint, author_id bigint);
drop table if exists books_genres;
create table books_genres(book_id bigint, genre_id bigint);
drop table if exists reviews;
create table reviews(id bigint identity primary key, book_id bigint, text varchar(255));

drop table if exists users;
create table users(id bigint identity primary key, name varchar(255), password varchar(255));

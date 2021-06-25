insert into authors (name) values ('Островский А.Н.');

insert into genres (name) values ('Пьеса');

insert into books (title) values ('На всякого мудреца довольно простоты');
insert into books_authors (book_id, author_id) values (1, 1);
insert into books_genres (book_id, genre_id) values (1, 1);
insert into reviews (book_id, text) values (1, 'Прекрасная пьеса!!');
insert into reviews (book_id, text) values (1, 'Хм.. ну ок.');
insert into reviews (book_id, text) values (1, 'Не читал...');

insert into books (title) values ('Не всё коту масленица');
insert into books_authors (book_id, author_id) values (3, 1);
insert into books_genres (book_id, genre_id) values (2, 1);

insert into books (title) values ('Волки и овцы');
insert into books_authors (book_id, author_id) values (2, 1);
insert into books_genres (book_id, genre_id) values (3, 1);

insert into books (title) values ('История России. XX — начало XXI века. Учебник для 9 кл');
insert into authors (name) values ('Данилов А.А.');
insert into authors (name) values ('Косулина Л.Г.');
insert into authors (name) values ('Пыжиков А.В.');
insert into genres (name) values ('Учебник');
insert into genres (name) values ('История');
insert into genres (name) values ('Образование');
insert into books_authors (book_id, author_id) values (4, 2);
insert into books_authors (book_id, author_id) values (4, 3);
insert into books_authors (book_id, author_id) values (4, 4);
insert into books_genres (book_id, genre_id) values (4, 2);
insert into books_genres (book_id, genre_id) values (4, 3);
insert into books_genres (book_id, genre_id) values (4, 4);
insert into reviews (book_id, text) values (4, 'Учитесь, детки, на здоровье!');
insert into reviews (book_id, text) values (4, 'Достало учится!!');
insert into reviews (book_id, text) values (4, 'Когда наконец коникулы777');
insert into reviews (book_id, text) values (4, 'Фсе учебники в топку!!111');

insert into books (title) values ('Мцыри');

insert into users (name, password, role) values ('admin', '$2y$12$LWdOCN1HY2ZmiF4MGRRBxOwoHepgHQJ00fsjd6kVPGDsYS6D02ok6', 'ADMIN');
insert into users (name, password, role) values ('reader', '$2y$12$a3DM//RQ.TTtiRxiOd8nEO9AiTnIO6aeoYACvzIB7whFfeowfDpV6', 'READER');
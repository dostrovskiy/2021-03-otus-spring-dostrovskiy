insert into authors (name) values ('Островский А.Н.');
insert into genres (name) values ('Пьеса');
insert into books (title, isbn) values ('На всякого мудреца довольно простоты', '123-5-456-78901-2');
insert into books_authors (book_id, author_id) values (1, 1);
insert into books_genres (book_id, genre_id) values (1, 1);
insert into reviews (book_id, text) values (1, 'Прекрасная пьеса!!');
insert into reviews (book_id, text) values (1, 'Хм.. ну ок.');
insert into reviews (book_id, text) values (1, 'Не читал...');

insert into books (title, isbn) values ('Война и мир', '123-5-456-78906-2');

insert into users (name, password, role) values ('admin', '$2y$12$LWdOCN1HY2ZmiF4MGRRBxOwoHepgHQJ00fsjd6kVPGDsYS6D02ok6', 'ADMIN');
insert into users (name, password, role) values ('reader', '$2y$12$a3DM//RQ.TTtiRxiOd8nEO9AiTnIO6aeoYACvzIB7whFfeowfDpV6', 'READER');
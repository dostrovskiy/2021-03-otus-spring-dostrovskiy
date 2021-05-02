insert into authors (name) values ('Островский А.Н.');
insert into genres (name) values ('Пьеса');
insert into books (title) values ('На всякого мудреца довольно простоты');
insert into books_authors (book_id, author_id) values (1, 1);
insert into books_genres (book_id, genre_id) values (1, 1);
insert into reviews (book_id, text) values (1, 'Прекрасная пьеса!!');
insert into reviews (book_id, text) values (1, 'Хм.. ну ок.');
insert into reviews (book_id, text) values (1, 'Не читал...');

insert into books (title) values ('Война и мир');

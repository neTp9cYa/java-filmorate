merge into MPAS
    using (values ('G', 'у фильма нет возрастных ограничений')) TMP (NAME, DESCRIPTION)
    on MPAS.NAME = TMP.NAME
    when not matched then INSERT (NAME, DESCRIPTION) values (TMP.NAME, TMP.DESCRIPTION);

merge into MPAS
    using (values ('PG', 'детям рекомендуется смотреть фильм с родителями')) TMP (NAME, DESCRIPTION)
    on MPAS.NAME = TMP.NAME
    when not matched then INSERT (NAME, DESCRIPTION) values (TMP.NAME, TMP.DESCRIPTION);

merge into MPAS
    using (values ('PG-13', 'детям до 13 лет просмотр не желателен')) TMP (NAME, DESCRIPTION)
    on MPAS.NAME = TMP.NAME
    when not matched then INSERT (NAME, DESCRIPTION) values (TMP.NAME, TMP.DESCRIPTION);

merge into MPAS
    using (values ('R',
                   'лицам до 17 лет просматривать фильм можно только в присутствии взрослого')) TMP (NAME, DESCRIPTION)
    on MPAS.NAME = TMP.NAME
    when not matched then INSERT (NAME, DESCRIPTION) values (TMP.NAME, TMP.DESCRIPTION);

merge into MPAS
    using (values ('NC-17', 'лицам до 18 лет просмотр запрещён')) TMP (NAME, DESCRIPTION)
    on MPAS.NAME = TMP.NAME
    when not matched then INSERT (NAME, DESCRIPTION) values (TMP.NAME, TMP.DESCRIPTION);

merge into GENRES
    using (values ('Комедия')) TMP (NAME)
    on GENRES.NAME = TMP.NAME
    when not matched then INSERT (NAME) values (TMP.NAME);

merge into GENRES
    using (values ('Драма')) TMP (NAME)
    on GENRES.NAME = TMP.NAME
    when not matched then INSERT (NAME) values (TMP.NAME);

merge into GENRES
    using (values ('Мультфильм')) TMP (NAME)
    on GENRES.NAME = TMP.NAME
    when not matched then INSERT (NAME) values (TMP.NAME);

merge into GENRES
    using (values ('Триллер')) TMP (NAME)
    on GENRES.NAME = TMP.NAME
    when not matched then INSERT (NAME) values (TMP.NAME);

merge into GENRES
    using (values ('Документальный')) TMP (NAME)
    on GENRES.NAME = TMP.NAME
    when not matched then INSERT (NAME) values (TMP.NAME);

merge into GENRES
    using (values ('Боевик')) TMP (NAME)
    on GENRES.NAME = TMP.NAME
    when not matched then INSERT (NAME) values (TMP.NAME);

insert into USERS (EMAIL, LOGIN, BIRTHDAY, NAME)
values
('vasya@yandex.ru', 'vasya', '2001-01-01', 'Вася'),
('masha@yandex.ru', 'masha', '2002-02-02', 'Маша'),
('petya@yandex.ru', 'petya', '2003-03-03', 'Петя');

insert into FRIENDS (USER_ID, FRIEND_ID)
values
    (1, 3),
    (2, 3),
    (3, 1);

insert into FILMS (NAME, DESCRIPTION, RELEASE_DATE, MPA_ID, DURATION, RATE, LIKE_COUNT)
values
    ('Название 1', 'Описание 1', '1991-01-01', 1, 1991, 1, 0),
    ('Название 2', 'Описание 2', '1992-02-02', 2, 1992, 2, 3),
    ('Название 3', 'Описание 3', '1993-03-03', 3, 1993, 3, 2);

insert into FILM_GENRES (FILM_ID, GENRE_ID)
values
    (1, 1),
    (1, 2),
    (1, 3);

insert into LIKES (FILM_ID, USER_ID)
values
    (2, 1),
    (2, 2),
    (2, 3),
    (3, 1),
    (3, 2);

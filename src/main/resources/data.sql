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

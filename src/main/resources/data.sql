merge into mpa_ratings
    using (values ('G', 'у фильма нет возрастных ограничений')) tmp (name, title)
    on mpa_ratings.name = tmp.name
    when not matched then insert values (tmp.name, tmp.title);

merge into mpa_ratings
    using (values ('PG', 'детям рекомендуется смотреть фильм с родителями')) tmp (name, title)
    on mpa_ratings.name = tmp.name
    when not matched then insert values (tmp.name, tmp.title);

merge into mpa_ratings
    using (values ('PG-13', 'детям до 13 лет просмотр не желателен')) tmp (name, title)
    on mpa_ratings.name = tmp.name
    when not matched then insert values (tmp.name, tmp.title);

merge into mpa_ratings
    using (values ('R', 'лицам до 17 лет просматривать фильм можно только в присутствии взрослого')) tmp (name, title)
    on mpa_ratings.name = tmp.name
    when not matched then insert values (tmp.name, tmp.title);

merge into mpa_ratings
    using (values ('NC-17', 'лицам до 18 лет просмотр запрещён')) tmp (name, title)
    on mpa_ratings.name = tmp.name
    when not matched then insert values (tmp.name, tmp.title);

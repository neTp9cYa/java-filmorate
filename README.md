# Сервис для фильмов

## База данных

### Схема
![schema](/docs/schema.png)

#### Рейтинг фильма

В таблице films колонка mpa_rating (Motion Picture Association Rating, рейтинг Ассоциации кинокомпаний) хранится в виде строки.
В этой колонке возможны следующие значения:
- G — у фильма нет возрастных ограничений,
- PG — детям рекомендуется смотреть фильм с родителями,
- PG-13 — детям до 13 лет просмотр не желателен,
- R — лицам до 17 лет просматривать фильм можно только в присутствии взрослого,
- NC-17 — лицам до 18 лет просмотр запрещён.
Это перечисление на вынесено в отдельную таблицу, так как все возможные значения известны и крайне редко меняются.

#### Популярные фильмы

В таблице films поле "like_count" - дополнительное поле для возможности быстрой выборки поулярных фильмов. Оно должно обновляться всякий раз при изменении данных в таблице likes. 

#### Друзья

В таблице friends колонка status хранится в виде строки.
В этой колонке возможны следующие значения:
- UNCONFIRMED — когда один пользователь отправил запрос на добавление другого пользователя в друзья
- CONFIRMED — когда второй пользователь согласился на добавление
Это перечисление на вынесено в отдельную таблицу, так как все возможные значения известны и крайне редко меняются.

Для взаимной дружбы двух пользоватлей в эту таблицу необходимо добавить две записи


### Примеры запросов

#### Добавление лайка

```sql
START TRANSACTION;

INSERT INTO likes
(film_id, user_id)
VALUES(1, 2);

UPDATE films
SET like_count = like_count + 1
WHERE id = 1;

COMMIT TRANSACTION;
```
#### Выбор популярных фильмов

```sql
SELECT *
FROM films 
ORDER BY like_count DESC
limit 5
```

#### Добавление в друзья

Пользователь с идентификатором 1, добавляет пользователя с идентификатором 2, как совего друга
```sql
INSERT INTO public.friends
(user_id, friend_id, status)
VALUES(1, 2, 'UNCONFIRMED');
```

Пользователь с идентификатором 2 подтверждает, что является другом пользователя с идентификатором 1

```sql
START TRANSACTION;

INSERT INTO public.friends
(user_id, friend_id, status)
VALUES(2, 1, 'CONFIRMED');

UPDATE friends
SET status = 'CONFIRMED'
WHERE user_id = 1 AND friend_id = 2

COMMIT TRANSACTION;
```

#### Получение списка друзей

```sql
SELECT u.*
FROM friends f
JOIN users u ON f.friend_id = u.id 
WHERE f.user_id = 1 c f.status = 'CONFIRMED'
```

#### Получение списка взаимных друзей

```sql
SELECT u.*
FROM friends f1
JOIN friends f2 ON f1.friend_id = f2.friend_id
JOIN users u ON f1.friend_id = u.id 
WHERE f1.user_id = 1 AND f1.status = 'CONFIRMED' AND f2.user_id = 2 AND f2.status = 'CONFIRMED'
```




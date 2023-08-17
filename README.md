# Сервис для фильмов

## База данных

### Схема
![schema](/docs/schema.png)

#### Популярные фильмы

В таблице films поле "like_count" - дополнительное поле для возможности быстрой выборки поулярных фильмов. Оно должно обновляться всякий раз при изменении данных в таблице likes. 

#### Друзья

В таблице friends колонка status хранится в виде строки.
В этой колонке возможны следующие значения:
- UNCONFIRMED — когда один пользователь отправил запрос на добавление другого пользователя в друзья
- CONFIRMED — когда второй пользователь согласился на добавление
Это перечисление на вынесено в отдельную таблицу, так как все возможные значения известны и крайне редко меняются.

Для взаимной дружбы двух пользоватлей в эту таблицу необходимо добавить две записи

### SQL описание схемы

#### Фильмы

```sql
CREATE TABLE public.genres (
	id int4 NOT NULL GENERATED ALWAYS AS IDENTITY( INCREMENT BY 1 MINVALUE 1 MAXVALUE 2147483647 START 1 CACHE 1 NO CYCLE),
	"name" varchar(50) NOT NULL,
	CONSTRAINT genres_pk PRIMARY KEY (id),
  CONSTRAINT genres_un UNIQUE (name)
);

CREATE TABLE public.mpa_ratings (
	name varchar(5) NOT NULL,
	title varchar(50) NOT NULL,
	CONSTRAINT mpa_ratings_pk PRIMARY KEY (id)
);

CREATE TABLE public.films (
	id int4 NOT NULL GENERATED ALWAYS AS IDENTITY( INCREMENT BY 1 MINVALUE 1 MAXVALUE 2147483647 START 1 CACHE 1 NO CYCLE),
	"name" varchar(200) NOT NULL,
	description varchar(200) NULL,
	release_date date NULL,
	duration int4 NULL,
	mpa_rating_name varchar(5) NULL,
	like_count int4 NOT NULL,
	CONSTRAINT films_pk PRIMARY KEY (id)
);

ALTER TABLE public.films ADD CONSTRAINT films_fk FOREIGN KEY (mpa_rating_name) REFERENCES public.mpa_ratings("name");

CREATE TABLE public.film_genres (
	film_id int4 NOT NULL,
	genre_id int4 NOT NULL,
	CONSTRAINT film_genres_pk PRIMARY KEY (film_id, genre_id)
);

ALTER TABLE public.film_genres ADD CONSTRAINT film_genres_fk_films FOREIGN KEY (film_id) REFERENCES public.films(id) ON DELETE CASCADE;
ALTER TABLE public.film_genres ADD CONSTRAINT film_genres_fk_genres FOREIGN KEY (genre_id) REFERENCES public.genres(id) ON DELETE CASCADE;
```

#### Пользователи

```sql
CREATE TABLE public.users (
	id int4 NOT NULL GENERATED ALWAYS AS IDENTITY( INCREMENT BY 1 MINVALUE 1 MAXVALUE 2147483647 START 1 CACHE 1 NO CYCLE),
	email varchar(50) NOT NULL,
	login varchar(50) NOT NULL,
	dirthday date NULL,
	"name" varchar(50) NULL,
	CONSTRAINT users_pk PRIMARY KEY (id),
  CONSTRAINT users_un UNIQUE (email)
);

CREATE TABLE public.friends (
  user_id int4 NOT NULL,
  friend_id int4 NOT NULL,
  status varchar(11) NULL,
  CONSTRAINT friends_pk PRIMARY KEY (user_id,friend_id)
);

ALTER TABLE public.friends ADD CONSTRAINT friends_fk_user_friends FOREIGN KEY (friend_id) REFERENCES public.users(id) ON DELETE CASCADE;
ALTER TABLE public.friends ADD CONSTRAINT friends_fk_users FOREIGN KEY (user_id) REFERENCES public.users(id) ON DELETE CASCADE;
```

#### Лайки

```sql
CREATE TABLE public.likes (
	film_id int4 NOT NULL,
	user_id int4 NOT NULL,
	CONSTRAINT likes_pk PRIMARY KEY (film_id, user_id)
);

ALTER TABLE public.likes ADD CONSTRAINT likes_fk_films FOREIGN KEY (film_id) REFERENCES public.films(id);
ALTER TABLE public.likes ADD CONSTRAINT likes_fk_users FOREIGN KEY (user_id) REFERENCES public.users(id);
```



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





create table if not exists GENRES
(
    GENRE_ID INTEGER auto_increment,
    NAME CHARACTER VARYING (50) not null,
    constraint GENRES_PK primary key (GENRE_ID)
);

create table if not exists MPAS
(
    MPA_ID INTEGER auto_increment,
    NAME CHARACTER VARYING (5) not null,
    DESCRIPTION CHARACTER VARYING (100) not null,
    constraint MPAS_PK primary key (MPA_ID)
);

create table if not exists FILMS
(
    FILM_ID INTEGER auto_increment,
    NAME CHARACTER VARYING (200) not null,
    DESCRIPTION CHARACTER VARYING (200),
    RELEASE_DATE DATE,
    MPA_ID INTEGER,
    DURATION INTEGER not null,
    RATE INTEGER not null,
    LIKE_COUNT INTEGER not null,
    constraint FILMS_PK primary key (FILM_ID),
    constraint FILMS_FK_MPA foreign key (MPA_ID) references MPAS
);

create table if not exists FILM_GENRES
(
    FILM_ID INTEGER not null,
    GENRE_ID INTEGER not null,
    constraint FILM_GENRES_PK primary key (FILM_ID, GENRE_ID),
    constraint FILM_GENRES_FK_FILM foreign key (FILM_ID) references FILMS on delete cascade,
    constraint FILM_GENRES_FK_GENRE foreign key (GENRE_ID) references GENRES on delete cascade
);

create table if not exists USERS
(
    USER_ID INTEGER auto_increment,
    EMAIL CHARACTER VARYING (50) not null,
    LOGIN CHARACTER VARYING (50) not null,
    BIRTHDAY DATE,
    NAME CHARACTER VARYING(50),
    constraint USERS_PK primary key (USER_ID)
);

create table if not exists FRIENDS
(
    USER_ID INTEGER not null,
    FRIEND_ID INTEGER not null,
    constraint FRIENDS_PK primary key (USER_ID, FRIEND_ID),
    constraint FRIENDS_FK_USER foreign key (USER_ID) references USERS on delete cascade,
    constraint FRIENDS_FK_USER_FRIEND foreign key (FRIEND_ID) references USERS on delete cascade
);

create table if not exists LIKES
(
    FILM_ID INTEGER not null,
    USER_ID INTEGER not null,
    constraint FILMORATE_LIKE_PK primary key (FILM_ID, USER_ID),
    constraint FILMORATE_LIKE_FK_FILM foreign key (FILM_ID) references FILMS on delete cascade,
    constraint FILMORATE_LIKE_FK_USER foreign key (USER_ID) references USERS
);

create table if not exists GENRES
(
    ID   INTEGER auto_increment,
    NAME CHARACTER VARYING(50) not null,
    constraint GENRES_PK
        primary key (ID)
);

create unique index if not exists GENRES_INDEX_NAME
    on GENRES (NAME);

create table if not exists MPA_RATINGS
(
    NAME  CHARACTER VARYING(5)  not null,
    TITLE CHARACTER VARYING(50) not null,
    constraint MPA_RATINGS_PK
        primary key (NAME)
);

create table if not exists FILMS
(
    ID              INTEGER auto_increment,
    NAME            CHARACTER VARYING(200) not null,
    DESCRIPTION     CHARACTER VARYING(200),
    RELEASE_DATE    DATE,
    MPA_RATING_NAME CHARACTER VARYING(5),
    LIKE_COUNT      INTEGER                not null,
    constraint FILMS_PK
        primary key (ID),
    constraint FILMS_FK_MPA_RATINGS
        foreign key (MPA_RATING_NAME) references MPA_RATINGS
);

create table if not exists FILM_GENRES
(
    FILM_ID  INTEGER not null,
    GENRE_ID INTEGER not null,
    constraint FILM_GENRES_PK
        primary key (FILM_ID, GENRE_ID),
    constraint FILM_GENRES_FK_FILMS
        foreign key (FILM_ID) references FILMS
            on delete cascade,
    constraint FILM_GENRES_FK_GENRES
        foreign key (GENRE_ID) references GENRES
            on delete cascade
);

create table if not exists USERS
(
    ID       INTEGER auto_increment,
    EMAIL    CHARACTER VARYING(50) not null,
    LOGIN    CHARACTER VARYING(50) not null,
    DIRTHDAY DATE,
    NAME     CHARACTER VARYING(50),
    constraint USERS_PK
        primary key (ID)
);

create table if not exists FRIENDS
(
    USER_ID   INTEGER not null,
    FRIEND_ID INTEGER not null,
    STATUS    CHARACTER VARYING(11),
    constraint KEY_NAME
        primary key (USER_ID, FRIEND_ID),
    constraint FRIENDS_FK_USERS
        foreign key (USER_ID) references USERS
            on delete cascade,
    constraint FRIENDS_FK_USER_FRIENDS
        foreign key (FRIEND_ID) references USERS
            on delete cascade
);

create table if not exists LIKES
(
    FILM_ID INTEGER not null,
    USER_ID INTEGER not null,
    constraint LIKES_PK
        primary key (FILM_ID, USER_ID),
    constraint LIKES_FK_FILMS
        foreign key (FILM_ID) references FILMS
            on delete cascade,
    constraint LIKES_FK_USERS
        foreign key (USER_ID) references USERS
);

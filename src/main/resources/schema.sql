create table if not exists GENRE
(
    ID
    INTEGER
    auto_increment,
    NAME
    CHARACTER
    VARYING
(
    50
) not null,
    constraint GENRE_PK
    primary key
(
    ID
)
    );

create table if not exists MPA
(
    ID
    INTEGER
    auto_increment,
    NAME
    CHARACTER
    VARYING
(
    5
) not null,
    DESCRIPTION CHARACTER VARYING
(
    100
) not null,
    constraint MPA_PK
    primary key
(
    ID
)
    );

create table if not exists FILM
(
    ID
    INTEGER
    auto_increment,
    NAME
    CHARACTER
    VARYING
(
    200
) not null,
    DESCRIPTION CHARACTER VARYING
(
    200
),
    RELEASE_DATE DATE,
    MPA_ID INTEGER,
    DURATION INTEGER not null,
    RATE INTEGER not null,
    LIKE_COUNT INTEGER not null,
    constraint FILM_PK
    primary key
(
    ID
),
    constraint FILM_FK_MPA
    foreign key
(
    MPA_ID
) references MPA
    );

create table if not exists FILM_GENRE
(
    FILM_ID
    INTEGER
    not
    null,
    GENRE_ID
    INTEGER
    not
    null,
    constraint
    FILM_GENRE_PK
    primary
    key
(
    FILM_ID,
    GENRE_ID
),
    constraint FILM_GENRE_FK_FILM
    foreign key
(
    FILM_ID
) references FILM
    on delete cascade,
    constraint FILM_GENRE_FK_GENRE
    foreign key
(
    GENRE_ID
) references GENRE
    on delete cascade
    );

create table if not exists FILMORATE_USER
(
    ID
    INTEGER
    auto_increment,
    EMAIL
    CHARACTER
    VARYING
(
    50
) not null,
    LOGIN CHARACTER VARYING
(
    50
) not null,
    BIRTHDAY DATE,
    NAME CHARACTER VARYING
(
    50
),
    constraint FILMORATE_USER_PK
    primary key
(
    ID
)
    );

create table if not exists FRIEND
(
    USER_ID
    INTEGER
    not
    null,
    FRIEND_ID
    INTEGER
    not
    null,
    constraint
    FRIEND_PK
    primary
    key
(
    USER_ID,
    FRIEND_ID
),
    constraint FRIEND_FK_USER
    foreign key
(
    USER_ID
) references FILMORATE_USER
    on delete cascade,
    constraint FRIEND_FK_USER_FRIEND
    foreign key
(
    FRIEND_ID
) references FILMORATE_USER
    on delete cascade
    );

create table if not exists FILMORATE_LIKE
(
    FILM_ID
    INTEGER
    not
    null,
    USER_ID
    INTEGER
    not
    null,
    constraint
    FILMORATE_LIKE_PK
    primary
    key
(
    FILM_ID,
    USER_ID
),
    constraint FILMORATE_LIKE_FK_FILM
    foreign key
(
    FILM_ID
) references FILM
    on delete cascade,
    constraint FILMORATE_LIKE_FK_USER
    foreign key
(
    USER_ID
) references FILMORATE_USER
    );

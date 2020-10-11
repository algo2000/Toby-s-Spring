create table User
(
    Id        varchar(10) not null
        primary key,
    Name      varchar(20) not null,
    Password  varchar(20) not null,
    Recommend int         not null,
    Login     int         not null,
    Level     tinyint     not null,
    email     varchar(30)
);



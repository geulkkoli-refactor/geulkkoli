create table if not exists hashtag
(
    hash_tag_id   bigint auto_increment
        primary key,
    created_at    varchar(255) not null,
    updated_at    varchar(255) null,
    hash_tag_name varchar(255) not null,
    hash_tag_type int          not null
);

create table if not exists roles
(
    role_id     bigint auto_increment
        primary key,
    role_number int null
);

create table if not exists topic
(
    topic_id             bigint auto_increment
        primary key,
    topic_name           varchar(255) null,
    topic_up_coming_date date         null,
    topic_use_date       date         null
);

create table if not exists users
(
    user_id      bigint auto_increment
        primary key,
    sign_up_date varchar(255) not null,
    email        varchar(255) not null,
    gender       varchar(255) not null,
    nick_name    varchar(255) not null,
    password     varchar(255) not null,
    phone_no     varchar(255) null,
    user_name    varchar(255) not null,
    role_id      bigint       null,
    constraint
        unique (email),
    constraint
        unique (nick_name),
    constraint
        unique (phone_no),
    constraint
        foreign key (role_id) references roles (role_id)
);

create table if not exists account_lock
(
    id         bigint auto_increment
        primary key,
    lock_until datetime(6)  not null,
    reason     varchar(200) not null,
    user_id    bigint       null,
    constraint
        foreign key (user_id) references users (user_id)
);

create table if not exists post
(
    post_id           bigint auto_increment
        primary key,
    created_at        varchar(255)   not null,
    updated_at        varchar(255)   null,
    image_upload_name varchar(255)   null,
    nick_name         varchar(255)   not null,
    body              varchar(15000) not null,
    post_hits         int            not null,
    title             varchar(255)   not null,
    user_id           bigint         null,
    constraint
        foreign key (user_id) references users (user_id)
);

create table if not exists comments
(
    comment_id bigint auto_increment
        primary key,
    created_at varchar(255) not null,
    updated_at varchar(255) null,
    body       varchar(255) null,
    post_id    bigint       null,
    author_id  bigint       null,
    constraint
        foreign key (post_id) references post (post_id),
    constraint
        foreign key (author_id) references users (user_id)
);

create table if not exists favorites
(
    favorites_id      bigint auto_increment primary key,
    post_id           bigint null,
    favorites_user_id bigint null,
    constraint
        foreign key (post_id) references post (post_id),
    constraint
        foreign key (favorites_user_id) references users (user_id)
);

create table if not exists post_hashtag
(
    post_hashtag_id bigint auto_increment
        primary key,
    created_at      varchar(255) not null,
    updated_at      varchar(255) null,
    hashtag_id      bigint       null,
    post_id         bigint       null,
    constraint
        foreign key (post_id) references post (post_id)
            on delete cascade,
    constraint
        foreign key (hashtag_id) references hashtag (hash_tag_id)
);

create table if not exists report
(
    report_id        bigint auto_increment
        primary key,
    reason           varchar(100) not null,
    reported_at      datetime(6)  not null,
    reported_post_id bigint       not null,
    reporter_id      bigint       not null,
    constraint unique_report
        unique (reported_post_id, reporter_id),
    constraint
        foreign key (reporter_id) references users (user_id),
    constraint
        foreign key (reported_post_id) references post (post_id)
            on delete cascade
);

create table if not exists social_info
(
    social_info_id      bigint auto_increment
        primary key,
    is_connected        tinyint(1) default 1 null,
    social_connect_date varchar(255)         null,
    social_id           varchar(255)         not null,
    social_type         varchar(255)         not null,
    user_id             bigint               not null,
    constraint unique (social_id, social_type),
    constraint foreign key (user_id) references users (user_id)
);

create table if not exists user_followings
(
    followings_id bigint auto_increment
        primary key,
    created_at    datetime(6) not null,
    followee_id   bigint      null,
    follower_id   bigint      null,
    constraint
        unique (followee_id, follower_id),
    constraint
        foreign key (followee_id) references users (user_id),
    constraint
        foreign key (follower_id) references users (user_id)
);

create index idx_user_email_nick_name
    on users (email, nick_name);


CREATE TABLE Author(id SERIAL  PRIMARY KEY,
                    first_name varchar(100) NOT NULL,
                    last_name varchar(100) NOT NULL,
                    email varchar(255) NOT NULL,
                    username varchar(100) NOT NULL
);

create table Post (
    id SERIAL  PRIMARY KEY ,
    version int,
    title varchar(255) not null,
    content text not null,
    published_on timestamp not null,
    updated_on timestamp,
    author int,
    foreign key (author) references Author(id)
);

create table Comment(
    post int not null,
    name varchar(100) not null,
    content text not null,
    published_on timestamp not null,
    updated_on timestamp,
    foreign key (post) references Post(id)
);

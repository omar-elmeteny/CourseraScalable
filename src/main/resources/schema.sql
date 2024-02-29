
Create database if not exists "usermanagement";

Create table if not exists Users (
    id int(11) not null auto_increment,
    name varchar(255) not null,
    age int(11) not null,
    primary key (`id`)
)




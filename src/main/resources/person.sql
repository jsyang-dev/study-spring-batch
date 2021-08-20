create table person
(
    id      bigint primary key auto_increment,
    name    varchar(255),
    age     int,
    address varchar(255)
);

insert into person(name, age, address)
values ('홍길동', 30, '서울');
insert into person(name, age, address)
values ('아무개', 25, '강원');
insert into person(name, age, address)
values ('임꺽정', 40, '경기');

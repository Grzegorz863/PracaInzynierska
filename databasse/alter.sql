create table tcpsdb.consortiums(
	consortium_id int8 unsigned auto_increment primary key,
    consortium_name varchar(255) not null,
    logo varchar(255)
) ENGINE INNODB;

create table tcpsdb.petrol_stations(
	station_id int8 unsigned auto_increment primary key,
    consortium_id int8 unsigned not null,
	longitude double not null,
    latitude double not null,
    has_food bool not null,
    apartment_number INT4 unsigned not null,
    postal_code char(6),
    station_name varchar(100) not null,
    city varchar(50) not null,
    street varchar(70) not null,
    description varchar(255)
) ENGINE INNODB;

alter table petrol_stations
add constraint consortium_station_fk
foreign key(consortium_id) references consortiums(consortium_id);

create table tcpsdb.users(
	user_id int8 unsigned auto_increment primary key,
    user_password char(60) not null,
    user_name varchar(255) not null,
	user_role varchar(25) not null
) ENGINE INNODB;

create table tcpsdb.ratings(
	rating_id int8 unsigned auto_increment primary key,
    user_id int8 unsigned not null,
    station_id int8 unsigned not null,
    rate double unsigned
) ENGINE INNODB;

alter table tcpsdb.ratings
add constraint user_rating_fk
foreign key(user_id) references tcpsdb.users(user_id);

alter table tcpsdb.ratings
add constraint station_rating_fk
foreign key(station_id) references tcpsdb.petrol_stations(station_id);

create table tcpsdb.petrol_prices(
	price_id int8 unsigned auto_increment primary key,
    station_id int8 unsigned not null,
    pb95_price float4 unsigned,
    pb98_price float4 unsigned,
    on_price float4 unsigned,
    lpg_price float4 unsigned
) ENGINE INNODB;

alter table tcpsdb.petrol_prices
add constraint station_prices_fk
foreign key(station_id) references tcpsdb.petrol_stations(station_id);

create table tcpsdb.historic_prices(
	historic_price_id int8 unsigned auto_increment primary key,
    user_id int8 unsigned not null,
    station_id int8 unsigned not null,
    pb95_price float4 unsigned,
    pb98_price float4 unsigned,
    on_price float4 unsigned,
    lpg_price float4 unsigned,
    insert_date datetime not null
) ENGINE INNODB;

alter table tcpsdb.historic_prices
add constraint station_historicprices_fk
foreign key(station_id) references tcpsdb.petrol_stations(station_id);

alter table tcpsdb.historic_prices
add constraint user_historicprices_fk
foreign key(user_id) references tcpsdb.users(user_id);


/* USUWANIE TABEL */
/*
drop table tcpsdb.consortiums;
drop table tcpsdb.petrol_stations;
drop table tcpsdb.users;
drop table tcpsdb.ratings;
drop table tcpsdb.petrol_prices;
drop table tcpsdb.historic_prices;
*/
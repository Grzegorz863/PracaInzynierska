create table tcpsdb.consortiums(
	consortium_id INT(6) unsigned auto_increment primary key,
    consortium_name varchar(255) not null,
    logo varchar(255)
) ENGINE INNODB;

create table tcpsdb.petrol_stations(
	station_id INT(6) unsigned auto_increment primary key,
    consortium_id INT(6) unsigned,
    station_name varchar(255) not null,
    city varchar(255) not null,
    street varchar(255) not null,
    apartment_number INT4 not null,
    postal_code varchar(10),
    longitude double,
    latitude double,
    has_food bool,
    description varchar(255)
) ENGINE INNODB;

alter table petrol_stations
add constraint consortium_station_fk
foreign key(consortium_id) references consortiums(consortium_id);

create table tcpsdb.users(
	user_id INT(7) unsigned auto_increment primary key,
    user_name varchar(255) not null,
    user_password varchar(255) not null,
    user_role varchar(255) not null
);

create table tcpsdb.ratings(
	rating_id INT(7) unsigned auto_increment primary key,
    user_id INT(7) unsigned not null,
    station_id INT(6) unsigned not null,
    rate double
);

alter table tcpsdb.ratings
add constraint user_rating_fk
foreign key(user_id) references tcpsdb.users(user_id);

alter table tcpsdb.ratings
add constraint station_rating_fk
foreign key(station_id) references tcpsdb.petrol_stations(station_id);

create table petrol_prices(
	price_id INT(6) unsigned auto_increment primary key,
    station_id INT(6) unsigned not null,
    pb95_price float4,
    pb98_price float4,
    on_price float4,
    lpg_price float4
);

alter table tcpsdb.petrol_prices
add constraint station_prices_fk
foreign key(station_id) references tcpsdb.petrol_stations(station_id);

create table tcpsdb.historic_prices(
	historic_price_id INT(6) unsigned auto_increment primary key,
    user_id INT(7) unsigned not null,
    station_id INT(6) unsigned not null,
    pb95_price float4,
    pb98_price float4,
    on_price float4,
    lpg_price float4,
    insert_date datetime not null
);

alter table tcpsdb.historic_prices
add constraint station_historicprices_fk
foreign key(station_id) references tcpsdb.petrol_stations(station_id);

alter table tcpsdb.historic_prices
add constraint user_historicprices_fk
foreign key(user_id) references tcpsdb.users(user_id);
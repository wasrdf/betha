create table tb_item(
id_item int identity ,
name varchar(100) not null,
description varchar(100),
unit_value numeric(10,2),
primary key(id_item)
);


create table tb_order(
id_order int identity ,
date_order date,
primary key(id_order)
);

create table tb_order_item(
id_order_item int identity,
id_order int,
id_item int,
quantity int,
total_cost numeric(10,2),
unit_cost numeric(10,2),
primary key(id_order_item)
);

alter table tb_order_item
add constraint fk_order_item_order foreign key(id_order) references tb_order(id_order);

alter table tb_order_item
add constraint fk_order_item_item foreign key(id_item) references tb_item(id_item);

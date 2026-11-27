create table shopping_carts (
                                id uuid not null,
                                created_at timestamp with time zone,
                                created_by_user_id uuid,
                                last_modified_at timestamp with time zone,
                                last_modified_by_user_id uuid,
                                total_amount numeric(38,2),
                                total_items integer,
                                version bigint,
                                customer_id uuid not null,
                                primary key (id)
);

create index idx_shopping_carts_customer_id
    on shopping_carts (customer_id);

alter table shopping_carts
    add constraint fk_shopping_carts_customer_id
        foreign key (customer_id) references customers(id);


create table shopping_cart_items (
                                     id uuid not null,
                                     available boolean,
                                     created_at timestamp with time zone,
                                     created_by_user_id uuid,
                                     last_modified_at timestamp with time zone,
                                     last_modified_by_user_id uuid,
                                     product_name varchar(255),
                                     price numeric(38,2),
                                     product_id uuid,
                                     quantity integer,
                                     total_amount numeric(38,2),
                                     version bigint,
                                     shopping_cart_id uuid not null,
                                     primary key (id)
);

create index idx_shopping_cart_items_shopping_cart_id
    on shopping_cart_items (shopping_cart_id);

alter table shopping_cart_items
    add constraint fk_shopping_cart_items_shopping_cart_id
        foreign key (shopping_cart_id) references shopping_carts(id);
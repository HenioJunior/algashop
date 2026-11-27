create table orders (
                        id bigint not null,
                        customer_id uuid not null,
                        status varchar(255),
                        total_amount numeric(38,2),
                        total_items integer,
                        canceled_at timestamp with time zone,
                        created_by_user_id uuid,
                        last_modified_at timestamp with time zone,
                        last_modified_by_user_id uuid,
                        paid_at timestamp with time zone,
                        payment_method varchar(255),
                        placed_at timestamp with time zone,
                        ready_at timestamp with time zone,
                        version bigint,
                        shipping_address_city varchar(255),
                        shipping_address_complement varchar(255),
                        shipping_address_neighborhood varchar(255),
                        shipping_address_number varchar(255),
                        shipping_address_state varchar(255),
                        shipping_address_street varchar(255),
                        shipping_address_zip_code varchar(255),
                        shipping_cost numeric(38,2),
                        shipping_expected_date date,
                        shipping_recipient_document varchar(255),
                        shipping_recipient_first_name varchar(255),
                        shipping_recipient_last_name varchar(255),
                        shipping_recipient_phone varchar(255),
                        billing_address_city varchar(255),
                        billing_address_complement varchar(255),
                        billing_address_neighborhood varchar(255),
                        billing_address_number varchar(255),
                        billing_address_state varchar(255),
                        billing_address_street varchar(255),
                        billing_address_zip_code varchar(255),
                        billing_document varchar(255),
                        billing_email varchar(255),
                        billing_first_name varchar(255),
                        billing_last_name varchar(255),
                        billing_phone varchar(255),
                        primary key (id)
);

create index idx_orders_customer_id
    on orders (customer_id);

alter table orders
    add constraint fk_orders_customer_id
        foreign key (customer_id) references customers(id);


create table order_items (
                             id bigint not null,
                             order_id bigint not null,
                             price numeric(38,2),
                             product_id uuid,
                             product_name varchar(255),
                             quantity integer,
                             total_amount numeric(38,2),
                             primary key (id)
);

create index idx_order_items_order_id
    on order_items (order_id);

alter table order_items
    add constraint fk_order_items_order_id
        foreign key (order_id) references orders(id);
create table invoices (
                          id uuid not null,
                          order_id varchar(255) not null,
                          customer_id uuid not null,
                          issued_at timestamp with time zone not null,
                          paid_at timestamp with time zone,
                          canceled_at timestamp with time zone,
                          expires_at timestamp with time zone,
                          total_amount numeric(38,2) not null,
                          status varchar(255) not null,
                          payment_settings_id uuid,
                          payer_full_name varchar(255),
                          payer_document varchar(255),
                          payer_phone varchar(255),
                          payer_email varchar(255),
                          payer_address_street varchar(255),
                          payer_address_number varchar(255),
                          payer_address_complement varchar(255),
                          payer_address_neighborhood varchar(255),
                          payer_address_city varchar(255),
                          payer_address_state varchar(255),
                          payer_address_zip_code varchar(255),
                          cancel_reason varchar(255),
                          primary key (id)
);

create index idx_invoices_customer_id
    on invoices (customer_id);

create index idx_invoices_order_id
    on invoices (order_id);

alter table invoices
    add constraint uq_invoices_payment_settings_id
        unique (payment_settings_id);

alter table invoices
    add constraint fk_invoices_payment_settings_id
        foreign key (payment_settings_id)
            references payment_settings(id);
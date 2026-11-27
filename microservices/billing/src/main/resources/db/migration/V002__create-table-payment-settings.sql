create table payment_settings (
                                  id uuid not null,
                                  credit_card_id uuid,
                                  gateway_code varchar(255),
                                  payment_method varchar(255) not null,
                                  primary key (id)
);

create index idx_payment_settings_credit_card_id
    on payment_settings (credit_card_id);

alter table payment_settings
    add constraint fk_payment_settings_credit_card_id
        foreign key (credit_card_id) references credit_cards(id);
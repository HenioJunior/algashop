create table credit_cards (
                              id uuid not null,
                              created_at timestamp with time zone not null,
                              customer_id uuid not null,
                              last_numbers varchar(4) not null,
                              brand varchar(50) not null,
                              exp_month integer not null,
                              exp_year integer not null,
                              gateway_code varchar(255) not null,
                              primary key (id)
);

create index idx_credit_cards_customer_id
    on credit_cards (customer_id);

alter table credit_cards
    add constraint ck_credit_cards_exp_month
        check (exp_month between 1 and 12);

alter table credit_cards
    add constraint ck_credit_cards_exp_year
        check (exp_year >= 2000);

alter table credit_cards
    add constraint ck_credit_cards_last_numbers
        check (last_numbers ~ '^[0-9]{4}$');
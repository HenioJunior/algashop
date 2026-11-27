create table invoice_line_items (
                                    invoice_id uuid not null,
                                    name varchar(255),
                                    price numeric(38,2),
                                    quantity integer,
                                    total_amount numeric(38,2)
);

create index idx_invoice_line_items_invoice_id
    on invoice_line_items (invoice_id);

alter table invoice_line_items
    add constraint fk_invoice_line_items_invoice_id
        foreign key (invoice_id) references invoices(id);
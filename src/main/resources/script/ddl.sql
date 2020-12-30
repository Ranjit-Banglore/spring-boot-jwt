create sequence transaction_id_seq ;
create sequence customer_id_seq ;
create sequence bank_id_seq ;

-- DROP TABLE public.customers;

CREATE TABLE public.customers (
	id int8 NOT NULL,
	email varchar(255) NOT NULL,
	"name" varchar(255) NULL,
	phone varchar(255) NULL,
	CONSTRAINT customers_pkey PRIMARY KEY (id)
);

-- DROP TABLE public.bankaccounts;

CREATE TABLE public.bankaccounts (
	id int8 NOT NULL,
	active bool NULL default false,
	balance float8 NULL default 0,
	iban varchar(255) NOT NULL,
	"type" varchar(255) NOT NULL,
	customer_id int8 NULL,
	CONSTRAINT bankaccounts_pkey PRIMARY KEY (id),
	CONSTRAINT bankaccounts_fkey FOREIGN KEY (customer_id) REFERENCES customers(id)
);

insert into "user" (id, active ,password ,roles ,name )
values (1,true,'$2a$12$.JieZ0puJTJCSf5eg7FtZek9Z7H1vwou0XM.oozKLLh.6GfiBUuMi',ROLE_USER,'ranjit');




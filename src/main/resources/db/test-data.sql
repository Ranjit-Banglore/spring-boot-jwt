--customer data
INSERT INTO customers
(id, email, name, phone)
VALUES(customer_id_seq.NEXTVAL, 'john@example.de', 'John', '+49875628345');
INSERT INTO public.customers
(id, email, name, phone)
VALUES(customer_id_seq.NEXTVAL, 'alice@example.de', 'Alice', '+4982149870');
INSERT INTO public.customers
(id, email, name, phone)
VALUES(customer_id_seq.NEXTVAL, 'david@example.de', 'David', '+49875678263');

--bankaccounts
INSERT INTO bankaccounts
(id, active, balance, iban, type, customer_id)
VALUES(bank_id_seq.NEXTVAL, true, 1231.0, 'DE59578454054765551413', 'CHECKINGS', 1);
INSERT INTO public.bankaccounts
(id, active, balance, iban, type, customer_id)
VALUES(bank_id_seq.NEXTVAL, true, 82421.0, 'DE96514388914485185983', 'PRIVATE_LOAN', 1);
INSERT INTO public.bankaccounts
(id, active, balance, iban, type, customer_id)
VALUES(bank_id_seq.NEXTVAL, true, 21731.0, 'DE21682132570987500492', 'SAVINGS', 1);
INSERT INTO public.bankaccounts
(id, active, balance, iban, type, customer_id)
VALUES(bank_id_seq.NEXTVAL, false, 5312173.0, 'DE21682132570987572137', 'PRIVATE_LOAN', 2);

INSERT INTO public.transaction_history
(id, amount, date, iban, net_balance, type)
VALUES(transaction_id_seq.NEXTVAL, 1284312838.98, '2020-12-22 23:11:10.169', 'DE59578454054765551413', 1284312838.98, 'CREDIT');
INSERT INTO public.transaction_history
(id, amount, date, iban, net_balance, type)
VALUES(transaction_id_seq.NEXTVAL, 1284312838.98, '2020-12-22 23:15:43.563', 'DE59578454054765551413', 2540913560.61, 'CREDIT');
INSERT INTO public.transaction_history
(id, amount, date, iban, net_balance, type)
VALUES(transaction_id_seq.NEXTVAL, 1284312838.98, '2020-12-22 23:15:44.410', 'DE59578454054765551413', 3825226399.59, 'CREDIT');
INSERT INTO public.transaction_history
(id, amount, date, iban, net_balance, type)
VALUES(transaction_id_seq.NEXTVAL, 1284312838.98, '2020-12-22 23:15:45.273', 'DE59578454054765551413', 5109539238.57, 'CREDIT');
INSERT INTO public.transaction_history
(id, amount, date, iban, net_balance, type)
VALUES(transaction_id_seq.NEXTVAL, 1284312838.98, '2020-12-22 23:15:45.942', 'DE59578454054765551413', 6393852077.549999, 'CREDIT');


--alter sequence customer_id_seq restart with 4;
--alter sequence bank_id_seq restart with 4;

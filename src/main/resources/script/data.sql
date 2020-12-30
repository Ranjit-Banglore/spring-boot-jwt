--customer data
INSERT INTO public.customers
(id, email, "name", phone)
VALUES(1, 'john@example.de', 'John', '+49875628345');
INSERT INTO public.customers
(id, email, "name", phone)
VALUES(2, 'alice@example.de', 'Alice', '+4982149870');
INSERT INTO public.customers
(id, email, "name", phone)
VALUES(3, 'david@example.de', 'David', '+49875678263');

--bankaccounts
INSERT INTO public.bankaccounts
(id, active, balance, iban, "type", customer_id)
VALUES(1, true, 0.0, 'DE59578454054765551413', 'CHECKINGS', 1);
INSERT INTO public.bankaccounts
(id, active, balance, iban, "type", customer_id)
VALUES(2, true, 0.0, 'DE96514388914485185983', 'PRIVATE_LOAN', 1);
INSERT INTO public.bankaccounts
(id, active, balance, iban, "type", customer_id)
VALUES(3, true, 0.0, 'DE21682132570987500492', 'SAVINGS', 1);

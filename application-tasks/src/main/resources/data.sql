INSERT INTO product (id, name, sku, price, enabled) VALUES (1, 'Milch', '102', 20, true);
INSERT INTO product (id, name, sku, price, enabled) VALUES (2, 'Brot', '2035', 30, true);
INSERT INTO product (id, name, sku, price, enabled) VALUES (3, 'Käse', 'S-155', 40, true);
INSERT INTO product (id, name, sku, price, enabled) VALUES (4, 'Wurst', '1488', 35, true);
INSERT INTO product (id, name, sku, price, enabled) VALUES (5, 'Couscous', 'B001', 45, true);

INSERT INTO product_eans (product_id, eans) VALUES (1, '12345678');
INSERT INTO product_eans (product_id, eans) VALUES (1, '77777777');
INSERT INTO product_eans (product_id, eans) VALUES (1, '23498128');
INSERT INTO product_eans (product_id, eans) VALUES (2, '34558821');
INSERT INTO product_eans (product_id, eans) VALUES (2, '12323410');
INSERT INTO product_eans (product_id, eans) VALUES (3, '34598146');
INSERT INTO product_eans (product_id, eans) VALUES (3, '43565922');
INSERT INTO product_eans (product_id, eans) VALUES (3, '23454045');
INSERT INTO product_eans (product_id, eans) VALUES (4, '18754629');
INSERT INTO product_eans (product_id, eans) VALUES (4, '46025548');
INSERT INTO product_eans (product_id, eans) VALUES (5, '54342316');

insert into users(id, username,password,enabled) values(1,'user1','user1',true);
insert into users(id, username,password,enabled) values(2,'user2','user2',true);
--
--insert into authorities(username,authority) values('steve','admin');
--insert into authorities(username,authority) values('john','superadmin');

-- roles
INSERT INTO roles (id, name) VALUES ('f8c3de3d-1fea-4d7c-a8b0-29f63c4c3454','ROLE_EMPLOYEE');
INSERT INTO roles (id, name) VALUES ('018b2f19-e79e-7d6a-a56d-29feb6211b04','ROLE_CUSTOMER');

-- users (BCrypt hashed password) -> sample password: 'pass123' hashed value must be created beforehand
INSERT INTO users (id, username, password_hash) VALUES
('550e8400-e29b-41d4-a716-446655440000','employee1','$2a$10$33vpgiaUGcatLqkpfQedTe8ROvnOVRCv9kXuFM33hP8QoDYYIUsRe'),
('a3b0c02c-1e34-4b7e-9a9f-cf8a7ef77d1e','customer1','$2a$10$33vpgiaUGcatLqkpfQedTe8ROvnOVRCv9kXuFM33hP8QoDYYIUsRe'),
('0877ee77-5f40-49c1-8e2e-f3a88d2f4f48','customer2','$2a$10$33vpgiaUGcatLqkpfQedTe8ROvnOVRCv9kXuFM33hP8QoDYYIUsRe');

-- user_roles (join)
INSERT INTO user_roles (user_id, role_id) VALUES ('550e8400-e29b-41d4-a716-446655440000','f8c3de3d-1fea-4d7c-a8b0-29f63c4c3454');
INSERT INTO user_roles (user_id, role_id) VALUES ('a3b0c02c-1e34-4b7e-9a9f-cf8a7ef77d1e','018b2f19-e79e-7d6a-a56d-29feb6211b04');
INSERT INTO user_roles (user_id, role_id) VALUES ('0877ee77-5f40-49c1-8e2e-f3a88d2f4f48','018b2f19-e79e-7d6a-a56d-29feb6211b04');

-- sample customer (domain table, assuming customers table exists)
INSERT INTO customers (id, name, surname, tckn) VALUES ('a3b0c02c-1e34-4b7e-9a9f-cf8a7ef77d1e','Aziz','Yilmaz','12345678901');
INSERT INTO customers (id, name, surname, tckn) VALUES ('0877ee77-5f40-49c1-8e2e-f3a88d2f4f48','Elif','Yilmaz','98765432109');

-- sample wallet
INSERT INTO wallets (id, customer_id, wallet_name, currency, balance, usable_balance, active_for_shopping, active_for_withdraw)
VALUES ('123e4567-e89b-42d3-a456-556642440000','a3b0c02c-1e34-4b7e-9a9f-cf8a7ef77d1e','MyWallet','TRY',0,0,true,true);

-- no transactions initially

INSERT INTO USERS (VERSION, USER_NAME, EMAIL, PASSWORD, ROLE, CREATE_DATE, ACTIVE) VALUES (1, 'admin', 'admin@gmail.com', 'admin', 'ADMIN', CURRENT_TIMESTAMP(), TRUE);

INSERT INTO PRODUCTS (VERSION, ITEM_NUMBER, PRODUCT_NAME, DESCRIPTION, STOCK, AVAILABLE, PRICE, DISCOUNT, CREATE_DATE, ACTIVE) VALUES (1, 111111, 'product', 'description', 10, TRUE, 100, 0, CURRENT_TIMESTAMP(), TRUE);

INSERT INTO TAGS (TAG_NAME) VALUES ('tag');
AUTHOR: EMRE TURAN
to use the application, 
make sure mysql database has needed tables that can be created with below queries:

CREATE TABLE cartinfo (
  user_id int DEFAULT NULL,
  product_id int DEFAULT NULL,
  quantity float4 DEFAULT NULL
)


CREATE TABLE orderinfo (
  orderId int NOT NULL AUTO_INCREMENT,
  userID int DEFAULT NULL,
  orderTime timestamp NULL DEFAULT NULL,
  deliveryTime timestamp NULL DEFAULT NULL,
  products varchar(255) DEFAULT NULL,
  carrier varchar(255) DEFAULT NULL,
  isdelivered tinyint(1) DEFAULT NULL,
  totalcost double DEFAULT NULL,
  PRIMARY KEY (orderId)
)


CREATE TABLE productinfo (
  id int NOT NULL AUTO_INCREMENT,
  name varchar(255) DEFAULT NULL,
  type varchar(255) DEFAULT NULL,
  price double DEFAULT NULL,
  stock float4 DEFAULT NULL,
  imagelocation varchar(255) DEFAULT NULL,
  threshold int DEFAULT NULL,
  sellerID int NOT NULL,
  PRIMARY KEY (id)
)


CREATE TABLE userinfo (
  id int NOT NULL AUTO_INCREMENT,
  username varchar(255) DEFAULT NULL,
  password varchar(255) DEFAULT NULL,
  role varchar(50) DEFAULT NULL,
  adress varchar(255) DEFAULT NULL,
  PRIMARY KEY (id)
)

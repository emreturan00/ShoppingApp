AUTHOR: EMRE TURAN

![image7](https://github.com/emreturan00/ShoppingApp/assets/93795815/19e7de99-563b-426d-999f-cc213ebbafac)

![image8](https://github.com/emreturan00/ShoppingApp/assets/93795815/8087c8a5-f65d-43ad-8bd7-7a86251259ca)

![image10](https://github.com/emreturan00/ShoppingApp/assets/93795815/725ff365-f0e6-4677-93ed-859be3dd019a)

![image2](https://github.com/emreturan00/ShoppingApp/assets/93795815/5223aea0-3430-41fb-8440-dd2642a13581)

![image12](https://github.com/emreturan00/ShoppingApp/assets/93795815/f8a3532a-76c7-477a-b043-c798e5b2db46)

----------------------------------------------------------------------------------------
to use the application, 
make sure mysql database has needed tables that can be created with below queries:

CREATE TABLE `cartinfo` (
  `user_id` int DEFAULT NULL,
  `product_id` int DEFAULT NULL,
  `quantity` float DEFAULT NULL
)


CREATE TABLE `orderinfo` (
  `orderId` int NOT NULL AUTO_INCREMENT,
  `userID` int DEFAULT NULL,
  `orderTime` timestamp NULL DEFAULT NULL,
  `deliveryTime` timestamp NULL DEFAULT NULL,
  `products` varchar(255) DEFAULT NULL,
  `carrier` varchar(255) DEFAULT NULL,
  `isdelivered` tinyint(1) DEFAULT NULL,
  `totalcost` double DEFAULT NULL,
  `isSelected` tinyint(1) DEFAULT '0',
  PRIMARY KEY (`orderId`)
)


CREATE TABLE `productinfo` (
  `id` int NOT NULL AUTO_INCREMENT,
  `name` varchar(255) DEFAULT NULL,
  `type` varchar(255) DEFAULT NULL,
  `price` double DEFAULT NULL,
  `stock` float DEFAULT NULL,
  `imagelocation` varchar(255) DEFAULT NULL,
  `threshold` int DEFAULT NULL,
  `doubled_price` tinyint(1) DEFAULT '0',
  PRIMARY KEY (`id`)
)


CREATE TABLE `userinfo` (
  `id` int NOT NULL AUTO_INCREMENT,
  `username` varchar(255) DEFAULT NULL,
  `password` varchar(255) DEFAULT NULL,
  `role` varchar(50) DEFAULT NULL,
  `adress` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
)

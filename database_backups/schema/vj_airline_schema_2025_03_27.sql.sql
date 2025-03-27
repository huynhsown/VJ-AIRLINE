-- Table: addons
CREATE TABLE `addons` (
  `is_active` bit(1) NOT NULL,
  `price` decimal(10,2) NOT NULL,
  `created_date` datetime(6) DEFAULT NULL,
  `id` bigint NOT NULL AUTO_INCREMENT,
  `modified_date` datetime(6) DEFAULT NULL,
  `addon_code` varchar(20) NOT NULL,
  `name` varchar(50) NOT NULL,
  `created_by` varchar(255) DEFAULT NULL,
  `description` text,
  `img_url` varchar(255) DEFAULT NULL,
  `modified_by` varchar(255) DEFAULT NULL,
  `addon_type` enum('INSURANCE','LUGGAGE','MEAL','OTHER','SEAT_SELECTION') DEFAULT NULL,
  `currency` enum('EUR','GBP','JPY','USD','VND') NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UKt96aua4h8n8dy2dcqdee1t7hp` (`addon_code`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- Table: aircraft
CREATE TABLE `aircraft` (
  `airline_id` bigint NOT NULL,
  `created_date` datetime(6) DEFAULT NULL,
  `id` bigint NOT NULL AUTO_INCREMENT,
  `manufacture_date` datetime(6) DEFAULT NULL,
  `model_id` bigint NOT NULL,
  `modified_date` datetime(6) DEFAULT NULL,
  `created_by` varchar(255) DEFAULT NULL,
  `modified_by` varchar(255) DEFAULT NULL,
  `registration_number` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FKmlcvw3yhx7oeustg3af2war8h` (`model_id`),
  KEY `FKnrxt7i0op3yc4x2pk51q3w6uc` (`airline_id`),
  CONSTRAINT `FKmlcvw3yhx7oeustg3af2war8h` FOREIGN KEY (`model_id`) REFERENCES `aircraft_model` (`id`),
  CONSTRAINT `FKnrxt7i0op3yc4x2pk51q3w6uc` FOREIGN KEY (`airline_id`) REFERENCES `airlines` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=11 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- Table: aircraft_model
CREATE TABLE `aircraft_model` (
  `business_capacity` int NOT NULL,
  `capacity` int NOT NULL,
  `premium_capacity` int NOT NULL,
  `standard_capacity` int NOT NULL,
  `created_date` datetime(6) DEFAULT NULL,
  `id` bigint NOT NULL AUTO_INCREMENT,
  `modified_date` datetime(6) DEFAULT NULL,
  `created_by` varchar(255) DEFAULT NULL,
  `model_code` varchar(255) DEFAULT NULL,
  `model_name` varchar(255) DEFAULT NULL,
  `modified_by` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=10 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- Table: airlines
CREATE TABLE `airlines` (
  `created_date` datetime(6) DEFAULT NULL,
  `id` bigint NOT NULL AUTO_INCREMENT,
  `modified_date` datetime(6) DEFAULT NULL,
  `airline_code` varchar(255) DEFAULT NULL,
  `airline_name` varchar(255) DEFAULT NULL,
  `created_by` varchar(255) DEFAULT NULL,
  `modified_by` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- Table: airports
CREATE TABLE `airports` (
  `created_date` datetime(6) DEFAULT NULL,
  `id` bigint NOT NULL AUTO_INCREMENT,
  `modified_date` datetime(6) DEFAULT NULL,
  `province_id` bigint NOT NULL,
  `airport_code` varchar(255) DEFAULT NULL,
  `airport_eng_name` varchar(255) DEFAULT NULL,
  `airport_name` varchar(255) DEFAULT NULL,
  `created_by` varchar(255) DEFAULT NULL,
  `modified_by` varchar(255) DEFAULT NULL,
  `time_zone` varchar(255) DEFAULT NULL,
  `utc_offset` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FKcya7npdtavcm040std6bo4qri` (`province_id`),
  CONSTRAINT `FKcya7npdtavcm040std6bo4qri` FOREIGN KEY (`province_id`) REFERENCES `provinces` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=20 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- Table: booking_addons
CREATE TABLE `booking_addons` (
  `price` decimal(10,2) NOT NULL,
  `quantity` int NOT NULL,
  `addon_id` bigint NOT NULL,
  `booking_detail_id` bigint NOT NULL,
  `created_date` datetime(6) DEFAULT NULL,
  `id` bigint NOT NULL AUTO_INCREMENT,
  `modified_date` datetime(6) DEFAULT NULL,
  `created_by` varchar(255) DEFAULT NULL,
  `modified_by` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FKi0lvba9hj6okmhniahrcc6snp` (`addon_id`),
  KEY `FKd326po3r707wi6oc42tub9yyg` (`booking_detail_id`),
  CONSTRAINT `FKd326po3r707wi6oc42tub9yyg` FOREIGN KEY (`booking_detail_id`) REFERENCES `booking_details` (`id`),
  CONSTRAINT `FKi0lvba9hj6okmhniahrcc6snp` FOREIGN KEY (`addon_id`) REFERENCES `addons` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- Table: booking_details
CREATE TABLE `booking_details` (
  `checked_in` bit(1) NOT NULL,
  `discount_amount` decimal(10,2) NOT NULL,
  `fare_amount` decimal(10,2) NOT NULL,
  `fee_amount` decimal(10,2) NOT NULL,
  `tax_amount` decimal(10,2) NOT NULL,
  `total_amount` decimal(10,2) NOT NULL,
  `booking_id` bigint NOT NULL,
  `checkin_time` datetime(6) DEFAULT NULL,
  `created_date` datetime(6) DEFAULT NULL,
  `fare_class_id` bigint NOT NULL,
  `flight_id` bigint NOT NULL,
  `id` bigint NOT NULL AUTO_INCREMENT,
  `modified_date` datetime(6) DEFAULT NULL,
  `passenger_id` bigint NOT NULL,
  `seat_reservation_id` bigint DEFAULT NULL,
  `seat_number` varchar(10) DEFAULT NULL,
  `created_by` varchar(255) DEFAULT NULL,
  `modified_by` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UKjecweuaehjh9gk4vvxmbltnuw` (`seat_reservation_id`),
  KEY `FKkbcan6ybv86uappnh0qtdmvas` (`booking_id`),
  KEY `FKp1f6ud2q5f46qr2hfbrsa9pcw` (`fare_class_id`),
  KEY `FK3flvem7vso3upva7hugvxvusi` (`flight_id`),
  KEY `FK3co4s1k2qdqlpjenk6pl0v1ow` (`passenger_id`),
  CONSTRAINT `FK3co4s1k2qdqlpjenk6pl0v1ow` FOREIGN KEY (`passenger_id`) REFERENCES `passengers` (`id`),
  CONSTRAINT `FK3flvem7vso3upva7hugvxvusi` FOREIGN KEY (`flight_id`) REFERENCES `flights` (`id`),
  CONSTRAINT `FKkbcan6ybv86uappnh0qtdmvas` FOREIGN KEY (`booking_id`) REFERENCES `bookings` (`id`),
  CONSTRAINT `FKp1f6ud2q5f46qr2hfbrsa9pcw` FOREIGN KEY (`fare_class_id`) REFERENCES `fare_classes` (`id`),
  CONSTRAINT `FKri11v307sd1ik8theds9n5xy5` FOREIGN KEY (`seat_reservation_id`) REFERENCES `seat_reservations` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- Table: booking_payments
CREATE TABLE `booking_payments` (
  `payment_amount` decimal(10,2) NOT NULL,
  `booking_id` bigint NOT NULL,
  `created_date` datetime(6) DEFAULT NULL,
  `id` bigint NOT NULL AUTO_INCREMENT,
  `modified_date` datetime(6) DEFAULT NULL,
  `payment_date` datetime(6) NOT NULL,
  `created_by` varchar(255) DEFAULT NULL,
  `modified_by` varchar(255) DEFAULT NULL,
  `transaction_id` varchar(255) DEFAULT NULL,
  `payment_method` enum('BANK_TRANSFER','CREDIT_CARD','CRYPTO','DEBIT_CARD','PAYPAL') NOT NULL,
  `payment_status` enum('COMPLETED','FAILED','PENDING','REFUNDED') NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UKf2d2q63qibhqliew52q6ag8jy` (`transaction_id`),
  KEY `FKfhwt29c9x2ibo2o6r2ieq84ej` (`booking_id`),
  CONSTRAINT `FKfhwt29c9x2ibo2o6r2ieq84ej` FOREIGN KEY (`booking_id`) REFERENCES `bookings` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- Table: booking_status
CREATE TABLE `booking_status` (
  `created_date` datetime(6) DEFAULT NULL,
  `id` bigint NOT NULL AUTO_INCREMENT,
  `modified_date` datetime(6) DEFAULT NULL,
  `status_name` varchar(50) NOT NULL,
  `created_by` varchar(255) DEFAULT NULL,
  `description` text,
  `modified_by` varchar(255) DEFAULT NULL,
  `status_code` enum('CANCELLED','COMPLETED','CONFIRMED','EXPIRED','PENDING') NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UK887g1av5u7u6ffxtsj6lleo8j` (`status_code`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- Table: bookings
CREATE TABLE `bookings` (
  `adult_count` int NOT NULL,
  `child_count` int NOT NULL,
  `discount_amount` decimal(10,2) DEFAULT NULL,
  `infant_count` int NOT NULL,
  `total_amount` decimal(10,2) NOT NULL,
  `booking_date` datetime(6) NOT NULL,
  `created_date` datetime(6) DEFAULT NULL,
  `id` bigint NOT NULL AUTO_INCREMENT,
  `modified_date` datetime(6) DEFAULT NULL,
  `status_id` bigint DEFAULT NULL,
  `user_id` bigint DEFAULT NULL,
  `currency` varchar(10) NOT NULL,
  `booking_reference` varchar(20) NOT NULL,
  `promo_code` varchar(20) DEFAULT NULL,
  `trip_type` varchar(20) DEFAULT NULL,
  `payment_method` varchar(50) DEFAULT NULL,
  `payment_reference` varchar(50) DEFAULT NULL,
  `created_by` varchar(255) DEFAULT NULL,
  `modified_by` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UKe92mgyq35mdeo8gc1un2o6uk0` (`booking_reference`),
  KEY `FKr1l5ilf74enja6uvfsv1r7sd7` (`status_id`),
  KEY `FKeyog2oic85xg7hsu2je2lx3s6` (`user_id`),
  CONSTRAINT `FKeyog2oic85xg7hsu2je2lx3s6` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`),
  CONSTRAINT `FKr1l5ilf74enja6uvfsv1r7sd7` FOREIGN KEY (`status_id`) REFERENCES `booking_status` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- Table: countries
CREATE TABLE `countries` (
  `created_date` datetime(6) DEFAULT NULL,
  `id` bigint NOT NULL AUTO_INCREMENT,
  `modified_date` datetime(6) DEFAULT NULL,
  `area_code` varchar(255) NOT NULL,
  `country_code` varchar(255) NOT NULL,
  `country_eng_name` varchar(255) DEFAULT NULL,
  `country_name` varchar(255) NOT NULL,
  `created_by` varchar(255) DEFAULT NULL,
  `modified_by` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UK1wtxwyxkh99xamhuwkebvvip7` (`area_code`),
  UNIQUE KEY `UKc9ccge90oirf3ivfxcd2xnmnw` (`country_code`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- Table: fare_classes
CREATE TABLE `fare_classes` (
  `baggage_allowance` int DEFAULT NULL,
  `change_allowed` tinyint(1) DEFAULT '0',
  `change_fee` float DEFAULT NULL,
  `is_active` tinyint(1) DEFAULT '1',
  `meal_included` tinyint(1) DEFAULT '0',
  `refund_allowed` tinyint(1) DEFAULT '0',
  `refund_fee` float DEFAULT NULL,
  `seat_selection` tinyint(1) DEFAULT '0',
  `airline_id` bigint DEFAULT NULL,
  `created_date` datetime(6) DEFAULT NULL,
  `id` bigint NOT NULL AUTO_INCREMENT,
  `modified_date` datetime(6) DEFAULT NULL,
  `code` varchar(255) NOT NULL,
  `created_by` varchar(255) DEFAULT NULL,
  `description` varchar(255) DEFAULT NULL,
  `modified_by` varchar(255) DEFAULT NULL,
  `name` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UKim8k95swt779qelr8n9r3f2np` (`code`),
  KEY `FKnpff0adsmb9otebycf8gspsor` (`airline_id`),
  CONSTRAINT `FKnpff0adsmb9otebycf8gspsor` FOREIGN KEY (`airline_id`) REFERENCES `airlines` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- Table: flight_fare_availability
CREATE TABLE `flight_fare_availability` (
  `available_seats` int DEFAULT NULL,
  `base_price` decimal(38,2) DEFAULT NULL,
  `created_date` datetime(6) DEFAULT NULL,
  `fare_class_id` bigint NOT NULL,
  `flight_id` bigint NOT NULL,
  `id` bigint NOT NULL AUTO_INCREMENT,
  `modified_date` datetime(6) DEFAULT NULL,
  `created_by` varchar(255) DEFAULT NULL,
  `modified_by` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FK70epse5l1cuc513lh1lon6so3` (`fare_class_id`),
  KEY `FKbcqkm9jlbkxwvr1c96cctqrx7` (`flight_id`),
  CONSTRAINT `FK70epse5l1cuc513lh1lon6so3` FOREIGN KEY (`fare_class_id`) REFERENCES `fare_classes` (`id`),
  CONSTRAINT `FKbcqkm9jlbkxwvr1c96cctqrx7` FOREIGN KEY (`flight_id`) REFERENCES `flights` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- Table: flight_status
CREATE TABLE `flight_status` (
  `created_date` datetime(6) DEFAULT NULL,
  `id` bigint NOT NULL AUTO_INCREMENT,
  `modified_date` datetime(6) DEFAULT NULL,
  `created_by` varchar(255) DEFAULT NULL,
  `modified_by` varchar(255) DEFAULT NULL,
  `status_code` varchar(255) DEFAULT NULL,
  `status_name` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- Table: flights
CREATE TABLE `flights` (
  `aircraft_id` bigint DEFAULT NULL,
  `airline_id` bigint DEFAULT NULL,
  `created_date` datetime(6) DEFAULT NULL,
  `id` bigint NOT NULL AUTO_INCREMENT,
  `modified_date` datetime(6) DEFAULT NULL,
  `route_id` bigint DEFAULT NULL,
  `scheduled_arrival` datetime(6) DEFAULT NULL,
  `scheduled_departure` datetime(6) DEFAULT NULL,
  `status_id` bigint DEFAULT NULL,
  `created_by` varchar(255) DEFAULT NULL,
  `flight_number` varchar(255) NOT NULL,
  `gate` varchar(255) DEFAULT NULL,
  `modified_by` varchar(255) DEFAULT NULL,
  `terminal` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UK6bx3i9v6ikjiy0ru5ybor8t7` (`flight_number`),
  KEY `FKghh4qrek715keuakfgy6sycl3` (`aircraft_id`),
  KEY `FKieor4j3ivp3xu584qenhfh0gd` (`airline_id`),
  KEY `FKidii0fa16kousyppakiwjlmsf` (`status_id`),
  KEY `FKggm6k4h1glfes1nsg0wesanvy` (`route_id`),
  CONSTRAINT `FKggm6k4h1glfes1nsg0wesanvy` FOREIGN KEY (`route_id`) REFERENCES `routes` (`id`),
  CONSTRAINT `FKghh4qrek715keuakfgy6sycl3` FOREIGN KEY (`aircraft_id`) REFERENCES `aircraft` (`id`),
  CONSTRAINT `FKidii0fa16kousyppakiwjlmsf` FOREIGN KEY (`status_id`) REFERENCES `flight_status` (`id`),
  CONSTRAINT `FKieor4j3ivp3xu584qenhfh0gd` FOREIGN KEY (`airline_id`) REFERENCES `airlines` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- Table: passengers
CREATE TABLE `passengers` (
  `date_of_birth` date DEFAULT NULL,
  `created_date` datetime(6) DEFAULT NULL,
  `id` bigint NOT NULL AUTO_INCREMENT,
  `modified_date` datetime(6) DEFAULT NULL,
  `phone` varchar(20) DEFAULT NULL,
  `first_name` varchar(50) NOT NULL,
  `id_number` varchar(50) DEFAULT NULL,
  `last_name` varchar(50) NOT NULL,
  `nationality` varchar(50) DEFAULT NULL,
  `email` varchar(100) DEFAULT NULL,
  `created_by` varchar(255) DEFAULT NULL,
  `modified_by` varchar(255) DEFAULT NULL,
  `gender` enum('FEMALE','MALE','OTHER') DEFAULT NULL,
  `id_type` enum('DRIVER_LICENSE','NATIONAL_ID','OTHER','PASSPORT') DEFAULT NULL,
  `passenger_type` enum('ADULT','CHILD','INFANT') NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- Table: promo_codes
CREATE TABLE `promo_codes` (
  `amount` decimal(10,2) NOT NULL,
  `current_usage` int NOT NULL,
  `is_active` bit(1) NOT NULL,
  `is_percentage` bit(1) NOT NULL,
  `max_usage` int DEFAULT NULL,
  `min_booking_amount` decimal(10,2) DEFAULT NULL,
  `valid_from` date DEFAULT NULL,
  `valid_to` date DEFAULT NULL,
  `created_date` datetime(6) DEFAULT NULL,
  `id` bigint NOT NULL AUTO_INCREMENT,
  `modified_date` datetime(6) DEFAULT NULL,
  `code` varchar(20) NOT NULL,
  `applicable_airlines` varchar(255) DEFAULT NULL,
  `created_by` varchar(255) DEFAULT NULL,
  `description` text,
  `modified_by` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UKj9mo0xgfs34t6e3c17anidd83` (`code`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- Table: provinces
CREATE TABLE `provinces` (
  `country_id` bigint NOT NULL,
  `created_date` datetime(6) DEFAULT NULL,
  `id` bigint NOT NULL AUTO_INCREMENT,
  `modified_date` datetime(6) DEFAULT NULL,
  `created_by` varchar(255) DEFAULT NULL,
  `modified_by` varchar(255) DEFAULT NULL,
  `province_code` varchar(255) DEFAULT NULL,
  `province_eng_name` varchar(255) DEFAULT NULL,
  `province_name` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FK48p9qkti5auert2gquvn76338` (`country_id`),
  CONSTRAINT `FK48p9qkti5auert2gquvn76338` FOREIGN KEY (`country_id`) REFERENCES `countries` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=20 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- Table: roles
CREATE TABLE `roles` (
  `created_date` datetime(6) DEFAULT NULL,
  `id` bigint NOT NULL AUTO_INCREMENT,
  `modified_date` datetime(6) DEFAULT NULL,
  `created_by` varchar(255) DEFAULT NULL,
  `description` text,
  `modified_by` varchar(255) DEFAULT NULL,
  `role_code` varchar(255) NOT NULL,
  `role_name` varchar(255) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UK949pwsnk7kxk0px0tbj3r3web` (`role_code`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- Table: routes
CREATE TABLE `routes` (
  `distance` int DEFAULT NULL,
  `estimated_duration` int DEFAULT NULL,
  `is_active` tinyint(1) DEFAULT '1',
  `airline_id` bigint NOT NULL,
  `created_date` datetime(6) DEFAULT NULL,
  `destination_airport` bigint NOT NULL,
  `id` bigint NOT NULL AUTO_INCREMENT,
  `modified_date` datetime(6) DEFAULT NULL,
  `origin_airport` bigint NOT NULL,
  `created_by` varchar(255) DEFAULT NULL,
  `modified_by` varchar(255) DEFAULT NULL,
  `route_code` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UK4idvimvwm5v8g5l492qxccy1j` (`origin_airport`,`destination_airport`),
  KEY `FKqye74xa0oy7y71wlkramdmjmh` (`airline_id`),
  KEY `FKoiqvqbs9bl1o5p0ioggyuilri` (`destination_airport`),
  CONSTRAINT `FKj1yq5roplnc7lsxlxvkmuttxn` FOREIGN KEY (`origin_airport`) REFERENCES `airports` (`id`),
  CONSTRAINT `FKoiqvqbs9bl1o5p0ioggyuilri` FOREIGN KEY (`destination_airport`) REFERENCES `airports` (`id`),
  CONSTRAINT `FKqye74xa0oy7y71wlkramdmjmh` FOREIGN KEY (`airline_id`) REFERENCES `airlines` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=21 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- Table: seat_reservations
CREATE TABLE `seat_reservations` (
  `created_date` datetime(6) DEFAULT NULL,
  `fare_class_id` bigint DEFAULT NULL,
  `flight_id` bigint DEFAULT NULL,
  `id` bigint NOT NULL AUTO_INCREMENT,
  `modified_date` datetime(6) DEFAULT NULL,
  `created_by` varchar(255) DEFAULT NULL,
  `modified_by` varchar(255) DEFAULT NULL,
  `seat_number` varchar(255) DEFAULT NULL,
  `seat_status` enum('AVAILABLE','BLOCKED','OCCUPIED','RESERVED') DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FK2fu9w80nugu8a7o08yeb6hncj` (`fare_class_id`),
  KEY `FKt7pv2y6d2b9vareyf75pu46rc` (`flight_id`),
  CONSTRAINT `FK2fu9w80nugu8a7o08yeb6hncj` FOREIGN KEY (`fare_class_id`) REFERENCES `fare_classes` (`id`),
  CONSTRAINT `FKt7pv2y6d2b9vareyf75pu46rc` FOREIGN KEY (`flight_id`) REFERENCES `flights` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- Table: users
CREATE TABLE `users` (
  `date_of_birth` date DEFAULT NULL,
  `email_verified` tinyint(1) NOT NULL DEFAULT '0',
  `is_active` tinyint(1) NOT NULL DEFAULT '0',
  `created_date` datetime(6) DEFAULT NULL,
  `id` bigint NOT NULL AUTO_INCREMENT,
  `modified_date` datetime(6) DEFAULT NULL,
  `role_id` bigint NOT NULL,
  `phone` varchar(20) DEFAULT NULL,
  `first_name` varchar(50) NOT NULL,
  `last_name` varchar(50) NOT NULL,
  `email` varchar(100) NOT NULL,
  `address` varchar(255) DEFAULT NULL,
  `created_by` varchar(255) DEFAULT NULL,
  `modified_by` varchar(255) DEFAULT NULL,
  `password_hash` varchar(255) NOT NULL,
  `username` varchar(255) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UK6dotkott2kjsp8vw4d0m25fb7` (`email`),
  UNIQUE KEY `UKr43af9ap4edm43mmtq01oddj6` (`username`),
  KEY `FKp56c1712k691lhsyewcssf40f` (`role_id`),
  CONSTRAINT `FKp56c1712k691lhsyewcssf40f` FOREIGN KEY (`role_id`) REFERENCES `roles` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

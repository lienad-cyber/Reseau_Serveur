CREATE DATABASE IF NOT EXISTS `e-commerce` CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE `e-commerce`;

CREATE TABLE IF NOT EXISTS `produits` (
    `id` INT(11) NOT NULL AUTO_INCREMENT,
    `categorie_id` INT(11) DEFAULT NULL,
    `nom` VARCHAR(100) NOT NULL,
    `description` TEXT DEFAULT NULL,
    `prix` DECIMAL(10,2) NOT NULL,
    `url_photo` VARCHAR(255) DEFAULT NULL,
    PRIMARY KEY (`id`),
    KEY `categorie_id` (`categorie_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE IF NOT EXISTS `categories` (
    `id` INT(11) NOT NULL AUTO_INCREMENT,
    `nom` VARCHAR(100) NOT NULL,
    PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

INSERT INTO `categories` (`nom`) VALUES
('Hommes'),
('Femmes'),
('Enfants'),

CREATE TABLE admin (
    id INT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(255) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL
);

INSERT INTO admin (username, password) 
VALUES ('admin', 'admin123');

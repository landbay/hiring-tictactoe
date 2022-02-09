DROP TABLE IF EXISTS `game`;
CREATE TABLE `game` (
                         `id` VARCHAR(255) NOT NULL,
                         `board` VARCHAR(255) NULL DEFAULT '000000000',
                         `next_turn` VARCHAR(255) NULL DEFAULT NULL,
                         `game_outcome` VARCHAR(255) NULL DEFAULT NULL,
                         `game_status` VARCHAR(255) NULL DEFAULT 'STARTED',
                         PRIMARY KEY (`id`)
);
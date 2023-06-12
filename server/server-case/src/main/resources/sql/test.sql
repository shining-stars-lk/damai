CREATE TABLE `test` (
    `id` bigint(11) NOT NULL,
    `column1` int DEFAULT NULL,
    `column2` int DEFAULT NULL,
    `column3` int DEFAULT NULL,
    `column4` varchar(255) DEFAULT NULL,
    `column5` varchar(255) DEFAULT NULL,
    `column6` varchar(255) DEFAULT NULL,
    `number` int DEFAULT NULL,
    PRIMARY KEY (`id`),
    KEY `idx_column1_2_3` (`column1`,`column2`,`column3`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
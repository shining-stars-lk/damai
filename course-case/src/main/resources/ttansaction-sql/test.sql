-- tes.test definition

CREATE TABLE `test` (
    `id` bigint(11) NOT NULL,
    `column_1` varchar(255) DEFAULT NULL,
    `column_2` varchar(255) DEFAULT NULL,
    `column_3` varchar(255) DEFAULT NULL,
    `column_4` varchar(255) DEFAULT NULL,
    `column_5` varchar(255) DEFAULT NULL,
    `column_6` varchar(255) DEFAULT NULL,
    `number` bigint(20) DEFAULT NULL,
    PRIMARY KEY (`id`),
    KEY `test_column_1_IDX` (`column_1`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
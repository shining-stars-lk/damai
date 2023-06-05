-- tes.test definition

CREATE TABLE `test` (
    `id` bigint(11) NOT NULL,
    `column1` varchar(255) DEFAULT NULL,
    `column2` varchar(255) DEFAULT NULL,
    `column3` varchar(255) DEFAULT NULL,
    `column4` varchar(255) DEFAULT NULL,
    `column5` varchar(255) DEFAULT NULL,
    `column6` varchar(255) DEFAULT NULL,
    `number` bigint(20) DEFAULT NULL,
    PRIMARY KEY (`id`),
    KEY `test_column_1_IDX` (`column1`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
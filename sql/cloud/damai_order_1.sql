USE damai_order_1;

DROP TABLE IF EXISTS `d_order_0`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `d_order_0` (
  `id` bigint(20) NOT NULL COMMENT '主键id',
  `order_number` bigint(20) NOT NULL COMMENT '订单编号',
  `program_id` bigint(20) NOT NULL COMMENT '节目表id',
  `program_item_picture` varchar(1024) DEFAULT NULL COMMENT '节目图片介绍',
  `user_id` bigint(20) NOT NULL COMMENT '用户id',
  `program_title` varchar(512) DEFAULT NULL COMMENT '节目标题',
  `program_place` varchar(100) DEFAULT NULL COMMENT '节目地点',
  `program_show_time` datetime DEFAULT NULL COMMENT '节目演出时间',
  `program_permit_choose_seat` tinyint(1) NOT NULL COMMENT '节目是否允许选座 1:允许选座 0:不允许选座',
  `distribution_mode` varchar(256) DEFAULT NULL COMMENT '配送方式',
  `take_ticket_mode` varchar(256) DEFAULT NULL COMMENT '取票方式',
  `order_price` decimal(10,0) DEFAULT NULL COMMENT '订单价格',
  `pay_order_type` int(3) DEFAULT NULL COMMENT '支付订单方式',
  `order_status` int(3) DEFAULT '1' COMMENT '订单状态 1:未支付 2:已取消 3:已支付 4:已退单',
  `create_order_time` datetime DEFAULT NULL COMMENT '生成订单时间',
  `cancel_order_time` datetime DEFAULT NULL COMMENT '取消订单时间',
  `pay_order_time` datetime DEFAULT NULL COMMENT '支付订单时间',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `edit_time` datetime DEFAULT NULL COMMENT '编辑时间',
  `status` tinyint(1) DEFAULT '1' COMMENT '1:正常 0:删除',
  PRIMARY KEY (`id`),
  UNIQUE KEY `d_order_order_number_IDX` (`order_number`) USING BTREE,
  KEY `user_id_IDX` (`user_id`) USING BTREE,
  KEY `program_id_IDX` (`program_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='订单表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `d_order_0`
--

LOCK TABLES `d_order_0` WRITE;
/*!40000 ALTER TABLE `d_order_0` DISABLE KEYS */;
/*!40000 ALTER TABLE `d_order_0` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `d_order_1`
--

DROP TABLE IF EXISTS `d_order_1`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `d_order_1` (
  `id` bigint(20) NOT NULL COMMENT '主键id',
  `order_number` bigint(20) NOT NULL COMMENT '订单编号',
  `program_id` bigint(20) NOT NULL COMMENT '节目表id',
  `program_item_picture` varchar(1024) DEFAULT NULL COMMENT '节目图片介绍',
  `user_id` bigint(20) NOT NULL COMMENT '用户id',
  `program_title` varchar(512) DEFAULT NULL COMMENT '节目标题',
  `program_place` varchar(100) DEFAULT NULL COMMENT '节目地点',
  `program_show_time` datetime DEFAULT NULL COMMENT '节目演出时间',
  `program_permit_choose_seat` tinyint(1) NOT NULL COMMENT '节目是否允许选座 1:允许选座 0:不允许选座',
  `distribution_mode` varchar(256) DEFAULT NULL COMMENT '配送方式',
  `take_ticket_mode` varchar(256) DEFAULT NULL COMMENT '取票方式',
  `order_price` decimal(10,0) DEFAULT NULL COMMENT '订单价格',
  `pay_order_type` int(3) DEFAULT NULL COMMENT '支付订单方式',
  `order_status` int(3) DEFAULT '1' COMMENT '订单状态 1:未支付 2:已取消 3:已支付 4:已退单',
  `create_order_time` datetime DEFAULT NULL COMMENT '生成订单时间',
  `cancel_order_time` datetime DEFAULT NULL COMMENT '取消订单时间',
  `pay_order_time` datetime DEFAULT NULL COMMENT '支付订单时间',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `edit_time` datetime DEFAULT NULL COMMENT '编辑时间',
  `status` tinyint(1) DEFAULT '1' COMMENT '1:正常 0:删除',
  PRIMARY KEY (`id`),
  UNIQUE KEY `d_order_order_number_IDX` (`order_number`) USING BTREE,
  KEY `user_id_IDX` (`user_id`) USING BTREE,
  KEY `program_id_IDX` (`program_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='订单表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `d_order_1`
--

LOCK TABLES `d_order_1` WRITE;
/*!40000 ALTER TABLE `d_order_1` DISABLE KEYS */;
/*!40000 ALTER TABLE `d_order_1` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `d_order_2`
--

DROP TABLE IF EXISTS `d_order_2`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `d_order_2` (
  `id` bigint(20) NOT NULL COMMENT '主键id',
  `order_number` bigint(20) NOT NULL COMMENT '订单编号',
  `program_id` bigint(20) NOT NULL COMMENT '节目表id',
  `program_item_picture` varchar(1024) DEFAULT NULL COMMENT '节目图片介绍',
  `user_id` bigint(20) NOT NULL COMMENT '用户id',
  `program_title` varchar(512) DEFAULT NULL COMMENT '节目标题',
  `program_place` varchar(100) DEFAULT NULL COMMENT '节目地点',
  `program_show_time` datetime DEFAULT NULL COMMENT '节目演出时间',
  `program_permit_choose_seat` tinyint(1) NOT NULL COMMENT '节目是否允许选座 1:允许选座 0:不允许选座',
  `distribution_mode` varchar(256) DEFAULT NULL COMMENT '配送方式',
  `take_ticket_mode` varchar(256) DEFAULT NULL COMMENT '取票方式',
  `order_price` decimal(10,0) DEFAULT NULL COMMENT '订单价格',
  `pay_order_type` int(3) DEFAULT NULL COMMENT '支付订单方式',
  `order_status` int(3) DEFAULT '1' COMMENT '订单状态 1:未支付 2:已取消 3:已支付 4:已退单',
  `create_order_time` datetime DEFAULT NULL COMMENT '生成订单时间',
  `cancel_order_time` datetime DEFAULT NULL COMMENT '取消订单时间',
  `pay_order_time` datetime DEFAULT NULL COMMENT '支付订单时间',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `edit_time` datetime DEFAULT NULL COMMENT '编辑时间',
  `status` tinyint(1) DEFAULT '1' COMMENT '1:正常 0:删除',
  PRIMARY KEY (`id`),
  UNIQUE KEY `d_order_order_number_IDX` (`order_number`) USING BTREE,
  KEY `user_id_IDX` (`user_id`) USING BTREE,
  KEY `program_id_IDX` (`program_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='订单表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `d_order_2`
--

LOCK TABLES `d_order_2` WRITE;
/*!40000 ALTER TABLE `d_order_2` DISABLE KEYS */;
/*!40000 ALTER TABLE `d_order_2` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `d_order_3`
--

DROP TABLE IF EXISTS `d_order_3`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `d_order_3` (
  `id` bigint(20) NOT NULL COMMENT '主键id',
  `order_number` bigint(20) NOT NULL COMMENT '订单编号',
  `program_id` bigint(20) NOT NULL COMMENT '节目表id',
  `program_item_picture` varchar(1024) DEFAULT NULL COMMENT '节目图片介绍',
  `user_id` bigint(20) NOT NULL COMMENT '用户id',
  `program_title` varchar(512) DEFAULT NULL COMMENT '节目标题',
  `program_place` varchar(100) DEFAULT NULL COMMENT '节目地点',
  `program_show_time` datetime DEFAULT NULL COMMENT '节目演出时间',
  `program_permit_choose_seat` tinyint(1) NOT NULL COMMENT '节目是否允许选座 1:允许选座 0:不允许选座',
  `distribution_mode` varchar(256) DEFAULT NULL COMMENT '配送方式',
  `take_ticket_mode` varchar(256) DEFAULT NULL COMMENT '取票方式',
  `order_price` decimal(10,0) DEFAULT NULL COMMENT '订单价格',
  `pay_order_type` int(3) DEFAULT NULL COMMENT '支付订单方式',
  `order_status` int(3) DEFAULT '1' COMMENT '订单状态 1:未支付 2:已取消 3:已支付 4:已退单',
  `create_order_time` datetime DEFAULT NULL COMMENT '生成订单时间',
  `cancel_order_time` datetime DEFAULT NULL COMMENT '取消订单时间',
  `pay_order_time` datetime DEFAULT NULL COMMENT '支付订单时间',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `edit_time` datetime DEFAULT NULL COMMENT '编辑时间',
  `status` tinyint(1) DEFAULT '1' COMMENT '1:正常 0:删除',
  PRIMARY KEY (`id`),
  UNIQUE KEY `d_order_order_number_IDX` (`order_number`) USING BTREE,
  KEY `user_id_IDX` (`user_id`) USING BTREE,
  KEY `program_id_IDX` (`program_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='订单表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `d_order_3`
--

LOCK TABLES `d_order_3` WRITE;
/*!40000 ALTER TABLE `d_order_3` DISABLE KEYS */;
/*!40000 ALTER TABLE `d_order_3` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `d_order_ticket_user_0`
--

DROP TABLE IF EXISTS `d_order_ticket_user_0`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `d_order_ticket_user_0` (
  `id` bigint(20) NOT NULL COMMENT '主键id',
  `order_number` bigint(20) NOT NULL COMMENT '订单编号',
  `program_id` bigint(20) NOT NULL COMMENT '节目表id',
  `user_id` bigint(20) NOT NULL COMMENT '用户id',
  `ticket_user_id` bigint(20) NOT NULL COMMENT '购票人id',
  `seat_id` bigint(20) NOT NULL COMMENT '座位id',
  `seat_info` varchar(100) DEFAULT NULL COMMENT '座位信息',
  `ticket_category_id` bigint(20) DEFAULT NULL COMMENT '节目票档id',
  `order_price` decimal(10,0) DEFAULT NULL COMMENT '订单价格',
  `pay_order_price` decimal(10,0) DEFAULT NULL COMMENT '支付订单价格',
  `pay_order_type` int(3) DEFAULT NULL COMMENT '支付订单方式',
  `order_status` int(3) DEFAULT '1' COMMENT '订单状态 1:未支付 2:已取消 3:已支付 4:已退单',
  `create_order_time` datetime DEFAULT NULL COMMENT '生成订单时间',
  `cancel_order_time` datetime DEFAULT NULL COMMENT '取消订单时间',
  `pay_order_time` datetime DEFAULT NULL COMMENT '支付订单时间',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `edit_time` datetime DEFAULT NULL COMMENT '编辑时间',
  `status` tinyint(1) DEFAULT '1' COMMENT '1:正常 0:删除',
  PRIMARY KEY (`id`),
  KEY `d_order_ticket_user_order_id_IDX` (`order_number`) USING BTREE,
  KEY `d_order_ticket_user_user_id_IDX` (`user_id`) USING BTREE,
  KEY `d_order_ticket_user_ticket_user_id_IDX` (`ticket_user_id`) USING BTREE,
  KEY `d_order_ticket_user_create_order_time_IDX` (`create_order_time`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='购票人订单表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `d_order_ticket_user_0`
--

LOCK TABLES `d_order_ticket_user_0` WRITE;
/*!40000 ALTER TABLE `d_order_ticket_user_0` DISABLE KEYS */;
/*!40000 ALTER TABLE `d_order_ticket_user_0` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `d_order_ticket_user_1`
--

DROP TABLE IF EXISTS `d_order_ticket_user_1`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `d_order_ticket_user_1` (
  `id` bigint(20) NOT NULL COMMENT '主键id',
  `order_number` bigint(20) NOT NULL COMMENT '订单编号',
  `program_id` bigint(20) NOT NULL COMMENT '节目表id',
  `user_id` bigint(20) NOT NULL COMMENT '用户id',
  `ticket_user_id` bigint(20) NOT NULL COMMENT '购票人id',
  `seat_id` bigint(20) NOT NULL COMMENT '座位id',
  `seat_info` varchar(100) DEFAULT NULL COMMENT '座位信息',
  `ticket_category_id` bigint(20) DEFAULT NULL COMMENT '节目票档id',
  `order_price` decimal(10,0) DEFAULT NULL COMMENT '订单价格',
  `pay_order_price` decimal(10,0) DEFAULT NULL COMMENT '支付订单价格',
  `pay_order_type` int(3) DEFAULT NULL COMMENT '支付订单方式',
  `order_status` int(3) DEFAULT '1' COMMENT '订单状态 1:未支付 2:已取消 3:已支付 4:已退单',
  `create_order_time` datetime DEFAULT NULL COMMENT '生成订单时间',
  `cancel_order_time` datetime DEFAULT NULL COMMENT '取消订单时间',
  `pay_order_time` datetime DEFAULT NULL COMMENT '支付订单时间',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `edit_time` datetime DEFAULT NULL COMMENT '编辑时间',
  `status` tinyint(1) DEFAULT '1' COMMENT '1:正常 0:删除',
  PRIMARY KEY (`id`),
  KEY `d_order_ticket_user_order_id_IDX` (`order_number`) USING BTREE,
  KEY `d_order_ticket_user_user_id_IDX` (`user_id`) USING BTREE,
  KEY `d_order_ticket_user_ticket_user_id_IDX` (`ticket_user_id`) USING BTREE,
  KEY `d_order_ticket_user_create_order_time_IDX` (`create_order_time`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='购票人订单表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `d_order_ticket_user_1`
--

LOCK TABLES `d_order_ticket_user_1` WRITE;
/*!40000 ALTER TABLE `d_order_ticket_user_1` DISABLE KEYS */;
/*!40000 ALTER TABLE `d_order_ticket_user_1` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `d_order_ticket_user_2`
--

DROP TABLE IF EXISTS `d_order_ticket_user_2`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `d_order_ticket_user_2` (
  `id` bigint(20) NOT NULL COMMENT '主键id',
  `order_number` bigint(20) NOT NULL COMMENT '订单编号',
  `program_id` bigint(20) NOT NULL COMMENT '节目表id',
  `user_id` bigint(20) NOT NULL COMMENT '用户id',
  `ticket_user_id` bigint(20) NOT NULL COMMENT '购票人id',
  `seat_id` bigint(20) NOT NULL COMMENT '座位id',
  `seat_info` varchar(100) DEFAULT NULL COMMENT '座位信息',
  `ticket_category_id` bigint(20) DEFAULT NULL COMMENT '节目票档id',
  `order_price` decimal(10,0) DEFAULT NULL COMMENT '订单价格',
  `pay_order_price` decimal(10,0) DEFAULT NULL COMMENT '支付订单价格',
  `pay_order_type` int(3) DEFAULT NULL COMMENT '支付订单方式',
  `order_status` int(3) DEFAULT '1' COMMENT '订单状态 1:未支付 2:已取消 3:已支付 4:已退单',
  `create_order_time` datetime DEFAULT NULL COMMENT '生成订单时间',
  `cancel_order_time` datetime DEFAULT NULL COMMENT '取消订单时间',
  `pay_order_time` datetime DEFAULT NULL COMMENT '支付订单时间',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `edit_time` datetime DEFAULT NULL COMMENT '编辑时间',
  `status` tinyint(1) DEFAULT '1' COMMENT '1:正常 0:删除',
  PRIMARY KEY (`id`),
  KEY `d_order_ticket_user_order_id_IDX` (`order_number`) USING BTREE,
  KEY `d_order_ticket_user_user_id_IDX` (`user_id`) USING BTREE,
  KEY `d_order_ticket_user_ticket_user_id_IDX` (`ticket_user_id`) USING BTREE,
  KEY `d_order_ticket_user_create_order_time_IDX` (`create_order_time`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='购票人订单表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `d_order_ticket_user_2`
--

LOCK TABLES `d_order_ticket_user_2` WRITE;
/*!40000 ALTER TABLE `d_order_ticket_user_2` DISABLE KEYS */;
/*!40000 ALTER TABLE `d_order_ticket_user_2` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `d_order_ticket_user_3`
--

DROP TABLE IF EXISTS `d_order_ticket_user_3`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `d_order_ticket_user_3` (
  `id` bigint(20) NOT NULL COMMENT '主键id',
  `order_number` bigint(20) NOT NULL COMMENT '订单编号',
  `program_id` bigint(20) NOT NULL COMMENT '节目表id',
  `user_id` bigint(20) NOT NULL COMMENT '用户id',
  `ticket_user_id` bigint(20) NOT NULL COMMENT '购票人id',
  `seat_id` bigint(20) NOT NULL COMMENT '座位id',
  `seat_info` varchar(100) DEFAULT NULL COMMENT '座位信息',
  `ticket_category_id` bigint(20) DEFAULT NULL COMMENT '节目票档id',
  `order_price` decimal(10,0) DEFAULT NULL COMMENT '订单价格',
  `pay_order_price` decimal(10,0) DEFAULT NULL COMMENT '支付订单价格',
  `pay_order_type` int(3) DEFAULT NULL COMMENT '支付订单方式',
  `order_status` int(3) DEFAULT '1' COMMENT '订单状态 1:未支付 2:已取消 3:已支付 4:已退单',
  `create_order_time` datetime DEFAULT NULL COMMENT '生成订单时间',
  `cancel_order_time` datetime DEFAULT NULL COMMENT '取消订单时间',
  `pay_order_time` datetime DEFAULT NULL COMMENT '支付订单时间',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `edit_time` datetime DEFAULT NULL COMMENT '编辑时间',
  `status` tinyint(1) DEFAULT '1' COMMENT '1:正常 0:删除',
  PRIMARY KEY (`id`),
  KEY `d_order_ticket_user_order_id_IDX` (`order_number`) USING BTREE,
  KEY `d_order_ticket_user_user_id_IDX` (`user_id`) USING BTREE,
  KEY `d_order_ticket_user_ticket_user_id_IDX` (`ticket_user_id`) USING BTREE,
  KEY `d_order_ticket_user_create_order_time_IDX` (`create_order_time`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='购票人订单表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `d_order_ticket_user_3`
--

LOCK TABLES `d_order_ticket_user_3` WRITE;
/*!40000 ALTER TABLE `d_order_ticket_user_3` DISABLE KEYS */;
/*!40000 ALTER TABLE `d_order_ticket_user_3` ENABLE KEYS */;
UNLOCK TABLES;
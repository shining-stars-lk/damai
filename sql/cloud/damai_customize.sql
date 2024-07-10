

USE damai_customize;

DROP TABLE IF EXISTS `d_api_data`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `d_api_data` (
  `id` bigint(20) NOT NULL COMMENT '主键id',
  `head_version` varchar(32) DEFAULT NULL COMMENT '请求版本',
  `api_address` varchar(32) DEFAULT NULL COMMENT '客户端ip',
  `api_method` varchar(32) DEFAULT NULL COMMENT '请求方法',
  `api_body` varchar(200) DEFAULT NULL COMMENT '请求体',
  `api_params` varchar(100) DEFAULT NULL COMMENT '请求参数',
  `api_url` varchar(100) DEFAULT NULL COMMENT '请求路径',
  `call_day_time` varchar(64) DEFAULT NULL COMMENT '按天维度记录请求时间',
  `call_hour_time` varchar(64) DEFAULT NULL COMMENT '按小时维度记录请求时间',
  `call_minute_time` varchar(64) DEFAULT NULL COMMENT '按分钟维度记录请求时间',
  `call_second_time` varchar(64) DEFAULT NULL COMMENT '按秒维度记录请求时间',
  `type` int(11) DEFAULT NULL COMMENT 'api规则生效类型 1一般规则 2深度规则',
  `status` int(11) DEFAULT '1' COMMENT '状态 1:未删除 0:删除(默认1)',
  `edit_time` datetime DEFAULT NULL COMMENT '编辑时间',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  PRIMARY KEY (`id`),
  KEY `idx_create_time` (`create_time`) USING BTREE,
  KEY `idx_api_address` (`api_address`) USING BTREE,
  KEY `idx_api_url` (`api_url`) USING BTREE,
  KEY `idx_call_day_time` (`call_day_time`) USING BTREE,
  KEY `idx_call_hour_time` (`call_hour_time`) USING BTREE,
  KEY `idx_call_minute_time` (`call_minute_time`) USING BTREE,
  KEY `idx_call_second_time` (`call_second_time`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='api执行表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `d_api_data`
--

LOCK TABLES `d_api_data` WRITE;
/*!40000 ALTER TABLE `d_api_data` DISABLE KEYS */;
/*!40000 ALTER TABLE `d_api_data` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `d_depth_rule`
--

DROP TABLE IF EXISTS `d_depth_rule`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `d_depth_rule` (
  `id` bigint(20) NOT NULL COMMENT 'id',
  `start_time_window` varchar(64) NOT NULL COMMENT '[限制开始时间]',
  `end_time_window` varchar(64) NOT NULL COMMENT '[限制结束时间]',
  `stat_time` int(11) NOT NULL COMMENT '统计时间',
  `stat_time_type` int(11) NOT NULL COMMENT '统计时间类型 1:秒 2:分钟',
  `threshold` int(11) NOT NULL COMMENT '调用限制阈值',
  `effective_time` int(11) NOT NULL COMMENT '限制时间',
  `effective_time_type` int(11) NOT NULL COMMENT '限制时间类型 1:秒 2:分钟',
  `limit_api` text COMMENT '限制路径 逗号分割',
  `message` varchar(64) DEFAULT NULL COMMENT '限制访问提示语',
  `status` tinyint(4) DEFAULT '1' COMMENT '状态标识1.正常 0. 禁用  (默认1)',
  `edit_time` datetime DEFAULT NULL COMMENT '编辑时间',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='深度调用限制规则表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `d_depth_rule`
--

LOCK TABLES `d_depth_rule` WRITE;
/*!40000 ALTER TABLE `d_depth_rule` DISABLE KEYS */;
/*!40000 ALTER TABLE `d_depth_rule` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `d_rule`
--

DROP TABLE IF EXISTS `d_rule`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `d_rule` (
  `id` bigint(20) NOT NULL COMMENT 'id',
  `stat_time` int(11) NOT NULL COMMENT '统计时间',
  `stat_time_type` int(11) NOT NULL COMMENT '统计时间类型 1:秒 2:分钟',
  `threshold` int(11) NOT NULL COMMENT '调用限制阈值',
  `effective_time` int(11) NOT NULL COMMENT '限制时间',
  `effective_time_type` int(11) NOT NULL COMMENT '限制时间类型 1:秒 2:分钟',
  `limit_api` text COMMENT '限制路径 逗号分割',
  `message` varchar(64) DEFAULT NULL COMMENT '限制访问提示语',
  `status` tinyint(4) DEFAULT '1' COMMENT '状态标识1.正常 0. 禁用  (默认1)',
  `edit_time` datetime DEFAULT NULL COMMENT '编辑时间',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='调用限制规则表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `d_rule`
--

LOCK TABLES `d_rule` WRITE;
/*!40000 ALTER TABLE `d_rule` DISABLE KEYS */;
/*!40000 ALTER TABLE `d_rule` ENABLE KEYS */;
UNLOCK TABLES;



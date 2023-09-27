-- v2.2.4及以上版本
create table ko_method_node
(
    id          varchar(400) not null primary key comment '主键',
    name        varchar(400) null comment '类名+方法名',
    class_name  varchar(400) null comment '类名',
    method_name varchar(400) null comment '方法名',
    route_name  varchar(400) null comment '路由，controller才有',
    method_type varchar(64)  null comment '方法类型'
) comment '方法信息表';


create table ko_method_relation
(
    id           varchar(400)   not null primary key comment '',
    source_id    varchar(400)   null comment '调用方id',
    target_id    varchar(400)   null comment '被调用方id',
    avg_run_time numeric(10, 2) null comment '平均耗时',
    max_run_time numeric(10, 2) null comment '最大耗时',
    min_run_time numeric(10, 2) null comment '最小耗时'
) comment '方法调用关系表';
;
create table ko_exception_node
(
    id         varchar(400) not null primary key comment '主键',
    name       varchar(400) null comment '异常名',
    class_name varchar(400) null comment '类名'
) comment '异常表';


create table ko_exception_relation
(
    id        varchar(400) not null primary key comment '',
    source_id varchar(400) null comment '调用方法id',
    target_id varchar(400) null comment '异常id',
    location  int          null comment '异常位置',
    message    varchar(400) null comment '异常消息'
) comment '异常关系表';

create table ko_param_ana
(
    source_id    varchar(400)   null comment '调用方法id',
    params       varchar(400)   null comment '参数组合，-分隔',
    avg_run_time numeric(10, 2) null comment '平均耗时',
    max_run_time numeric(10, 2) null comment '最大耗时',
    min_run_time numeric(10, 2) null comment '最小耗时'
) comment '参数分析表';

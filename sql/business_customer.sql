-- ----------------------------
-- 业务客户信息表
-- ----------------------------
drop table if exists business_customer;
create table business_customer (
  customer_id      bigint(20)      not null auto_increment    comment '客户ID',
  customer_name   varchar(50)     not null                   comment '客户姓名',
  phone           varchar(11)     default ''                 comment '手机号码',
  email           varchar(50)     default ''                 comment '电子邮箱',
  address         varchar(255)    default ''                 comment '联系地址',
  id_card         varchar(18)     default ''                 comment '身份证号',
  status          char(1)         default '0'                comment '状态（0正常 1停用）',
  del_flag        char(1)         default '0'                comment '删除标志（0代表存在 2代表删除）',
  create_by       varchar(64)     default ''                 comment '创建者',
  create_time     datetime                                   comment '创建时间',
  update_by       varchar(64)     default ''                 comment '更新者',
  update_time     datetime                                   comment '更新时间',
  remark          varchar(500)    default null               comment '备注',
  primary key (customer_id),
  unique key uk_customer_name_del_flag (customer_name, del_flag),
  key idx_customer_phone (phone),
  key idx_customer_status_del_flag (status, del_flag)
) engine=innodb auto_increment=1 comment = '业务客户信息表';

-- ----------------------------
-- 初始化-业务客户信息表数据
-- ----------------------------
insert into business_customer values(1, '张三', '13800138000', 'zhangsan@example.com', '北京市朝阳区', '110101199001011234', '0', '0', 'admin', sysdate(), '', null, '测试客户1');
insert into business_customer values(2, '李四', '13800138001', 'lisi@example.com', '上海市浦东新区', '110101199001011235', '0', '0', 'admin', sysdate(), '', null, '测试客户2');

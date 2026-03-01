-- ----------------------------
-- 客户管理菜单及权限（业务模块）
-- ----------------------------
-- 说明：
-- 1. 父菜单挂载在“系统管理”(menu_id=1) 下
-- 2. 默认授权给管理员角色(role_id=1)

-- 清理旧数据（按本脚本约定的menu_id）
delete from sys_role_menu where menu_id in (2000, 2001, 2002, 2003, 2004, 2005);
delete from sys_menu where menu_id in (2000, 2001, 2002, 2003, 2004, 2005);

-- 一级菜单（页面）
insert into sys_menu values('2000', '客户管理', '1', '10', 'customer', 'business/customer/index', '', 1, 0, 'C', '0', '0', 'system:customer:list', 'user', 'admin', sysdate(), '', null, '客户管理菜单');

-- 按钮权限
insert into sys_menu values('2001', '客户查询', '2000', '1', '#', '', '', 1, 0, 'F', '0', '0', 'system:customer:query', '#', 'admin', sysdate(), '', null, '');
insert into sys_menu values('2002', '客户新增', '2000', '2', '#', '', '', 1, 0, 'F', '0', '0', 'system:customer:add', '#', 'admin', sysdate(), '', null, '');
insert into sys_menu values('2003', '客户修改', '2000', '3', '#', '', '', 1, 0, 'F', '0', '0', 'system:customer:edit', '#', 'admin', sysdate(), '', null, '');
insert into sys_menu values('2004', '客户删除', '2000', '4', '#', '', '', 1, 0, 'F', '0', '0', 'system:customer:remove', '#', 'admin', sysdate(), '', null, '');
insert into sys_menu values('2005', '客户导出', '2000', '5', '#', '', '', 1, 0, 'F', '0', '0', 'system:customer:export', '#', 'admin', sysdate(), '', null, '');

-- 管理员角色授权
insert into sys_role_menu values ('1', '2000');
insert into sys_role_menu values ('1', '2001');
insert into sys_role_menu values ('1', '2002');
insert into sys_role_menu values ('1', '2003');
insert into sys_role_menu values ('1', '2004');
insert into sys_role_menu values ('1', '2005');

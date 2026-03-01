## Why

当前系统仅有系统用户（sys_user）管理功能，缺乏独立的业务客户信息管理模块。随着业务发展，需要一个专门的客户信息录入、查询、编辑功能，用于管理业务层面的客户数据，与系统用户进行解耦。

## What Changes

- 新增业务客户信息表 `business_customer`
- 后端：创建客户管理模块（Controller、Service、Mapper、Domain层）
- 前端：创建客户管理页面（列表、新增、编辑、删除）
- 支持客户信息的增删改查操作

## Capabilities

### New Capabilities
- `customer-info-management`: 客户信息管理功能，包括新增、编辑、删除、查询客户信息

### Modified Capabilities
- (无)

## Impact

- 新增数据库表 `business_customer`
- 后端新增模块：`ruoyi-admin`（Controller）、`ruoyi-system`（Service/Mapper/Domain）
- 前端新增页面和API调用

## Context

当前系统已有系统用户管理功能（sys_user），但这是用于系统管理的用户，不适合用于业务层面的客户信息管理。业务部门需要独立的客户信息管理功能，包括客户姓名、手机号、邮箱、地址、身份证号等字段。

项目为 Spring Boot + Vue 前后端分离架构，后端使用 MyBatis 访问数据库，前端使用 Element UI。

## Goals / Non-Goals

**Goals:**
- 创建业务客户信息表 `business_customer`
- 实现客户的增删改查功能
- 前端页面支持客户信息录入

**Non-Goals:**
- 不涉及权限控制（复用系统级权限）
- 不与现有 sys_user 表关联
- 暂不包含客户导入导出功能

## Decisions

1. **表命名**: 使用 `business_customer` 前缀，与系统表区分
2. **模块位置**: 复用 `ruoyi-system` 模块的 Service/Mapper 层，复用 `ruoyi-admin` 的 Controller 层
3. **前端位置**: 新增 `views/business/customer/` 目录

## Risks / Trade-offs

- [风险] 字段验证规则需要根据业务需求调整 → 上线前与业务确认
- [风险] 身份证号存储需考虑脱敏 → 后续可增加脱敏展示功能

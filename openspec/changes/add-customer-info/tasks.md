## 1. 数据库

- [x] 1.1 创建数据库表 `business_customer`（姓名、手机号、邮箱、地址、身份证号）
- [ ] 1.2 添加基础数据（如果需要）

## 2. 后端 - Domain 层

- [x] 2.1 创建 `BusinessCustomer` 实体类（继承 BaseEntity）
- [x] 2.2 添加字段验证注解

## 3. 后端 - Mapper 层

- [x] 3.1 创建 `BusinessCustomerMapper` 接口
- [x] 3.2 创建 `BusinessCustomerMapper.xml` SQL映射文件

## 4. 后端 - Service 层

- [x] 4.1 创建 `IBusinessCustomerService` 接口
- [x] 4.2 创建 `BusinessCustomerServiceImpl` 实现类
- [x] 4.3 实现 CRUD 方法

## 5. 后端 - Controller 层

- [x] 5.1 创建 `BusinessCustomerController` 控制器
- [x] 5.2 添加增删改查接口
- [x] 5.3 添加权限注解和日志记录

## 6. 前端 - API

- [x] 6.1 创建 `business/customer.js` API 接口
- [x] 7.1 创建客户列表页面 `customer/index.vue`
- [x] 7.2 创建客户新增/编辑对话框
- [x] 7.3 添加表单验证规则

## 8. 测试

- [ ] 8.1 后端接口测试（使用 Swagger）
- [ ] 8.2 前端功能测试

## 9. 构建

- [ ] 9.1 后端构建验证 `mvn clean install -DskipTests`
- [ ] 9.2 前端构建验证 `npm run build:prod`

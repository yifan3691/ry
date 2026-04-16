# RuoYi v3.8.7

基于 Spring Boot 2.5.15 和 Vue 2.6 的前后端分离管理系统，仓库内包含若依基础模块和自定义业务模块 `ods-business`。

## 技术栈

- 后端：Java 1.8、Spring Boot 2.5.15、Spring Security、MyBatis、Druid、JWT
- 前端：Vue 2.6.12、Element UI 2.15.14、Axios
- 构建：Maven 3.x、Node.js、npm
- 数据库：MySQL

## 目录结构

```text
ruoyi/
├── ruoyi-admin/       # Web 入口、启动模块
├── ruoyi-framework/   # 安全、AOP、公共框架配置
├── ruoyi-system/      # 系统管理模块
├── ruoyi-quartz/      # 定时任务模块
├── ruoyi-generator/   # 代码生成模块
├── ruoyi-common/      # 公共工具与通用能力
├── ods-business/      # 自定义业务模块
├── ruoyi-vue/         # Vue 2 前端项目
└── sql/               # 数据库脚本
```

## 环境要求

- JDK 1.8
- Maven 3.x
- Node.js 14+ 和 npm
- MySQL 5.7/8.0

## 本地启动

### 1. 后端

后端配置文件位于 `ruoyi-admin/src/main/resources/`，默认端口为 `8082`。

```bash
mvn clean install -DskipTests
cd ruoyi-admin
mvn spring-boot:run
```

常用配置：

- `application.yml`：应用基础配置、端口、Swagger、上传路径
- `application-druid.yml`：数据库连接配置
- `application-pro.yml`：生产环境配置

### 2. 前端

前端目录为 `ruoyi-vue/`，开发环境默认通过 `/dev-api` 代理到 `http://localhost:8082`。

```bash
cd ruoyi-vue
npm install
npm run dev
```

常用命令：

```bash
npm run build:prod
npm run build:stage
npm run lint
```

## 开发说明

- 权限标识格式：`module:function:action`
- Controller 统一返回 `AjaxResult` 或 `TableDataInfo`
- Domain 类继承 `BaseEntity`
- MyBatis XML 位于各模块 `resources/mapper/` 目录
- 前端接口默认放在 `ruoyi-vue/src/api/`
- 业务代码注释使用中文

## 相关地址

- Swagger：`http://localhost:8082/swagger-ui/index.html`
- 前端开发环境：`http://localhost/`
- 若依文档：[http://doc.ruoyi.vip](http://doc.ruoyi.vip)

# AGENTS.md - RuoYi项目开发规范

## 项目概述

RuoYi v3.8.7 - Spring Boot + Vue前后端分离的快速开发框架
- **后端**: Java 1.8, Spring Boot 2.5.15, Spring Framework 5.3.33
- **前端**: Vue 2.6.12, Element UI 2.15.14
- **构建工具**: Maven 3.x
- **数据库**: MySQL (使用Druid连接池)
- **ORM**: MyBatis

## 构建命令

### 后端 (Java)

```bash
# 构建整个项目
mvn clean install

# 构建特定模块
cd ruoyi-admin && mvn clean install

# 跳过测试（更快构建）
mvn clean install -DskipTests

# 运行应用程序
cd ruoyi-admin && mvn spring-boot:run

# 生产环境打包
mvn clean package -DskipTests
```

### 前端 (Vue)

位于 `ruoyi-vue/` 目录：

```bash
cd ruoyi-vue

# 安装依赖
npm install

# 开发服务器
npm run dev

# 生产环境构建
npm run build:prod

# 测试环境构建
npm run build:stage

# 代码检查
npm run lint

# 自动修复代码问题
npm run lint -- --fix
```

## 代码规范

### Java代码规范

1. **格式化**:
   - 4个空格缩进
   - 左大括号放在同一行（K&R风格）
   - 最大行长度：120个字符
   - UTF-8编码

2. **命名约定**:
   - 类名：PascalCase（例如：`SysUserController`）
   - 方法/变量：camelCase（例如：`getUserList()`）
   - 常量：UPPER_SNAKE_CASE
   - 接口：服务类以'I'为前缀（例如：`ISysUserService`）
   - Mapper接口：以'Mapper'为后缀（例如：`SysUserMapper`）

3. **包结构**:
   ```
   com.ruoyi.{module}/
   ├── controller/     # REST控制器（继承BaseController）
   ├── service/        # 服务接口（I前缀）
   │   └── impl/       # 服务实现类
   ├── mapper/         # MyBatis Mapper接口
   ├── domain/         # 实体类（继承BaseEntity）
   └── util/           # 工具类
   ```

4. **Domain类规范**:
   - 继承`BaseEntity`以获取公共字段
   - 使用`@Excel`注解实现导出功能
   - 包含`serialVersionUID`
   - 使用验证注解（`@NotBlank`、`@Size`等）
   - 使用`ToStringBuilder`重写`toString()`方法

5. **Controller规范**:
   - 使用`@RestController`和`@RequestMapping`注解
   - 继承`BaseController`
   - 使用`@PreAuthorize("@ss.hasPermi('module:function:action')")`进行权限控制
   - 使用`@Log(title = "描述", businessType = BusinessType.XXX)`记录操作日志
   - 返回`AjaxResult`或`TableDataInfo`
   - 使用`@Validated`进行请求参数验证

6. **Import顺序**:
   ```java
   // 1. java.*
   import java.util.List;
   
   // 2. javax.*
   import javax.servlet.http.HttpServletResponse;
   
   // 3. 第三方库
   import org.springframework.beans.factory.annotation.Autowired;
   
   // 4. 项目内部导入
   import com.ruoyi.common.core.controller.BaseController;
   import com.ruoyi.system.domain.SysUser;
   ```

7. **注释**: 业务逻辑描述使用中文注释

### Vue/JavaScript代码规范

位于 `.eslintrc.js` 文件：

1. **格式化**:
   - 2个空格缩进
   - 单引号
   - 不使用分号
   - LF换行符
   - 文件末尾必须有换行符

2. **主要ESLint规则**:
   - `comma-dangle: never`
   - `eqeqeq: always`
   - `camelcase: off`
   - `no-console: off`（允许使用console）
   - Vue组件：PascalCase命名

3. **EditorConfig**（2空格缩进，去除多余空格）

## 架构模式

### 后端

- **Controller层**: REST API端点，请求验证，权限检查
- **Service层**: 业务逻辑，事务管理
- **Mapper层**: 数据访问（MyBatis XML映射文件在`resources/mapper/`目录）
- **Domain层**: 实体类，包含验证和Excel注解

### 安全

- 基于JWT的身份验证
- 权限格式：`module:function:action`（例如：`system:user:list`）
- 使用`@ss.hasPermi()`进行方法级授权

### 错误处理

- 使用`AjaxResult.success()`和`AjaxResult.error()`返回响应
- 业务异常继承`BaseException`
- 全局异常处理器在`GlobalExceptionHandler`中

## 模块结构

```
ruoyi/                          # 根目录
├── ruoyi-admin/               # Web入口，控制器
├── ruoyi-framework/           # 框架配置，安全，AOP
├── ruoyi-system/              # 系统模块（用户，角色，菜单等）
├── ruoyi-quartz/              # 定时任务
├── ruoyi-generator/           # 代码生成
├── ruoyi-common/              # 公共工具类和常量
├── ods-business/              # 自定义业务模块
└── ruoyi-vue/                # 前端Vue项目
```

## 数据库约定

- 表前缀：`sys_`用于系统表
- 主键：`{table}_id`（例如：`user_id`）
- 公共字段：`create_by`、`create_time`、`update_by`、`update_time`、`remark`
- 软删除：使用`del_flag`（0=正常，2=已删除）

## 测试

- 项目中暂无单元测试
- 通过Swagger UI进行手动测试（`/swagger-ui/index.html`）
- 使用开发环境配置（`application-dev.yml`）

## 常用工具类

- **ExcelUtil**: Excel导入导出
- **SecurityUtils**: 获取当前用户信息
- **StringUtils**: 字符串工具类（来自Apache Commons）
- **DateUtils**: 日期格式化和解析

## 参考资料

- RuoYi文档：http://doc.ruoyi.vip
- Spring Boot: 2.5.15
- Vue.js: 2.6.12
- Element UI: 2.15.14

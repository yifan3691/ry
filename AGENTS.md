# AGENTS.md - Coding Guidelines for RuoYi Project

## Project Overview

RuoYi v3.8.7 - Spring Boot + Vue前后端分离的快速开发框架
- **Backend**: Java 1.8, Spring Boot 2.5.15, Spring Framework 5.3.33
- **Frontend**: Vue 2.6.12, Element UI 2.15.14
- **Build Tool**: Maven 3.x
- **Database**: MySQL (with Druid connection pool)
- **ORM**: MyBatis

## Build Commands

### Backend (Java)

```bash
# Build entire project
mvn clean install

# Build specific module
cd ruoyi-admin && mvn clean install

# Skip tests (faster build)
mvn clean install -DskipTests

# Run application
cd ruoyi-admin && mvn spring-boot:run

# Package for production
mvn clean package -DskipTests
```

### Frontend (Vue)

Located in `ruoyi-vue/if-based-on-vue/`:

```bash
cd ruoyi-vue/if-based-on-vue

# Install dependencies
npm install

# Development server
npm run dev

# Production build
npm run build:prod

# Staging build
npm run build:stage

# Lint code
npm run lint

# Fix linting issues
npm run lint -- --fix
```

## Code Style Guidelines

### Java Code Style

1. **Formatting**:
   - 4 spaces indentation
   - Opening brace on same line (K&R style)
   - Max line length: 120 characters
   - UTF-8 encoding

2. **Naming Conventions**:
   - Classes: PascalCase (e.g., `SysUserController`)
   - Methods/Variables: camelCase (e.g., `getUserList()`)
   - Constants: UPPER_SNAKE_CASE
   - Interfaces: Prefix with 'I' for services (e.g., `ISysUserService`)
   - Mapper interfaces: Suffix with 'Mapper' (e.g., `SysUserMapper`)

3. **Package Structure**:
   ```
   com.ruoyi.{module}/
   ├── controller/     # REST controllers (extends BaseController)
   ├── service/        # Service interfaces (I prefix)
   │   └── impl/       # Service implementations
   ├── mapper/         # MyBatis mapper interfaces
   ├── domain/         # Entity/POJO classes (extends BaseEntity)
   └── util/           # Utility classes
   ```

4. **Domain Classes**:
   - Extend `BaseEntity` for common fields
   - Use `@Excel` annotation for export functionality
   - Include `serialVersionUID`
   - Use validation annotations (`@NotBlank`, `@Size`, etc.)
   - Override `toString()` using `ToStringBuilder`

5. **Controller Conventions**:
   - Annotate with `@RestController` and `@RequestMapping`
   - Extend `BaseController`
   - Use `@PreAuthorize("@ss.hasPermi('module:function:action')")` for permissions
   - Use `@Log(title = "描述", businessType = BusinessType.XXX)` for operation logging
   - Return `AjaxResult` or `TableDataInfo`
   - Use `@Validated` for request validation

6. **Import Order**:
   ```java
   // 1. java.*
   import java.util.List;
   
   // 2. javax.*
   import javax.servlet.http.HttpServletResponse;
   
   // 3. Third-party libraries
   import org.springframework.beans.factory.annotation.Autowired;
   
   // 4. Project imports
   import com.ruoyi.common.core.controller.BaseController;
   import com.ruoyi.system.domain.SysUser;
   ```

7. **Comments**: Use Chinese comments for business logic descriptions

### Vue/JavaScript Code Style

Located in `.eslintrc.js`:

1. **Formatting**:
   - 2 spaces indentation
   - Single quotes
   - No semicolons
   - LF line endings
   - Trailing newline required

2. **Key ESLint Rules**:
   - `comma-dangle: never`
   - `eqeqeq: always`
   - `camelcase: off`
   - `no-console: off` (allowed)
   - Vue components: PascalCase naming

3. **EditorConfig** (2-space indent, trim whitespace)

## Architecture Patterns

### Backend

- **Controller Layer**: REST API endpoints, request validation, permission checks
- **Service Layer**: Business logic, transaction management
- **Mapper Layer**: Data access (MyBatis XML mappers in `resources/mapper/`)
- **Domain Layer**: Entity classes with validation and Excel annotations

### Security

- JWT-based authentication
- Permission format: `module:function:action` (e.g., `system:user:list`)
- Use `@ss.hasPermi()` for method-level authorization

### Error Handling

- Use `AjaxResult.success()` and `AjaxResult.error()` for responses
- Business exceptions extend `BaseException`
- Global exception handler in `GlobalExceptionHandler`

## Module Structure

```
ruoyi/                          # Root
├── ruoyi-admin/               # Web entry point, controllers
├── ruoyi-framework/           # Framework config, security, AOP
├── ruoyi-system/              # System module (user, role, menu, etc.)
├── ruoyi-quartz/              # Scheduled tasks
├── ruoyi-generator/           # Code generation
├── ruoyi-common/              # Common utilities, constants
├── ods-business/              # Custom business module
└── ruoyi-vue/if-based-on-vue/ # Frontend Vue project
```

## Database Conventions

- Table prefix: `sys_` for system tables
- Primary key: `{table}_id` (e.g., `user_id`)
- Common fields: `create_by`, `create_time`, `update_by`, `update_time`, `remark`
- Soft delete: use `del_flag` (0=normal, 2=deleted)

## Testing

- No unit tests currently in the project
- Manual testing via Swagger UI at `/swagger-ui/index.html`
- Use test profiles for development (`application-dev.yml`)

## Common Utilities

- **ExcelUtil**: Excel import/export
- **SecurityUtils**: Get current user info
- **StringUtils**: String utilities (from Apache Commons)
- **DateUtils**: Date formatting and parsing

## References

- RuoYi Documentation: http://doc.ruoyi.vip
- Spring Boot: 2.5.15
- Vue.js: 2.6.12
- Element UI: 2.15.14

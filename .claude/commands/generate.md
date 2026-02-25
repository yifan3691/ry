# RuoYi CRUD 代码生成器

根据用户提供的需求，为 RuoYi 项目生成完整的 CRUD 模块代码。

## 输入参数

用户需要提供以下信息（通过 $ARGUMENTS 传入，格式自由）：
- 模块名称（如：system、monitor、business）
- 实体名称（中文描述 + 英文类名）
- 数据库表名
- 字段列表（字段名、类型、中文描述、是否必填等）

如果用户未提供完整信息，请通过 AskUserQuestion 工具逐步询问收集。

## 生成规范

严格遵循本项目 AGENTS.md 中定义的编码规范。

### 1. Domain 实体类

- 放置在 `ruoyi-system/src/main/java/com/ruoyi/system/domain/` 或对应模块目录
- 继承 `BaseEntity`
- 包含 `serialVersionUID`
- 字段使用 `@Excel` 注解支持导出
- 日期字段使用 `@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")`
- 必填字段使用 `@NotBlank` / `@NotNull`
- 手动编写 getter/setter（项目不使用 Lombok）
- 中文注释描述每个字段

### 2. Mapper 接口

- 放置在对应模块的 `mapper/` 包下
- 接口命名：`{EntityName}Mapper`
- 标准方法：
  - `select{Entity}ById(Long id)` - 根据ID查询
  - `select{Entity}List({Entity} entity)` - 查询列表
  - `insert{Entity}({Entity} entity)` - 新增
  - `update{Entity}({Entity} entity)` - 修改
  - `delete{Entity}ById(Long id)` - 根据ID删除
  - `delete{Entity}ByIds(Long[] ids)` - 批量删除

### 3. MyBatis XML Mapper

- 放置在 `src/main/resources/mapper/{module}/` 目录
- `namespace` 对应 Mapper 接口全限定名
- 定义 `resultMap` 映射（snake_case 列 -> camelCase 属性）
- 列表查询使用 `<where>` + `<if>` 动态条件
- 批量删除使用 `<foreach>`
- 插入使用 `useGeneratedKeys="true" keyProperty="主键字段"`

### 4. Service 接口

- 放置在对应模块的 `service/` 包下
- 接口命名：`I{EntityName}Service`（I 前缀）
- 标准 CRUD 方法，每个方法含 Javadoc 注释

### 5. Service 实现类

- 放置在 `service/impl/` 包下
- 类命名：`{EntityName}ServiceImpl`
- 使用 `@Service` 注解
- 使用 `@Autowired` 注入 Mapper
- 实现接口所有方法，添加 `@Override`

### 6. Controller 控制器

- 放置在 `ruoyi-admin/src/main/java/com/ruoyi/web/controller/{module}/`
- 继承 `BaseController`
- 使用 `@RestController` + `@RequestMapping`
- 标准 REST 端点：
  - `GET /list` - 分页查询列表，返回 `TableDataInfo`，调用 `startPage()`
  - `GET /export` 或 `POST /export` - 导出 Excel
  - `GET /{id}` - 根据ID查询详情
  - `POST` - 新增，使用 `@Validated`
  - `PUT` - 修改，使用 `@Validated`
  - `DELETE /{ids}` - 批量删除
- 每个方法添加 `@PreAuthorize("@ss.hasPermi('{module}:{entity}:{action}')")` 权限注解
- 写操作添加 `@Log(title = "描述", businessType = BusinessType.XXX)` 审计日志

## 生成流程

1. 解析用户输入，确认模块名、实体名、表名、字段信息
2. 如果信息不完整，通过 AskUserQuestion 询问缺失信息
3. 按以下顺序生成代码文件：
   a. Domain 实体类
   b. Mapper 接口
   c. MyBatis XML Mapper
   d. Service 接口
   e. Service 实现类
   f. Controller 控制器
4. 使用 TodoWrite 跟踪每个文件的生成进度
5. 生成完毕后，汇总所有创建的文件路径

## 示例调用

```
/generate 模块:system 实体:公告通知 SysNotice 表:sys_notice 字段:notice_id(Long,主键),notice_title(String,公告标题,必填),notice_type(String,公告类型),notice_content(String,公告内容),status(String,状态)
```

## 注意事项

- 生成前先检查目标目录是否存在，不存在则创建
- 检查是否已有同名文件，避免覆盖已有代码
- 权限标识格式：`{module}:{entity小写}:{action}`
- 所有注释使用中文
- Import 顺序：java.* -> javax.* -> 第三方库 -> 项目内部

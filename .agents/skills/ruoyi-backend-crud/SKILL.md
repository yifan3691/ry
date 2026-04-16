---
name: ruoyi-backend-crud
description: Use when working on RuoYi backend CRUD or business modules in this repository, especially when the user asks to add or modify Java domain classes, controllers, services, mappers, MyBatis XML, Excel export fields, permission codes, or list/detail/add/edit/delete APIs in ruoyi-admin, ruoyi-system, ruoyi-common, ruoyi-framework, ruoyi-quartz, ruoyi-generator, or ods-business.
---

# RuoYi Backend CRUD

Use this skill for backend work that follows the RuoYi layered pattern in this repository.

## Scope

- Add or update entity or domain classes, service interfaces and implementations, controllers, mapper interfaces, and mapper XML files.
- Build or adjust standard CRUD endpoints, Excel export support, permission codes, and audit log annotations.
- Prefer matching an existing neighboring module over inventing a new structure.

## Required project conventions

- Read `AGENTS.md` first when the target module or naming rule is unclear.
- Java code uses 4-space indentation, K&R braces, and UTF-8.
- Business comments should be in Chinese.
- Import order: `java.*` -> `javax.*` -> third-party -> project packages.
- Do not assume Lombok is available.

## Backend structure rules

- Domain classes usually live under `com.ruoyi.<module>.domain`.
- Domain classes should extend `BaseEntity`, include `serialVersionUID`, use `@Excel` where export is needed, keep validation annotations on required fields, and override `toString()` with `ToStringBuilder`.
- Service interfaces use the `I` prefix, for example `ISysUserService`.
- Service implementations live under `service/impl/`.
- Mapper interfaces end with `Mapper`.
- MyBatis XML files belong in `src/main/resources/mapper/<module>/`.

## Controller rules

- Controllers should extend `BaseController`.
- Use `@RestController` and `@RequestMapping`.
- Protect endpoints with `@PreAuthorize("@ss.hasPermi('module:entity:action')")`.
- Write operations should carry `@Log(..., businessType = BusinessType.XXX)`.
- List APIs should call `startPage()` and return `getDataTable(list)`.
- Controller responses should be `AjaxResult` or `TableDataInfo`.

## Default CRUD shape

When creating a standard module, prefer this endpoint set unless the user asks otherwise:

- `GET /list`
- `POST /export` or `GET /export`
- `GET /{id}`
- `POST`
- `PUT`
- `DELETE /{ids}`

## Mapper XML rules

- Use `resultMap` for snake_case to camelCase mapping.
- Use `<where>` and `<if>` for optional query conditions.
- Use `<foreach>` for batch delete or batch query input.
- Use generated keys for inserts when the table uses an auto-increment primary key.

## Working style

1. Find one similar module in the same layer and mirror its structure first.
2. Keep permission strings in `module:entity:action` format.
3. Avoid broad root-level builds when a module-scoped verification is enough.
4. If you commit, the commit message must be Chinese.

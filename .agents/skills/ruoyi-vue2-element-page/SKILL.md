---
name: ruoyi-vue2-element-page
description: Use when building or modifying Vue 2 and Element UI pages in this repository's ruoyi-vue frontend, including login pages, CRUD list pages, forms, dialogs, dashboards, API modules, and page-level styling. Use whenever the user mentions RuoYi frontend work, Element UI, Vue single-file components, src/views, src/api, or wants a page to look better without breaking the existing data flow.
---

# RuoYi Vue2 Element Page

Use this skill for frontend changes under `ruoyi-vue/`.

## Scope

- Create or modify Vue single-file components in `src/views/`.
- Add or update frontend API modules in `src/api/`.
- Adjust Element UI forms, tables, dialogs, filters, and login flows.
- Improve page styling while preserving existing business logic unless the user asks for functional changes.

## Required project conventions

- The frontend is Vue 2.6.12 with Element UI 2.15.14.
- JavaScript and Vue formatting uses 2-space indentation, single quotes, no semicolons, and LF line endings.
- Match the current project structure before introducing new abstractions.
- Prefer reusing existing RuoYi page patterns for table queries, dialog forms, and export or import flows.

## Typical file placement

- Page components: `ruoyi-vue/src/views/...`
- API methods: `ruoyi-vue/src/api/...`
- Shared UI pieces: `ruoyi-vue/src/components/...`
- Global styles: `ruoyi-vue/src/assets/styles/...`

## Page implementation rules

- For CRUD list pages, preserve the common RuoYi interaction shape: `loading`, `ids`, `single`, `multiple`, `showSearch`, `total`, `queryParams`, `form`, and `rules`.
- Keep `handleQuery`, `resetQuery`, `getList`, `handleAdd`, `handleUpdate`, and `submitForm` patterns aligned with neighboring pages when building a standard admin page.
- If touching login or authentication pages, preserve existing captcha, cookie, and store dispatch behavior unless the user explicitly asks to change it.
- For design-heavy pages, be visually intentional, but still keep desktop and mobile loading usable.

## Verification

- Prefer targeted verification first, for example `./node_modules/.bin/eslint --no-ignore --ext .js,.vue <file>`.
- Use `npm run build:stage` when the change affects shared layout, routing, build behavior, or wider style interactions.

## Working style

1. Start from a nearby page with similar behavior.
2. Keep API paths, field names, and backend contracts unchanged unless the task includes backend coordination.
3. Avoid introducing Vue 3 or non-Element UI patterns into this Vue 2 project.
4. If you commit, the commit message must be Chinese.

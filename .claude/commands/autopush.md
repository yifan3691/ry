# 自动分支、提交并推送到远程

在开始编写代码之前自动创建新分支，任务完成后自动提交所有更改并推送到远程仓库。

## 使用方式

```
/autopush <任务描述>
```

例如：
```
/autopush 修复用户登录验证码失效问题
/autopush 新增部门管理导出功能
/autopush 重构系统日志模块
```

## 工作流程

收到任务后，严格按以下顺序执行：

### 第一阶段：创建分支

1. 运行 `git status` 确认当前工作区干净，如果有未提交的更改，先提示用户处理
2. 运行 `git checkout master && git pull origin master` 确保基于最新的 master 分支
3. 根据任务描述自动生成分支名，格式为 `feat/<简短英文描述>` 或 `fix/<简短英文描述>`，规则：
   - 新功能用 `feat/`，如 `feat/add-dept-export`
   - Bug 修复用 `fix/`，如 `fix/login-captcha-expired`
   - 重构用 `refactor/`，如 `refactor/sys-log-module`
   - 分支名使用小写英文 + 短横线，不超过 40 个字符
4. 运行 `git checkout -b <分支名>` 创建并切换到新分支
5. 告知用户分支已创建，开始执行任务

### 第二阶段：执行任务

按照 $ARGUMENTS 中的任务描述完成编码工作。编码过程中遵循 AGENTS.md 中的项目规范。

### 第三阶段：提交并推送

任务完成后：

1. 运行 `git status` 查看所有变更文件
2. 运行 `git diff` 和 `git diff --cached` 查看具体改动
3. 检查是否有敏感文件（.env、credentials、密钥文件等），如果有则警告用户并排除
4. 运行 `git add .` 暂存所有变更（排除敏感文件）
5. 根据实际改动内容生成提交信息，格式：
   ```
   <type>: <中文描述>

   - 具体改动点1
   - 具体改动点2

   Co-Authored-By: Claude <noreply@anthropic.com>
   ```
   其中 type 为 feat / fix / refactor / docs / chore 等
6. 运行 `git commit` 提交
7. 运行 `git push -u origin <分支名>` 推送到远程
8. 输出最终结果摘要，包括：
   - 分支名称
   - 提交哈希
   - 变更文件列表
   - 远程推送状态

## 注意事项

- 绝对不要直接在 master 分支上提交代码
- 绝对不要使用 `--force` 推送
- 绝对不要提交包含密钥、密码、token 等敏感信息的文件
- 如果 push 失败（如网络问题），告知用户手动重试命令
- 如果 pre-commit hook 修改了文件，创建新的提交包含这些修改（不要 amend）
- 每次只处理一个任务，一个分支

# 笔记本到台式机迁移与下一单元断点

**用途：** 在台式机恢复同一仓库、数据库和学习断点。
**适用日期：** 2026-09-06 之后开始下一单元时。

## 1. 当前目标

长期目标是以 Java 后端为主线，争取字节跳动或其他高质量公司的研发岗位，并在可靠后端基本盘完成后用 AI Agent 应用工程形成差异化。

当前处于阶段 2：Spring Boot + MySQL 可靠单体。阶段 1 的 Java 领域核心已经完成；阶段 2 已完成第一条从 HTTP 到 MySQL 的纵向链路。

## 2. 已确认的软件状态

- 仓库：`https://github.com/xxby-cmd/SaleSystem`
- 分支：`master`
- 远端 HEAD：`1ede191`（`Java与MySQL相互联通，打通后端`）
- 知识笔记提交：`5ca86ac`
- Java：25
- Spring Boot：3.5.16
- 构建：Maven
- 数据库：MySQL，默认端口 3306
- 数据库名：`ProductInformation`
- 初始化脚本：`SQL/database/V1_ProductInformation.sql`
- 数据库配置：`src/main/resources/application.properties`

已验证：Maven 测试共 68 个全部通过；真实 POST 可以写入 MySQL；GET 可以查询；不存在返回 404；重复编码时返回 500，并符合“错误前保存、错误处停止、错误后不保存”的当前契约。

## 3. 笔记本上尚未进入远端的修改

`src/main/java/com/xxby/Product/ProductCatalogService.java` 仅删除了一个未使用的 `java.sql.SQLException` import，目前没有提交。它不改变行为，也不阻塞台式机学习。不要误以为台式机漏了功能代码。

## 4. 台式机恢复步骤

### 4.1 获取代码

如果台式机还没有仓库：

```powershell
git clone https://github.com/xxby-cmd/SaleSystem
cd SaleSystem
```

如果已经有仓库并且没有本地未提交工作：

```powershell
git switch master
git pull --ff-only origin master
```

若 `git status` 显示本地修改，不要强行覆盖；先判断修改属于谁、是否需要保留。

### 4.2 检查工具

```powershell
java -version
mvn -version
mysql --version
git status
git log -3 --oneline
```

Java 应为 25；Maven 必须能找到 Java；MySQL 服务必须已经启动。

### 4.3 初始化数据库

在 MySQL 客户端执行：

```text
SQL/database/V1_ProductInformation.sql
```

随后确认 `ProductInformation` 数据库和 `total` 表存在。不要把 root 密码写入源码、脚本、笔记或 Git。

### 4.4 配置凭据

应用从环境变量读取：

```text
SALES_DB_USERNAME
SALES_DB_PASSWORD
```

在台式机本地设置真实值后，重新打开终端。迁移文档不记录实际用户名和密码。

### 4.5 验证构建

普通执行：

```powershell
mvn test
```

如果 Windows/JUnit 只在清理系统临时目录时报 `AccessDeniedException`，可建立项目内部临时目录并执行：

```powershell
New-Item -ItemType Directory -Force target/test-tmp
mvn "-DargLine=-Djava.io.tmpdir=target/test-tmp" test
```

这只用于区分环境权限问题与业务测试失败，不能用于隐藏真实断言失败。

### 4.6 启动应用

```powershell
mvn spring-boot:run
```

启动成功后，再用公开 HTTP 接口验证查询；不要仅凭控制台出现 Spring 标志就判断数据库链路正常。

## 5. 下一单元

下一单元是：**客户资源 CRUD 与标准 REST 接口**。

它要证明你能独立完成一个完整数据库资源，而不是只打通一次新增与查询。

最小功能：

1. 新增客户；
2. 根据数据库 ID 查询；
3. 根据客户编码查询；
4. 修改允许修改的客户资料；
5. 停用或删除客户；
6. 客户编码重复时返回冲突；
7. 客户不存在时返回 404。

## 6. 开始编码前必须先设计

由用户亲自写出以下答案，AI 先审查，不直接给完整代码：

- 客户有哪些最小字段；
- 哪个是数据库主键，哪个是业务唯一编码；
- 客户编码能否修改；
- “删除客户”是物理删除还是状态停用；
- 名称为空、编码重复、查询不存在分别是什么结果；
- POST、GET、PUT/PATCH、DELETE 分别使用什么路径和状态码。

## 7. 本单元核心知识

- REST 资源建模与 HTTP 方法语义；
- 数据库代理主键与业务唯一键；
- JDBC 完整 CRUD；
- `executeUpdate()` 返回的影响行数；
- 插入后取得自增主键；
- 请求 DTO、响应 DTO 与领域对象的区别；
- 404、409 与 500 的边界；
- 更新和删除的幂等语义。

## 8. 当前不要做

- 不做前端页面；
- 不引入 JPA、MyBatis、Redis、消息队列或微服务；
- 不补完整 ERP 客户资料；
- 不先做复杂权限和登录；
- 不把商品目录外键补完当成下一单元主任务；
- 不由 AI 直接生成整套 CRUD 后让用户照抄。

## 9. 下一次建议的开场指令

```text
请先阅读 MEMORY.md、memory/hot-cache.md 和 memory/wiki/desktop-migration.md，
再检查当前代码、Git、Java、Maven 和 MySQL 状态。
从“客户资源 CRUD”单元开始，先审查我对字段、主键、唯一键、删除语义和 REST 路径的设计，
不要立即给完整代码。
```


# 2026-08-21｜JUnit、Maven 与 Git 工作流学习笔记

- 学习时段：13:30—17:30
- 路线阶段：阶段 1——Java 领域核心与可测试最小程序
- 对应岗位能力：Java 基础、业务校验、JUnit 测试设计、Maven 构建、Git 日常工作流、故障定位
- 当前掌握结论：在提示、审查和纠错下完成，记为 L2；尚不能记为独立掌握

## 1. 今天完成了什么

1. 从 Oracle 官方来源安装并验证 JDK 25，命令行实际使用 Java 25。
2. 全局安装 Apache Maven 3.9.16，并理解 IDEA 测试与 Maven 测试的区别。
3. 为 `ProductModel` 和 `QuotationItem` 编写 5 个 JUnit 5 测试方法。
4. 用测试暴露并修复“无参创建对象后首次调用 Setter，列表索引为 `-1`”的问题。
5. 修复了两个会错误通过的价格测试：原测试先触发数量异常，并没有真正验证价格规则。
6. 使用 `mvn clean test` 在 IDE 外完成构建，结果为 5 个测试全部通过。
7. 完善 `.gitignore`，停止跟踪 IDEA 工程文件，并忽略构建产物、本地下载和私人学习资料。
8. 完成提交与推送；同时排查过一次 GitHub 443 端口连接超时。

软件通过不等于知识掌握。今天的代码和测试是学习证据，但仍需要间隔复写验证。

## 2. `assertThrows` 为什么可能“假通过”

下面这种测试只说明某处抛出了 `IllegalArgumentException`：

```java
assertThrows(IllegalArgumentException.class,
        () -> new QuotationItem("SAU32X175", 0, new BigDecimal("-100.00")));
```

构造器先校验数量，再校验价格。数量 `0` 已经非法，因此程序在到达价格校验前就抛出同类型异常。测试虽然是绿色，却没有证明负价格被拒绝。

正确原则：测试某条规则时，其他输入必须保持合法，只改变正在验证的变量。例如测试负价格时使用合法的正数量。

这不是数据“污染”，而是测试没有隔离变量，也没有证明异常来自预期规则。

## 3. `this`、字段和集合元素不是一回事

执行下面的代码时：

```java
ProductModel product = new ProductModel();
product.setProductCode("SAU32X150");
```

- `product` 对象已经创建，所以方法中的 `this` 存在，不是 `null`。
- `this.productCode` 还没有赋值，所以字段是 `null`。
- 当前对象还没有加入静态 `productList`。
- `productList.indexOf(this)` 找不到该对象，因此返回 `-1`。
- 如果继续执行 `productList.get(-1)`，会发生索引越界。

修复后的关键行为是：第一次赋值时直接设置字段并把对象加入列表；已有编码的对象再次修改时才走更新路径。

## 4. 静态集合为什么会让测试互相影响

`static` 字段属于类，而不是某一个对象。在同一个 JVM 和类加载器中，所有 `ProductModel` 对象以及所有测试方法共享同一个 `productList`。

因此可能出现：

1. 测试 A 加入 `SAU32X150`。
2. 测试 A 结束，但静态列表没有自动清空。
3. 测试 B 也加入 `SAU32X150`，本来并不想测试重复。
4. 测试 B 因测试 A 遗留的数据而失败。

这叫共享可变状态导致的测试相互污染。当前仅识别该风险，不在本学习单元扩大重构。未来可以通过取消全局静态集合、显式仓储对象，或在每个测试前重置状态来解决。

## 5. `BigDecimal.equals()` 与 `compareTo()`

```java
new BigDecimal("2.0").equals(new BigDecimal("2.00"))      // false
new BigDecimal("2.0").compareTo(new BigDecimal("2.00")) // 0
```

- `equals()` 同时考虑数值和小数位数（scale）。
- `compareTo()` 比较数值大小，不要求 scale 相同。
- JUnit 的 `assertEquals(BigDecimal, BigDecimal)` 最终使用对象相等语义，因此也会区分 `2.0` 与 `2.00`。

当前测试的选择：

- 普通小计测试使用 `assertEquals`，同时验证当前乘法结果的金额表示。
- 零价测试使用 `compareTo(BigDecimal.ZERO) == 0`，只验证数值是否为零。

金额规则必须由业务决定。不能笼统地认为所有金额比较都应该永远使用其中一种方式。

## 6. Maven 到底做了什么

`pom.xml` 不保存依赖 JAR。它声明项目坐标、Java 版本、依赖坐标、插件和构建配置。

典型流程：

```text
读取 pom.xml
  -> 从 Maven Central 解析并下载缺失依赖
  -> 缓存到 C:\Users\XXBY\.m2\repository
  -> 将依赖 JAR 加入编译/测试 classpath
  -> 编译主代码到 target\classes
  -> 编译测试代码到 target\test-classes
  -> Surefire 运行测试
  -> 报告写入 target\surefire-reports
```

重要结论：

- 依赖一般不会被复制进项目源码目录。
- 第一次运行需要下载，之后通常复用 `.m2` 中的缓存。
- `mvn clean` 删除项目的 `target`，不会删除 `.m2` 依赖缓存。
- `target` 是可重新生成的构建产物，不应提交到 Git。

常用命令：

```powershell
mvn -version
mvn test
mvn clean test
```

`mvn clean test` 的主要阶段是：

```text
clean
  -> resources
  -> compile
  -> testResources
  -> testCompile
  -> test（Surefire）
```

## 7. IDEA 绿色按钮与 Maven 测试

IDEA 默认可以使用自己的编译器和 JUnit Runner 运行单个测试，反馈快，适合日常开发。IDEA 也可以配置为把构建/测试委托给 Maven。

Maven 按 `pom.xml` 使用统一的构建规则，适合：

- 在 IDE 外验证项目；
- 团队成员在不同电脑上复现构建；
- CI 自动执行测试；
- 检查项目是否暗中依赖 IDEA 配置。

实际工作不是二选一：开发时经常用 IDEA 快速运行局部测试，提交前和 CI 中再用 Maven/Gradle执行统一构建。

## 8. `.gitignore` 与“已经被跟踪”的文件

`.gitignore` 只阻止尚未被 Git 跟踪的文件进入版本库。某文件如果已经提交过，后来再写进 `.gitignore`，Git 仍会继续跟踪它。

停止跟踪但保留本地文件时使用：

```powershell
git rm --cached -r .idea
```

然后提交索引变化。今天已验证 `.idea` 不再被跟踪，`target`、本地下载目录和私人导航资料会被忽略。

## 9. Git 推送失败的排查思路

今天 IDEA 的完整错误是：

```text
Failed to connect to github.com port 443 ... Could not connect to server
```

定位证据：

- 本地提交存在，分支领先远程；
- 远程 URL 和分支关联正确；
- DNS 能解析 GitHub；
- 连接 GitHub 443 端口超时。

因此当时属于网络连接问题，不是代码冲突或账号认证问题。排查时应先读完整错误，再区分：

- `Could not connect`：网络、代理、防火墙或端口问题；
- `Authentication failed` / `403`：认证或权限问题；
- `non-fast-forward`：远程存在本地没有的提交；
- `rejected` / branch protection：分支保护或服务端规则。

## 10. 今天出现的真实错误与纠偏

1. 把 `this` 误认为 `null`：实际上对象存在，空的是字段，对象也尚未进入列表。
2. 价格异常测试使用非法数量：测试因错误原因通过。
3. 认为 `pom.xml` 保存依赖：实际上它保存依赖声明，JAR 缓存在 `.m2`。
4. 认为 Maven 会把依赖复制到项目编译目录：实际上它主要通过 classpath 引用依赖，只把本项目编译结果写入 `target`。
5. 认为 Maven 只在大项目最终完成后运行：实际上本地提交前和 CI 中都会频繁运行。

## 11. 当前自动测试证据

测试覆盖：

1. 无参 `ProductModel` 首次 Setter 录入并标准化；
2. 重复产品编码被拒绝；
3. 正常金额小计；
4. 零价赠品小计；
5. 零/负数量被拒绝；
6. `null`/负价格被拒绝。

验收结果：5 个测试方法全部通过，Failures、Errors、Skipped 均为 0。

尚未覆盖：静态列表的测试隔离、Setter 的全部失败边界、`QuotationItem` 不完整状态的统一契约、并发和数据库。这些不是今天继续扩展的任务。

## 12. 24—72 小时独立复写任务

不能查看当前实现和本笔记中的答案，从空白完成：

1. `ProductModel`：无参/有参创建、编码标准化、空白和重复校验、首次 Setter 录入。
2. `QuotationItem`：型号、正数量、非负且非空价格、小计计算、零价赠品。
3. 至少 5 个 JUnit 测试，确保价格测试不会先因数量失败。
4. 用 Maven 在 IDEA 外运行全部测试。
5. 口头解释 `this`、`static`、`indexOf()`、`BigDecimal` 两种比较以及 Maven 的目录结构。

只有无提示完成并能解决常见错误，才把这一单元从 L2 升为 L3。

## 13. 当前明确不做

- 不开始 Spring Boot、MySQL、Redis、消息队列或微服务。
- 不引入复杂 Agent 工作流。
- 不为追求代码“高级”提前进行大规模架构重构。
- 不把今天通过的测试写成“已经独立掌握”。

下一步先完成间隔复写闸门，再决定阶段 1 的下一个最小学习单元。

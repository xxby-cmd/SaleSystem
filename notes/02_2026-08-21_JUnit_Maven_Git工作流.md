# JUnit、Maven 与 Git：核心知识

## 1. 异常测试：通过不等于测对

`assertThrows` 只保证抛出了指定类型的异常，不保证异常来自目标规则。

```java
// 错误：数量 0 会先抛异常，无法证明负价格校验有效
assertThrows(IllegalArgumentException.class,
        () -> new QuotationItem("SAU32X175", 0, new BigDecimal("-1")));

// 正确：测试负价格时，其他输入全部合法
assertThrows(IllegalArgumentException.class,
        () -> new QuotationItem("SAU32X175", 1, new BigDecimal("-1")));
```

可迁移原则：

- 一次测试只改变一个变量；
- 其他输入保持合法，确保执行到目标分支；
- 多条规则使用同一异常类型时，必要时再断言异常消息或错误码；
- 测试的价值是排除错误实现，不是得到绿色结果。

## 2. 校验必须先于状态修改

对象更新的可靠顺序：

```text
读取输入 → 校验全部规则 → 标准化 → 一次性写入字段
```

如果先修改字段，后续校验失败，对象可能留下部分更新状态。该原则同样适用于 Setter、批量修改和数据库事务：失败时应尽量做到全部生效或完全不生效。

## 3. `this`、空字段与集合查找

进入实例方法说明当前对象存在，因此 `this` 不是 `null`。但对象的字段可以是 `null`，对象也可能尚未加入集合。

```java
ProductModel product = new ProductModel(); // 对象存在
// product.productCode 仍可能为 null
// product 也可能不在 productList 中
```

必须区分：

1. 对象引用为 `null`；
2. 对象存在，但字段为 `null`；
3. 对象存在，但集合中没有该对象。

`List.indexOf(target)` 找不到时返回 `-1`，继续 `get(-1)` 会索引越界。它通过 `equals()` 判断元素相等；类没有重写 `equals()` 时，默认通常比较是否为同一个对象，而不是字段内容是否相同。

使用 `indexOf()` 前要明确：查找的是同一对象还是业务上相同的对象，以及找不到时应该新增、返回空结果还是抛异常。

## 4. 静态可变状态会污染测试

`static` 字段属于类，同一 JVM 中的所有对象和测试共享它：

```java
private static final List<ProductModel> productList = new ArrayList<>();
```

测试 A 写入的数据不会随测试方法结束自动清空，因此测试 B 可能因残留数据失败。典型信号：

- 单独运行通过，整组运行失败；
- 改变测试顺序后结果变化。

常见解决方式：取消全局静态仓储、为每个测试创建独立依赖，或在 `@BeforeEach` 中显式重置。测试不应依赖执行顺序。

## 5. `BigDecimal` 金额规则

### 精确创建

```java
new BigDecimal("19.80") // 推荐
new BigDecimal(19.80)   // 不推荐：double 误差已经产生
```

### 两种相等语义

```java
new BigDecimal("2.0").equals(new BigDecimal("2.00"))      // false
new BigDecimal("2.0").compareTo(new BigDecimal("2.00")) // 0
```

- `equals()`：数值和小数位数（scale）都相同；
- `compareTo()`：只比较数值大小。

```java
price.compareTo(BigDecimal.ZERO) < 0  // 负数
price.compareTo(BigDecimal.ZERO) == 0 // 零
```

只关心金额数值时使用 `compareTo()`；表示精度也是业务契约时才使用 `equals()`。计算过程中不要随意截断，结算或展示时再按明确规则 `setScale(scale, roundingMode)`。

## 6. Maven 的核心模型

`pom.xml` 保存依赖和构建声明，不保存依赖 JAR。Maven 下载缺失构件到本地仓库，再通过 classpath 参与编译和测试。

```text
pom.xml
  → 远程仓库下载缺失构件
  → ~/.m2/repository 本地缓存
  → target/classes 主代码字节码
  → target/test-classes 测试字节码
  → target/surefire-reports 测试报告
```

`mvn clean test` 的主要过程：

```text
clean → resources → compile → testResources → testCompile → test
```

- `clean` 删除 `target`，不会删除 `.m2`；
- Surefire 负责发现和运行 JUnit 测试；
- `mvn -version` 还要检查 Maven 实际使用的 Java 版本；
- IDEA 适合快速运行局部测试，Maven用于 IDE 外、团队和 CI 的统一构建；
- IDEA 通过但 Maven 失败，通常说明 JDK、依赖、目录或构建配置不一致。

## 7. `.gitignore` 只忽略未跟踪文件

文件已经提交后，再加入 `.gitignore` 仍会被 Git 跟踪。停止跟踪但保留本地文件：

```powershell
git rm --cached -r .idea
```

通常忽略构建产物、IDE 个人状态、日志、缓存、密钥和私人配置；不应忽略源码、测试、`pom.xml` 和团队需要的构建配置。

## 8. Git 推送失败要按错误分类

| 错误特征 | 优先排查 |
|---|---|
| `Could not connect` / 443 超时 | 网络、代理、防火墙、TCP 连接 |
| `Authentication failed` / `403` | 凭据、Token、仓库权限 |
| `non-fast-forward` | 远程存在本地尚未合并的提交 |
| `rejected` / branch protection | 保护分支、CI、服务端策略 |
| `repository not found` | 地址错误、仓库不存在或无权限 |

诊断顺序：确认本地提交 → 检查分支和远程地址 → 读取完整错误 → 判断网络、认证、历史冲突或服务端规则 → 只修复证据指向的问题。

## 9. 自测

1. 为什么 `assertThrows` 可能因错误原因通过？
2. 为什么字段为 `null` 不代表 `this` 为 `null`？
3. `indexOf()` 使用什么判断元素相等？
4. 为什么静态集合会使测试结果依赖执行顺序？
5. `BigDecimal("2.0")` 与 `BigDecimal("2.00")` 在两种比较中有何区别？
6. `pom.xml`、`.m2`、`target/classes` 和 Surefire 报告分别保存什么？
7. IDEA 测试通过后，为什么仍需要 Maven 构建？
8. 为什么把已提交的 `.idea` 写进 `.gitignore` 还不够？

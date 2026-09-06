# Spring、JDBC 与 MySQL 后端核心

## 1. 从网页请求到数据库

Java 后端最基本的工作，是接收外部请求、执行规则、读写数据库，再把结果返回：

```text
客户端 JSON
→ Controller（HTTP 接口）
→ Service（业务流程）
→ Repository（持久化抽象）
→ JDBC（执行 SQL）
→ MySQL（保存数据）
```

分层的本质是职责分离。HTTP 格式、业务规则和 SQL 是三类不同的变化，混在一个类里会导致任何修改都牵动整个程序。

## 2. JDBC 是什么

JDBC（Java Database Connectivity）是 Java 访问关系型数据库的标准接口。Java 规定统一的编程方式，具体数据库厂商提供驱动实现。

JDBC 中最重要的对象：

| 对象 | 作用 |
|---|---|
| `DataSource` | 提供数据库连接，通常还管理连接池 |
| `Connection` | 代表一次数据库连接和事务上下文 |
| `PreparedStatement` | 保存预编译 SQL，并安全绑定参数 |
| `ResultSet` | 保存查询返回的结果行 |

基本过程：

```java
try (Connection connection = dataSource.getConnection();
     PreparedStatement statement = connection.prepareStatement(
             "SELECT code, name FROM product WHERE code = ?")) {

    statement.setString(1, code);

    try (ResultSet result = statement.executeQuery()) {
        if (result.next()) {
            String name = result.getString("name");
        }
    }
}
```

JDBC 不是数据库，也不是 ORM。它是 Java 与数据库通信的标准通道；SQL 仍然由程序员明确编写。

## 3. MySQL Connector/J 做什么

JDBC 只定义接口，MySQL Connector/J 是 MySQL 官方提供的 JDBC 驱动。它负责把 JDBC 调用转换成 MySQL 能理解的网络协议，并把 MySQL 返回的数据转换成 JDBC 结果。

```xml
<dependency>
    <groupId>com.mysql</groupId>
    <artifactId>mysql-connector-j</artifactId>
    <scope>runtime</scope>
</dependency>
```

`runtime` 表示业务源码通常只依赖 JDBC 标准接口，驱动主要在程序运行时负责连接 MySQL。

连接地址示例：

```properties
spring.datasource.url=jdbc:mysql://127.0.0.1:3306/shop
spring.datasource.username=${DB_USERNAME}
spring.datasource.password=${DB_PASSWORD}
```

连接地址可拆成：

```text
jdbc:mysql:// 主机 : 端口 / 数据库名
```

- `127.0.0.1` 表示本机；
- MySQL 默认端口是 `3306`；
- 最后一段指定要访问的数据库；
- 用户名和密码使用环境变量注入，不能提交到 Git。

## 4. Spring JDBC Starter 做什么

```xml
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-jdbc</artifactId>
</dependency>
```

它把 Spring 的数据库基础设施引入项目。Spring Boot 读取 `spring.datasource.*` 配置后，会自动创建 `DataSource`，并将它交给需要的 Bean。

两项依赖职责不同：

```text
spring-boot-starter-jdbc：Spring 侧的连接、事务和 JDBC 支持
mysql-connector-j：MySQL 侧的具体驱动实现
```

只有 Starter 而没有 MySQL 驱动，程序不知道怎样和 MySQL 通信；只有驱动而没有 Starter，也可以手写原生 JDBC，但失去 Spring Boot 的自动配置和统一管理。

## 5. IoC、Bean 与依赖注入

IoC（控制反转）表示对象不再自己创建全部依赖，而由 Spring 容器统一创建、保存和装配。

```java
@Service
public class ProductService {
    private final ProductRepository repository;

    public ProductService(ProductRepository repository) {
        this.repository = repository;
    }
}
```

这里 Service 没有 `new JdbcProductRepository()`。Spring 找到 Repository 的实现并通过构造方法注入。

Bean 是被 Spring 容器管理的对象。常见组件注解：

- `@RestController`：HTTP 接口组件；
- `@Service`：应用服务组件；
- `@Repository`：数据访问组件，并表明它属于持久化边界；
- `@Component`：通用组件。

推荐构造器注入，因为依赖明确、字段可以是 `final`、对象创建后就是完整状态，而且测试时容易手动传入替代实现。

## 6. Controller：HTTP 边界

Controller 只负责协议适配：路径、方法、参数、JSON 和响应。

```java
@RestController
@RequestMapping("/products")
public class ProductController {
    private final ProductService service;

    public ProductController(ProductService service) {
        this.service = service;
    }

    @PostMapping
    public void create(@RequestBody CreateProductRequest request) {
        service.create(request);
    }

    @GetMapping("/{code}")
    public ProductResponse find(@PathVariable String code) {
        return service.find(code);
    }
}
```

- `@RequestMapping` 定义公共路径；
- `@PostMapping` 表示新增；
- `@GetMapping` 表示查询；
- `@RequestBody` 把 JSON 转换成 Java 对象；
- `@PathVariable` 读取路径变量；
- `@RequestParam` 读取 `?code=...` 形式的查询参数。

Controller 不应编写 SQL，也不应堆放产品编码解析等核心业务逻辑。

## 7. DTO：接口数据不是领域对象

DTO（Data Transfer Object）用于描述接口输入或输出的数据形状。

```java
public record CreateProductRequest(
        String productCode,
        ProductName productName,
        Brand brand,
        Unit unit) {}
```

`record` 适合不可变的数据载体：编译器自动生成构造方法、访问方法、`equals`、`hashCode` 和 `toString`。

请求 DTO 与领域对象分开的原因：

- 客户端只能填写允许输入的字段；
- 缸径、行程等派生值可以由服务端计算；
- 领域对象不必为了 JSON 反序列化开放无参构造和任意 setter；
- 接口变化与内部业务模型变化可以相互隔离。

可靠的方向是：

```text
不可信 JSON → 请求 DTO → 校验与转换 → 合法领域对象
```

## 8. Service：组织一次业务用例

Service 负责把输入转换成领域对象，并协调仓储完成一个完整动作。

```java
@Service
public class ProductService {
    private final ProductRepository repository;

    public void create(CreateProductRequest request) {
        Product product = new Product(
                request.productCode(),
                request.productName(),
                request.brand(),
                request.unit());
        repository.save(product);
    }
}
```

Service 不负责 HTTP 状态码，也不应依赖 MySQL 的具体类。它表达的是“创建商品”“按编码查询”等应用用例。

领域对象自身负责始终成立的规则，例如编码格式、派生字段和不可变身份。Service 负责多个对象或步骤之间的协调。

## 9. Repository：隔离业务与数据库

Repository 用接口表达业务层需要的持久化能力：

```java
public interface ProductRepository {
    void save(Product product);
    Optional<Product> findByCode(String code);
}
```

JDBC 实现类负责 SQL：

```java
@Repository
public class JdbcProductRepository implements ProductRepository {
    private final DataSource dataSource;

    public JdbcProductRepository(DataSource dataSource) {
        this.dataSource = dataSource;
    }
}
```

这体现依赖倒置：业务层依赖抽象能力，数据库层实现该能力。将来改用 MyBatis、JPA 或内存实现时，Service 的业务流程可以保持稳定。

并非每个类都必须有接口。只有当接口能隔离外部技术、支持替换实现或形成清晰边界时，它才有实际价值。

## 10. `PreparedStatement` 与参数化 SQL

错误方式：

```java
String sql = "SELECT * FROM product WHERE code = '" + code + "'";
```

正确方式：

```java
String sql = "SELECT code, name FROM product WHERE code = ?";
PreparedStatement statement = connection.prepareStatement(sql);
statement.setString(1, code);
```

参数化 SQL 将“SQL 结构”和“数据值”分开，主要作用是：

- 防止 SQL 注入；
- 自动处理引号和特殊字符；
- 明确 Java 类型如何传给数据库；
- 让数据库有机会复用执行计划。

SQL 参数编号从 `1` 开始。写入 `NULL` 时要明确数据库类型：

```java
statement.setNull(8, Types.VARCHAR);
```

`executeUpdate()` 用于 `INSERT`、`UPDATE`、`DELETE`，返回受影响行数；`executeQuery()` 用于 `SELECT`，返回 `ResultSet`。

## 11. Java 对象与数据库行的双向映射

保存时需要把对象拆成列：

```text
Product 对象 → getter/枚举名称 → SQL 参数 → 数据库行
```

查询时需要把列重新组合成对象：

```text
数据库行 → ResultSet → 类型转换 → Product 对象
```

枚举可通过名字保存：

```java
statement.setString(1, product.getBrand().name());
Brand brand = Enum.valueOf(Brand.class, result.getString("brand"));
```

这种映射要求 Java 枚举名与数据库值严格一致。随意重命名枚举会破坏历史数据，因此持久化值属于长期数据契约。

如果缸径和行程能从产品编码推导，构造领域对象时统一解析，可避免多个入口各写一套解析逻辑。但同时保存原始编码和派生列会产生冗余，修改编码时必须同步更新，否则数据会自相矛盾。

## 12. 资源必须关闭

数据库连接数量有限。忘记关闭连接会逐渐耗尽连接池，使后续请求无法访问数据库。

```java
try (Connection connection = dataSource.getConnection();
     PreparedStatement statement = connection.prepareStatement(sql);
     ResultSet result = statement.executeQuery()) {
    // 使用资源
}
```

`try-with-resources` 会在正常结束或发生异常时自动关闭资源，并按创建的相反顺序关闭。可关闭资源应尽量在括号中声明，避免遗漏异常路径。

## 13. `Optional` 与查无数据

按唯一编码查询只有两种正常结果：找到一个对象，或者没有找到。

```java
Optional<Product> findByCode(String code);
```

Repository 返回 `Optional.empty()` 表示 SQL 正常执行，但没有匹配行。Service 再决定业务含义：

```java
return repository.findByCode(code)
        .orElseThrow(() -> new ProductNotFoundException("商品不存在"));
```

必须区分：

- `Optional.empty()`：查询成功，但数据不存在；
- `SQLException`：数据库访问失败；
- 非法参数异常：输入本身不合法。

`Optional` 主要用于方法返回值，不应被当成所有 `null` 的替代品。

## 14. 异常翻译与统一响应

底层技术异常不应穿透所有层：

```text
SQLException
→ Repository 包装为 DatabaseException
→ 全局异常处理器转换为 HTTP 响应
```

包装时保留原始原因：

```java
throw new DatabaseException("数据库写入失败", cause);
```

`@RestControllerAdvice` 可以集中处理 Controller 抛出的异常：

```java
@ExceptionHandler(ProductNotFoundException.class)
public ResponseEntity<ErrorResponse> handleNotFound(
        ProductNotFoundException exception) {
    return ResponseEntity.status(404)
            .body(new ErrorResponse(404, "Not Found", exception.getMessage()));
}
```

这样 Controller 不需要重复 `try/catch`。对外只返回稳定、必要的信息；SQL、密码、路径和堆栈只留在安全的服务端日志中。

常见语义：输入非法为 400，资源不存在为 404，唯一键冲突为 409，未恢复的数据库故障为 500。

## 15. 数据库约束是最后防线

Java 校验能尽早给出清晰提示，但无法代替数据库约束。两个并发请求可能同时检查到“编码不存在”，然后同时插入；最终只能由数据库 `UNIQUE` 可靠阻止重复。

```text
前端校验：改善输入体验
Java 校验：表达业务规则
数据库约束：保护最终数据
```

三层校验是不同防线，不是无意义重复。`PRIMARY KEY`、`UNIQUE`、`NOT NULL`、`CHECK` 和 `FOREIGN KEY` 应根据数据事实设计。

## 16. 事务决定批量写入如何失败

一次写入多条记录时，必须先规定失败语义：

- 全部成功或全部回滚；
- 保存到第一条错误为止；
- 错误行失败，其余继续。

JDBC 默认自动提交时，每次成功的 `executeUpdate()` 都可能立即提交。因此循环中第二条失败，第一条通常已经保留，后面的语句不会执行。

要求全部成功或失败时：

```java
connection.setAutoCommit(false);
try {
    // 多次写入
    connection.commit();
} catch (Exception exception) {
    connection.rollback();
    throw exception;
}
```

事务的边界应对应一个不可拆分的业务动作，而不是机械地包住越多代码越好。

如果业务规则依赖输入顺序，应使用保证顺序的集合：`List` 或 `LinkedHashMap`。`HashMap` 不保证插入顺序，不能表达“错误之前和错误之后”。

## 17. 领域对象的身份与可变性

对象可以允许修改普通属性，但身份字段需要特别谨慎。若商品编码同时是 `Map` 的键、数据库唯一键和对象字段，直接修改对象字段可能造成三处身份不一致。

常见选择：

1. 编码不可变，输错时删除并重新创建；
2. 允许修改，但必须由统一业务方法同时检查冲突、更新索引和数据库；
3. 使用稳定的数据库主键作为内部身份，把可修改编码作为受唯一约束的业务字段。

`final` 表示字段在对象构造完成后不能重新赋值，适合表达对象生命周期内不应变化的核心身份或派生事实。它限制的是引用重新赋值，不代表引用指向的对象一定不可变。

## 18. 空对象与持久化身份

Java 内存中创建一个有名称但没有商品的目录很容易；但如果数据库只有商品表，空目录没有任何记录承载名称，程序重启后它就不存在。

需要独立存在、允许为空、以后还能被修改的业务概念，通常需要独立表：

```text
catalog(id, name)
product(id, catalog_id, code, ...)
```

`product.catalog_id` 通过外键指向目录。Java 中“对象创建成功”和数据库中“状态持久存在”不是同一件事。

## 19. 最小知识总图

```text
Spring Boot
├─ 根据配置自动创建 DataSource
├─ 扫描 Controller、Service、Repository 成为 Bean
└─ 通过构造器完成依赖注入

Web 层
├─ JSON ↔ DTO
├─ HTTP 路由与状态码
└─ 统一异常响应

业务层
├─ DTO → 领域对象
├─ 业务规则与用例编排
└─ 只依赖 Repository 接口

持久化层
├─ DataSource 获取 Connection
├─ PreparedStatement 执行参数化 SQL
├─ ResultSet 映射领域对象
└─ SQLException 翻译为应用异常

MySQL
├─ 长期保存数据
├─ 唯一、非空、引用等约束
└─ 事务保证约定的失败结果
```

真正掌握这条链路，不是记住注解，而是能解释：每层为什么存在、数据怎样流动、依赖由谁创建、SQL 怎样执行、结果怎样还原成对象，以及失败时由哪一层负责处理。

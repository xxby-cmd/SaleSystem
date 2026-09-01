# Java 领域对象、金额校验与 JUnit 基础

## 1. 类、对象与字段

- **类**是对象的结构和行为定义，例如 `com.xxby.Product.ProductModel`、`com.xxby.Quotation.QuotationItem`。
- **对象**是根据类创建的具体实例。
- 字段保存对象状态；方法读取、修改或使用这些状态。
- Java 普通变量使用小驼峰命名，如 `productCode`；类名使用大驼峰命名，如 `com.xxby.Product.ProductModel`。

```java
private String productCode;
private int quantity;
private BigDecimal price;
```

## 2. 构造器与 Setter

构造器用于创建对象并设置初始状态：

```java
import com.xxby.Quotation.QuotationItem;

QuotationItem item = new QuotationItem("SAU32X100", 3, new BigDecimal("19.80"));
```

Setter 用于创建对象后修改字段：

```java
item.setQuantity(5);
```

如果允许先创建空对象再补充属性，就要接受对象暂时处于“不完整状态”：

```java
import com.xxby.Quotation.QuotationItem;

QuotationItem item = new QuotationItem();
```

此时字段可能是默认值：引用类型为 `null`，`int` 为 `0`。因此必须明确：对象未填写完整时，哪些方法可以调用，哪些操作应被拒绝。

## 3. `this` 与参数同名

方法参数和字段同名时：

```java
this.price = price;
```

- `price` 表示方法参数。
- `this.price` 表示当前对象的字段。

只写 `price = BigDecimal.ZERO` 修改的只是局部参数，不会修改对象字段。

## 4. 输入校验与校验顺序

业务对象应拒绝明显非法数据：产品编码不能为 `null`、空字符串或纯空格；数量必须大于 `0`；价格不能为 `null` 或小于 `0`；零价格可以表示赠品。

必须先检查 `null`，再调用对象方法：

```java
if (price == null) {
    throw new IllegalArgumentException("价格不能为空");
}
if (price.compareTo(BigDecimal.ZERO) < 0) {
    throw new IllegalArgumentException("价格不能小于0");
}
```

否则在 `null` 上调用 `compareTo()`、`trim()` 等方法会产生 `NullPointerException`，而不是清楚的业务错误。

## 5. 字符串标准化

用户输入的产品编码可以先标准化，再保存和比较：

```java
String normalized = productCode.trim().toUpperCase(Locale.ROOT);
```

- `trim()`：删除首尾空格。
- `isEmpty()`：判断长度是否为零。
- `isBlank()`：判断是否为空或只包含空白。
- `toUpperCase(Locale.ROOT)`：稳定地转换为大写，不依赖本机语言环境。

先标准化再检查重复，可以让 `" sau32x100 "` 和 `"SAU32X100"` 被视为同一个编码。

## 6. `null`、零和未填写

以下状态不能混为一谈：

```text
price == null   尚未填写价格
price == 0      已明确填写零元，可表示赠品
price < 0       非法价格
```

同样，空对象中的 `quantity == 0` 可能表示“尚未填写”，而业务输入 `0` 表示“非法数量”。如果允许空对象存在，就需要统一规定不完整状态的处理方式。

实用原则：

> 草稿可以暂时不完整，但信息不完整时不能执行正式计算或提交。

不要用返回 `0` 掩盖所有缺失字段，否则真正的零元赠品与未填写价格无法区分。

## 7. 为什么金额使用 `BigDecimal`

`double` 使用二进制浮点数，无法精确表示很多十进制小数，因此不适合直接表示业务金额。

金额应通过字符串创建：

```java
BigDecimal price = new BigDecimal("19.80");
```

不要使用 `new BigDecimal(19.80)`，因为 `19.80` 会先变成不精确的 `double`。

常用操作：

```java
price.compareTo(BigDecimal.ZERO);             // 比较大小
price.multiply(BigDecimal.valueOf(quantity)); // 乘法
```

`BigDecimal` 是不可变对象，`multiply()`、`setScale()` 等方法会返回新对象，不会修改原对象。

## 8. 内部精度与显示精度

内部计算可以保留输入的多位小数：

```text
单价：19.999
数量：3
内部小计：59.997
```

显示给用户时再保留两位：

```java
BigDecimal displayAmount = amount.setScale(2, RoundingMode.HALF_UP);
```

需要区分：

- **计算精度**：内部真实保存和参与计算的精度。
- **显示精度**：界面或报表展示的小数位数。
- **舍入规则**：例如 `HALF_UP`，必须由业务明确指定。

显示两位不应反过来修改内部原始金额。

## 9. 重复检查与对象引用

集合中保存对象时，保存的是对象引用。如果列表中的元素就是 `this`，通过列表找到它再赋值与直接修改 `this` 操作的是同一个对象。

静态集合：

```java
import com.xxby.Product.ProductModel;

private static final List<ProductModel> productList = new ArrayList<>();
```

会被所有对象和测试共享。它适合小实验展示重复检查，但长期会带来状态残留、测试互相影响和职责混杂。正式系统通常由独立的仓库或服务管理产品集合。

## 10. 异常与控制台输出

非法参数可以抛出：

```java
throw new IllegalArgumentException("数量必须大于0");
```

异常信息应与真实规则一致。例如允许零价格时，应写“价格不能小于0”，而不是“价格必须大于0”。

业务对象主要负责保存数据、校验和计算。`System.out.println()` 属于展示行为，通常由 `Main`、界面或接口层负责。这样同一份业务逻辑以后可以复用于控制台、Web API 和自动测试。

## 11. Maven 工程基础

Maven 标准目录：

```text
src/main/java   正式业务代码
src/test/java   测试代码
pom.xml         项目与依赖配置
target          Maven 生成的编译结果
```

常用命令：

```bash
mvn compile
mvn test
```

- `mvn compile`：编译正式代码。
- `mvn test`：编译正式代码和测试代码，再运行测试。
- `BUILD SUCCESS` 只说明本次 Maven 流程成功。
- 必须同时查看 `Tests run`；`Tests run: 0` 表示没有执行任何测试，不能说明业务正确。

## 12. JUnit 5 最小知识

JUnit 测试方法使用 `@Test`：

```java
@Test
void calculatesSubtotal() {
    // 准备数据、执行行为、断言结果
}
```

常用断言：

```java
assertEquals(expected, actual);
assertThrows(IllegalArgumentException.class, () -> operation());
```

测试不是测试工程师的专属工作。后端开发通常需要为自己负责的核心规则编写单元测试。

当前小模块只需覆盖：正常金额、零元赠品、非法数量和价格、空对象补齐字段后的行为，以及重复产品编码规则。

## 13. 适合学习项目的开发节奏

学习项目不需要一次达到生产级，也不能完全不验证就持续增加功能：

```text
实现一个最小功能
    -> 验证正常路径
    -> 编写少量核心测试
    -> 只修复测试暴露的阻塞问题
    -> 继续下一个功能
```

出现以下情况时再暂停开发并维护代码：同类错误反复出现；修改一处导致多个功能损坏；旧结构阻止新功能；测试互相影响；自己已经无法解释原有代码。

核心原则：

> 测试服务于开发，重构服务于解决真实问题；既不追求过早完美，也不把已知的关键错误无限推迟。

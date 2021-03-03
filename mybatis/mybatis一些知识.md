# mybatis
## JDBC 
* JDBC就是 Java 数据库连接,含义: Java语言操作数据库
* 因为数据库差异过大，SUN 公司提供的数据库操作接口规范 API 称之为 JDBC, 各厂商提供的自家数据库 API 接口称之为 驱动
## MyBatis
* Mybatis 是一款优秀的 ORM（持久层）框架，使用 Java 语言 编写
## MyBatis与JDBC
* 之前操作数据库 需要手动装在驱动，创建链接，结果解析，关闭资源
* Mybatis 针对 JDBC 中重复操作做了封装, 同时扩展并优化部分功能
## MyBatis 名词解释
* SqlSession: 负责执行select,insert,update,delete 操作,同时负责获取映射器和事务管理，封装了JDBC的基础操作
* SqlSessionFactory: 负责创建SqlSession
* SqlSessionFactoryBuilder: 负责创建 SqlSessionFactory类的构造器类
* Configuration: MyBatis 配置类
* MappedStatement:  是保存 SQL 语句的数据结构, 其中的类属性都是由解析 .xml 文件中的 SQL 标签转化而成
* Executor: SqlSession 对象对应一个 Executor, Executor 对象作用于 增删改查方法 以及 事务、缓存 等操作
* ParameterHandler: 参数处理器
* StatementHandler： Statement 处理器
* ResultSetHandler：将JDBC 返回的数据进行解析，并包装成java类
* Interceptor：拦截器
## MyBatis 架构图
![mybatis架构图，来源掘金](https://p3-juejin.byteimg.com/tos-cn-i-k3u1fbpfcp/d48fb3b5d8f9476a999312f6e7e22f67~tplv-k3u1fbpfcp-zoom-1.image) <br/>
* 配置解析 在 Mybatis 初始化时, 会加载 Mybatis-config.xml 文件中的配置信息, 解析后的配置信息会 转换成 Java 对象添加到 Configuration 对象
* SQL 解析 Mybatis 提供出了动态 SQL, 加入了许多判断循环型标签, 比如 : if、where、foreach、set 等, 帮助开发者节约了大量的 SQL 拼写时间
  SQL 解析模块的作用就是将 Mybatis 提供的动态 SQL 标签解析为带占位符的 SQL 语句, 并在后期将实参对占位符进行替换
* SQL 执行 ：SQL 的执行过程涉及几个比较重要的对象, Executor、StatementHandler、ParameterHandler、ResultSetHandler

Executor 负责维护 一级、二级缓存以及事务提交回滚操作, 举个查询的例子, 查询请求会由 Executor 交给 StatementHandler 完成

StatementHandler 通过 ParameterHandler 完成 SQL 语句的实参绑定, 通过 java.sql.Statement 执行 SQL 语句并拿到对应的 结果集映射

最后交由 ResultSetHandler 对结果集进行解析, 将 JDBC 类型转换为程序自定义的对象

插件
插件模块是 Mybatis 提供的一层扩展, 可以针对 SQL 执行的四大对象进行 拦截并执行自定义插件 
## SQL 解析
Mybatis 创建SqlSessionFactory 时候会去解析mybatis-config.xml,然后解析configuration 标签下的子标签，解析mapping.xml,
具体的insert 标签等会被解析成MappedStatement 其余的会被解析成其他Java对象
## MappedStatement 类
```java
public final class MappedStatement {
    // 资源名称 比如userMapper.xml
    private String resource;
    // 全局配置
    private Configuration configuration;
    // 标签节点命名 id
    private String id;
    // sql 返回最大行数
    private Integer fetchSize;
    // 超时时间
    private Integer timeout;
    //statement 类型
    private StatementType statementType;
    // resultSet 类型
    private ResultSetType resultSetType;
    // sql 语句
    private SqlSource sqlSource;
    private Cache cache;
    private ParameterMap parameterMap;
    private List<ResultMap> resultMaps;
    private boolean flushCacheRequired;
    private boolean useCache;
    private boolean resultOrdered;
    private SqlCommandType sqlCommandType;
    private KeyGenerator keyGenerator;
    private String[] keyProperties;
    private String[] keyColumns;
    private boolean hasNestedResultMaps;
    private String databaseId;
    private Log statementLog;
    private LanguageDriver lang;
    private String[] resultSets;
}   
```

Mapper 接口的存储与实现
在平常我们写的 SSM 框架中, 定义了 Mapper 接口与 .xml 对应的 SQL 文件, 在 Service 层直接注入 xxxMapper 就可以了
也没有看到像 JDBC 操作数据库的操作, Mybatis 在中间是如何为我们省略下这些重复繁琐的操作呢
这里使用 Mybatis 源码中的测试类进行验证, 首先定义 Mapper 接口, 省事直接注解定义 SQL

这里使用 SqlSession 来获取 Mapper 操作数据库, 测试方法如下

创建 SqlSession
#1 从 SqlSessionFactory 中打开一个 新的 SqlSession
获取 Mapper 实例
#2 就存在一个疑问点, 定义的 AutoConstructorMapper 明明是个接口, 为什么可以实例化为对象?
动态代理方法调用
#3 通过创建的对象调用类中具体的方法, 这里具体聊一下 #2 操作
SqlSession 是一个接口, 有一个 默认的实现类 DefaultSqlSession, 类中包含了 Configuration 属性
Mapper 接口的信息以及 .xml 中 SQL 语句是在 Mybatis 初始化时添加 到 Configuration 的 MapperRegistry 属性中的

#2 中的 getMapper 就是从 MapperRegistry 中获取 Mapper
看一下 MapperRegistry 的类属性都有什么

config 为 保持全局唯一 的 Configuration 对象引用
knownMappers 中 Key-Class 是 Mapper 对象, Value-MapperProxyFactory 是通过 Mapper 对象衍生出的 Mapper 代理工厂
再看一下 MapperProxyFactory 类的结构信息

mapperInterface 属性是 Mapper 对象的引用, methodCache 的 key 是 Mapper 中的方法, value 是 Mapper 解析对应 SQL 产生的 MapperMethod

📖 Mybatis 设计 methodCache 属性时使用到了 懒加载机制, 在初始化时不会增加对应 Method, 而是在 第一次调用时新增


MapperMethod 运行时数据如下, 比较容易理解

通过一个实际例子帮忙理解一下 MapperRegistry 类关系, Mapper 初始化第一次调用的对象状态, 可以看到 methodCache 容量为0

我们目前已经知道 MapperRegistry 的类关系, 回头继续看一下第二步的 MapperRegistry#getMapper() 处理步骤

核心处理在 MapperProxyFactory#newInstance() 方法中, 继续跟进

MapperProxy 继承了 InvocationHandler 接口, 通过 newInstance() 最终返回的是由 Java Proxy 动态代理返回的动态代理实现类
看到这里就清楚了步骤二中接口为什么能够被实例化, 返回的是 接口的动态代理实现类
Mybatis Sql 的执行过程
根据 Mybatis SQL 执行流程图进一步了解

大致可以分为以下几步操作:

📖 在前面的内容中, 知道了 Mybatis Mapper 是动态代理的实现, 查看 SQL 执行过程, 就需要紧跟实现了 InvocationHandler 的 MapperProxy 类

执行增删改查
@Select(" SELECT * FROM SUBJECT WHERE ID = #{id}")
PrimitiveSubject getSubject(@Param("id") final int id);
复制代码
我们以上述方法举例, 调用方通过 SqlSession 获取 Mapper 动态代理对象, 执行 Mapper 方法时会通过 InvocationHandler 进行代理

在 MapperMethod#execute 中, 根据 MapperMethod -> SqlCommand -> SqlCommandType 来确定增、删、改、查方法

📖 SqlCommandType 是一个枚举类型, 对应五种类型 UNKNOWN、INSERT、UPDATE、DELETE、SELECT、FLUSH


参数处理
查询操作对应 SELECT 枚举值, if else 中判断为返回值是否集合、无返回值、单条查询等, 这里以查询单条记录作为入口
Object param = method.convertArgsToSqlCommandParam(args);
result = sqlSession.selectOne(command.getName(), param);
复制代码



📖 这里能够解释一个之前困扰我的问题, 那就是为什么方法入参只有单个 @Param("id"), 但是参数 param 对象会存在两个键值对

继续查看 SqlSession#selectOne 方法, sqlSession 是一个接口, 具体还是要看实现类 DefaultSqlSession

因为单条和查询多条以及分页查询都是走的一个方法, 所以在查询的过程中, 会将分页的参数进行添加

执行器处理
在 Mybatis 源码中, 创建的执行器默认是 CachingExecutor, 使用了装饰者模式, 在类中保持了 Executor 接口的引用, CachingExecutor 在持有的执行器基础上增加了缓存的功能

delegate.query 就是在具体的执行器了, 默认 SimpleExecutor, query 方法统一在抽象父类 BaseExecutor 中维护

BaseExecutor#queryFromDatabase 方法执行了缓存占位符以及执行具体方法, 并将查询返回数据添加至缓存

BaseExecutor#doQuery 方法是由具体的 SimpleExecutor 实现

执行 SQL
因为我们 SQL 中使用了参数占位符, 使用的是 PreparedStatementHandler 对象, 执行预编译SQL的 Handler, 实际使用 PreparedStatement 进行 SQL 调用

返回数据解析
将 JDBC 返回类型转换为 Java 类型, 根据 resultSets 和 resultMap 进行转换




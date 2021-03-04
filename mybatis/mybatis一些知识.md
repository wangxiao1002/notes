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






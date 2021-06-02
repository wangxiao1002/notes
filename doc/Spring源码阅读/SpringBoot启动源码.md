## SpringBoot 优点
* 起步依赖
* 自动装配
* Actuator 监控
* 内置容器
## 启动源码
```java
public ConfigurableApplicationContext run(String... args) {

    // 1.创建并启动计时监控类

    StopWatch stopWatch = new StopWatch();

    stopWatch.start();

    // 2.声明应用上下文对象和异常报告集合

    ConfigurableApplicationContext context = null;

    Collection<SpringBootExceptionReporter> exceptionReporters = new ArrayList();

    // 3.设置系统属性 headless 的值

    this.configureHeadlessProperty();

    // 4.创建所有 Spring 运行监听器并发布应用启动事件

    SpringApplicationRunListeners listeners = this.getRunListeners(args);

    listeners.starting();

    Collection exceptionReporters;

    try {

        // 5.处理 args 参数

        ApplicationArguments applicationArguments = new DefaultApplicationArguments(args);

        // 6.准备环境

        ConfigurableEnvironment environment = this.prepareEnvironment(listeners, applicationArguments);

        this.configureIgnoreBeanInfo(environment);

        // 7.创建 Banner 的打印类

        Banner printedBanner = this.printBanner(environment);

        // 8.创建应用上下文

        context = this.createApplicationContext();

        // 9.实例化异常报告器

        exceptionReporters = this.getSpringFactoriesInstances(SpringBootExceptionReporter.class, new Class[]{ConfigurableApplicationContext.class}, context);

        // 10.准备应用上下文

        this.prepareContext(context, environment, listeners, applicationArguments, printedBanner);

        // 11.刷新应用上下文

        this.refreshContext(context);

        // 12.应用上下文刷新之后的事件的处理

        this.afterRefresh(context, applicationArguments);

        // 13.停止计时监控类

        stopWatch.stop();

        // 14.输出日志记录执行主类名、时间信息

        if (this.logStartupInfo) {

            (new StartupInfoLogger(this.mainApplicationClass)).logStarted(this.getApplicationLog(), stopWatch);

        }

        // 15.发布应用上下文启动完成事件

        listeners.started(context);

        // 16.执行所有 Runner 运行器

        this.callRunners(context, applicationArguments);

    } catch (Throwable var10) {

        this.handleRunFailure(context, var10, exceptionReporters, listeners);

        throw new IllegalStateException(var10);

    }

    try {

        // 17.发布应用上下文就绪事件

        listeners.running(context);

        // 18.返回应用上下文对象

        return context;

    } catch (Throwable var9) {

        this.handleRunFailure(context, var9, exceptionReporters, (SpringApplicationRunListeners)null);

        throw new IllegalStateException(var9);

    }

}

```
# Spring Cloud 1.x

## 回顾Spring Cloud 1.x 版本各个组件

* 注册中心 Eureka
* RPC与负载均衡 Feign和Ribbon
* 网关 Zuul
* 熔断保护 Hystrix
* 配置中心 Config

## 微服务带来的优点

* 降低耦合，单个服务负责单一功能，eg:支付，订单，可有单独团队负责，易于开发和维护
* 独立部署，缩短交付周期
* 技术选型灵活，每个服务之间并不许知道其他服务所采用的语言框架，只需要负责实现业务功能，提供Api
* 容错性更加高

## **Eureka**



主要负责服务注册和发现，也就是将微服务的元数据注册到Eureka,可以通过Eureka找到微服务，Eureka分为Server端和Client端，Client端保持连接Server 的心跳，这样Server端就可以监控各个微服务的正常。

###  **Eureka Server**

Eureka Server 提供服务注册，单独节点启动后，都会到Eureka Server 进行注册，Eureka Server中的服务注册表会存储所有可调用服务的元数据

### **Eureka Client**

Client 是Java客户端，用于简化与Server端交互，客户端同事也具备一个内置的，使用轮询负载算法的负载均衡器，在服务启动后会每隔30秒（默认周期）发送心跳到Server 端，保持和证明当前服务是可用的，如果Server 端在90秒（默认周期）未收到客户端心跳会将该服务从服务注册表中移除

### **Eureka Server的自我保护机制**

如果在15分钟内超过85%的服务节点都没有发送正常的心跳，那么这是后会判定Server端和Client端出现了网络故障，此时会有如下几种情况：

* Server 端不在从服务注册表中剔除长时间没收到心跳的服务
* Server端仍能接受新服务的注册以及服务注册表的查询，但是不会同步其他服务节点，保证了当前节点是可用的
* 当网络稳定时候，会将新的注册信息同步到其他节点中
### **与ZK作比较**
*因为本人使用的第一个注册中心是ZK*

* CAP理论 一个分布式系统不可能同时满足C（一致性）、A（可用性）和P（分区容错性） 

* ZK 保证了C P

* Eureka 保证金AP

* zk 当节点失去网络信号后会重新选举Master，导致整个注册服务瘫痪不可用

## **Ribbon & Feign**

在Spring Cloud中常见的服务调用方式就是Ribbon+ RestTemplate 和 Feign

### 负载均衡

* 将请求分发到多个可用服务器，数据库，应用等的功能，提高可靠性

* 负载均衡常用策略：随机（Random），轮询（RoundRobin）,一致性Hash( ConsistentHash ),Hash,加权（Weighted）,

* Ribbon默认采用轮询
### RestTemplate

Spring提供的访问RESTful服务模板，在使用Ribbon+RestTemplate时候，Ribbon需要自己构建Http请求，模拟Http请求然后使用RestTemplate发送给其他服务，

### Feign

Feign中继承了Ribbon,Feign是一个声明式的web客户端接口，只需要在本地写接口添加注解，就可以调用。
## **Zuul**
服务网关是提供了统一入口
### 功能
* 服务鉴权，过滤
* 路由带路将服务根据Url 转发到其他服务
### Filters
* Zuul 是现实一些Fitlers,类比Serverlet中Fiter

* Filters中通信是使用Request Context 进行数据传递

* Filter 类型 PRE 前置，ROUTING 路由 POST 后置 ERROR 错误

## **Hystrix**
Hystrix 是一个用于处理分布式系统中延迟和容错的开源组件
### 服务熔断
当调用某个服务不可用或者响应事件太长时候，会对服务进行将级、进而熔断个微服务的调用，快速返回错误的响应信息。<br/>
Spring Cloud 的中默认使用Hystirx 实现熔断机制，Hystirx 会监控微服务的调用状况，当服务始备到一定的阈值，默认是5秒内20此调用时被就会启动熔断机制。熔断机制的注解是@HystirxCommand
### Hytrix 监控与断路器
我们只需要在服务接口上添加Hystrix标签，就可以实现对这个接口的监控和断路器功能。
Hystrix Dashboard监控面板，提供了一个界面，可以监控各个服务上的服务调用所消耗的时间等。需要添加注解 @EnableHystrixDashboard
## **Config**
### 配置文件和ConfigServer
配置文件存储在远端Git（比如GitHub，Gitee等仓库），config-server从远端Git拉取配置文件，并保存到本地Git。
本地Git和config-server的交互是双向的，因为当远端Git无法访问时，会从本地Git获取配置文件。
config-client（即各个微服务），从config-server拉取配置文件。
Config Server：提供配置文件的存储、以接口的形式将配置文件的内容提供出去。
Config Client：通过接口获取数据、并依据此数据初始化自己的应用。
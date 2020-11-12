# simpleSpring 
学习Spring的IOC,DI。手动实现大致功能，方便理解和以后的阅读源码 
## IOC 
* Inversion of Control 即控制反转，其意思是之前由客代码来创建的对象交由 IOC 容器来进行控制，对象的创建，初始化以及后面的管理都由 IOC 完成。
* IOC 的设计优势解耦
* 实现设计模式：工厂设计模式
### 手动实现思路
* Bean 定义, Bean 里面肯定包含Class(创建时候通过反射获取)，生命周期，创建销毁方法,对于工厂设计模式还需要提供工厂信息，创建该类的工厂名 (factoryName) 和方法名(methodName
* Bean 创建 class.newInstance()
* Bean 工厂 提供 getBeanByName(String name)/getBeanByType(Class clazz) 来获取bean,以及注册Bean
* 类图如下：
![Bean关系图](https://img-blog.csdnimg.cn/20201112164538709.png?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L3dlaXhpbl80MjA0MzAzMA==,size_16,color_FFFFFF,t_70#pic_center)
## DI
* 工厂类 实现思路全部是 class.newInstance(),或者Method method class.getMethod(bd.getStaticCreateBeanMethod()),method.invoke(clazz,null),上述方法中可以调用init,或者工厂类创建Bean的方法，
* 依赖注入 主要是赋值 （构造参数，set方法）
## IOC 与 DI 区别
* 假设一个类A类里面需要B类，A类并不想知道B的创建和使用周期，仅仅是使用，将创建/销毁过程交予其他组件来完成，这叫IOC,A类依赖于B 需要B的实例 这叫DI
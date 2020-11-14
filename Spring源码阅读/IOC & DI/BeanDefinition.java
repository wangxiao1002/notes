/**
 * bean 信息
 * @author wang xiao
 * @date Created in 16:47 2020/11/12
 */
public interface BeanDefinition {

    String SCOPE_SINGLETON = "singleton";
    String SCOPE_PROTOTYPE = "prototype";

    String SINGLETON = null;

    Class<?> getBeanClass();

    String getBeanFactory();

    String getCreateBeanMethod();

    String getStaticCreateBeanMethod();

    String getBeanFactoryCreateMethodName();

    String getBeanInitMethodName();

    String getScope();

    boolean isSingleton ();

    boolean isPrototype();

    boolean validate();

}

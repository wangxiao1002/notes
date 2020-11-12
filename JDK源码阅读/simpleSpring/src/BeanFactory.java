import java.io.IOException;

/**
 * BeanFactory
 * @author wang xiao
 * @date Created in 17:01 2020/11/12
 */
public interface BeanFactory {


    /**
     *  注册Bean
     * @author wangxiao
     * @date 17:02 2020/11/12
     * @param bd BeanDefinition
     * @param beanName beanName
     */
    void register(BeanDefinition bd, String beanName);


    /**
     *  获取bean
     * @author wangxiao
     * @date 17:04 2020/11/12
     * @param beanName beanName
     * @return java.lang.Object
     */
    Object getBean(String beanName);

    /**
     *  销毁
     * @author wangxiao
     * @date 17:05 2020/11/12
     */
    void destroyFactory() throws IOException;
}

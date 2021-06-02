/**
 * @author wang xiao
 * @date Created in 17:06 2020/11/12
 */
public interface BeanDefinitionRegistry {

    /**
     *  是否已经有beanName
     * @author wangxiao
     * @date 17:03 2020/11/12
     * @param beanName beanName
     * @return boolean
     */
    boolean containsBeanDefinition(String beanName);

    /**
     *  获取Bean 信息
     * @author wangxiao
     * @date 17:03 2020/11/12
     * @param beanName beanName
     * @return BeanDefinition
     */
    BeanDefinition getBeanDefinition(String beanName);
}

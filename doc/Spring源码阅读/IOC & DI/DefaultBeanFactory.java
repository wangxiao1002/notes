
import java.io.IOException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * @author wang xiao
 * @date Created in 17:06 2020/11/12
 */
public class DefaultBeanFactory implements BeanFactory,BeanDefinitionRegistry {

    private final Map<String,BeanDefinition> BEAN_DEF_FACTORY = new HashMap<>(8);

    private final Map<String,Object> BEAN_FACTORY = new HashMap<>(8);

    @Override
    public boolean containsBeanDefinition(String beanName) {
        return BEAN_DEF_FACTORY.containsKey(beanName);
    }

    @Override
    public BeanDefinition getBeanDefinition(String beanName) {
        return BEAN_DEF_FACTORY.get(beanName);
    }

    @Override
    public void register(BeanDefinition bd, String beanName) {
        if (Objects.isNull(bd) || Objects.isNull(beanName)){
            return;
        }
        if (!bd.validate()){
            return;
        }
        if (!containsBeanDefinition(beanName)){
            BEAN_DEF_FACTORY.put(beanName,bd);
        }
    }

    @Override
    public Object getBean(String beanName) {
        if (BEAN_FACTORY.containsKey(beanName)){
            return BEAN_FACTORY.get(beanName);
        }
        return doGetBean(beanName);
    }

    @Override
    public void destroyFactory() throws IOException {
        this.BEAN_FACTORY.clear();
        this.BEAN_DEF_FACTORY.clear();
    }



    public Object doGetBean(String beanName)  {
        if(!BEAN_FACTORY.containsKey(beanName)){
           return null;
        }

        Object instance = BEAN_FACTORY.get(beanName);
        if(instance != null){
            return instance;
        }
        if(!BEAN_DEF_FACTORY.containsKey(beanName)){
            return null;
        }
        BeanDefinition bd = BEAN_DEF_FACTORY.get(beanName);

        Class<?> beanClass = bd.getBeanClass();

        if(beanClass != null){
            instance = createBeanByConstruct(beanClass);
            if(instance == null){
                instance = createBeanByStaticFactoryMethod(bd);
            }
        }
        if(instance == null && null != (bd.getBeanFactoryCreateMethodName())){
            instance = createBeanByFactoryMethod(bd);
        }

        this.doInit(bd, instance);

        if(bd.isSingleton()){
            BEAN_FACTORY.put(beanName, instance);
        }

        return instance;
    }

    private void doInit(BeanDefinition bd, Object instance) {
        Class<?> beanClass = instance.getClass();
        if(null != (bd.getBeanInitMethodName())){
            try {
                Method method = beanClass.getMethod(bd.getBeanInitMethodName(), null);
                method.invoke(instance, null);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    protected Object createBeanByConstruct (Class<?> clazz)  {
        try {
            return clazz.newInstance();
        }catch (IllegalAccessException | InstantiationException e){
            e.printStackTrace();
            return null;
        }

    }


    /**
     * 普通工厂方法创建实例
     * @param bd BeanDefinition
     * @return Object
     */
    private Object createBeanByFactoryMethod(BeanDefinition bd) {
        Object instance = null;
        try {
            Object factory = doGetBean(bd.getBeanFactory());
            Method method = factory.getClass().getMethod(bd.getBeanFactoryCreateMethodName());
            instance = method.invoke(factory, null);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return instance;
    }

    /**
     * 静态方法创建实例
     * @param bd BeanDefinition
     * @return Object
     */
    private Object createBeanByStaticFactoryMethod(BeanDefinition bd) {
        Object instance = null;
        try {
            Class<?> beanClass = bd.getBeanClass();
            Method method = beanClass.getMethod(bd.getStaticCreateBeanMethod());
            instance = method.invoke(beanClass, null);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return instance;
    }

}

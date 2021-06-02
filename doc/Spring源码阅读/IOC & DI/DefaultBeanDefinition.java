/**
 * @author wang xiao
 * @date Created in 16:53 2020/11/12
 */
public class DefaultBeanDefinition implements BeanDefinition {

    private Class<?> clazz;

    private String beanFactoryName;

    private String createBeanMethodName;

    private String staticCreateBeanMethodName;

    private String beanInitMethodName;

    private String beanDestroyMethodName;

    private String beanFactoryCreateMethodName;

    private boolean singleton;

    private boolean prototype;

    private boolean validate;

    private String scope;

    public String getBeanFactoryName() {
        return beanFactoryName;
    }

    public void setBeanFactoryName(String beanFactoryName) {
        this.beanFactoryName = beanFactoryName;
    }

    public String getCreateBeanMethodName() {
        return createBeanMethodName;
    }

    public void setCreateBeanMethodName(String createBeanMethodName) {
        this.createBeanMethodName = createBeanMethodName;
    }

    public String getStaticCreateBeanMethodName() {
        return staticCreateBeanMethodName;
    }

    public void setStaticCreateBeanMethodName(String staticCreateBeanMethodName) {
        this.staticCreateBeanMethodName = staticCreateBeanMethodName;
    }

    @Override
    public String getBeanInitMethodName() {
        return beanInitMethodName;
    }

    public void setBeanInitMethodName(String beanInitMethodName) {
        this.beanInitMethodName = beanInitMethodName;
    }

    public Class<?> getClazz() {
        return clazz;
    }

    public void setClazz(Class<?> clazz) {
        this.clazz = clazz;
    }

    public String getBeanDestroyMethodName() {
        return beanDestroyMethodName;
    }

    public void setBeanDestroyMethodName(String beanDestroyMethodName) {
        this.beanDestroyMethodName = beanDestroyMethodName;
    }


    @Override
    public boolean isPrototype() {
        return prototype;
    }

    public void setPrototype(boolean prototype) {
        this.prototype = prototype;
    }

    public boolean isValidate() {
        return validate;
    }

    public void setValidate(boolean validate) {
        this.validate = validate;
    }

    @Override
    public String getScope() {
        return scope;
    }

    public void setScope(String scope) {
        this.scope = scope;
    }

    @Override
    public boolean isSingleton() {
        return singleton;
    }

    public void setSingleton(boolean singleton) {
        this.singleton = singleton;
    }

    @Override
    public Class<?> getBeanClass() {
        return clazz;
    }

    @Override
    public String getBeanFactory() {
        return beanFactoryName;
    }

    @Override
    public String getCreateBeanMethod() {
        return createBeanMethodName;
    }

    @Override
    public String getStaticCreateBeanMethod() {
        return staticCreateBeanMethodName;
    }


    @Override
    public boolean validate() {
        return validate;
    }

    @Override
    public String getBeanFactoryCreateMethodName() {
        return beanFactoryCreateMethodName;
    }

    public void setBeanFactoryCreateMethodName(String beanFactoryCreateMethodName) {
        this.beanFactoryCreateMethodName = beanFactoryCreateMethodName;
    }
}

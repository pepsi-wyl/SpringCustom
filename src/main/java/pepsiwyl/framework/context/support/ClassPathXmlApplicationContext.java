package pepsiwyl.framework.context.support;

/**
 * @author by pepsi-wyl
 * @date 2022-10-02 10:41
 */

import lombok.SneakyThrows;
import pepsiwyl.framework.beans.BeanDefinition;
import pepsiwyl.framework.beans.MutablePropertyValues;
import pepsiwyl.framework.beans.PropertyValue;
import pepsiwyl.framework.beans.factory.support.BeanDefinitionRegistry;
import pepsiwyl.framework.beans.factory.xml.XmlBeanDefinitionReader;
import pepsiwyl.framework.utils.StringUtils;

import java.lang.reflect.Method;

/**
 * IOC容器具体子实现类
 * 加载类路径下的XML格式的配置文件
 */

public class ClassPathXmlApplicationContext extends AbstractApplicationContext {

    @SneakyThrows
    // 初始化父类AbstractApplicationContext对象 并加载对象
    public ClassPathXmlApplicationContext(String configLocation) {
        this.configLocation = configLocation;
        this.beanDefinitionReader = new XmlBeanDefinitionReader();
        super.refresh();
    }

    // 根据bean对象的名称获取bean对象
    @Override
    public Object getBean(String name) throws Exception {

        /**
         * 判断对象容器中是否包含指定名称的bean对象 包含直接返回 不包含自行创建
         */

        Object existObj = singletonObjects.get(name);
        if (existObj != null) return existObj;

        /**
         * 自行创建bean
         */

        // 获取注册表对象
        BeanDefinitionRegistry beanDefinitionRegistry = beanDefinitionReader.getRegistry();
        // 获取BeanDefinition对象
        BeanDefinition beanDefinition = beanDefinitionRegistry.getBeanDefinition(name);
        if (beanDefinition == null) return null;

        // 获取ClassName属性
        String className = beanDefinition.getClassName();
        // 通过反射获取字节码对象
        Class<?> clazz = Class.forName(className);

        // 通过反射创建对象
        Object beanObj = clazz.getConstructor().newInstance();

        // 进行依赖注入操作
        MutablePropertyValues propertyValues = beanDefinition.getMutableProperties();
        for (PropertyValue propertyValue : propertyValues) {

            // 获取name属性值
            String propertyValueName = propertyValue.getName();
            // 获取value属性
            String propertyValueValue = propertyValue.getValue();
            // 获取ref属性
            String propertyValueRef = propertyValue.getRef();

            // 处理ref属性
            if (propertyValueRef != null && !"".equals(propertyValueRef)) {

                // 获取依赖的bean对象
                Object beanRef = getBean(propertyValueName);
                // 获取拼接方法名称
                String methodName = StringUtils.getSetterMethodNameByFieldName(propertyValueName);

                // 反射获取方法对象并且注入
                Method[] methods = clazz.getMethods();
                for (Method method : methods) {
                    if (methodName.equals(method.getName())) method.invoke(beanObj, beanRef);
                }
            }

            // 处理value属性
            if (propertyValueValue != null && !"".equals(propertyValueValue)) {

                // 获取拼接方法名称
                String methodName = StringUtils.getSetterMethodNameByFieldName(propertyValueName);

                // 反射获取方法对象并且注入
                Method method = clazz.getMethod(methodName, String.class);
                method.invoke(beanObj, propertyValueValue);
            }

        }

        // 将该对象存储在map容器中
        singletonObjects.put(name, beanObj);

        // 返回创建的对象
        return beanObj;
    }

    @Override
    public <T> T getBean(String name, Class<? extends T> clazz) throws Exception {
        Object bean = getBean(name);
        if (bean != null) return clazz.cast(bean);
        return null;
    }

}

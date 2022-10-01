package pepsiwyl.framework.context.support;

/**
 * @author by pepsi-wyl
 * @date 2022-10-01 20:48
 */

import pepsiwyl.framework.beans.factory.support.BeanDefinitionReader;
import pepsiwyl.framework.beans.factory.support.BeanDefinitionRegistry;
import pepsiwyl.framework.context.ApplicationContext;

import java.util.HashMap;
import java.util.Map;

/**
 * ApplicationContext接口的子实现类，用于立即加载
 */

public abstract class AbstractApplicationContext implements ApplicationContext {

    // 声明解析器 子类实现
    protected BeanDefinitionReader beanDefinitionReader;

    // 定义用于存储bean对象的map容器
    protected Map<String, Object> singletonObjects = new HashMap<>();

    // 声明配置文件路径的变量
    protected String configLocation;

    @Override
    public void refresh() throws IllegalStateException, Exception {
        // 加载BeanDefinition对象
        beanDefinitionReader.loadBeanDefinitions(configLocation);
        // 初始化Bean对象
        finishBeanInitialization();
    }

    // 初始化Bean对象
    private void finishBeanInitialization() throws Exception {
        // 获取注册表对象
        BeanDefinitionRegistry registry = beanDefinitionReader.getRegistry();
        // 获取BeanDefinition对象
        String[] beanDefinitionNames = registry.getBeanDefinitionNames();
        // 进行Bean的初始化
        for (String beanDefinitionName : beanDefinitionNames) {
            getBean(beanDefinitionName);
        }
    }
}

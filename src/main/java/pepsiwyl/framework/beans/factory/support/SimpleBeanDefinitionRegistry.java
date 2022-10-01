package pepsiwyl.framework.beans.factory.support;

/**
 * @author by pepsi-wyl
 * @date 2022-10-01 17:30
 */

import pepsiwyl.framework.beans.BeanDefinition;

import java.util.HashMap;
import java.util.Map;

/**
 * 注册表注册对象 注册表接口的子实现类
 */

public class SimpleBeanDefinitionRegistry implements BeanDefinitionRegistry {

    // 定义容器，用于存储BeanDefinition对象
    private Map<String, BeanDefinition> beanDefinitionMap = new HashMap<>();

    @Override
    public void registerBeanDefinition(String beanName, BeanDefinition beanDefinition) {
        beanDefinitionMap.put(beanName, beanDefinition);
    }

    @Override
    public void removeBeanDefinition(String beanName) {
        beanDefinitionMap.remove(beanName);
    }

    @Override
    public BeanDefinition getBeanDefinition(String beanName) {
        return beanDefinitionMap.get(beanName);
    }

    @Override
    public String[] getBeanDefinitionNames() {
        return beanDefinitionMap.keySet().toArray(new String[0]);
    }

    @Override
    public boolean containsBeanDefinition(String beanName) {
        return beanDefinitionMap.containsKey(beanName);
    }

    @Override
    public int getBeanDefinitionCount() {
        return beanDefinitionMap.size();
    }

}

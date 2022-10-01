package pepsiwyl.framework.beans.factory.support;

/**
 * @author by pepsi-wyl
 * @date 2022-10-01 17:26
 */

import pepsiwyl.framework.beans.BeanDefinition;

/**
 * 注册表接口
 */

public interface BeanDefinitionRegistry {

    // 向注册表中注册bean
    void registerBeanDefinition(String beanName, BeanDefinition beanDefinition);

    // 向注册表中删除指定名称的bean
    void removeBeanDefinition(String beanName);

    // 获取注册表中指定名称的bean
    BeanDefinition getBeanDefinition(String beanName);

    // 获取注册表中所有的bean的名称
    String[] getBeanDefinitionNames();

    // 判断注册表中是否已经注册了指定名称的bean
    boolean containsBeanDefinition(String beanName);

    // 获取注册表中所有的bean的数量
    int getBeanDefinitionCount();

}

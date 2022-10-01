package pepsiwyl.framework.context;

/**
 * @author by pepsi-wyl
 * @date 2022-10-01 20:37
 */

import pepsiwyl.framework.beans.factory.BeanFactory;

/**
 * 定义非延时加载功能
 */

public interface ApplicationContext extends BeanFactory {
    // 进行配置文件加载并对对象进行创建
    void refresh() throws IllegalStateException, Exception;
}

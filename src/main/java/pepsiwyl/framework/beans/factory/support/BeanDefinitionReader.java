package pepsiwyl.framework.beans.factory.support;

/**
 * @author by pepsi-wyl
 * @date 2022-10-01 19:12
 */

/**
 * 解析配置文件 只是定义规范
 */

public interface BeanDefinitionReader {

    // 获取注册表对象
    BeanDefinitionRegistry getRegistry();

    // 加载配置文件并在注册表中注册
    void loadBeanDefinitions(String configLocation) throws Exception;

}
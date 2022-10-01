package pepsiwyl.framework.beans.factory;

/**
 * @author by pepsi-wyl
 * @date 2022-10-01 20:32
 */

/**
 * IOC容器父(根)接口
 */

public interface BeanFactory {
    // 根据bean对象的名称获取bean对象
    Object getBean(String name) throws Exception;

    // 根据bean对象的名称获取bean对象，并进行类型转换
    <T> T getBean(String name, Class<? extends T> clazz) throws Exception;
}

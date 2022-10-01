package pepsiwyl.framework.beans.factory.xml;

import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import pepsiwyl.framework.beans.BeanDefinition;
import pepsiwyl.framework.beans.MutablePropertyValues;
import pepsiwyl.framework.beans.PropertyValue;
import pepsiwyl.framework.beans.factory.support.BeanDefinitionReader;
import pepsiwyl.framework.beans.factory.support.BeanDefinitionRegistry;
import pepsiwyl.framework.beans.factory.support.SimpleBeanDefinitionRegistry;

import java.io.InputStream;
import java.util.List;

/**
 * @author by pepsi-wyl
 * @date 2022-10-01 19:16
 */

/**
 * 针对XML配置文件进行解析的类
 */

public class XmlBeanDefinitionReader implements BeanDefinitionReader {

    // 声明注册表对象
    private BeanDefinitionRegistry registry;

    // 初始化注册表对象
    public XmlBeanDefinitionReader() {
        this.registry = new SimpleBeanDefinitionRegistry();
    }

    @Override
    public BeanDefinitionRegistry getRegistry() {
        return registry;
    }

    @Override
    public void loadBeanDefinitions(String configLocation) throws Exception {
        // 获取类路径下的配置文件
        InputStream is = XmlBeanDefinitionReader.class.getClassLoader().getResourceAsStream(configLocation);
        // Dom4J进行XML配置文件解析
        Document document = new SAXReader().read(is);
        // 根据Document获取根标签对象
        Element rootElement = document.getRootElement();
        // 解析配置文件
        parseBean(rootElement);
    }

    // 解析配置文件
    private void parseBean(Element rootElement) {
        // 获取根标签下的所有Bean标签对象
        List<Element> beanElements = rootElement.elements("bean");

        // 遍历集合
        for (Element beanElement : beanElements) {
            // 一个bean标签封装一个BeanDefinition对象
            BeanDefinition beanDefinition = new BeanDefinition();

            String id = beanElement.attributeValue("id");             // 获取ID属性
            String className = beanElement.attributeValue("class");   // 获取Class属性

            // 将ID和Class封装到BeanDefinition对象中
            beanDefinition.setId(id);
            beanDefinition.setClassName(className);

            // 创建MutablePropertyValues对象
            MutablePropertyValues mutablePropertyValues = new MutablePropertyValues();
            // 解析得到bean标签下所有的Property标签对象 封装成MutablePropertyValues对象
            List<Element> propertyElements = beanElement.elements("property");

            // 循环遍历封装
            for (Element propertyElement : propertyElements) {

                String name = propertyElement.attributeValue("name");      // 获取name属性
                String ref = propertyElement.attributeValue("ref");        // 获取ref属性
                String value = propertyElement.attributeValue("value");    // 获取value属性

                // 封装成PropertyValue对象
                PropertyValue propertyValue = new PropertyValue(name, ref, value);

                // 加入mutablePropertyValues集合中
                mutablePropertyValues.addPropertyValue(propertyValue);
            }

            // 将MutablePropertyValues封装到BeanDefinition对象中
            beanDefinition.setMutableProperties(mutablePropertyValues);

            // 将BeanDefinition注册到注册表中
            registry.registerBeanDefinition(id, beanDefinition);
        }
    }

    public static void main(String[] args) throws Exception {
        XmlBeanDefinitionReader xmlBeanDefinitionReader = new XmlBeanDefinitionReader();

        xmlBeanDefinitionReader.loadBeanDefinitions("ApplicationContext.xml");
        BeanDefinitionRegistry registry = xmlBeanDefinitionReader.getRegistry();

        String[] beanDefinitionNames = registry.getBeanDefinitionNames();
        for (String beanDefinitionName : beanDefinitionNames) {
            BeanDefinition beanDefinition = registry.getBeanDefinition(beanDefinitionName);
            MutablePropertyValues mutableProperties = beanDefinition.getMutableProperties();
            PropertyValue[] propertyValues = mutableProperties.getPropertyValues();

            // 输出测试
            System.out.println(beanDefinition.toString());
            for (PropertyValue propertyValue : propertyValues) {
                System.out.println(propertyValue);
            }
        }
    }

}

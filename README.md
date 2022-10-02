# Spring IOC相关接口
## BeanFactory解析
:::tips
Spring中Bean的创建是**典型的工厂模式**，这一系列的**Bean工厂**，即**IoC容器**，为开发者管理对象之间的依赖关系提供了很多便利和基础服务，在**Spring中有许多IoC容器的实现供用户选择。**
:::
:::tips
![image-20200429185050396.png](https://cdn.nlark.com/yuque/0/2022/png/23219042/1664592135068-44315f9f-1fbe-4367-ba45-bd7c20994e55.png#clientId=u852acded-c9ed-4&crop=0&crop=0&crop=1&crop=1&errorMessage=unknown%20error&from=drop&id=u888bab2b&margin=%5Bobject%20Object%5D&name=image-20200429185050396.png&originHeight=551&originWidth=1083&originalType=binary&ratio=1&rotation=0&showTitle=false&size=25363&status=error&style=none&taskId=ub77b1c00-09c7-47ea-bb75-9d7d1c3548a&title=)
**BeanFactory作为最顶层的一个接口，定义了IoC容器的基本功能规范，只对IoC容器的基本行为做了定义**，不关心Bean是如何定义及怎样加载的。正如我们只关心能从工厂里得到什么产品，不关心工厂是怎么生产这些产品的。

有三个重要的子接口：**ListableBeanFactory  HierarchicalBeanFactory  AutowireCapableBeanFactory ，**共同定义了Bean的集合、Bean之间的关系及Bean行为。
每个接口都有使用场合，为了**区分在Spring内部操作过程中对象的传递和转化**，对对象的数据访问所做的限制

- ListableBeanFactory接口表示这些Bean可列表化
- HierarchicalBeanFactory表示这些Bean有继承关系，也就是每个Bean可能有父 Bean
- AutowireCapableBeanFactory 接口定义Bean的自动装配规则

**最终的默认实现类是 DefaultListableBeanFactory**，它实现了所有的接口
:::
:::tips
![image-20200430220155371.png](https://cdn.nlark.com/yuque/0/2022/png/23219042/1664592190334-f26d6982-2e7d-417a-9a10-6ec0b644a62f.png#clientId=u852acded-c9ed-4&crop=0&crop=0&crop=1&crop=1&errorMessage=unknown%20error&from=drop&id=ud19b163b&margin=%5Bobject%20Object%5D&name=image-20200430220155371.png&originHeight=590&originWidth=846&originalType=binary&ratio=1&rotation=0&showTitle=false&size=102796&status=error&style=none&taskId=u1489db0d-3abe-4e38-9e0c-86344704b10&title=)
**ApplicationContext接口**是BeanFactory中一个很重要的子接口，用来规范容器中的bean对象是**非延时加载**，即在**创建容器对象的时候就对象bean进行初始化，并存储到一个容器中**。
Spring提供了许多**具体的IoC容器实现**

- **ClasspathXmlApplicationContext** : 根据类路径加载xml配置文件，并创建IOC容器对象
- **FileSystemXmlApplicationContext** ：根据系统路径加载xml配置文件，并创建IOC容器对象
- **AnnotationConfigApplicationContext** ：加载注解类配置，并创建IOC容器
:::
```java
public interface BeanFactory {

    String FACTORY_BEAN_PREFIX = "&";

    // 根据bean的名称获取IOC容器中的的bean对象
    Object getBean(String name) throws BeansException;

    // 根据bean的名称获取IOC容器中的的bean对象，并指定获取到的bean对象的类型，这样我们使用时就不需要进行类型强转了
    <T> T getBean(String name, Class<T> requiredType) throws BeansException;
    Object getBean(String name, Object... args) throws BeansException;
    <T> T getBean(Class<T> requiredType) throws BeansException;
    <T> T getBean(Class<T> requiredType, Object... args) throws BeansException;

    <T> ObjectProvider<T> getBeanProvider(Class<T> requiredType);
    <T> ObjectProvider<T> getBeanProvider(ResolvableType requiredType);

    // 判断容器中是否包含指定名称的bean对象
    boolean containsBean(String name);

    // 根据bean的名称判断是否是单例
    boolean isSingleton(String name) throws NoSuchBeanDefinitionException;
    boolean isPrototype(String name) throws NoSuchBeanDefinitionException;
    boolean isTypeMatch(String name, ResolvableType typeToMatch) throws NoSuchBeanDefinitionException;
    boolean isTypeMatch(String name, Class<?> typeToMatch) throws NoSuchBeanDefinitionException;

    @Nullable
    Class<?> getType(String name) throws NoSuchBeanDefinitionException;
    String[] getAliases(String name);
}
```
## BeanDefinition解析
:::tips
Spring IoC 容器管理我们定义的各种Bean对象及其相互关系，**Bean对象**在Spring实现中是**以BeanDefinition来描述**的
继承体系如图所示：![image-20200429204239868.png](https://cdn.nlark.com/yuque/0/2022/png/23219042/1664606402250-76dd6c44-3393-4a1c-a3cf-c9bb7ce8bc16.png#clientId=u852acded-c9ed-4&crop=0&crop=0&crop=1&crop=1&errorMessage=unknown%20error&from=drop&id=RlwVi&margin=%5Bobject%20Object%5D&name=image-20200429204239868.png&originHeight=562&originWidth=913&originalType=binary&ratio=1&rotation=0&showTitle=false&size=24687&status=error&style=none&taskId=u744dda9c-0ac2-4f68-8c0e-86579853a4f&title=)
:::
```java
<bean id="userDao" class="com.itheima.dao.impl.UserDaoImpl"></bean>
bean标签还有很多属性：scope、init-method、destory-method等。
```
## BeanDefinitionReader解析
:::tips
Bean的**解析过程非常复杂，**主要就是**对Spring配置文件的解析，功能被分得很细，**因为这里需要**被扩展的地方很多，必须保证足够的灵活性**，以应对可能的变化。解析过程主要通过**BeanDefinitionReader**来完成，类结构如图
![image.png](https://cdn.nlark.com/yuque/0/2022/png/23219042/1664608177768-7792a4fa-6d55-404a-9ab6-c9db853043f4.png#clientId=u852acded-c9ed-4&crop=0&crop=0&crop=1&crop=1&errorMessage=unknown%20error&from=paste&height=590&id=uea5e0c32&margin=%5Bobject%20Object%5D&name=image.png&originHeight=590&originWidth=894&originalType=binary&ratio=1&rotation=0&showTitle=false&size=502252&status=error&style=none&taskId=u9e306e0f-5421-4afc-9c83-50d7eb93549&title=&width=894)
:::
```java

public interface BeanDefinitionReader {

    // 获取BeanDefinitionRegistry注册器对象
    BeanDefinitionRegistry getRegistry();

    @Nullable
    ResourceLoader getResourceLoader();

    @Nullable
    ClassLoader getBeanClassLoader();

    BeanNameGenerator getBeanNameGenerator();

   /*
    * loadBeanDefinitions加载bean定义，从指定的资源中
    */
    int loadBeanDefinitions(Resource resource) throws BeanDefinitionStoreException;
    int loadBeanDefinitions(Resource... resources) throws BeanDefinitionStoreException;
    int loadBeanDefinitions(String location) throws BeanDefinitionStoreException;
    int loadBeanDefinitions(String... locations) throws BeanDefinitionStoreException;
}
```
## BeanDefinitionRegistry解析
:::tips
BeanDefinitionReader用来**解析bean定义**，并**封装BeanDefinition对象**，配置文件中定义了很多bean标签，**解析的BeanDefinition对象存储BeanDefinition的注册中心。**

![image-20200429211132185.png](https://cdn.nlark.com/yuque/0/2022/png/23219042/1664608626056-c7ed3a4e-650a-44fc-94c6-18963fb3ca11.png#clientId=u852acded-c9ed-4&crop=0&crop=0&crop=1&crop=1&errorMessage=unknown%20error&from=drop&id=ued03f84c&margin=%5Bobject%20Object%5D&name=image-20200429211132185.png&originHeight=217&originWidth=960&originalType=binary&ratio=1&rotation=0&showTitle=false&size=10209&status=error&style=none&taskId=uac791fbf-da2d-4202-9a55-986b2f5cb2d&title=)
**BeanDefinitionRegistry是注册中心顶层接口**
主要子实现类有：

- SimpleBeanDefinitionRegistry
- DefaultListableBeanFactory
:::
```
// 如下代码，用来注册bean
private final Map<String, BeanDefinition> beanDefinitionMap = new ConcurrentHashMap(64);
```
```
// 如下代码，用来注册bean
private final Map<String, BeanDefinition> beanDefinitionMap = new ConcurrentHashMap<>(256);
```
```java
public interface BeanDefinitionRegistry extends AliasRegistry {

    // 向注册表中注册bean
    void registerBeanDefinition(String beanName, BeanDefinition beanDefinition) throws BeanDefinitionStoreException;

    // 向注册表中删除指定名称的bean
    void removeBeanDefinition(String beanName) throws NoSuchBeanDefinitionException;

    // 获取注册表中指定名称的bean
    BeanDefinition getBeanDefinition(String beanName) throws NoSuchBeanDefinitionException;
    
    // 判断注册表中是否已经注册了指定名称的bean
    boolean containsBeanDefinition(String beanName);
    
    // 获取注册表中所有的bean的名称
    String[] getBeanDefinitionNames();

    // 获取注册表中所有的bean的数量
    int getBeanDefinitionCount();

    // 指定名称的bean是否使用过
    boolean isBeanNameInUse(String beanName);
    
}
```
## 创建容器
:::tips
ClassPathXmlApplicationContext**对Bean配置资源的载入**是从 **refresh()** 方法开始的。refresh() 方法是一个模板方法，规定了 IoC 容器的启动流程，通过调用其父类AbstractApplicationContext的refresh()方法启动整个IoC容器对Bean定义的载入过程，有些逻辑要交给其子类实现。
:::
# 自定义Spring IOC
:::tips
对下面的配置文件进行解析，并自定义Spring IOC 对涉及到的对象进行管理。
:::
```xml
<?xml version="1.0" encoding="UTF-8"?>
<beans>
    <bean id="userDao" class="pepsiwyl.mapper.impl.UserDaoImpl"/>
    <bean id="userService" class="pepsiwyl.service.impl.UserServiceImpl">
        <property name="userDao" ref="userDao"/>
    </bean>
</beans>
```
## 定义Bean相关的Pojo类
### PropertyValue类
:::tips
用于封装bean的属性，体现到上面的配置文件就是**封装bean标签的子标签property标签数据**
:::
```java
/**
 * 封装bean标签下Property标签的属性
 * name ref value
 */

@Builder

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode

public class PropertyValue {
    private String name;
    private String ref;
    private String value;
}

```
### MutablePropertyValues类
:::tips
一个bean标签**可以有多个property子标签**，所以再定义一个MutablePropertyValues类，用来存储并管理多个PropertyValue对象。
:::
```java
/**
 * 存储和管理多个PropertyValue对象
 */

public class MutablePropertyValues implements Iterable<PropertyValue> {

    // 定义list集合对象，用来存储PropertyValue对象
    private final List<PropertyValue> propertyValueList;

    // 无参构造
    public MutablePropertyValues() {
        this.propertyValueList = new ArrayList<>();
    }

    // 有参构造
    public MutablePropertyValues(List<PropertyValue> propertyValueList) {
        // 三目表达式
        this.propertyValueList = (propertyValueList != null ? propertyValueList : new ArrayList<>());
    }

    // 获取迭代器对象
    @Override
    public Iterator<PropertyValue> iterator() {
        // 返回集合对象的迭代器对象
        return propertyValueList.iterator();
    }

    // 获取所有PropertyValue对象，返回数组类型
    public PropertyValue[] getPropertyValues() {
        // 调用集合转化为数组的方法 并且指定数组类型为PropertyValue类型
        return propertyValueList.toArray(new PropertyValue[0]);
    }

    // 添加PropertyValue对象
    public MutablePropertyValues addPropertyValue(PropertyValue propertyValue) {
        // 判空
        if (propertyValue == null) return this;

        // 判断PropertyValue是否重复，重复则进行覆盖
        for (int i = 0; i < this.propertyValueList.size(); i++) {
            PropertyValue currentPropertyValue = this.propertyValueList.get(i);
            if (currentPropertyValue.getName().equals(propertyValue.getName())) {
                propertyValueList.set(i, propertyValue);
                return this;
            }
        }

        // PropertyValue没有重复，进行添加
        propertyValueList.add(propertyValue);
        return this;
    }

    // 根据PropertyName名称获取PropertyValue对象
    public PropertyValue getPropertyValue(String propertyName) {
        // 遍历集合对象
        for (PropertyValue propertyValue : propertyValueList) {
            if (propertyValue.getName().equals(propertyName)) return propertyValue;
        }
        return null;
    }

    // 判断是否有PropertyName名称的PropertyValue对象
    public boolean contains(String propertyValue) {
        return getPropertyValue(propertyValue) != null;
    }

    // 判断集合是否为空
    public boolean isEmpty() {
        return propertyValueList.isEmpty();
    }

}
```
### BeanDefinition类
:::tips
**BeanDefinition类**用来封装bean信息的，主要包含id（即bean对象的名称）、class（需要交由spring管理的类的全类名）及子标签property数据。
:::
```java
/**
 * 封装Bean标签数据
 *    id 属性
 *    class 属性
 *    property子标签数据
 */

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class BeanDefinition {
    private String id;
    private String className;
    private MutablePropertyValues mutableProperties = new MutablePropertyValues();
}
```
## 定义注册表相关类
### BeanDefinitionRegistry接口
:::tips
BeanDefinitionRegistry接口**定义了注册表的相关操作**，定义如下功能：

- 注册BeanDefinition对象到注册表中
- 从注册表中删除指定名称的BeanDefinition对象
- 根据名称从注册表中获取BeanDefinition对象
- 判断注册表中是否包含指定名称的BeanDefinition对象
- 获取注册表中BeanDefinition对象的个数
- 获取注册表中所有的BeanDefinition的名称
:::
```java
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
```
### SimpleBeanDefinitionRegistry类
:::tips
实现了BeanDefinitionRegistry接口，**定义了Map集合作为注册表容器**。
:::
```java
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
```
## 定义解析器相关类
### BeanDefinitionReader接口
:::tips
BeanDefinitionReader是用来**解析配置文件并在注册表中注册bean的信息**。定义了两个规范：

- 获取注册表的功能，让外界可以通过该对象获取注册表对象
- 加载配置文件，并注册bean数据
:::
```java
/**
 * 解析配置文件 只是定义规范
 */

public interface BeanDefinitionReader {

    // 获取注册表对象
    BeanDefinitionRegistry getRegistry();

    // 加载配置文件并在注册表中注册
    void loadBeanDefinitions(String configLocation) throws Exception;

}
```
### XmlBeanDefinitionReader类
:::tips
XmlBeanDefinitionReader类是**专门用来解析xml配置文件的**。实现BeanDefinitionReader接口中的两个功能。
:::
```java
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
```
## IOC容器相关类
### BeanFactory接口
:::tips
在该接口中**定义IOC容器**的统一规范即**获取bean对象**
:::
```java
/**
 * IOC容器父(根)接口
 */

public interface BeanFactory {
    // 根据bean对象的名称获取bean对象
    Object getBean(String name) throws Exception;

    // 根据bean对象的名称获取bean对象，并进行类型转换
    <T> T getBean(String name, Class<? extends T> clazz) throws Exception;
}
```
### ApplicationContext接口
:::tips
该接口的所有子实现类对**bean对象的创建都是非延时的**，在该接口中定义 refresh() 方法，主要有两个功能：

- **加载配置文件**
- 根据注册表中的**BeanDefinition对象封装的数据进行bean对象的创建**
:::
```java
/**
 * 定义非延时加载功能
 */

public interface ApplicationContext extends BeanFactory {
    // 进行配置文件加载并对对象进行创建
    void refresh() throws IllegalStateException, Exception;
}
```
### AbstractApplicationContext类
:::tips

- 作为ApplicationContext接口的子类，该类**非延时加载**，在类中**定义Map集合**，作为bean对象存储的容器
- 声明BeanDefinitionReader类型的变量，用来**进行配置文件的解析**，符合单一职责原则，子类明确创建BeanDefinitionReader哪个子实现类对象，**对象创建交由子类实现**
:::
```java
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
```
### ClassPathXmlApplicationContext类
:::tips
加载类路径下的配置文件，并进行bean对象的创建

- 在构造方法中，**创建BeanDefinitionReader对象**
- 在构造方法中，**调用refresh()方法，用于进行配置文件加载、创建bean对象并存储到容器中**
- **重写父接口中的getBean()方法**，并实现依赖注入操作
:::
```java
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
```
## 测试
### 安装到本地仓库
![image.png](https://cdn.nlark.com/yuque/0/2022/png/23219042/1664696638871-952cc721-9f6d-4230-8171-ee0ae7817a4a.png#clientId=u7ea446d5-d127-4&crop=0&crop=0&crop=1&crop=1&from=paste&height=950&id=u080e7e8f&margin=%5Bobject%20Object%5D&name=image.png&originHeight=950&originWidth=1479&originalType=binary&ratio=1&rotation=0&showTitle=false&size=1071287&status=done&style=none&taskId=u5bf3e33d-d795-4177-a93e-69ff4914d3f&title=&width=1479)
### 新建maven工程引入依赖
```xml
    <dependencies>
        <dependency>
            <groupId>org.framework</groupId>
            <artifactId>SpringCustom</artifactId>
            <version>1.0-SNAPSHOT</version>
        </dependency>
    </dependencies>
```
### 编写测试代码
```java
public interface UserDao {
    void addUser(String user);
}

public class UserDaoImpl implements UserDao {

    private String username;
    private String password;

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public void addUser(String user) {
        System.out.println("添加" + user + " " + username + " " + password);
    }
}
```
```java
public interface UserService {
    void addUser(String user);
}

public class UserServiceImpl implements UserService {
    private UserDao userDao;

    public void setUserDao(UserDao userDao) {
        this.userDao = userDao;
    }

    @Override
    public void addUser(String user) {
        userDao.addUser(user);
    }
}
```
```java
public class UserController {
    public static void main(String[] args) throws Exception {
        ClassPathXmlApplicationContext applicationContext = new ClassPathXmlApplicationContext("ApplicationContext.xml");
        UserService userService = applicationContext.getBean("userService", UserService.class);
        System.out.println(userService);
        userService.addUser("user");
    }
}
```
### 编写配置文件
```java
<?xml version="1.0" encoding="UTF-8"?>
<beans>
    <bean id="userDao" class="dao.UserDaoImpl">
        <property name="username" value="pepsiwyl"></property>
        <property name="password" value="000000"></property>
    </bean>
    <bean id="userService" class="service.UserServiceImpl">
        <property name="userDao" ref="userDao"></property>
    </bean>
</beans>
```
### 测试结果
![image.png](https://cdn.nlark.com/yuque/0/2022/png/23219042/1664696853544-6a32000a-9258-4ff3-aca4-ea881b31616d.png#clientId=u7ea446d5-d127-4&crop=0&crop=0&crop=1&crop=1&from=paste&height=69&id=u9c9f89eb&margin=%5Bobject%20Object%5D&name=image.png&originHeight=69&originWidth=354&originalType=binary&ratio=1&rotation=0&showTitle=false&size=26343&status=done&style=none&taskId=u3c06d2d6-a22a-4d0e-8b3d-6172337d021&title=&width=354)
## 总结
### 使用的设计模式
:::tips

- **工厂模式**   使用工厂模式 + 配置文件
- **单例模式**   Spring IOC管理的bean对象都是单例的，此处的单例不是通过构造器进行单例的控制的，而是**spring框架对每一个bean只创建了一个对象**
- **模板方法模式**  AbstractApplicationContext类中的finishBeanInitialization()方法调用了子类的getBean()方法，因为**getBean()的实现和环境息息相关**
- **迭代器模式**      对于MutablePropertyValues类定义使用到了迭代器模式，因为此类存储并管理PropertyValue对象，也属于一个容器，所以给该容器提供一个遍历方式。

spring框架其实使用到了很多设计模式，如AOP使用到了代理模式，选择JDK代理或者CGLIB代理使用到了策略模式，还有适配器模式，装饰者模式，观察者模式等。
:::
### 符合大部分设计原则
### 整个设计和Spring的设计还是有一定的出入
:::tips
pring框架底层是很复杂的，进行了很深入的封装，并对外提供了很好的扩展性。而我们自定义SpringIOC有以下几个目的：

- 了解Spring底层对对象的大体管理机制。
- 了解设计模式在具体的开发中的使用。
- 以后学习spring源码，通过该案例的实现，可以降低spring学习的入门成本。
:::


package pepsiwyl.framework.beans;

/**
 * @author by pepsi-wyl
 * @date 2022-10-01 16:18
 */

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

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

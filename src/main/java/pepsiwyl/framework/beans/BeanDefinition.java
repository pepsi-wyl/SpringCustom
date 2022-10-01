package pepsiwyl.framework.beans;

/**
 * @author by pepsi-wyl
 * @date 2022-10-01 16:57
 */

import lombok.*;

/**
 * 封装Bean标签数据
 * id 属性
 * class 属性
 * property子标签数据
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

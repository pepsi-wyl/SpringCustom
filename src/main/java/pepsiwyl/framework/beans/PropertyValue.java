package pepsiwyl.framework.beans;

import lombok.*;

/**
 * @author by pepsi-wyl
 * @date 2022-10-01 16:01
 */


/**
 * 封装bean标签下Property标签的属性
 * name ref value
 */

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class PropertyValue {
    private String name;
    private String ref;
    private String value;
}

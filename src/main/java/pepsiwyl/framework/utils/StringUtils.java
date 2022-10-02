package pepsiwyl.framework.utils;

/**
 * @author by pepsi-wyl
 * @date 2022-10-02 11:14
 */

// String工具类
public class StringUtils {

    private StringUtils() {
    }

    /**
     * 通过属性名称获取setter方法
     */
    public static String getSetterMethodNameByFieldName(String fieldName) {
        return "set" + fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1);
    }

    public static void main(String[] args) {
        String userDao = getSetterMethodNameByFieldName("userDao");
        System.out.println(userDao);
    }

}

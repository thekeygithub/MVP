package com.ebao.hospitaldapp.utils;


/**
 * 集合相关工具类
 */
public class CollectionUtils extends org.springframework.util.CollectionUtils {

    /**
     * 检查数组对象是否为空
     *
     * @param array
     * @return
     */
    public static <T> boolean isEmpty(T[] array) {
        if (array == null) return true;
        return array.length == 0;
    }
}

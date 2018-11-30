package com.ebao.hospitaldapp.utils;


import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 字符串工具类
 */
public final class StringUtils extends org.springframework.util.StringUtils {

    /* ============================================================================ */
    /* 常量和singleton。 */
    /* ============================================================================ */

    /**
     * 空字符串。
     */
    public static final String EMPTY_STRING = "";

    private static final String FILE_SEPARATOR = "/";

    /* ============================================================================ */
    /* 判空函数。 */
    /*                                                                              */
    /* 以下方法用来判定一个字符串是否为： */
    /* 1. null */
    /* 2. empty - "" */
    /* 3. blank - "全部是空白" - 空白由Character.isWhitespace所定义。 */
    /* ============================================================================ */

    /**
     * 将字符串转义以方便html输出
     */
//    public String escapeHtml(String s) {
//
//        return org.apache.commons.lang.StringEscapeUtils.escapeHtml(s);
//    }
//
//    public String unescapeHtml(String s) {
//        return org.apache.commons.lang.StringEscapeUtils.unescapeHtml(s);
//    }
    public String arrayIndex(String[] args, int _index) {
        return args[_index];
    }

    /**
     * 检查字符串是否为 <code>null</code> 或空字符串 <code>""</code>或只包含空格。
     * <p/>
     * <pre>
     *
     *    StringUtil.isEmpty(null)      = true
     *    StringUtil.isEmpty(&quot;&quot;)        = true
     *    StringUtil.isEmpty(&quot; &quot;)       = true
     *    StringUtil.isEmpty(&quot;bob&quot;)     = false
     *    StringUtil.isEmpty(&quot;  bob  &quot;) = false
     *
     * </pre>
     *
     * @param str 要检查的字符串
     * @return 如果为空, 则返回 <code>true</code>
     */
//    public static boolean isEmpty(String str) {
//        return isEmpty(str, true);
//    }
//
//    public static boolean isEmpty(String str, boolean trim) {
//        if ((str == null) || (str.isEmpty()))
//            return true;
//        if (trim)
//            return str.trim().isEmpty();
//        return str.isEmpty();
//    }

    /**
     * 检查字符串是否不是 <code>null</code> 和空字符串 <code>""</code>。
     *
     * @param str 要检查的字符串
     * @return 如果不为空, 则返回 <code>true</code>
     */
//    public static boolean isNotEmpty(String str) {
//        return !isEmpty(str);
//    }

    /**
     * 检查字符串是否是空白： <code>null</code> 、空字符串 <code>""</code> 或只有空白字符。
     * <p/>
     * <pre>
     *
     *    StringUtil.isBlank(null)      = true
     *    StringUtil.isBlank(&quot;&quot;)        = true
     *    StringUtil.isBlank(&quot; &quot;)       = true
     *    StringUtil.isBlank(&quot;bob&quot;)     = false
     *    StringUtil.isBlank(&quot;  bob  &quot;) = false
     *
     * </pre>
     *
     * @param str 要检查的字符串
     * @return 如果为空白, 则返回 <code>true</code>
     */
    public static boolean isBlank(String str) {
        int length;

        if ((str == null) || ((length = str.length()) == 0)) {
            return true;
        }

        for (int i = 0; i < length; i++) {
            if (!Character.isWhitespace(str.charAt(i))) {
                return false;
            }
        }

        return true;
    }

    /**
     * 检查字符串是否不是空白： <code>null</code> 、空字符串 <code>""</code> 或只有空白字符。
     *
     * @param str 要检查的字符串
     * @return 如果不是空白, 则返回 <code>true</code>
     */
    public static boolean isNotBlank(String str) {
        return !isBlank(str);
    }

    /* ============================================================================ */
    /* 默认值函数。 */
    /*                                                                              */
    /* 当字符串为null、empty或blank时，将字符串转换成指定的默认字符串。 */
    /* ============================================================================ */

    public static String emptyIfNull(Object source) {
        return (source == null) ? EMPTY_STRING : defaultIfBlank(source.toString());
    }

    /**
     * 如果字符串是 <code>null</code> ，则返回空字符串 <code>""</code> ，否则返回字符串本身。
     * <p/>
     * <pre>
     *
     *    StringUtil.defaultIfNull(null)  = &quot;&quot;
     *    StringUtil.defaultIfNull(&quot;&quot;)    = &quot;&quot;
     *    StringUtil.defaultIfNull(&quot;  &quot;)  = &quot;  &quot;
     *    StringUtil.defaultIfNull(&quot;bat&quot;) = &quot;bat&quot;
     *
     * </pre>
     *
     * @param str 要转换的字符串
     * @return 字符串本身或空字符串 <code>""</code>
     */
    public static String defaultIfNull(String str) {
        return (str == null) ? EMPTY_STRING : str;
    }

    /**
     * 如果字符串是 <code>null</code> ，则返回指定默认字符串，否则返回字符串本身。
     * <p/>
     * <pre>
     *
     *    StringUtil.defaultIfNull(null, &quot;default&quot;)  = &quot;default&quot;
     *    StringUtil.defaultIfNull(&quot;&quot;, &quot;default&quot;)    = &quot;&quot;
     *    StringUtil.defaultIfNull(&quot;  &quot;, &quot;default&quot;)  = &quot;  &quot;
     *    StringUtil.defaultIfNull(&quot;bat&quot;, &quot;default&quot;) = &quot;bat&quot;
     *
     * </pre>
     *
     * @param str        要转换的字符串
     * @param defaultStr 默认字符串
     * @return 字符串本身或指定的默认字符串
     */
    public static String defaultIfNull(String str, String defaultStr) {
        return (str == null) ? defaultStr : str;
    }

    /**
     * 如果字符串是 <code>null</code> 或空字符串 <code>""</code> ，则返回空字符串
     * <code>""</code> ，否则返回字符串本身。
     * <p/>
     * <p>
     * 此方法实际上和 <code>defaultIfNull(String)</code> 等效。
     * <p/>
     * <pre>
     *
     *    StringUtil.defaultIfEmpty(null)  = &quot;&quot;
     *    StringUtil.defaultIfEmpty(&quot;&quot;)    = &quot;&quot;
     *    StringUtil.defaultIfEmpty(&quot;  &quot;)  = &quot;  &quot;
     *    StringUtil.defaultIfEmpty(&quot;bat&quot;) = &quot;bat&quot;
     *
     * </pre>
     * <p/>
     * </p>
     *
     * @param str 要转换的字符串
     * @return 字符串本身或空字符串 <code>""</code>
     */
    public static String defaultIfEmpty(String str) {
        return isEmpty(str) ? EMPTY_STRING : str;
    }

    /**
     * 如果字符串是 <code>null</code> 或空字符串 <code>""</code> ，则返回指定默认字符串，否则返回字符串本身。
     * <p/>
     * <pre>
     *
     *    StringUtil.defaultIfEmpty(null, &quot;default&quot;)  = &quot;default&quot;
     *    StringUtil.defaultIfEmpty(&quot;&quot;, &quot;default&quot;)    = &quot;default&quot;
     *    StringUtil.defaultIfEmpty(&quot;  &quot;, &quot;default&quot;)  = &quot;  &quot;
     *    StringUtil.defaultIfEmpty(&quot;bat&quot;, &quot;default&quot;) = &quot;bat&quot;
     *
     * </pre>
     *
     * @param str        要转换的字符串
     * @param defaultStr 默认字符串
     * @return 字符串本身或指定的默认字符串
     */
    public static String defaultIfEmpty(String str, String defaultStr) {
        return isEmpty(str) ? defaultStr : str;
    }

    /**
     * 如果字符串是空白： <code>null</code> 、空字符串 <code>""</code> 或只有空白字符，则返回空字符串
     * <code>""</code> ，否则返回字符串本身。
     * <p/>
     * <pre>
     *
     *    StringUtil.defaultIfBlank(null)  = &quot;&quot;
     *    StringUtil.defaultIfBlank(&quot;&quot;)    = &quot;&quot;
     *    StringUtil.defaultIfBlank(&quot;  &quot;)  = &quot;&quot;
     *    StringUtil.defaultIfBlank(&quot;bat&quot;) = &quot;bat&quot;
     *
     * </pre>
     *
     * @param str 要转换的字符串
     * @return 字符串本身或空字符串 <code>""</code>
     */
    public static String defaultIfBlank(String str) {
        return isBlank(str) ? EMPTY_STRING : str;
    }

    /**
     * 如果字符串是 <code>null</code> 或空字符串 <code>""</code> ，则返回指定默认字符串，否则返回字符串本身。
     * <p/>
     * <pre>
     *
     *    StringUtil.defaultIfBlank(null, &quot;default&quot;)  = &quot;default&quot;
     *    StringUtil.defaultIfBlank(&quot;&quot;, &quot;default&quot;)    = &quot;default&quot;
     *    StringUtil.defaultIfBlank(&quot;  &quot;, &quot;default&quot;)  = &quot;default&quot;
     *    StringUtil.defaultIfBlank(&quot;bat&quot;, &quot;default&quot;) = &quot;bat&quot;
     *
     * </pre>
     *
     * @param str        要转换的字符串
     * @param defaultStr 默认字符串
     * @return 字符串本身或指定的默认字符串
     */
    public static String defaultIfBlank(String str, String defaultStr) {
        return isBlank(str) ? defaultStr : str;
    }

/* ============================================================================ */
/* 去空白（或指定字符）的函数。 */
/*                                                                              */
/* 以下方法用来除去一个字串中的空白或指定字符。 */
/* ============================================================================ */

    /**
     * 除去字符串头尾部的空白，如果字符串是 <code>null</code> ，依然返回 <code>null</code>。
     * <p/>
     * <p>
     * 注意，和 <code>String.trim</code> 不同，此方法使用
     * <code>Character.isWhitespace</code> 来判定空白， 因而可以除去英文字符集之外的其它空白，如中文空格。
     * <p/>
     * <pre>
     *
     *    StringUtil.trim(null)          = null
     *    StringUtil.trim(&quot;&quot;)            = &quot;&quot;
     *    StringUtil.trim(&quot;     &quot;)       = &quot;&quot;
     *    StringUtil.trim(&quot;abc&quot;)         = &quot;abc&quot;
     *    StringUtil.trim(&quot;    abc    &quot;) = &quot;abc&quot;
     *
     * </pre>
     * <p/>
     * </p>
     *
     * @param str 要处理的字符串
     * @return 除去空白的字符串，如果原字串为 <code>null</code> ，则返回 <code>null</code>
     */
    public static String trim(String str) {
        return trim(str, null, 0);
    }

    /**
     * 除去字符串头尾部的指定字符，如果字符串是 <code>null</code> ，依然返回 <code>null</code>。
     * <p/>
     * <pre>
     *
     *    StringUtil.trim(null, *)          = null
     *    StringUtil.trim(&quot;&quot;, *)            = &quot;&quot;
     *    StringUtil.trim(&quot;abc&quot;, null)      = &quot;abc&quot;
     *    StringUtil.trim(&quot;  abc&quot;, null)    = &quot;abc&quot;
     *    StringUtil.trim(&quot;abc  &quot;, null)    = &quot;abc&quot;
     *    StringUtil.trim(&quot; abc &quot;, null)    = &quot;abc&quot;
     *    StringUtil.trim(&quot;  abcyx&quot;, &quot;xyz&quot;) = &quot;  abc&quot;
     *
     * </pre>
     *
     * @param str        要处理的字符串
     * @param stripChars 要除去的字符，如果为 <code>null</code> 表示除去空白字符
     * @return 除去指定字符后的的字符串，如果原字串为 <code>null</code> ，则返回 <code>null</code>
     */
    public static String trim(String str, String stripChars) {
        return trim(str, stripChars, 0);
    }

    /**
     * 除去字符串头部的空白，如果字符串是 <code>null</code> ，则返回 <code>null</code>。
     * <p/>
     * <p>
     * 注意，和 <code>String.trim</code> 不同，此方法使用
     * <code>Character.isWhitespace</code> 来判定空白， 因而可以除去英文字符集之外的其它空白，如中文空格。
     * <p/>
     * <pre>
     *
     *    StringUtil.trimStart(null)         = null
     *    StringUtil.trimStart(&quot;&quot;)           = &quot;&quot;
     *    StringUtil.trimStart(&quot;abc&quot;)        = &quot;abc&quot;
     *    StringUtil.trimStart(&quot;  abc&quot;)      = &quot;abc&quot;
     *    StringUtil.trimStart(&quot;abc  &quot;)      = &quot;abc  &quot;
     *    StringUtil.trimStart(&quot; abc &quot;)      = &quot;abc &quot;
     *
     * </pre>
     * <p/>
     * </p>
     *
     * @param str 要处理的字符串
     * @return 除去空白的字符串，如果原字串为 <code>null</code> 或结果字符串为 <code>""</code>
     * ，则返回 <code>null</code>
     */
    public static String trimStart(String str) {
        return trim(str, null, -1);
    }

    /**
     * 除去字符串头部的指定字符，如果字符串是 <code>null</code> ，依然返回 <code>null</code>。
     * <p/>
     * <pre>
     *
     *    StringUtil.trimStart(null, *)          = null
     *    StringUtil.trimStart(&quot;&quot;, *)            = &quot;&quot;
     *    StringUtil.trimStart(&quot;abc&quot;, &quot;&quot;)        = &quot;abc&quot;
     *    StringUtil.trimStart(&quot;abc&quot;, null)      = &quot;abc&quot;
     *    StringUtil.trimStart(&quot;  abc&quot;, null)    = &quot;abc&quot;
     *    StringUtil.trimStart(&quot;abc  &quot;, null)    = &quot;abc  &quot;
     *    StringUtil.trimStart(&quot; abc &quot;, null)    = &quot;abc &quot;
     *    StringUtil.trimStart(&quot;yxabc  &quot;, &quot;xyz&quot;) = &quot;abc  &quot;
     *
     * </pre>
     *
     * @param str        要处理的字符串
     * @param stripChars 要除去的字符，如果为 <code>null</code> 表示除去空白字符
     * @return 除去指定字符后的的字符串，如果原字串为 <code>null</code> ，则返回 <code>null</code>
     */
    public static String trimStart(String str, String stripChars) {
        return trim(str, stripChars, -1);
    }

    /**
     * 除去字符串尾部的空白，如果字符串是 <code>null</code> ，则返回 <code>null</code>。
     * <p/>
     * <p>
     * 注意，和 <code>String.trim</code> 不同，此方法使用
     * <code>Character.isWhitespace</code> 来判定空白， 因而可以除去英文字符集之外的其它空白，如中文空格。
     * <p/>
     * <pre>
     *
     *    StringUtil.trimEnd(null)       = null
     *    StringUtil.trimEnd(&quot;&quot;)         = &quot;&quot;
     *    StringUtil.trimEnd(&quot;abc&quot;)      = &quot;abc&quot;
     *    StringUtil.trimEnd(&quot;  abc&quot;)    = &quot;  abc&quot;
     *    StringUtil.trimEnd(&quot;abc  &quot;)    = &quot;abc&quot;
     *    StringUtil.trimEnd(&quot; abc &quot;)    = &quot; abc&quot;
     *
     * </pre>
     * <p/>
     * </p>
     *
     * @param str 要处理的字符串
     * @return 除去空白的字符串，如果原字串为 <code>null</code> 或结果字符串为 <code>""</code>
     * ，则返回 <code>null</code>
     */
    public static String trimEnd(String str) {
        return trim(str, null, 1);
    }

    /**
     * 除去字符串尾部的指定字符，如果字符串是 <code>null</code> ，依然返回 <code>null</code>。
     * <p/>
     * <pre>
     *
     *    StringUtil.trimEnd(null, *)          = null
     *    StringUtil.trimEnd(&quot;&quot;, *)            = &quot;&quot;
     *    StringUtil.trimEnd(&quot;abc&quot;, &quot;&quot;)        = &quot;abc&quot;
     *    StringUtil.trimEnd(&quot;abc&quot;, null)      = &quot;abc&quot;
     *    StringUtil.trimEnd(&quot;  abc&quot;, null)    = &quot;  abc&quot;
     *    StringUtil.trimEnd(&quot;abc  &quot;, null)    = &quot;abc&quot;
     *    StringUtil.trimEnd(&quot; abc &quot;, null)    = &quot; abc&quot;
     *    StringUtil.trimEnd(&quot;  abcyx&quot;, &quot;xyz&quot;) = &quot;  abc&quot;
     *
     * </pre>
     *
     * @param str        要处理的字符串
     * @param stripChars 要除去的字符，如果为 <code>null</code> 表示除去空白字符
     * @return 除去指定字符后的的字符串，如果原字串为 <code>null</code> ，则返回 <code>null</code>
     */
    public static String trimEnd(String str, String stripChars) {
        return trim(str, stripChars, 1);
    }

    /**
     * 除去字符串头尾部的空白，如果结果字符串是空字符串 <code>""</code> ，则返回 <code>null</code>。
     * <p/>
     * <p>
     * 注意，和 <code>String.trim</code> 不同，此方法使用
     * <code>Character.isWhitespace</code> 来判定空白， 因而可以除去英文字符集之外的其它空白，如中文空格。
     * <p/>
     * <pre>
     *
     *    StringUtil.trimToNull(null)          = null
     *    StringUtil.trimToNull(&quot;&quot;)            = null
     *    StringUtil.trimToNull(&quot;     &quot;)       = null
     *    StringUtil.trimToNull(&quot;abc&quot;)         = &quot;abc&quot;
     *    StringUtil.trimToNull(&quot;    abc    &quot;) = &quot;abc&quot;
     *
     * </pre>
     * <p/>
     * </p>
     *
     * @param str 要处理的字符串
     * @return 除去空白的字符串，如果原字串为 <code>null</code> 或结果字符串为 <code>""</code>
     * ，则返回 <code>null</code>
     */
    public static String trimToNull(String str) {
        return trimToNull(str, null);
    }

    /**
     * 除去字符串头尾部的空白，如果结果字符串是空字符串 <code>""</code> ，则返回 <code>null</code>。
     * <p/>
     * <p>
     * 注意，和 <code>String.trim</code> 不同，此方法使用
     * <code>Character.isWhitespace</code> 来判定空白， 因而可以除去英文字符集之外的其它空白，如中文空格。
     * <p/>
     * <pre>
     *
     *    StringUtil.trim(null, *)          = null
     *    StringUtil.trim(&quot;&quot;, *)            = null
     *    StringUtil.trim(&quot;abc&quot;, null)      = &quot;abc&quot;
     *    StringUtil.trim(&quot;  abc&quot;, null)    = &quot;abc&quot;
     *    StringUtil.trim(&quot;abc  &quot;, null)    = &quot;abc&quot;
     *    StringUtil.trim(&quot; abc &quot;, null)    = &quot;abc&quot;
     *    StringUtil.trim(&quot;  abcyx&quot;, &quot;xyz&quot;) = &quot;  abc&quot;
     *
     * </pre>
     * <p/>
     * </p>
     *
     * @param str        要处理的字符串
     * @param stripChars 要除去的字符，如果为 <code>null</code> 表示除去空白字符
     * @return 除去空白的字符串，如果原字串为 <code>null</code> 或结果字符串为 <code>""</code>
     * ，则返回 <code>null</code>
     */
    public static String trimToNull(String str, String stripChars) {
        String result = trim(str, stripChars);

        if ((result == null) || (result.length() == 0)) {
            return null;
        }

        return result;
    }

    /**
     * 除去字符串头尾部的空白，如果字符串是 <code>null</code> ，则返回空字符串 <code>""</code>。
     * <p/>
     * <p>
     * 注意，和 <code>String.trim</code> 不同，此方法使用
     * <code>Character.isWhitespace</code> 来判定空白， 因而可以除去英文字符集之外的其它空白，如中文空格。
     * <p/>
     * <pre>
     *
     *    StringUtil.trimToEmpty(null)          = &quot;&quot;
     *    StringUtil.trimToEmpty(&quot;&quot;)            = &quot;&quot;
     *    StringUtil.trimToEmpty(&quot;     &quot;)       = &quot;&quot;
     *    StringUtil.trimToEmpty(&quot;abc&quot;)         = &quot;abc&quot;
     *    StringUtil.trimToEmpty(&quot;    abc    &quot;) = &quot;abc&quot;
     *
     * </pre>
     * <p/>
     * </p>
     *
     * @param str 要处理的字符串
     * @return 除去空白的字符串，如果原字串为 <code>null</code> 或结果字符串为 <code>""</code>
     * ，则返回 <code>null</code>
     */
    public static String trimToEmpty(String str) {
        return trimToEmpty(str, null);
    }

    /**
     * 除去字符串头尾部的空白，如果字符串是 <code>null</code> ，则返回空字符串 <code>""</code>。
     * <p/>
     * <p>
     * 注意，和 <code>String.trim</code> 不同，此方法使用
     * <code>Character.isWhitespace</code> 来判定空白， 因而可以除去英文字符集之外的其它空白，如中文空格。
     * <p/>
     * <pre>
     *
     *    StringUtil.trim(null, *)          = &quot;&quot;
     *    StringUtil.trim(&quot;&quot;, *)            = &quot;&quot;
     *    StringUtil.trim(&quot;abc&quot;, null)      = &quot;abc&quot;
     *    StringUtil.trim(&quot;  abc&quot;, null)    = &quot;abc&quot;
     *    StringUtil.trim(&quot;abc  &quot;, null)    = &quot;abc&quot;
     *    StringUtil.trim(&quot; abc &quot;, null)    = &quot;abc&quot;
     *    StringUtil.trim(&quot;  abcyx&quot;, &quot;xyz&quot;) = &quot;  abc&quot;
     *
     * </pre>
     * <p/>
     * </p>
     *
     * @param str 要处理的字符串
     * @return 除去空白的字符串，如果原字串为 <code>null</code> 或结果字符串为 <code>""</code>
     * ，则返回 <code>null</code>
     */
    public static String trimToEmpty(String str, String stripChars) {
        String result = trim(str, stripChars);

        if (result == null) {
            return EMPTY_STRING;
        }

        return result;
    }

    /**
     * 除去字符串头尾部的指定字符，如果字符串是 <code>null</code> ，依然返回 <code>null</code>。
     * <p/>
     * <pre>
     *
     *    StringUtil.trim(null, *)          = null
     *    StringUtil.trim(&quot;&quot;, *)            = &quot;&quot;
     *    StringUtil.trim(&quot;abc&quot;, null)      = &quot;abc&quot;
     *    StringUtil.trim(&quot;  abc&quot;, null)    = &quot;abc&quot;
     *    StringUtil.trim(&quot;abc  &quot;, null)    = &quot;abc&quot;
     *    StringUtil.trim(&quot; abc &quot;, null)    = &quot;abc&quot;
     *    StringUtil.trim(&quot;  abcyx&quot;, &quot;xyz&quot;) = &quot;  abc&quot;
     *
     * </pre>
     *
     * @param str        要处理的字符串
     * @param stripChars 要除去的字符，如果为 <code>null</code> 表示除去空白字符
     * @param mode       <code>-1</code> 表示trimStart， <code>0</code> 表示trim全部，
     *                   <code>1</code> 表示trimEnd
     * @return 除去指定字符后的的字符串，如果原字串为 <code>null</code> ，则返回 <code>null</code>
     */
    private static String trim(String str, String stripChars, int mode) {
        if (str == null) {
            return null;
        }

        int length = str.length();
        int start = 0;
        int end = length;
        // 扫描字符串头部
        if (mode <= 0) {
            if (stripChars == null) {
                while ((start < end)
                        && (Character.isWhitespace(str.charAt(start)))) {
                    start++;
                }
            } else if (stripChars.length() == 0) {
                return str;
            } else {
                while ((start < end)
                        && (stripChars.indexOf(str.charAt(start)) != -1)) {
                    start++;
                }
            }
        }
        // 扫描字符串尾部
        if (mode >= 0) {
            if (stripChars == null) {
                while ((start < end)
                        && (Character.isWhitespace(str.charAt(end - 1)))) {
                    end--;
                }
            } else if (stripChars.length() == 0) {
                return str;
            } else {
                while ((start < end)
                        && (stripChars.indexOf(str.charAt(end - 1)) != -1)) {
                    end--;
                }
            }
        }

        if ((start > 0) || (end < length)) {
            return str.substring(start, end);
        }

        return str;
    }

/* ============================================================================ */
/* 比较函数。 */
/*                                                                              */
/* 以下方法用来比较两个字符串是否相同。 */
/* ============================================================================ */

    /**
     * 比较两个字符串（大小写敏感）。
     * <p/>
     * <pre>
     *
     *    StringUtil.equals(null, null)   = true
     *    StringUtil.equals(null, &quot;abc&quot;)  = false
     *    StringUtil.equals(&quot;abc&quot;, null)  = false
     *    StringUtil.equals(&quot;abc&quot;, &quot;abc&quot;) = true
     *    StringUtil.equals(&quot;abc&quot;, &quot;ABC&quot;) = false
     *
     * </pre>
     *
     * @param str1 要比较的字符串1
     * @param str2 要比较的字符串2
     * @return 如果两个字符串相同，或者都是 <code>null</code> ，则返回 <code>true</code>
     */
    public static boolean equals(String str1, String str2) {
        return equals(str1, str2, true);
    }


    public static boolean equals(String str1, String str2, boolean nullValid) {
        if (str1 == null) {
            if (nullValid) return str2 == null;
            else return false;
        }
        return str1.equals(str2);
    }

    /**
     * 比较两个字符串（大小写不敏感）。
     * <p/>
     * <pre>
     *
     *    StringUtil.equalsIgnoreCase(null, null)   = true
     *    StringUtil.equalsIgnoreCase(null, &quot;abc&quot;)  = false
     *    StringUtil.equalsIgnoreCase(&quot;abc&quot;, null)  = false
     *    StringUtil.equalsIgnoreCase(&quot;abc&quot;, &quot;abc&quot;) = true
     *    StringUtil.equalsIgnoreCase(&quot;abc&quot;, &quot;ABC&quot;) = true
     *
     * </pre>
     *
     * @param str1 要比较的字符串1
     * @param str2 要比较的字符串2
     * @return 如果两个字符串相同，或者都是 <code>null</code> ，则返回 <code>true</code>
     */
    public static boolean equalsIgnoreCase(String str1, String str2) {
        return equalsIgnoreCase(str1, str2, true);
    }

    public static boolean equalsIgnoreCase(String str1, String str2, boolean nullValid) {
        if (str1 == null) {
            if (nullValid) return str2 == null;
            else return false;
        }
        return str1.equalsIgnoreCase(str2);
    }

/* ============================================================================ */
/* 大小写转换。 */
/* ============================================================================ */

    /**
     * 将字符串转换成大写。
     * <p/>
     * <p>
     * 如果字符串是 <code>null</code> 则返回 <code>null</code>。
     * <p/>
     * <pre>
     *
     *    StringUtil.toUpperCase(null)  = null
     *    StringUtil.toUpperCase(&quot;&quot;)    = &quot;&quot;
     *    StringUtil.toUpperCase(&quot;aBc&quot;) = &quot;ABC&quot;
     *
     * </pre>
     * <p/>
     * </p>
     *
     * @param str 要转换的字符串
     * @return 大写字符串，如果原字符串为 <code>null</code> ，则返回 <code>null</code>
     */
    public static String toUpperCase(String str) {
        if (str == null) {
            return null;
        }

        return str.toUpperCase();
    }

    /**
     * 将字符串转换成小写。
     * <p/>
     * <p>
     * 如果字符串是 <code>null</code> 则返回 <code>null</code>。
     * <p/>
     * <pre>
     *
     *    StringUtil.toLowerCase(null)  = null
     *    StringUtil.toLowerCase(&quot;&quot;)    = &quot;&quot;
     *    StringUtil.toLowerCase(&quot;aBc&quot;) = &quot;abc&quot;
     *
     * </pre>
     * <p/>
     * </p>
     *
     * @param str 要转换的字符串
     * @return 大写字符串，如果原字符串为 <code>null</code> ，则返回 <code>null</code>
     */
    public static String toLowerCase(String str) {
        if (str == null) {
            return null;
        }

        return str.toLowerCase();
    }

    /**
     * 将字符串的首字符转成大写（ <code>Character.toTitleCase</code> ），其它字符不变。
     * <p/>
     * <p>
     * 如果字符串是 <code>null</code> 则返回 <code>null</code>。
     * <p/>
     * <pre>
     *
     *    StringUtil.capitalize(null)  = null
     *    StringUtil.capitalize(&quot;&quot;)    = &quot;&quot;
     *    StringUtil.capitalize(&quot;cat&quot;) = &quot;Cat&quot;
     *    StringUtil.capitalize(&quot;cAt&quot;) = &quot;CAt&quot;
     *
     * </pre>
     * <p/>
     * </p>
     *
     * @param str 要转换的字符串
     * @return 首字符为大写的字符串，如果原字符串为 <code>null</code> ，则返回 <code>null</code>
     */
    public static String capitalize(String str) {
        int strLen;

        if ((str == null) || ((strLen = str.length()) == 0)) {
            return str;
        }

        return String.valueOf(Character.toTitleCase(str.charAt(0))) + str.substring(1);
    }

    /**
     * 将字符串的首字符转成小写，其它字符不变。
     * <p/>
     * <p>
     * 如果字符串是 <code>null</code> 则返回 <code>null</code>。
     * <p/>
     * <pre>
     *
     *    StringUtil.uncapitalize(null)  = null
     *    StringUtil.uncapitalize(&quot;&quot;)    = &quot;&quot;
     *    StringUtil.uncapitalize(&quot;Cat&quot;) = &quot;cat&quot;
     *    StringUtil.uncapitalize(&quot;CAT&quot;) = &quot;cAT&quot;
     *
     * </pre>
     * <p/>
     * </p>
     *
     * @param str 要转换的字符串
     * @return 首字符为小写的字符串，如果原字符串为 <code>null</code> ，则返回 <code>null</code>
     */
    public static String uncapitalize(String str) {
        int strLen;

        if ((str == null) || ((strLen = str.length()) == 0)) {
            return str;
        }

        return String.valueOf(Character.toLowerCase(str.charAt(0))) +
                str.substring(1);
    }

    /**
     * 反转字符串的大小写。
     * <p/>
     * <p>
     * 如果字符串是 <code>null</code> 则返回 <code>null</code>。
     * <p/>
     * <pre>
     *
     *    StringUtil.swapCase(null)                 = null
     *    StringUtil.swapCase(&quot;&quot;)                   = &quot;&quot;
     *    StringUtil.swapCase(&quot;The dog has a BONE&quot;) = &quot;tHE DOG HAS A bone&quot;
     *
     * </pre>
     * <p/>
     * </p>
     *
     * @param str 要转换的字符串
     * @return 大小写被反转的字符串，如果原字符串为 <code>null</code> ，则返回 <code>null</code>
     */
    public static String swapCase(String str) {
        int strLen;

        if ((str == null) || ((strLen = str.length()) == 0)) {
            return str;
        }

        StringBuilder buffer = new StringBuilder(strLen);

        char ch = 0;

        for (int i = 0; i < strLen; i++) {
            ch = str.charAt(i);

            if (Character.isUpperCase(ch)) {
                ch = Character.toLowerCase(ch);
            } else if (Character.isTitleCase(ch)) {
                ch = Character.toLowerCase(ch);
            } else if (Character.isLowerCase(ch)) {
                ch = Character.toUpperCase(ch);
            }

            buffer.append(ch);
        }

        return buffer.toString();
    }

    /**
     * 将字符串转换成camel case。
     * <p/>
     * <p>
     * 如果字符串是 <code>null</code> 则返回 <code>null</code>。
     * <p/>
     * <pre>
     *
     *    StringUtil.toCamelCase(null)  = null
     *    StringUtil.toCamelCase(&quot;&quot;)    = &quot;&quot;
     *    StringUtil.toCamelCase(&quot;aBc&quot;) = &quot;aBc&quot;
     *    StringUtil.toCamelCase(&quot;aBc def&quot;) = &quot;aBcDef&quot;
     *    StringUtil.toCamelCase(&quot;aBc def_ghi&quot;) = &quot;aBcDefGhi&quot;
     *    StringUtil.toCamelCase(&quot;aBc def_ghi 123&quot;) = &quot;aBcDefGhi123&quot;
     *
     * </pre>
     * <p/>
     * </p>
     * <p/>
     * <p>
     * 此方法会保留除了下划线和空白以外的所有分隔符。
     * </p>
     *
     * @param str 要转换的字符串
     * @return camel case字符串，如果原字符串为 <code>null</code> ，则返回 <code>null</code>
     */
    public static String toCamelCase(String str) {
        return CAMEL_CASE_TOKENIZER.parse(str);
    }

    /**
     * 将字符串转换成pascal case。
     * <p/>
     * <p>
     * 如果字符串是 <code>null</code> 则返回 <code>null</code>。
     * <p/>
     * <pre>
     *
     *    StringUtil.toPascalCase(null)  = null
     *    StringUtil.toPascalCase(&quot;&quot;)    = &quot;&quot;
     *    StringUtil.toPascalCase(&quot;aBc&quot;) = &quot;ABc&quot;
     *    StringUtil.toPascalCase(&quot;aBc def&quot;) = &quot;ABcDef&quot;
     *    StringUtil.toPascalCase(&quot;aBc def_ghi&quot;) = &quot;ABcDefGhi&quot;
     *    StringUtil.toPascalCase(&quot;aBc def_ghi 123&quot;) = &quot;aBcDefGhi123&quot;
     *
     * </pre>
     * <p/>
     * </p>
     * <p/>
     * <p>
     * 此方法会保留除了下划线和空白以外的所有分隔符。
     * </p>
     *
     * @param str 要转换的字符串
     * @return pascal case字符串，如果原字符串为 <code>null</code> ，则返回 <code>null</code>
     */
    public static String toPascalCase(String str) {
        return PASCAL_CASE_TOKENIZER.parse(str);
    }

    /**
     * 将字符串转换成下划线分隔的大写字符串。
     * <p/>
     * <p>
     * 如果字符串是 <code>null</code> 则返回 <code>null</code>。
     * <p/>
     * <pre>
     *
     *    StringUtil.toUpperCaseWithUnderscores(null)  = null
     *    StringUtil.toUpperCaseWithUnderscores(&quot;&quot;)    = &quot;&quot;
     *    StringUtil.toUpperCaseWithUnderscores(&quot;aBc&quot;) = &quot;A_BC&quot;
     *    StringUtil.toUpperCaseWithUnderscores(&quot;aBc def&quot;) = &quot;A_BC_DEF&quot;
     *    StringUtil.toUpperCaseWithUnderscores(&quot;aBc def_ghi&quot;) = &quot;A_BC_DEF_GHI&quot;
     *    StringUtil.toUpperCaseWithUnderscores(&quot;aBc def_ghi 123&quot;) = &quot;A_BC_DEF_GHI_123&quot;
     *    StringUtil.toUpperCaseWithUnderscores(&quot;__a__Bc__&quot;) = &quot;__A__BC__&quot;
     *
     * </pre>
     * <p/>
     * </p>
     * <p/>
     * <p>
     * 此方法会保留除了空白以外的所有分隔符。
     * </p>
     *
     * @param str 要转换的字符串
     * @return 下划线分隔的大写字符串，如果原字符串为 <code>null</code> ，则返回 <code>null</code>
     */
    public static String toUpperCaseWithUnderscores(String str) {
        return UPPER_CASE_WITH_UNDERSCORES_TOKENIZER.parse(str);
    }

    /**
     * 将字符串转换成下划线分隔的小写字符串。
     * <p/>
     * <p>
     * 如果字符串是 <code>null</code> 则返回 <code>null</code>。
     * <p/>
     * <pre>
     *
     *    StringUtil.toLowerCaseWithUnderscores(null)  = null
     *    StringUtil.toLowerCaseWithUnderscores(&quot;&quot;)    = &quot;&quot;
     *    StringUtil.toLowerCaseWithUnderscores(&quot;aBc&quot;) = &quot;a_bc&quot;
     *    StringUtil.toLowerCaseWithUnderscores(&quot;aBc def&quot;) = &quot;a_bc_def&quot;
     *    StringUtil.toLowerCaseWithUnderscores(&quot;aBc def_ghi&quot;) = &quot;a_bc_def_ghi&quot;
     *    StringUtil.toLowerCaseWithUnderscores(&quot;aBc def_ghi 123&quot;) = &quot;a_bc_def_ghi_123&quot;
     *    StringUtil.toLowerCaseWithUnderscores(&quot;__a__Bc__&quot;) = &quot;__a__bc__&quot;
     *
     * </pre>
     * <p/>
     * </p>
     * <p/>
     * <p>
     * 此方法会保留除了空白以外的所有分隔符。
     * </p>
     *
     * @param str 要转换的字符串
     * @return 下划线分隔的小写字符串，如果原字符串为 <code>null</code> ，则返回 <code>null</code>
     */
    public static String toLowerCaseWithUnderscores(String str) {
        return LOWER_CASE_WITH_UNDERSCORES_TOKENIZER.parse(str);
    }

    /**
     * 解析单词的解析器。
     */
    private static final WordTokenizer CAMEL_CASE_TOKENIZER = new WordTokenizer() {
        protected void startSentence(StringBuilder buffer, char ch) {
            buffer.append(Character.toLowerCase(ch));
        }

        protected void startWord(StringBuilder buffer, char ch) {
            if (!isDelimiter(buffer.charAt(buffer.length() - 1))) {
                buffer.append(Character.toUpperCase(ch));
            } else {
                buffer.append(Character.toLowerCase(ch));
            }
        }

        protected void inWord(StringBuilder buffer, char ch) {
            buffer.append(Character.toLowerCase(ch));
        }

        protected void startDigitSentence(StringBuilder buffer, char ch) {
            buffer.append(ch);
        }

        protected void startDigitWord(StringBuilder buffer, char ch) {
            buffer.append(ch);
        }

        protected void inDigitWord(StringBuilder buffer, char ch) {
            buffer.append(ch);
        }

        protected void inDelimiter(StringBuilder buffer, char ch) {
            if (ch != UNDERSCORE) {
                buffer.append(ch);
            }
        }
    };

    private static final WordTokenizer PASCAL_CASE_TOKENIZER = new WordTokenizer() {
        protected void startSentence(StringBuilder buffer, char ch) {
            buffer.append(Character.toUpperCase(ch));
        }

        protected void startWord(StringBuilder buffer, char ch) {
            buffer.append(Character.toUpperCase(ch));
        }

        protected void inWord(StringBuilder buffer, char ch) {
            buffer.append(Character.toLowerCase(ch));
        }

        protected void startDigitSentence(StringBuilder buffer, char ch) {
            buffer.append(ch);
        }

        protected void startDigitWord(StringBuilder buffer, char ch) {
            buffer.append(ch);
        }

        protected void inDigitWord(StringBuilder buffer, char ch) {
            buffer.append(ch);
        }

        protected void inDelimiter(StringBuilder buffer, char ch) {
            if (ch != UNDERSCORE) {
                buffer.append(ch);
            }
        }
    };

    private static final WordTokenizer UPPER_CASE_WITH_UNDERSCORES_TOKENIZER = new WordTokenizer() {
        protected void startSentence(StringBuilder buffer, char ch) {
            buffer.append(Character.toUpperCase(ch));
        }

        protected void startWord(StringBuilder buffer, char ch) {
            if (!isDelimiter(buffer.charAt(buffer.length() - 1))) {
                buffer.append(UNDERSCORE);
            }

            buffer.append(Character.toUpperCase(ch));
        }

        protected void inWord(StringBuilder buffer, char ch) {
            buffer.append(Character.toUpperCase(ch));
        }

        protected void startDigitSentence(StringBuilder buffer, char ch) {
            buffer.append(ch);
        }

        protected void startDigitWord(StringBuilder buffer, char ch) {
            if (!isDelimiter(buffer.charAt(buffer.length() - 1))) {
                buffer.append(UNDERSCORE);
            }

            buffer.append(ch);
        }

        protected void inDigitWord(StringBuilder buffer, char ch) {
            buffer.append(ch);
        }

        protected void inDelimiter(StringBuilder buffer, char ch) {
            buffer.append(ch);
        }
    };

    private static final WordTokenizer LOWER_CASE_WITH_UNDERSCORES_TOKENIZER = new WordTokenizer() {
        protected void startSentence(StringBuilder buffer, char ch) {
            buffer.append(Character.toLowerCase(ch));
        }

        protected void startWord(StringBuilder buffer, char ch) {
            if (!isDelimiter(buffer.charAt(buffer.length() - 1))) {
                buffer.append(UNDERSCORE);
            }

            buffer.append(Character.toLowerCase(ch));
        }

        protected void inWord(StringBuilder buffer, char ch) {
            buffer.append(Character.toLowerCase(ch));
        }

        protected void startDigitSentence(StringBuilder buffer, char ch) {
            buffer.append(ch);
        }

        protected void startDigitWord(StringBuilder buffer, char ch) {
            if (!isDelimiter(buffer.charAt(buffer.length() - 1))) {
                buffer.append(UNDERSCORE);
            }

            buffer.append(ch);
        }

        protected void inDigitWord(StringBuilder buffer, char ch) {
            buffer.append(ch);
        }

        protected void inDelimiter(StringBuilder buffer, char ch) {
            buffer.append(ch);
        }
    };

    public static String getString(String value) {
        if (isEmpty(value)) return "";
        return value;
    }

    /**
     * 解析出下列语法所构成的 <code>SENTENCE</code>。
     * <p/>
     * <pre>
     *
     *     SENTENCE = WORD (DELIMITER* WORD)*
     *
     *     WORD = UPPER_CASE_WORD | LOWER_CASE_WORD | TITLE_CASE_WORD | DIGIT_WORD
     *
     *     UPPER_CASE_WORD = UPPER_CASE_LETTER+
     *     LOWER_CASE_WORD = LOWER_CASE_LETTER+
     *     TITLE_CASE_WORD = UPPER_CASE_LETTER LOWER_CASE_LETTER+
     *     DIGIT_WORD      = DIGIT+
     *
     *     UPPER_CASE_LETTER = Character.isUpperCase()
     *     LOWER_CASE_LETTER = Character.isLowerCase()
     *     DIGIT             = Character.isDigit()
     *     NON_LETTER_DIGIT  = !Character.isUpperCase() &amp;&amp; !Character.isLowerCase() &amp;&amp; !Character.isDigit()
     *
     *     DELIMITER = WHITESPACE | NON_LETTER_DIGIT
     *
     * </pre>
     */
    private abstract static class WordTokenizer {
        protected static final char UNDERSCORE = '_';

        /**
         * Parse sentence。
         */
        public String parse(String str) {
            if (isEmpty(str)) {
                return str;
            }

            int length = str.length();
            StringBuilder buffer = new StringBuilder(length);

            for (int index = 0; index < length; index++) {
                char ch = str.charAt(index);

// 忽略空白。
                if (Character.isWhitespace(ch)) {
                    continue;
                }

// 大写字母开始：UpperCaseWord或是TitleCaseWord。
                if (Character.isUpperCase(ch)) {
                    int wordIndex = index + 1;

                    while (wordIndex < length) {
                        char wordChar = str.charAt(wordIndex);

                        if (Character.isUpperCase(wordChar)) {
                            wordIndex++;
                        } else if (Character.isLowerCase(wordChar)) {
                            wordIndex--;
                            break;
                        } else {
                            break;
                        }
                    }

// 1. wordIndex == length，说明最后一个字母为大写，以upperCaseWord处理之。
// 2. wordIndex == index，说明index处为一个titleCaseWord。
// 3. wordIndex > index，说明index到wordIndex -
// 1处全部是大写，以upperCaseWord处理。
                    if ((wordIndex == length) || (wordIndex > index)) {
                        index = parseUpperCaseWord(buffer, str, index,
                                wordIndex);
                    } else {
                        index = parseTitleCaseWord(buffer, str, index);
                    }

                    continue;
                }

// 小写字母开始：LowerCaseWord。
                if (Character.isLowerCase(ch)) {
                    index = parseLowerCaseWord(buffer, str, index);
                    continue;
                }

// 数字开始：DigitWord。
                if (Character.isDigit(ch)) {
                    index = parseDigitWord(buffer, str, index);
                    continue;
                }

// 非字母数字开始：Delimiter。
                inDelimiter(buffer, ch);
            }

            return buffer.toString();
        }

        private int parseUpperCaseWord(StringBuilder buffer, String str,
                                       int index, int length) {
            char ch = str.charAt(index++);

// 首字母，必然存在且为大写。
            if (buffer.length() == 0) {
                startSentence(buffer, ch);
            } else {
                startWord(buffer, ch);
            }

// 后续字母，必为小写。
            for (; index < length; index++) {
                ch = str.charAt(index);
                inWord(buffer, ch);
            }

            return index - 1;
        }

        private int parseLowerCaseWord(StringBuilder buffer, String str,
                                       int index) {
            char ch = str.charAt(index++);

// 首字母，必然存在且为小写。
            if (buffer.length() == 0) {
                startSentence(buffer, ch);
            } else {
                startWord(buffer, ch);
            }

// 后续字母，必为小写。
            int length = str.length();

            for (; index < length; index++) {
                ch = str.charAt(index);

                if (Character.isLowerCase(ch)) {
                    inWord(buffer, ch);
                } else {
                    break;
                }
            }

            return index - 1;
        }

        private int parseTitleCaseWord(StringBuilder buffer, String str,
                                       int index) {
            char ch = str.charAt(index++);

// 首字母，必然存在且为大写。
            if (buffer.length() == 0) {
                startSentence(buffer, ch);
            } else {
                startWord(buffer, ch);
            }

// 后续字母，必为小写。
            int length = str.length();

            for (; index < length; index++) {
                ch = str.charAt(index);

                if (Character.isLowerCase(ch)) {
                    inWord(buffer, ch);
                } else {
                    break;
                }
            }

            return index - 1;
        }

        private int parseDigitWord(StringBuilder buffer, String str, int index) {
            char ch = str.charAt(index++);

// 首字符，必然存在且为数字。
            if (buffer.length() == 0) {
                startDigitSentence(buffer, ch);
            } else {
                startDigitWord(buffer, ch);
            }

// 后续字符，必为数字。
            int length = str.length();

            for (; index < length; index++) {
                ch = str.charAt(index);

                if (Character.isDigit(ch)) {
                    inDigitWord(buffer, ch);
                } else {
                    break;
                }
            }

            return index - 1;
        }

        protected boolean isDelimiter(char ch) {
            return !Character.isUpperCase(ch) && !Character.isLowerCase(ch)
                    && !Character.isDigit(ch);
        }

        protected abstract void startSentence(StringBuilder buffer, char ch);

        protected abstract void startWord(StringBuilder buffer, char ch);

        protected abstract void inWord(StringBuilder buffer, char ch);

        protected abstract void startDigitSentence(StringBuilder buffer, char ch);

        protected abstract void startDigitWord(StringBuilder buffer, char ch);

        protected abstract void inDigitWord(StringBuilder buffer, char ch);

        protected abstract void inDelimiter(StringBuilder buffer, char ch);
    }

/* ============================================================================ */
/* 字符串分割函数。 */
/*                                                                              */
/* 将字符串按指定分隔符分割。 */
/* ============================================================================ */

    /**
     * 将字符串按空白字符分割。
     * <p/>
     * <p>
     * 分隔符不会出现在目标数组中，连续的分隔符就被看作一个。如果字符串为 <code>null</code> ，则返回
     * <code>null</code>。
     * <p/>
     * <pre>
     *
     *    StringUtil.split(null)       = null
     *    StringUtil.split(&quot;&quot;)         = []
     *    StringUtil.split(&quot;abc def&quot;)  = [&quot;abc&quot;, &quot;def&quot;]
     *    StringUtil.split(&quot;abc  def&quot;) = [&quot;abc&quot;, &quot;def&quot;]
     *    StringUtil.split(&quot; abc &quot;)    = [&quot;abc&quot;]
     *
     * </pre>
     * <p/>
     * </p>
     *
     * @param str 要分割的字符串
     * @return 分割后的字符串数组，如果原字符串为 <code>null</code> ，则返回 <code>null</code>
     */
    public static String[] split(String str) {
        return split(str, null, -1);
    }

    /**
     * 将字符串按指定字符分割。
     * <p/>
     * <p>
     * 分隔符不会出现在目标数组中，连续的分隔符就被看作一个。如果字符串为 <code>null</code> ，则返回
     * <code>null</code>。
     * <p/>
     * <pre>
     *
     *    StringUtil.split(null, *)         = null
     *    StringUtil.split(&quot;&quot;, *)           = []
     *    StringUtil.split(&quot;a.b.c&quot;, '.')    = [&quot;a&quot;, &quot;b&quot;, &quot;c&quot;]
     *    StringUtil.split(&quot;a..b.c&quot;, '.')   = [&quot;a&quot;, &quot;b&quot;, &quot;c&quot;]
     *    StringUtil.split(&quot;a:b:c&quot;, '.')    = [&quot;a:b:c&quot;]
     *    StringUtil.split(&quot;a b c&quot;, ' ')    = [&quot;a&quot;, &quot;b&quot;, &quot;c&quot;]
     *
     * </pre>
     * <p/>
     * </p>
     *
     * @param str           要分割的字符串
     * @param separatorChar 分隔符
     * @return 分割后的字符串数组，如果原字符串为 <code>null</code> ，则返回 <code>null</code>
     */
    public static String[] split(String str, char separatorChar) {
        if (str == null) {
            return null;
        }

        int length = str.length();

        if (length == 0) {
            return null;
        }

        List<String> list = new ArrayList<String>();
        int i = 0;
        int start = 0;
        boolean match = false;

        while (i < length) {
            if (str.charAt(i) == separatorChar) {
                if (match) {
                    list.add(str.substring(start, i));
                    match = false;
                }

                start = ++i;
                continue;
            }

            match = true;
            i++;
        }

        if (match) {
            list.add(str.substring(start, i));
        }

        return (String[]) list.toArray(new String[list.size()]);
    }

    /**
     * 将字符串按指定字符分割。
     * <p/>
     * <p>
     * 分隔符不会出现在目标数组中，连续的分隔符就被看作一个。如果字符串为 <code>null</code> ，则返回
     * <code>null</code>。
     * <p/>
     * <pre>
     *
     *    StringUtil.split(null, *)                = null
     *    StringUtil.split(&quot;&quot;, *)                  = []
     *    StringUtil.split(&quot;abc def&quot;, null)        = [&quot;abc&quot;, &quot;def&quot;]
     *    StringUtil.split(&quot;abc def&quot;, &quot; &quot;)         = [&quot;abc&quot;, &quot;def&quot;]
     *    StringUtil.split(&quot;abc  def&quot;, &quot; &quot;)        = [&quot;abc&quot;, &quot;def&quot;]
     *    StringUtil.split(&quot; ab:  cd::ef  &quot;, &quot;:&quot;)  = [&quot;ab&quot;, &quot;cd&quot;, &quot;ef&quot;]
     *    StringUtil.split(&quot;abc.def&quot;, &quot;&quot;)          = [&quot;abc.def&quot;]
     *
     * </pre>
     * <p/>
     * </p>
     *
     * @param str            要分割的字符串
     * @param separatorChars 分隔符
     * @return 分割后的字符串数组，如果原字符串为 <code>null</code> ，则返回 <code>null</code>
     */
    public static String[] split(String str, String separatorChars) {
        return split(str, separatorChars, -1);
    }

    /**
     * 将字符串按指定字符分割。
     * <p/>
     * <p>
     * 分隔符不会出现在目标数组中，连续的分隔符就被看作一个。如果字符串为 <code>null</code> ，则返回
     * <code>null</code>。
     * <p/>
     * <pre>
     *
     *    StringUtil.split(null, *, *)                 = null
     *    StringUtil.split(&quot;&quot;, *, *)                   = []
     *    StringUtil.split(&quot;ab cd ef&quot;, null, 0)        = [&quot;ab&quot;, &quot;cd&quot;, &quot;ef&quot;]
     *    StringUtil.split(&quot;  ab   cd ef  &quot;, null, 0)  = [&quot;ab&quot;, &quot;cd&quot;, &quot;ef&quot;]
     *    StringUtil.split(&quot;ab:cd::ef&quot;, &quot;:&quot;, 0)        = [&quot;ab&quot;, &quot;cd&quot;, &quot;ef&quot;]
     *    StringUtil.split(&quot;ab:cd:ef&quot;, &quot;:&quot;, 2)         = [&quot;ab&quot;, &quot;cdef&quot;]
     *    StringUtil.split(&quot;abc.def&quot;, &quot;&quot;, 2)           = [&quot;abc.def&quot;]
     *
     * </pre>
     * <p/>
     * </p>
     *
     * @param str            要分割的字符串
     * @param separatorChars 分隔符
     * @param max            返回的数组的最大个数，如果小于等于0，则表示无限制
     * @return 分割后的字符串数组，如果原字符串为 <code>null</code> ，则返回 <code>null</code>
     */
    public static String[] split(String str, String separatorChars, int max) {
        if (str == null) {
            return null;
        }

        int length = str.length();

        if (length == 0) {
            return null;
        }

        List<String> list = new ArrayList<String>();
        int sizePlus1 = 1;
        int i = 0;
        int start = 0;
        boolean match = false;

        if (separatorChars == null) {
// null表示使用空白作为分隔符
            while (i < length) {
                if (Character.isWhitespace(str.charAt(i))) {
                    if (match) {
                        if (sizePlus1++ == max) {
                            i = length;
                        }

                        list.add(str.substring(start, i));
                        match = false;
                    }

                    start = ++i;
                    continue;
                }

                match = true;
                i++;
            }
        } else if (separatorChars.length() == 1) {
// 优化分隔符长度为1的情形
            char sep = separatorChars.charAt(0);

            while (i < length) {
                if (str.charAt(i) == sep) {
                    if (match) {
                        if (sizePlus1++ == max) {
                            i = length;
                        }

                        list.add(str.substring(start, i));
                        match = false;
                    }

                    start = ++i;
                    continue;
                }

                match = true;
                i++;
            }
        } else {
// 一般情形
            while (i < length) {
                if (separatorChars.indexOf(str.charAt(i)) >= 0) {
                    if (match) {
                        if (sizePlus1++ == max) {
                            i = length;
                        }

                        list.add(str.substring(start, i));
                        match = false;
                    }

                    start = ++i;
                    continue;
                }

                match = true;
                i++;
            }
        }

        if (match) {
            list.add(str.substring(start, i));
        }

        return (String[]) list.toArray(new String[list.size()]);
    }

/* ============================================================================ */
/* 字符串截取函数。 */
/* ============================================================================ */

    public enum CutRemainPart {Left, Right}

    ;

    public static String cut(String source, CutRemainPart remainPart, int remainLength) {

        if (isEmpty(source) || remainLength <= 0)
            return EMPTY_STRING;

        if (source.length() <= remainLength)
            return source;

        if (remainPart == null) remainPart = CutRemainPart.Left;
        if (remainPart == CutRemainPart.Right)
            return source.substring(source.length() - remainLength);
        else
            return source.substring(0, remainLength);
    }


/* ============================================================================ */
/* 字符串连接函数。 */
/*                                                                              */
/* 将多个对象按指定分隔符连接成字符串。 */
/* ============================================================================ */

    public static String join(char sep, String... array) {
        return join(array, sep);
    }

    public static String join(String... array) {
        return join(array, null);
    }

    /**
     * 将数组中的元素连接成一个字符串。
     * <p/>
     * <pre>
     *
     *    StringUtil.join(null)            = null
     *    StringUtil.join([])              = &quot;&quot;
     *    StringUtil.join([null])          = &quot;&quot;
     *    StringUtil.join([&quot;a&quot;, &quot;b&quot;, &quot;c&quot;]) = &quot;abc&quot;
     *    StringUtil.join([null, &quot;&quot;, &quot;a&quot;]) = &quot;a&quot;
     *
     * </pre>
     *
     * @param array 要连接的数组
     * @return 连接后的字符串，如果原数组为 <code>null</code> ，则返回 <code>null</code>
     */
    public static String join(Object[] array) {
        return join(array, null);
    }

    /**
     * 将数组中的元素连接成一个字符串。
     * <p/>
     * <pre>
     *
     *    StringUtil.join(null, *)               = null
     *    StringUtil.join([], *)                 = &quot;&quot;
     *    StringUtil.join([null], *)             = &quot;&quot;
     *    StringUtil.join([&quot;a&quot;, &quot;b&quot;, &quot;c&quot;], ';')  = &quot;a;b;c&quot;
     *    StringUtil.join([&quot;a&quot;, &quot;b&quot;, &quot;c&quot;], null) = &quot;abc&quot;
     *    StringUtil.join([null, &quot;&quot;, &quot;a&quot;], ';')  = &quot;;;a&quot;
     *
     * </pre>
     *
     * @param array     要连接的数组
     * @param separator 分隔符
     * @return 连接后的字符串，如果原数组为 <code>null</code> ，则返回 <code>null</code>
     */
    public static String join(Object[] array, char separator) {
        if (array == null) {
            return null;
        }

        int arraySize = array.length;
        int bufSize = (arraySize == 0) ? 0 : ((((array[0] == null) ? 16
                : array[0].toString().length()) + 1) * arraySize);
        StringBuilder buf = new StringBuilder(bufSize);

        for (int i = 0; i < arraySize; i++) {
            if (i > 0) {
                buf.append(separator);
            }

            if (array[i] != null) {
                buf.append(array[i]);
            }
        }

        return buf.toString();
    }

    /**
     * 将数组中的元素连接成一个字符串。
     * <p/>
     * <pre>
     *
     *    StringUtil.join(null, *)                = null
     *    StringUtil.join([], *)                  = &quot;&quot;
     *    StringUtil.join([null], *)              = &quot;&quot;
     *    StringUtil.join([&quot;a&quot;, &quot;b&quot;, &quot;c&quot;], &quot;--&quot;)  = &quot;a--b--c&quot;
     *    StringUtil.join([&quot;a&quot;, &quot;b&quot;, &quot;c&quot;], null)  = &quot;abc&quot;
     *    StringUtil.join([&quot;a&quot;, &quot;b&quot;, &quot;c&quot;], &quot;&quot;)    = &quot;abc&quot;
     *    StringUtil.join([null, &quot;&quot;, &quot;a&quot;], ',')   = &quot;,,a&quot;
     *
     * </pre>
     *
     * @param array     要连接的数组
     * @param separator 分隔符
     * @return 连接后的字符串，如果原数组为 <code>null</code> ，则返回 <code>null</code>
     */
    public static String join(Object[] array, String separator) {
        if (array == null) {
            return null;
        }

        int arraySize = array.length;

// ArraySize == 0: Len = 0
// ArraySize > 0: Len = NofStrings *(len(firstString) + len(separator))
// (估计大约所有的字符串都一样长)
        int bufSize = (arraySize == 0) ? 0
                : (arraySize * (((array[0] == null) ? 16 : array[0].toString()
                .length()) + ((separator != null) ? separator.length()
                : 0)));

        StringBuilder buf = new StringBuilder(bufSize);

        for (int i = 0; i < arraySize; i++) {
            if ((separator != null) && (i > 0)) {
                buf.append(separator);
            }

            if (array[i] != null) {
                buf.append(array[i]);
            }
        }

        return buf.toString();
    }

    public static String join(long[] array, String separator) {
        if (array == null) {
            return null;
        }

        if (separator == null) {
            separator = EMPTY_STRING;
        }

        int arraySize = array.length;

        StringBuilder buf = new StringBuilder(256);

        for (int i = 0; i < arraySize; i++) {
            if ((i > 0)) {
                buf.append(separator);
            }
            buf.append(array[i]);
        }

        return buf.toString();
    }

    /**
     * 将 <code>Iterator</code> 中的元素连接成一个字符串。
     * <p/>
     * <pre>
     *
     *    StringUtil.join(null, *)                = null
     *    StringUtil.join([], *)                  = &quot;&quot;
     *    StringUtil.join([null], *)              = &quot;&quot;
     *    StringUtil.join([&quot;a&quot;, &quot;b&quot;, &quot;c&quot;], &quot;--&quot;)  = &quot;a--b--c&quot;
     *    StringUtil.join([&quot;a&quot;, &quot;b&quot;, &quot;c&quot;], null)  = &quot;abc&quot;
     *    StringUtil.join([&quot;a&quot;, &quot;b&quot;, &quot;c&quot;], &quot;&quot;)    = &quot;abc&quot;
     *    StringUtil.join([null, &quot;&quot;, &quot;a&quot;], ',')   = &quot;,,a&quot;
     *
     * </pre>
     *
     * @param iterator  要连接的 <code>Iterator</code>
     * @param separator 分隔符
     * @return 连接后的字符串，如果原数组为 <code>null</code> ，则返回 <code>null</code>
     */
    public static String join(Iterator iterator, char separator) {
        if (iterator == null) {
            return null;
        }

        StringBuilder buf = new StringBuilder(256); // Java默认值是16, 可能偏小

        while (iterator.hasNext()) {
            Object obj = iterator.next();

            if (obj != null) {
                buf.append(obj);
            }

            if (iterator.hasNext()) {
                buf.append(separator);
            }
        }

        return buf.toString();
    }

    /**
     * 将 <code>Iterator</code> 中的元素连接成一个字符串。
     * <p/>
     * <pre>
     *
     *    StringUtil.join(null, *)                = null
     *    StringUtil.join([], *)                  = &quot;&quot;
     *    StringUtil.join([null], *)              = &quot;&quot;
     *    StringUtil.join([&quot;a&quot;, &quot;b&quot;, &quot;c&quot;], &quot;--&quot;)  = &quot;a--b--c&quot;
     *    StringUtil.join([&quot;a&quot;, &quot;b&quot;, &quot;c&quot;], null)  = &quot;abc&quot;
     *    StringUtil.join([&quot;a&quot;, &quot;b&quot;, &quot;c&quot;], &quot;&quot;)    = &quot;abc&quot;
     *    StringUtil.join([null, &quot;&quot;, &quot;a&quot;], ',')   = &quot;,,a&quot;
     *
     * </pre>
     *
     * @param iterator  要连接的 <code>Iterator</code>
     * @param separator 分隔符
     * @return 连接后的字符串，如果原数组为 <code>null</code> ，则返回 <code>null</code>
     */
    public static String join(Iterator iterator, String separator) {
        if (iterator == null) {
            return null;
        }

        StringBuilder buf = new StringBuilder(256); // Java默认值是16, 可能偏小

        while (iterator.hasNext()) {
            Object obj = iterator.next();

            if (obj != null) {
                buf.append(obj);
            }

            if ((separator != null) && iterator.hasNext()) {
                buf.append(separator);
            }
        }
        return buf.toString();
    }

    /* ============================================================================ */
    /* 字符串补齐函数。 */
    /* ============================================================================ */

    /**
     * 在字符串前加0补全数字字符串至指定长度
     *
     * @param number       原始字符串
     * @param targetLength 结果字符串总长度
     * @return 指定长度字符串
     */
    public static String lengthenNumber(Number number, int targetLength) {
        if (number == null) number = 0;
        return lengthenByPreChar(number.toString(), targetLength, '0');
    }

    /**
     * 在字符串前使用指定字符补全至指定长度
     *
     * @param source       原始字符串
     * @param targetLength 结果字符串总长度
     * @param fillChar     指定补全字符
     * @return 指定长度字符串
     */
    public static String lengthenByPreChar(String source, int targetLength, char fillChar) {
        return getFixedLengthStringByChar(source, targetLength, FillDirection.Leading, fillChar);
    }

    /**
     * 在字符串后使用指定字符补全至指定长度
     *
     * @param source       原始字符串
     * @param targetLength 结果字符串总长度
     * @param specChar     指定补全字符
     * @return 指定长度字符串
     */
    public static String lengthenByAppendChar(String source, int targetLength, char specChar) {
        return getFixedLengthStringByChar(source, targetLength, FillDirection.Trailing, specChar);
    }


    public enum CharRandomType {Uppercase, Lowercase, LetterIgnoreCase, Number, Mix}

    /**
     * 在字符串前使用随机字符补全至指定长度
     *
     * @param source       原始字符串
     * @param targetLength 结果字符串总长度
     * @param randomType   补全字符类型
     * @return 指定长度字符串
     */
    public static String lengthenByPreRandom(String source, int targetLength, CharRandomType randomType) {
        return getFixedLengthStringByRandom(source, targetLength, FillDirection.Leading, randomType);
    }

    /**
     * 在字符串后使用随机字符补全至指定长度
     *
     * @param source       原始字符串
     * @param targetLength 结果字符串总长度
     * @param randomType   补全字符类型
     * @return 指定长度字符串
     */
    public static String lengthenByAppendRandom(String source, int targetLength, CharRandomType randomType) {
        return getFixedLengthStringByRandom(source, targetLength, FillDirection.Trailing, randomType);
    }

    private static char[] getFillCharArray(int length, char fillChar) {
        char[] chars = new char[length];
        for (int i = 0; i < length; i++) {
            chars[i] = fillChar;
        }
        return chars;
    }

    private static char[] getFillCharArray(int length, CharRandomType type) {
        char[] chars = new char[length];
        int currentType; // 0-大写字母，1-小写字母，2-数字
        for (int i = 0; i < length; i++) {
            switch (type) {
                case Uppercase:
                    currentType = 0;
                    break;
                case Lowercase:
                    currentType = 1;
                    break;
                case LetterIgnoreCase:
                    currentType = new Random().nextInt(2);
                    break;
                case Number:
                    currentType = 2;
                    break;
                case Mix:
                    currentType = new Random().nextInt(3);
                    break;
                default:
                    currentType = -1;
                    break;
            }
            if (currentType == 0)
                chars[i] = (char) (65 + (new Random()).nextInt(25));
            else if (currentType == 1)
                chars[i] = (char) (97 + (new Random()).nextInt(25));
            else if (currentType == 2)
                chars[i] = (char) (48 + (new Random()).nextInt(9));
            else
                chars[i] = '0';
        }
        return chars;
    }

    private static String checkFillSource(String srcValue, int length) {
        if (srcValue == null)
            srcValue = EMPTY_STRING;
        if (length == srcValue.length())
            return srcValue;
        if (length < srcValue.length())
            return srcValue.substring(0, length);
        return null;
    }

    private enum FillDirection {Leading, Trailing, Middle}

    private static String getFixedLengthStringByChar(String source, int length, FillDirection direction, char fixedChar) {
        return getFixedLengthString(source, length, direction, null, fixedChar);
    }

    private static String getFixedLengthStringByRandom(String source, int length, FillDirection direction, CharRandomType randomType) {
        return getFixedLengthString(source, length, direction, randomType, '0');
    }

    private static String getFixedLengthString(String source, int length, FillDirection direction, CharRandomType randomType, char fixedChar) {
        String result = checkFillSource(source, length);
        if (result != null)
            return result;

        char[] chars;
        if (randomType == null)
            chars = getFillCharArray(length - source.length(), fixedChar);
        else
            chars = getFillCharArray(length - source.length(), randomType);

        if (direction == FillDirection.Trailing)
            return source + String.valueOf(chars);

        return String.valueOf(chars) + source;
    }

    public static String getEllipsis(String str, int length) {
        if (!hasText(str)) return "";
        if (str.length() <= length) return str;
        return str.substring(0, length) + "...";
    }

    public static String safeMobile(String mobile) {
        return safe(mobile, '*', 5);
    }

    public static String safe(String src, char safe, int len) {
        if (!hasText(src)) return "";
//        if (direction == null) direction = FillDirection.Middle;
        int start = (src.length() - len) / 2;
        StringBuilder builder = new StringBuilder();
        builder.append(src.substring(0, start));
        for (int i = 0; i < len; i++) {
            builder.append(safe);
        }
        builder.append(src.substring(start + len - 1));
        return builder.toString();
    }

    /**
     * 字符串转化成为16进制字符串
     *
     * @param s
     * @return
     */
    public static String strTo16(String s) {
        String str = "";
        for (int i = 0; i < s.length(); i++) {
            int ch = (int) s.charAt(i);
            String s4 = Integer.toHexString(ch);
            str = str + s4;
        }
        return str;
    }

    /**
     * 16进制转换成为string类型字符串
     *
     * @param s
     * @return
     */
    public static String hexStringToString(String s) {
        if (s == null || s.equals("")) {
            return null;
        }
        s = s.replace(" ", "");
        byte[] baKeyword = new byte[s.length() / 2];
        for (int i = 0; i < baKeyword.length; i++) {
            try {
                baKeyword[i] = (byte) (0xff & Integer.parseInt(
                        s.substring(i * 2, i * 2 + 2), 16));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        try {
            s = new String(baKeyword, "UTF-8");
            //new String();
        } catch (Exception e1) {
            e1.printStackTrace();
        }
        return s;
    }


    /* ============================================================================ */
    /* 字符串格式验证 */
    /* 判定字符串内容是否为：字母、数字、空白、手机号、email等 */
    /* ============================================================================ */
    public static class Validate {

        /**
         * 判断字符串是否只包含unicode字母。
         * <p/>
         * <p>
         * <code>null</code> 将返回 <code>false</code> ，空字符串 <code>""</code> 将返回
         * <code>true</code>。
         * </p>
         * <p/>
         * <pre>
         *
         *    StringUtil.isAlpha(null)   = false
         *    StringUtil.isAlpha(&quot;&quot;)     = true
         *    StringUtil.isAlpha(&quot;  &quot;)   = false
         *    StringUtil.isAlpha(&quot;abc&quot;)  = true
         *    StringUtil.isAlpha(&quot;ab2c&quot;) = false
         *    StringUtil.isAlpha(&quot;ab-c&quot;) = false
         *
         * </pre>
         *
         * @param str 要检查的字符串
         * @return 如果字符串非 <code>null</code> 并且全由unicode字母组成，则返回 <code>true</code>
         */
        public static boolean isAlpha(String str) {
            if (str == null) {
                return false;
            }

            int length = str.length();

            for (int i = 0; i < length; i++) {
                if (!Character.isLetter(str.charAt(i))) {
                    return false;
                }
            }

            return true;
        }

        /**
         * 判断字符串是否只包含unicode字母和空格 <code>' '</code>。
         * <p/>
         * <p>
         * <code>null</code> 将返回 <code>false</code> ，空字符串 <code>""</code> 将返回
         * <code>true</code>。
         * </p>
         * <p/>
         * <pre>
         *
         *    StringUtil.isAlphaSpace(null)   = false
         *    StringUtil.isAlphaSpace(&quot;&quot;)     = true
         *    StringUtil.isAlphaSpace(&quot;  &quot;)   = true
         *    StringUtil.isAlphaSpace(&quot;abc&quot;)  = true
         *    StringUtil.isAlphaSpace(&quot;ab c&quot;) = true
         *    StringUtil.isAlphaSpace(&quot;ab2c&quot;) = false
         *    StringUtil.isAlphaSpace(&quot;ab-c&quot;) = false
         *
         * </pre>
         *
         * @param str 要检查的字符串
         * @return 如果字符串非 <code>null</code> 并且全由unicode字母和空格组成，则返回
         * <code>true</code>
         */
        public static boolean isAlphaSpace(String str) {
            if (str == null) {
                return false;
            }

            int length = str.length();

            for (int i = 0; i < length; i++) {
                if (!Character.isLetter(str.charAt(i)) && (str.charAt(i) != ' ')) {
                    return false;
                }
            }

            return true;
        }

        /**
         * 判断字符串是否只包含unicode字母和数字。
         * <p/>
         * <p>
         * <code>null</code> 将返回 <code>false</code> ，空字符串 <code>""</code> 将返回
         * <code>true</code>。
         * </p>
         * <p/>
         * <pre>
         *
         *    StringUtil.isAlphanumeric(null)   = false
         *    StringUtil.isAlphanumeric(&quot;&quot;)     = true
         *    StringUtil.isAlphanumeric(&quot;  &quot;)   = false
         *    StringUtil.isAlphanumeric(&quot;abc&quot;)  = true
         *    StringUtil.isAlphanumeric(&quot;ab c&quot;) = false
         *    StringUtil.isAlphanumeric(&quot;ab2c&quot;) = true
         *    StringUtil.isAlphanumeric(&quot;ab-c&quot;) = false
         *
         * </pre>
         *
         * @param str 要检查的字符串
         * @return 如果字符串非 <code>null</code> 并且全由unicode字母数字组成，则返回
         * <code>true</code>
         */
        public static boolean isAlphanumeric(String str) {
            if (str == null) {
                return false;
            }

            int length = str.length();

            for (int i = 0; i < length; i++) {
                if (!Character.isLetterOrDigit(str.charAt(i))) {
                    return false;
                }
            }

            return true;
        }

        /**
         * 判断字符串是否只包含unicode字母数字和空格 <code>' '</code>。
         * <p/>
         * <p>
         * <code>null</code> 将返回 <code>false</code> ，空字符串 <code>""</code> 将返回
         * <code>true</code>。
         * </p>
         * <p/>
         * <pre>
         *
         *    StringUtil.isAlphanumericSpace(null)   = false
         *    StringUtil.isAlphanumericSpace(&quot;&quot;)     = true
         *    StringUtil.isAlphanumericSpace(&quot;  &quot;)   = true
         *    StringUtil.isAlphanumericSpace(&quot;abc&quot;)  = true
         *    StringUtil.isAlphanumericSpace(&quot;ab c&quot;) = true
         *    StringUtil.isAlphanumericSpace(&quot;ab2c&quot;) = true
         *    StringUtil.isAlphanumericSpace(&quot;ab-c&quot;) = false
         *
         * </pre>
         *
         * @param str 要检查的字符串
         * @return 如果字符串非 <code>null</code> 并且全由unicode字母数字和空格组成，则返回
         * <code>true</code>
         */
        public static boolean isAlphanumericSpace(String str) {
            if (str == null) {
                return false;
            }

            int length = str.length();

            for (int i = 0; i < length; i++) {
                if (!Character.isLetterOrDigit(str.charAt(i))
                        && (str.charAt(i) != ' ')) {
                    return false;
                }
            }

            return true;
        }

        /**
         * 判断字符串是否只包含unicode数字。
         * <p/>
         * <p>
         * <code>null</code> 将返回 <code>false</code> ，空字符串 <code>""</code> 将返回
         * <code>true</code>。
         * </p>
         * <p/>
         * <pre>
         *
         *    StringUtil.isNumeric(null)   = false
         *    StringUtil.isNumeric(&quot;&quot;)     = true
         *    StringUtil.isNumeric(&quot;  &quot;)   = false
         *    StringUtil.isNumeric(&quot;123&quot;)  = true
         *    StringUtil.isNumeric(&quot;12 3&quot;) = false
         *    StringUtil.isNumeric(&quot;ab2c&quot;) = false
         *    StringUtil.isNumeric(&quot;12-3&quot;) = false
         *    StringUtil.isNumeric(&quot;12.3&quot;) = false
         *
         * </pre>
         *
         * @param str 要检查的字符串
         * @return 如果字符串非 <code>null</code> 并且全由unicode数字组成，则返回 <code>true</code>
         */
        public static boolean isNumeric(String str) {
            if (str == null) {
                return false;
            }

            int length = str.length();

            for (int i = 0; i < length; i++) {
                if (!Character.isDigit(str.charAt(i))) {
                    return false;
                }
            }

            return true;
        }

        /**
         * 判断字符串是否只包含unicode数字和空格 <code>' '</code>。
         * <p/>
         * <p>
         * <code>null</code> 将返回 <code>false</code> ，空字符串 <code>""</code> 将返回
         * <code>true</code>。
         * </p>
         * <p/>
         * <pre>
         *
         *    StringUtil.isNumericSpace(null)   = false
         *    StringUtil.isNumericSpace(&quot;&quot;)     = true
         *    StringUtil.isNumericSpace(&quot;  &quot;)   = true
         *    StringUtil.isNumericSpace(&quot;123&quot;)  = true
         *    StringUtil.isNumericSpace(&quot;12 3&quot;) = true
         *    StringUtil.isNumericSpace(&quot;ab2c&quot;) = false
         *    StringUtil.isNumericSpace(&quot;12-3&quot;) = false
         *    StringUtil.isNumericSpace(&quot;12.3&quot;) = false
         *
         * </pre>
         *
         * @param str 要检查的字符串
         * @return 如果字符串非 <code>null</code> 并且全由unicode数字和空格组成，则返回
         * <code>true</code>
         */
        public static boolean isNumericSpace(String str) {
            if (str == null) {
                return false;
            }

            int length = str.length();

            for (int i = 0; i < length; i++) {
                if (!Character.isDigit(str.charAt(i)) && (str.charAt(i) != ' ')) {
                    return false;
                }
            }

            return true;
        }

        /**
         * 判断字符串是否只包含unicode空白。
         * <p/>
         * <p>
         * <code>null</code> 将返回 <code>false</code> ，空字符串 <code>""</code> 将返回
         * <code>true</code>。
         * </p>
         * <p/>
         * <pre>
         *
         *    StringUtil.isWhitespace(null)   = false
         *    StringUtil.isWhitespace(&quot;&quot;)     = true
         *    StringUtil.isWhitespace(&quot;  &quot;)   = true
         *    StringUtil.isWhitespace(&quot;abc&quot;)  = false
         *    StringUtil.isWhitespace(&quot;ab2c&quot;) = false
         *    StringUtil.isWhitespace(&quot;ab-c&quot;) = false
         *
         * </pre>
         *
         * @param str 要检查的字符串
         * @return 如果字符串非 <code>null</code> 并且全由unicode空白组成，则返回 <code>true</code>
         */
        public static boolean isWhitespace(String str) {
            if (str == null) {
                return false;
            }

            int length = str.length();

            for (int i = 0; i < length; i++) {
                if (!Character.isWhitespace(str.charAt(i))) {
                    return false;
                }
            }

            return true;
        }

        /**
         * 检查email格式
         *
         * @param string
         * @return
         */
        public static boolean isEmail(String string) {
            if (!hasText(string))
                return false;
            string = string.trim();
            String regExp = "^(\\w)+(\\.\\w+)*@(\\w)+((\\.\\w+)+)$";
            Pattern p = Pattern.compile(regExp);
            Matcher m = p.matcher(string);
            return m.find();
        }

    }

    public static class Html {
        /**
         * 还原Html特殊字符
         *
         * @param string
         * @return
         */
        public static String decodeHtml(String string) {
            if (isEmpty(string))
                return string;

            string = string.replace("&gt;", ">");
            string = string.replace("&amp;gt;", ">");
            string = string.replace("&lt;", "<");
            string = string.replace("&amp;lt;", "<");
            string = string.replace("&quot;", "\"");
            string = string.replace("&amp;quot;", "\"");
            string = string.replace("&#39;", "\'");
            string = string.replace("&amp;#39;", "\'");
            string = string.replace("&amp;amp;", "&amp;");
            string = string.replace("&amp;nbsp;", "&nbsp;");
            string = string.replace("<br/> ", "\n");
            return string;
        }

        /**
         * 将标签转化为字符
         *
         * @param source
         * @return
         */
        public static String filterHtmlEncode(String source) {
            source = source.replace(">", "&gt;");
            source = source.replace(">", "&amp;gt;");
            source = source.replace("<", "&lt;");
            source = source.replace("<", "&amp;lt;");
            source = source.replace(" ", "&nbsp;");
            source = source.replace(" ", "&amp;nbsp;");
            source = source.replace("\"", "&quot;");
            source = source.replace("\"", "&amp;quot;");
            source = source.replace("\'", "&#39;");
            source = source.replace("\'", "&amp;#39;");
            source = source.replace("\n", "<br/> ");
            return source;
        }

        /**
         * 基本功能：替换指定的标签
         * <p>
         *
         * @param str
         * @param beforeTag 要替换的标签
         * @param tagAttrib 要替换的标签属性值
         * @param startTag  新标签开始标记
         * @param endTag    新标签结束标记
         * @return String
         * @如：替换img标签的src属性值为[img]属性值[/img]
         */
        public static String replaceHtmlTag(String str, String beforeTag,
                                            String tagAttrib, String startTag, String endTag) {
            String regxpForTag = "<[url=file://s/]\\s[/url]*" + beforeTag + "[url=file://s+([%5e%3e]*)//s]\\s+([^>]*)\\s[/url]*>";
            String regxpForTagAttrib = tagAttrib + "=\"([^\"]+)\"";
            Pattern patternForTag = Pattern.compile(regxpForTag);
            Pattern patternForAttrib = Pattern.compile(regxpForTagAttrib);
            Matcher matcherForTag = patternForTag.matcher(str);
            StringBuffer sb = new StringBuffer();
            boolean result = matcherForTag.find();
            while (result) {
                StringBuffer sbreplace = new StringBuffer();
                Matcher matcherForAttrib = patternForAttrib.matcher(matcherForTag.group(1));
                if (matcherForAttrib.find()) {
                    matcherForAttrib.appendReplacement(sbreplace, startTag + matcherForAttrib.group(1) + endTag);
                }
                matcherForTag.appendReplacement(sb, sbreplace.toString());
                result = matcherForTag.find();
            }
            matcherForTag.appendTail(sb);
            return sb.toString();
        }

        /**
         * 基本功能：过滤指定标签
         * <p>
         *
         * @param str
         * @param tag 指定标签
         * @return String
         */
        public static String filterHtmlTag(String str, String tag) {
            String regxp = "<[url=file://s/]\\s[/url]*" + tag + "[url=file://s+([%5e%3e]*)//s]\\s+([^>]*)\\s[/url]*>";
            Pattern pattern = Pattern.compile(regxp);
            Matcher matcher = pattern.matcher(str);
            StringBuffer sb = new StringBuffer();
            boolean result1 = matcher.find();
            while (result1) {
                matcher.appendReplacement(sb, "");
                result1 = matcher.find();
            }
            matcher.appendTail(sb);
            return sb.toString();
        }

        /* ============================================================================ */
    /* HTML相关的正则表达式工具类。 */
    /* ============================================================================ */
        private final static String regxpForHtml = "<([^>]*)>"; // 过滤所有以<开头以>结尾的标签
        private final static String regxpForImgTag = "<[url=file://s*img//s+([%5E%3E]*)//s]\\s*img\\s+([^>]*)\\s[/url]*>"; // 找出IMG标签
        private final static String regxpForImaTagSrcAttrib = "src=\"([^\"]+)\""; // 找出IMG标签的SRC属性

        /**
         * 基本功能：替换标记以正常显示
         * <p>
         *
         * @param input
         * @return String
         */
        public static String replaceTag(String input) {
            if (!hasSpecialChars(input)) {
                return input;
            }
            StringBuilder filtered = new StringBuilder(input.length());
            char c;
            for (int i = 0; i <= input.length() - 1; i++) {
                c = input.charAt(i);
                switch (c) {
                    case '<':
                        filtered.append("&lt;");
                        break;
                    case '>':
                        filtered.append("&gt;");
                        break;
                    case '"':
                        filtered.append("&quot;");
                        break;
                    case '&':
                        filtered.append("&amp;");
                        break;
                    default:
                        filtered.append(c);
                }
            }
            return (filtered.toString());
        }

        /**
         * 基本功能：判断标记是否存在
         * <p>
         *
         * @param input
         * @return boolean
         */
        public static boolean hasSpecialChars(String input) {
            boolean flag = false;
            if ((input != null) && (input.length() > 0)) {
                char c;
                for (int i = 0; i <= input.length() - 1; i++) {
                    c = input.charAt(i);
                    switch (c) {
                        case '>':
                        case '<':
                        case '"':
                        case '&':
                            flag = true;
                            break;
                        default:
                            break;
                    }
                }
            }
            return flag;
        }

        /**
         * 基本功能：过滤所有以"<"开头以">"结尾的标签
         * <p>
         *
         * @param str
         * @return String
         */
        public static String filterHtml(String str) {
            if (isEmpty(str))
                return str;
            Pattern pattern = Pattern.compile(regxpForHtml);
            Matcher matcher = pattern.matcher(str);
            StringBuffer sb = new StringBuffer();
            boolean result1 = matcher.find();
            while (result1) {
                matcher.appendReplacement(sb, "");
                result1 = matcher.find();
            }
            matcher.appendTail(sb);
            return sb.toString();
        }
    }

}

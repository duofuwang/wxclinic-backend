package com.dopoiv.clinic.common.utils;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.lang.Dict;
import cn.hutool.core.text.StrFormatter;
import org.apache.commons.lang.RandomStringUtils;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 字符串工具类
 *
 * @author ywgy
 */
public class StringUtil extends org.apache.commons.lang.StringUtils {
    /**
     * 空字符串
     */
    private static final String NULLSTR = "";

    /**
     * 空字符串
     */
    private static final String NULL = "null";

    /**
     * 下划线
     */
    private static final char SEPARATOR = '_';
    /**
     * 换行
     */
    private static final String LINE_BREAK= "\n";


    private static final char []  STRING = {'a','b','c','d','e','f','g','h','i','j','k','l','n','m','o','p','q','r','s','t','z','x','1','2','3','4','5','6','7','8','9','0'};

    public static String getRandomString(int stringLength) {
        return  RandomStringUtils.random(stringLength,STRING);
    }


    private static Pattern humpPattern = Pattern.compile("[A-Z]");

    /**
     * 获取参数不为空值
     *
     * @param value defaultValue 要判断的value
     * @return value 返回值
     */
    public static <T> T nvl(T value, T defaultValue) {
        return value != null ? value : defaultValue;
    }

    /**
     * * 判断一个Collection是否为空， 包含List，Set，Queue
     *
     * @param coll 要判断的Collection
     * @return true：为空 false：非空
     */
    public static boolean isEmpty(Collection<?> coll) {
        return isNull(coll) || coll.isEmpty();
    }

    /**
     * * 判断一个Collection是否非空，包含List，Set，Queue
     *
     * @param coll 要判断的Collection
     * @return true：非空 false：空
     */
    public static boolean isNotEmpty(Collection<?> coll) {
        return !isEmpty(coll);
    }

    /**
     * * 判断一个对象数组是否为空
     *
     * @param objects 要判断的对象数组
     *                * @return true：为空 false：非空
     */
    public static boolean isEmpty(Object[] objects) {
        return isNull(objects) || (objects.length == 0);
    }


    /**
     * * 判断一个对象数组是否非空
     *
     * @param objects 要判断的对象数组
     * @return true：非空 false：空
     */
    public static boolean isNotEmpty(Object[] objects) {
        return !isEmpty(objects);
    }

    /**
     * * 判断一个Map是否为空
     *
     * @param map 要判断的Map
     * @return true：为空 false：非空
     */
    public static boolean isEmpty(Map<?, ?> map) {
        return isNull(map) || map.isEmpty();
    }

    /**
     * * 判断一个Map是否为空
     *
     * @param map 要判断的Map
     * @return true：非空 false：空
     */
    public static boolean isNotEmpty(Map<?, ?> map) {
        return !isEmpty(map);
    }

    /**
     * * 判断一个字符串是否为空串
     *
     * @param str String
     * @return true：为空 false：非空
     */
    public static boolean isEmpty(String str) {
        return isNull(str) || NULLSTR.equals(str.trim()) ||NULL.equals(str.toLowerCase());
    }

    /**
     * * 判断一个字符串是否为非空串
     *
     * @param str String
     * @return true：非空串 false：空串
     */
    public static boolean isNotEmpty(String str) {
        return !isEmpty(str);
    }

    /**
     * * 判断一个对象是否为空
     *
     * @param object Object
     * @return true：为空 false：非空
     */
    public static boolean isNull(Object object) {
        return object == null;
    }

    /**
    * <b>方法名：</b>判断对象里面每一个属性都是否为空的<br>
    * <b>说明：</b> <br>
    * @param object
    * @return boolean  true 空
    * <b>修改履历：</b>
    *
    * @author 2020/7/6   hufeng
    */
    public static boolean isObjectNull(Object object) throws IllegalAccessException{
        boolean flag = false;
        if(null!=object){
            for(Field field : object.getClass().getDeclaredFields()){
                //在用反射时访问私有变量（private修饰变量）
                field.setAccessible(true);
                if(field.get(object) == null || field.get(object).equals("")){
                    flag = true;
                    return flag;
                }
                if(field.get(object) != null&&field.get(object).toString().trim().equals("")){
                    flag = true;
                    return flag;
                }
            }
        }else{
            flag=true;
        }
        return flag;
    }

    /**
    * <b>方法名：</b>判断对象是否为空，包括属性<br>
    * <b>说明：</b> <br>
    * @param object
    * @return boolean true 非空
    * <b>修改履历：</b>
    *
    * @author 2020/7/6   hufeng
    */
    public static boolean isNotObjectNull(Object object) throws IllegalAccessException{
        return  !isObjectNull(object);
    }




    /**
     * * 判断一个对象是否非空
     *
     * @param object Object
     * @return true：非空 false：空
     */
    public static boolean isNotNull(Object object) {
        return !isNull(object);
    }

    /**
     * * 判断一个对象是否是数组类型（Java基本型别的数组）
     *
     * @param object 对象
     * @return true：是数组 false：不是数组
     */
    public static boolean isArray(Object object) {
        return isNotNull(object) && object.getClass().isArray();
    }

    /**
     * 去空格
     */
    public static String trim(String str) {
        return (str == null ? "" : str.trim());
    }

    /**
     * 截取字符串
     *
     * @param str   字符串
     * @param start 开始
     * @return 结果
     */
    public static String substring(final String str, int start) {
        if (str == null) {
            return NULLSTR;
        }

        if (start < 0) {
            start = str.length() + start;
        }

        if (start < 0) {
            start = 0;
        }
        if (start > str.length()) {
            return NULLSTR;
        }

        return str.substring(start);
    }

    /**
     * 截取字符串
     *
     * @param str   字符串
     * @param start 开始
     * @param end   结束
     * @return 结果
     */
    public static String substring(final String str, int start, int end) {
        if (str == null) {
            return NULLSTR;
        }

        if (end < 0) {
            end = str.length() + end;
        }
        if (start < 0) {
            start = str.length() + start;
        }

        if (end > str.length()) {
            end = str.length();
        }

        if (start > end) {
            return NULLSTR;
        }

        if (start < 0) {
            start = 0;
        }
        if (end < 0) {
            end = 0;
        }

        return str.substring(start, end);
    }

    /**
     * 格式化文本, {} 表示占位符<br>
     * 此方法只是简单将占位符 {} 按照顺序替换为参数<br>
     * 如果想输出 {} 使用 \\转义 { 即可，如果想输出 {} 之前的 \ 使用双转义符 \\\\ 即可<br>
     * 例：<br>
     * 通常使用：format("this is {} for {}", "a", "b") -> this is a for b<br>
     * 转义{}： format("this is \\{} for {}", "a", "b") -> this is \{} for a<br>
     * 转义\： format("this is \\\\{} for {}", "a", "b") -> this is \a for b<br>
     *
     * @param template 文本模板，被替换的部分用 {} 表示
     * @param params   参数值
     * @return 格式化后的文本
     */
    public static String format(String template, Object... params) {
        if (isEmpty(params) || isEmpty(template)) {
            return template;
        }
        return StrFormatter.format(template, params);
    }

    /**
     * 字符串转set
     *
     * @param str 字符串
     * @param sep 分隔符
     * @return set集合
     */
    public static final Set<String> str2Set(String str, String sep) {
        return new HashSet<String>(str2List(str, sep, true, false));
    }

    public static final List<String> str2List(String str, String sep) {
       return str2List(str,sep,true,true);
    }

    /**
     * 字符串转list
     *
     * @param str         字符串
     * @param sep         分隔符
     * @param filterBlank 过滤纯空白
     * @param trim        去掉首尾空白
     * @return list集合
     */
    public static final List<String> str2List(String str, String sep, boolean filterBlank, boolean trim) {
        List<String> list = new ArrayList<String>();
        if (StringUtil.isEmpty(str)) {
            return list;
        }

        // 过滤空白字符串
        if (filterBlank && StringUtil.isBlank(str)) {
            return list;
        }
        String[] split = str.split(sep);
        for (String string : split) {
            if (filterBlank && StringUtil.isBlank(string)) {
                continue;
            }
            if (trim) {
                string = string.trim();
            }
            list.add(string);
        }

        return list;
    }

    /**
     * 下划线转驼峰命名
     */
    public static String toUnderScoreCase(String str) {
        if (str == null) {
            return null;
        }
        StringBuilder sb = new StringBuilder();
        // 前置字符是否大写
        boolean preCharIsUpperCase = true;
        // 当前字符是否大写
        boolean curreCharIsUpperCase = true;
        // 下一字符是否大写
        boolean nexteCharIsUpperCase = true;
        for (int i = 0; i < str.length(); i++) {
            char c = str.charAt(i);
            if (i > 0) {
                preCharIsUpperCase = Character.isUpperCase(str.charAt(i - 1));
            } else {
                preCharIsUpperCase = false;
            }

            curreCharIsUpperCase = Character.isUpperCase(c);

            if (i < (str.length() - 1)) {
                nexteCharIsUpperCase = Character.isUpperCase(str.charAt(i + 1));
            }

            if (preCharIsUpperCase && curreCharIsUpperCase && !nexteCharIsUpperCase) {
                sb.append(SEPARATOR);
            } else if ((i != 0 && !preCharIsUpperCase) && curreCharIsUpperCase) {
                sb.append(SEPARATOR);
            }
            sb.append(Character.toLowerCase(c));
        }

        return sb.toString();
    }

    /**
     * 是否包含字符串
     *
     * @param str  验证字符串
     * @param strs 字符串组
     * @return 包含返回true
     */
    public static boolean inStringIgnoreCase(String str, String... strs) {
        if (str != null && strs != null) {
            for (String s : strs) {
                if (str.equalsIgnoreCase(trim(s))) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * 将下划线大写方式命名的字符串转换为驼峰式。如果转换前的下划线大写方式命名的字符串为空，则返回空字符串。 例如：HELLO_WORLD->HelloWorld
     *
     * @param name 转换前的下划线大写方式命名的字符串
     * @return 转换后的驼峰式命名的字符串
     */
    public static String convertToCamelCase(String name) {
        StringBuilder result = new StringBuilder();
        // 快速检查
        if (name == null || name.isEmpty()) {
            // 没必要转换
            return "";
        } else if (!name.contains("_")) {
            // 不含下划线，仅将首字母大写
            return name.substring(0, 1).toUpperCase() + name.substring(1);
        }
        // 用下划线将原始字符串分割
        String[] camels = name.split("_");
        for (String camel : camels) {
            // 跳过原始字符串中开头、结尾的下换线或双重下划线
            if (camel.isEmpty()) {
                continue;
            }
            // 首字母大写
            result.append(camel.substring(0, 1).toUpperCase());
            result.append(camel.substring(1).toLowerCase());
        }
        return result.toString();
    }

    /**
     * 驼峰式命名法 例如：user_name->userName
     */
    public static String toCamelCase(String s) {
        if (s == null) {
            return null;
        }
        s = s.toLowerCase();
        StringBuilder sb = new StringBuilder(s.length());
        boolean upperCase = false;
        for (int i = 0; i < s.length(); i++) {
            char c = s.charAt(i);

            if (c == SEPARATOR) {
                upperCase = true;
            } else if (upperCase) {
                sb.append(Character.toUpperCase(c));
                upperCase = false;
            } else {
                sb.append(c);
            }
        }
        return sb.toString();
    }

    /**
     * 移除某个符号以前的数据
     * @param str
     * @param ch
     * @return
     */
    public  static String removeSpot(String str,String ch){
        if(str.indexOf(ch)!=-1){
            return str.substring(str.indexOf(ch)+1,str.length());
        }else{
            return str;
        }

    }
    /**
    * <b>方法名：</b> 查找替换 <br>
    * <b>说明：</b> <br>
     * @param str 字符
 * @param ch 查找
 * @param ch2 替换
 * @param trim  去空格
    * @return java.lang.String
    * <b>修改履历：</b>
    *
    * @author 2020/9/1 20:07  hufeng
    */
    public  static String replaceStr(String str,String ch2,boolean trim,String... strs){
        if(isNotEmpty(str)){
            for (int i = 0; i <strs.length; i++) {
                str=str.replace(strs[i], ch2);
            }
            if(trim){
              return  str.trim();
            }else{
                return  str;
            }
        }
        return "";
    }



    public static <T> T cast(Object obj)
    {
        return (T) obj;
    }

    /**
    * <b>方法名：</b>LongList 转 string<br>
    * <b>说明：</b> <br>
    * @param array
    * @return java.lang.String
    * <b>修改履历：</b>
    *
    * @author 2020/7/15   hufeng
    */
    public static String arrayGetString(List<Long> array){
        String s="";
        if (CollUtil.isNotEmpty(array)) {
            for (int i = 0; i <array.size() ; i++) {
                s+=array.get(i)+",";
            }
            s = s.substring(0,s.length() - 1);
        }
        return s;
    }
    /**
     * <b>方法名：</b>StringList 转 string<br>
     * <b>说明：</b> <br>
     * @param array
     * @return java.lang.String
     * <b>修改履历：</b>
     *
     * @author 2020/7/15   hufeng
     */
    public static  String arrayStringGetString(List<String> array){
        String s="";
        if (CollUtil.isNotEmpty(array)) {
            for (int i = 0; i <array.size() ; i++) {
                s+=array.get(i)+",";
            }
            s = s.substring(0,s.length() - 1);
        }
        return s;
    }

    /**
     * 获取属性名数组
     * */
    public static String[] getFiledName(Object o){
        Field[] fields=o.getClass().getDeclaredFields();
        String[] fieldNames=new String[fields.length];
        for(int i=0;i<fields.length;i++){
            fieldNames[i]=fields[i].getName();
        }
        return fieldNames;
    }

    /**
     * 根据属性名获取属性值
     * @param fieldName
     * @param o
     * @return
     */
    public static Object getFieldValueByName(String fieldName, Object o) {
        try {
            String firstLetter = fieldName.substring(0, 1).toUpperCase();
            String getter = "get" + firstLetter + fieldName.substring(1);
            Method method = o.getClass().getMethod(getter, new Class[]{});
            Object value = method.invoke(o, new Object[]{});
            return value;
        } catch (Exception e) {
            return null;
        }
    }
    /**
    * <b>方法名：</b> 获取字符串中的数字<br>
    * <b>说明：</b> <br>
     * @param  str
    * @return int
    * <b>修改履历：</b>
    *
    * @author 2020/7/22 16:35  hufeng
    */
    public  static  int  getNumber(String str){
        str = str.trim();
        String str2 = "";
        for (int i = 0; i < str.length(); i++) {
            if (str.charAt(i) >= 48 && str.charAt(i) <= 57) {
                str2 += str.charAt(i);
            }
        }
        if (isNotEmpty(str2)){
            return Integer.parseInt(str2);
        }else{
            return 0;
        }

    }

    public  static Dict getListDict(String text){
        String[] lines = text.split(LINE_BREAK);
        Dict dict=new Dict();
        for (int i = 0; i <lines.length ; i++) {
            String [] value=lines[i].split(" ");
            String key=value[0];
            LinkedList<String> list=new LinkedList<>();
            for (int j = 0; j <value.length ; j++) {
                if(j!=0){
                    list.add(value[j]);
                }
            }
            dict.set(key,list);
        }
        return dict;
    }

    /** 驼峰转下划线*/
    public static String humpToLine2(String str) {
        if(isEmpty(str)){
            return "";
        }
        Matcher matcher = humpPattern.matcher(str);
        StringBuffer sb = new StringBuffer();
        while (matcher.find()) {
            matcher.appendReplacement(sb, "_" + matcher.group(0).toLowerCase());
        }
        matcher.appendTail(sb);
        return sb.toString();
    }

}
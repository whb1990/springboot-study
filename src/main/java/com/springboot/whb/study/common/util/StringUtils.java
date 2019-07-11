package com.springboot.whb.study.common.util;


import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import java.io.*;
import java.util.Collection;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author: whb
 * @date: 2017/7/11 11:18
 * @description: 字符串工具类
 */
public final class StringUtils extends org.apache.commons.lang.StringUtils {

    /**
     * 字符串是否乱码正则表达式
     */
    private static final Pattern messPattern = Pattern.compile("\\s*|t*|r*|n*");
    /**
     * 字符串是否包含中文正则表达式
     */
    private static final Pattern containsChinesePattern = Pattern.compile("[\u4e00-\u9fa5]");

    public static String join(Collection list, String sep, String prefix) {
        Object[] array = (list == null) ? null : list.toArray();
        return join(array, sep, prefix);
    }

    public static String join(Object[] array, String sep, String prefix) {
        if (array == null) {
            return "";
        }
        int arraySize = array.length;
        if (arraySize == 0) {
            return "";
        }
        if (sep == null) {
            sep = "";
        }
        if (prefix == null) {
            prefix = "";
        }
        StringBuilder buf = new StringBuilder(prefix);
        for (int i = 0; i < arraySize; ++i) {
            if (i > 0) {
                buf.append(sep);
            }
            buf.append((array[i] == null) ? "" : array[i]);
        }
        return buf.toString();
    }

    public static String jsonJoin(String[] array) {
        int arraySize = array.length;
        int bufSize = arraySize * (array[0].length() + 3);
        StringBuilder buf = new StringBuilder(bufSize);
        for (int i = 0; i < arraySize; ++i) {
            if (i > 0) {
                buf.append(',');
            }
            buf.append('"');
            buf.append(array[i]);
            buf.append('"');
        }
        return buf.toString();
    }

    public static boolean isNullOrEmpty(String s) {
        return ((s == null) || ("".equals(s)));
    }

    public static boolean inStringArray(String s, String[] array) {
        for (String x : array) {
            if (x.equals(s)) {
                return true;
            }
        }
        return false;
    }

    /**
     * 集合对象转成json字符串
     *
     * @param obj
     * @return
     * @author whb
     */
    public static String toJSONArrStr(Object obj) {
        if (obj != null) {
            return JSONArray.toJSONString(obj);
        }
        return "";
    }

    /**
     * 非集合对象转成json字符串
     *
     * @param obj
     * @return
     * @author whb
     */
    public static String toJSONObjStr(Object obj) {
        if (obj != null) {
            return JSONObject.toJSONString(obj);
        }
        return "";
    }

    /**
     * @param value
     * @return
     * @Description 判断指定值是否存在
     * @Author whb 2017-12-12
     */
    public static boolean isSet(Object value) {
        return value != null && !value.equals("");
    }

    /**
     * @param array
     * @return
     * @Description 判断指定集合是否存在
     * @Author whb 2017-12-12
     */
    public static boolean isSet(Collection<?> array) {
        return (array != null && !array.isEmpty());
    }

    /**
     * @param value
     * @param compare
     * @return
     * @Description 判断两个值是否相等
     * @Author whb 2017-12-12
     */
    public static boolean isSet(Object value, Object compare) {
        return value != null && value.equals(compare);
    }

    /**
     * 对象转数组
     *
     * @param obj
     * @return
     * @author whb 2017-12-12
     */
    public byte[] toByteArray(Object obj) {
        byte[] bytes = null;
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        try {
            ObjectOutputStream oos = new ObjectOutputStream(bos);
            oos.writeObject(obj);
            oos.flush();
            bytes = bos.toByteArray();
            oos.close();
            bos.close();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return bytes;
    }

    /**
     * 数组转对象
     *
     * @param bytes
     * @return
     * @author whb 2017-12-12
     */
    public Object toObject(byte[] bytes) {
        Object obj = null;
        try {
            ByteArrayInputStream bis = new ByteArrayInputStream(bytes);
            ObjectInputStream ois = new ObjectInputStream(bis);
            obj = ois.readObject();
            ois.close();
            bis.close();
        } catch (IOException ex) {
            ex.printStackTrace();
        } catch (ClassNotFoundException ex) {
            ex.printStackTrace();
        }
        return obj;
    }

    /**
     * 将字节数组输出为文件
     *
     * @param bytes
     * @param outputFile
     * @return
     * @author whb 2017-12-12
     */
    public File getFileFromBytes(byte[] bytes, String outputFile) {
        BufferedOutputStream stream = null;
        File file = null;
        try {
            file = new File(outputFile);
            FileOutputStream fstream = new FileOutputStream(file);
            stream = new BufferedOutputStream(fstream);
            stream.write(bytes);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (null != stream) {
                try {
                    stream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return file;
    }

    /**
     * 文件转对象
     *
     * @param inputFile
     * @return
     * @throws IOException
     * @throws ClassNotFoundException
     * @author whb 2017-12-12
     */
    public Object fileToObject(File inputFile) throws IOException, ClassNotFoundException {
        FileInputStream fis = new FileInputStream(inputFile);
        ObjectInputStream ois = new ObjectInputStream(fis);
        Object obj = ois.readObject();
        return obj;
    }

    /**
     * 截取指定长度的字符串内容，超出截取长度的以..补位
     *
     * @param str
     * @param toCount
     * @return
     */
    public static String subSimpleNavTitle(String str, int toCount) {
        int reInt = 0;
        String reStr = "";
        if (str == null) {
            return "";
        }
        char[] tempChar = str.toCharArray();
        for (int kk = 0; (kk < tempChar.length && toCount > reInt); kk++) {
            String s1 = String.valueOf(tempChar[kk]);
            byte[] b = s1.getBytes();
            reInt += b.length;
            if (toCount < reInt) {
                break;
            }
            reStr += tempChar[kk];
        }
        if (tempChar.length > toCount) {
            return reStr + "...";
        } else {
            return reStr;
        }
    }

    /**
     * 判断字符串是否可以转double数值
     *
     * @param str--字符串
     * @return
     * @author whb 2018-4-18
     */
    public static boolean canStrToDouble(String str) {
        boolean flag = true;
        if (StringUtils.isBlank(str.trim())) {
            return false;
        }
        try {
            Double.parseDouble(str);
            flag = true;
        } catch (Exception e) {
            flag = false;
        }
        return flag;
    }

    /**
     * 判断字符串是否可以转long数值
     *
     * @param str--字符串
     * @return
     * @author whb 2018-4-18
     */
    public static boolean canStrToLong(String str) {
        boolean flag = true;
        if (StringUtils.isBlank(str.trim())) {
            return false;
        }
        try {
            Long.parseLong(str);
            flag = true;
        } catch (Exception e) {
            flag = false;
        }
        return flag;
    }

    /**
     * 判断字符串是否可以转int数值
     *
     * @param str--字符串
     * @return
     * @author whb 2018-4-18
     */
    public static boolean canStrToInteger(String str) {
        boolean flag = true;
        if (StringUtils.isBlank(str.trim())) {
            return false;
        }
        try {
            Integer.parseInt(str);
            flag = true;
        } catch (Exception e) {
            flag = false;
        }
        return flag;
    }

    /**
     * @author whb
     * @description 首字母大写
     * @date 2018/5/25
     */
    public static String firstWordToUpper(String source) {
        if (isEmpty(source)) {
            return source;
        }
        if (source.length() == 1) {
            return source.substring(0, 1).toUpperCase();
        }
        return source.substring(0, 1).toUpperCase() + source.substring(1, source.length());

    }

    /**
     * @desc 判断字符串是否包含中文
     * @author whb
     * @date 2018/7/16
     */
    public static boolean isContainsChinese(String str) {
        Matcher matcher = containsChinesePattern.matcher(str);
        if (matcher.find()) {
            return true;
        }
        return false;
    }

    /**
     * @param str
     * @return
     * @desc字符串去空格
     * @author whb
     * @date 2018/9/19
     */
    public static String trimString(String str) {
        if (StringUtils.isEmpty(str)) {
            return null;
        }
        return str.trim();
    }

    /**
     * 判断字符是否是中文
     *
     * @param c 字符
     * @return 是否是中文（true：中文；false：非中文）
     */
    public static boolean isChinese(char c) {
        Character.UnicodeBlock ub = Character.UnicodeBlock.of(c);
        if (ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS
                || ub == Character.UnicodeBlock.CJK_COMPATIBILITY_IDEOGRAPHS
                || ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS_EXTENSION_A
                || ub == Character.UnicodeBlock.GENERAL_PUNCTUATION
                || ub == Character.UnicodeBlock.CJK_SYMBOLS_AND_PUNCTUATION
                || ub == Character.UnicodeBlock.HALFWIDTH_AND_FULLWIDTH_FORMS) {
            return true;
        }
        return false;
    }

    /**
     * 判断字符串是否是乱码
     *
     * @param strName 字符串
     * @return 是否乱码（true：乱码；false：非乱码）
     */
    public static boolean isMessByCode(String strName) {
        Matcher m = messPattern.matcher(strName);
        String after = m.replaceAll("");
        String temp = after.replaceAll("\\p{P}", "");
        char[] ch = temp.trim().toCharArray();
        float chLength = ch.length;
        float count = 0;
        for (int i = 0; i < ch.length; i++) {
            char c = ch[i];
            if (!Character.isLetterOrDigit(c)) {
                if (!isChinese(c)) {
                    count = count + 1;
                }
            }
        }
        float result = count / chLength;
        if (result > 0.4) {
            return true;
        }
        return false;
    }

    /**
     * 字符串转中文
     * 用getBytes(encoding)：返回字符串的一个byte数组
     * 当b[0]为63时，应该是转码错误
     * A、不乱码的汉字字符串：
     * 1、encoding用GB2312时，每byte是负数；
     * 2、encoding用ISO-8859-1时，b[i]全是63.
     * B、乱码的汉字字符串：
     * 1、encoding用ISO-8859-1时，每byte也是负数；
     * 2、encoding用GB2312时，b[i]大部分是63
     * C、英文字符串
     * 1、encoding用ISO-8859-1和GB2312时，每byte都大于0；
     * 总结：给定一个字符串，用getBytes("iso-8859-1")，
     * 1、如果b[i]有63，不用转码；对应A-2.
     * 2、如果b[i]全大于0，那么为英文字符串，不用转码；对应B-1
     * 3、如果b[i]有小于0的，那么已经乱码，要转码；对应C-1
     *
     * @param str
     * @return
     */
    public static String toChinese(String str) {
        if (str == null) {
            return null;
        }
        String retStr = str;
        byte b[];
        try {
            b = str.getBytes("iso-8859-1");
            for (int i = 0; i < b.length; i++) {
                byte b1 = b[i];
                if (b1 == 63) {
                    break;
                } else if (b1 > 0) {
                    continue;
                } else if (b1 < 0) {
                    retStr = new String(b, "UTF-8");
                    break;
                }
            }
        } catch (UnsupportedEncodingException e) {

        }
        return retStr;
    }
}
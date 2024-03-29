package com.springboot.whb.study.common.util;

/**
 * @author: whb
 * @date: 2019/7/12 11:02
 * @description: 大写数字转阿拉伯数字
 */
public class ArabicToChineseUtils {

    public static String[] units = {"", "十", "百", "千", "万", "十万", "百万", "千万", "亿", "十亿", "百亿", "千亿", "万亿"};

    public static char[] numArray = {'零', '一', '二', '三', '四', '五', '六', '七', '八', '九'};

    /**
     * @param args
     */
    public static void main(String[] args) {
        int num = 11;
        String numStr = formatInteger(num);
        //int n = numStr.indexOf("一十");
        //System.out.println("n:" + n);
        /*if (numStr.startsWith("一十")) {
            numStr = numStr.substring(1);
        }*/
        print("num= " + num + ", convert result: " + numStr);
        double decimal = 245006.234206;
        print("============================================================");
        String decStr = formatDecimal(decimal);
        print("decimal= " + decimal + ", decStr: " + decStr);
    }

    public static String formatInteger(int num) {
        if (num == 0) {
            return numArray[num] + "";
        }
        char[] val = String.valueOf(num).toCharArray();
        int len = val.length;
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < len; i++) {
            String m = val[i] + "";
            int n = Integer.valueOf(m);
            boolean isZero = n == 0;
            String unit = units[(len - 1) - i];
            if (isZero) {
                if ('0' == val[i - 1]) {
                    // not need process if the last digital bits is 0
                    continue;
                } else {
                    // no unit for 0
                    if (i + 1 != len) {
                        sb.append(numArray[n]);
                    }
                }
            } else {
                sb.append(numArray[n]);
                sb.append(unit);
            }
        }
        //如果是以一十开头，即（10~19之间的数字），则去掉开头的一
        if (sb.indexOf("一十") == 0) {
            return sb.substring(1);
        }
        if (sb.lastIndexOf("零") == sb.length() - 1) {
            if (num > 9 && num < 20) {
                return sb.substring(1, sb.length() - 1);
            }
            return sb.substring(0, sb.length() - 1);
        }
        return sb.toString();
    }

    public static String formatDecimal(double decimal) {
        String decimals = String.valueOf(decimal);
        int decIndex = decimals.indexOf(".");
        int integ = Integer.valueOf(decimals.substring(0, decIndex));
        int dec = Integer.valueOf(decimals.substring(decIndex + 1));
        String result = formatInteger(integ) + "." + formatFractionalPart(dec);
        return result;
    }

    public static String formatFractionalPart(int decimal) {
        char[] val = String.valueOf(decimal).toCharArray();
        int len = val.length;
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < len; i++) {
            int n = Integer.valueOf(val[i] + "");
            sb.append(numArray[n]);
        }
        return sb.toString();
    }

    public static void print(Object arg0) {
        System.out.println(arg0);
    }
}

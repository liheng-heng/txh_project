package com.txh.im.utils;

import android.content.Context;
import android.provider.Settings;
import android.util.Log;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Administrator on 2016/1/25.
 * <p/>
 * 文本工具类
 */
public class TextUtil {


    /**
     * 验证手机号
     *
     * @param mobiles
     * @return
     */
    public static boolean isMobileNO(String mobiles) {

        /**
         * 新版手机号码验证
         */
        Pattern p = Pattern.compile("^((13[0-9])|(14[0-9])|(15[0-9])|(17[0-9])|(18[0-9]))\\d{8}$");
        Matcher m = p.matcher(mobiles);
        return m.matches();

        /**
         * 之前老版手机号码验证
         */
//        Pattern p = Pattern.compile("^((13[0-9])|(15[^4,\\D])|(17[^4,\\D])|(18[0-9]))\\d{8}$");
//        Matcher m = p.matcher(mobiles);
//        return m.matches();


    }

    /**
     * 检查是否为空
     */
    public static boolean isNull(String content) {
        if (content == null || content.isEmpty()) {
            return true;
        }
        return false;
    }

    /**
     * 验证是否是数字
     *
     * @param str
     * @return
     */
    public static boolean isNumber(String str) {
        Pattern pattern = Pattern.compile("[0-9]*");
        java.util.regex.Matcher match = pattern.matcher(str);
        if (match.matches() == false) {
            return false;
        } else {
            return true;
        }
    }

    /**
     * 判断给定字符串是否空白串。 空白串是指由空格、制表符、回车符、换行符组成的字符串 若输入字符串为null或空字符串，返回true
     *
     * @param input
     * @return boolean
     */
    public static boolean isEmpty(String input) {
        if (input == null || "".equals(input))
            return true;

        for (int i = 0; i < input.length(); i++) {
            char c = input.charAt(i);
            if (c != ' ' && c != '\t' && c != '\r' && c != '\n') {
                return false;
            }
        }
        return true;
    }

    /**
     * 字符串转义---去除字符串中的换行符和空格
     *
     * @param str
     * @return
     */
    public static String replaceBlank(String str) {
        String dest = "";
        if (str != null) {
            Pattern p = Pattern.compile("\\s*|\t|\r|\n");
            Matcher m = p.matcher(str);
            dest = m.replaceAll("");
        }
        return dest;
    }

    /**
     * 获取手机机器码
     */
    public static String GetPhoneNum(Context context) {
        return Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID);
    }

    // 手机号码隐藏
    public static String phoneHiddenfour(String phone) {
        if (phone == null)
            return "";
        return phone.substring(0, 3) + "****" + phone.substring(7, phone.length());
    }


    /**
     * 字符串中是否包含包含特殊字符
     *
     * @param str
     * @return
     */
    public static boolean isContainsSpecialCharacter(String str) {
        String regEx = "[`~!@#$%^&*()+=|{}':;',\\[\\].<>/?~！@#￥%……&*（）——+|{}【】‘；：”“’。，、？_-]";
        Pattern p = Pattern.compile(regEx);
        Matcher m = p.matcher(str);
        if (m.find()) {
            return true;
        }
        return false;
    }

    /**
     * 字符串中是否包含包含特殊字符---去除 “-”和“_”
     *
     * @param str
     * @return
     */
    public static boolean isContainsSpecialCharacter2(String str) {
        String regEx = "[`~!@#$%^&*()+=|{}':;',\\[\\].<>/?~！@#￥%……&*（）——+|{}【】‘；：”“’。，、？]";
        Pattern p = Pattern.compile(regEx);
        Matcher m = p.matcher(str);
        if (m.find()) {
            return true;
        }
        return false;
    }

    /**
     * 是否包含中文
     *
     * @param str
     * @return
     */
    public static boolean isContainsChinese(String str) {
        String regEx = "[\u4e00-\u9fa5]";
        Pattern pat = Pattern.compile(regEx);
        Matcher matcher = pat.matcher(str);
        boolean flg = false;
        if (matcher.find()) {
            flg = true;
        }
        return flg;
    }

    /**
     * 是否既包含特殊字符又包含中文
     *
     * @param str
     * @return
     */
    public static boolean isContainCH_AND_SC(String str) {
        return isContainsSpecialCharacter(str) && isContainsChinese(str);
    }

    /**
     * 是否包含特殊字符或者包含中文
     *
     * @param str
     * @return
     */
    public static boolean isContainCH_OR_SC(String str) {
        return isContainsSpecialCharacter(str) || isContainsChinese(str);
    }

    /**
     * 判断string中是否包含char_str
     *
     * @param string
     * @param char_str
     * @return
     */
    public static boolean isContain_charstr(String string, String char_str) {
        int result = string.indexOf(char_str);
        if (result != -1) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * 判断string中是否至少包含两个字母
     *
     * @param string
     * @return
     */
    public static boolean isComtainTwoChar(String string) {
        String reg2 = "[a-zA-Z]";
        int m = 0;
        //是否包含两位字母
        for (int i = 0; i < string.length(); i++) {

            char c = string.charAt(i);
            String str = String.valueOf(c);//把字符c转换成字符串str
            Log.i("----------->", str);
            if (str.matches(reg2)) {
                m = m + 1;
            }
        }

        if (m >= 2) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * 判断字符串中是否包含数字
     *
     * @param string
     * @return
     */
    public static boolean isComtainNumber(String string) {

        String reg2 = "[0-9]";
        int m = 0;
        //是否包含两位字母
        for (int i = 0; i < string.length(); i++) {

            char c = string.charAt(i);
            String str = String.valueOf(c);
            Log.i("----------->", str);
            if (str.matches(reg2)) {
                return true;
            }
        }
        return false;
    }
}

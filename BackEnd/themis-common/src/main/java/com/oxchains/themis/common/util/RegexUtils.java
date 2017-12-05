package com.oxchains.themis.common.util;

import jdk.nashorn.internal.runtime.regexp.joni.Regex;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author oxchains
 * @time 2017-12-05 10:08
 * @name RegexUtils
 * @desc:
 */
public class RegexUtils {
    //国内电话
    public static final String REGEX_PHONE="^(\\(\\d{3,4}-)|\\d{3.4}-)?\\d{7,8}$";
    public static final String REGEX_MOBILEPHONE="^((13[0-9])|(14[5|7])|(15([0-3]|[5-9]))|(18[0-3,5-9]))\\\\d{8}$";
    public static final String REGEX_EMAIL="[a-zA-Z_]{1,}[0-9]{0,}@(([a-zA-z0-9]-*){1,}\\.){1,3}[a-zA-z\\-]{1,}";
    public static final String REGEX_ZH="^[\\u0391-\\uFFE5]+$";
    public static final String REGEX_URL="^((http|https)://)?([\\w-]+\\.)+[\\w-]+(/[\\w-./?%&=]*)?$ ";

    public static boolean match(String str ,String regex){
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(str);
        return matcher.matches();
    }
    private RegexUtils(){}
}

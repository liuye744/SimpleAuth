package com.codingcube.simpleauth.validated.util;

public class Regex {
    /**
     * 邮箱
     */
    public final static String EMAIL = "^w+([-+.]w+)*@w+([-.]w+)*.w+([-.]w+)*$";
    /**
     * 网站
     */
    public final static String URL = "[a-zA-Z0-9\\-\\.]+\\.[a-zA-Z]{2,}(:[0-9]{1,5})?(\\/[\\S]*)?$";
    /**
     * 网站
     */
    public final static String DOMAIN_NAME = "^[a-zA-Z0-9\\-\\.]+\\.[a-zA-Z]{2,}$\n";

    /**
     * 长度在6~18之间，只能包含字母、数字和下划线
     */
    public final static String PASSWORD = "^[a-zA-Z]\\w{5,17}$";
    /**
     * 必须包含大小写字母和数字的组合，可以使用特殊字符，长度在8-16之间
     */
    public final static String STRONG_PASSWORD = "^(?=.\\d)(?=.[a-z])(?=.*[A-Z]).{8,16}$";

    /**
     * 汉字
     */
    public final static String NUMBER = "^[0-9]*$";

    /**
     * 汉字
     */
    public final static String CHINESE_CHARACTER = "^[\\u4e00-\\u9fa5]{0,}$";
    /**
     * 必须包含大小写字母和数字的组合，不能使用特殊字符，长度在8-16之间
     */
    public final static String STRONG_PASSWORD_WITHOUT_SPECIAL_CHARACTER = "^(?=.*\\d)(?=.*[a-z])(?=.*[A-Z])[a-zA-Z0-9]{8,16}$";
    /**
     * 允许5-16字节，允许字母数字下划线
     */
    public final static String UNIVERSAL_IDENTITY = "^[a-zA-Z][a-zA-Z0-9_]{4,15}$";

    /**
     * 中国移动电话号码
     */
    public final static String CHINESE_MOBIL_PHONE_NUMBER = "^(13[0-9]|14[01456879]|15[0-35-9]|16[2567]|17[0-8]|18[0-9]|19[0-35-9])\\d{8}$";

    /**
     * 中国移动电话号码
     */
    public final static String CHINESE_LANDLINE_NUMBER = "\\d{3}-\\d{8}|\\d{4}-\\d{7}";

    /**
     * 中国身份证号
     */
    public final static String CHINESE_ID_NUMBER = "(^\\d{15}$)|(^\\d{18}$)|(^\\d{17}(\\d|X|x)$)";

    /**
     * xml文件名
     */
    public final static String XML_FILE_NAME = "^([a-zA-Z]+-?)+[a-zA-Z0-9]+\\\\.[x|X][m|M][l|L]$";

    /**
     * HTML
     */
    public final static String HTML = "<(\\S*?)[^>]*>.*?|<.*? />";

    /**
     * 中国邮政编码
     */
    public final static String CHINA_POSTAL_CODE = "[1-9]\\d{5}(?!\\d)";

    /**
     * ipv4
     */
    public final static String IPv4 = "((2(5[0-5]|[0-4]\\d))|[0-1]?\\d{1,2})(\\.((2(5[0-5]|[0-4]\\d))|[0-1]?\\d{1,2})){3}";
    /**
     * ipv6
     */
    public final static String IPv6 = "(([0-9a-fA-F]{1,4}:){7,7}[0-9a-fA-F]{1,4}|([0-9a-fA-F]{1,4}:){1,7}:|([0-9a-fA-F]{1,4}:){1,6}:[0-9a-fA-F]{1,4}|([0-9a-fA-F]{1,4}:){1,5}(:[0-9a-fA-F]{1,4}){1,2}|([0-9a-fA-F]{1,4}:){1,4}(:[0-9a-fA-F]{1,4}){1,3}|([0-9a-fA-F]{1,4}:){1,3}(:[0-9a-fA-F]{1,4}){1,4}|([0-9a-fA-F]{1,4}:){1,2}(:[0-9a-fA-F]{1,4}){1,5}|[0-9a-fA-F]{1,4}:((:[0-9a-fA-F]{1,4}){1,6})|:((:[0-9a-fA-F]{1,4}){1,7}|:)|fe80:(:[0-9a-fA-F]{0,4}){0,4}%[0-9a-zA-Z]{1,}|::(ffff(:0{1,4}){0,1}:){0,1}((25[0-5]|(2[0-4]|1{0,1}[0-9]){0,1}[0-9])\\.){3,3}(25[0-5]|(2[0-4]|1{0,1}[0-9]){0,1}[0-9])|([0-9a-fA-F]{1,4}:){1,4}:((25[0-5]|(2[0-4]|1{0,1}[0-9]){0,1}[0-9])\\.){3,3}(25[0-5]|(2[0-4]|1{0,1}[0-9]){0,1}[0-9]))";
}

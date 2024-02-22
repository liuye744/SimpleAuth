package com.codingcube.simpleauth.validated.util;

public class Regex {
    /**
     * 邮箱
     */
    public final static String EMAIL = "\\w[-\\w.+]*@([A-Za-z0-9][-A-Za-z0-9]+\\.)+[A-Za-z]{2,14}";

    /**
     * URL
     */
    public final static String URL = "^(https?:\\/\\/)?(([0-9a-z.]+\\.[a-z]+)|(([0-9]{1,3}\\.){3}[0-9]{1,3}))(:[0-9]+)?(\\/[0-9a-z%/.\\-_]*)?(\\?[0-9a-z=&%_\\-]*)?(\\#[0-9a-z=&%_\\-]*)?$";

    /**
     * 提取超链接
     */
    public final static String EXTRACT_HYPERLINK = "(<a\\\\s*(?!.*\\\\brel=)[^>]*)(href=”https?:\\\\/\\\\/)((?!(?:(?:www\\\\.)?’.implode(‘|(?:www\\\\.)?’, $follow_list).’))[^” rel=”external nofollow” ]+)”((?!.*\\\\brel=)[^>]*)(?:[^>]*)>";

    /**
     * 域名
     */
    public final static String DOMAIN_NAME = "^[a-zA-Z0-9\\-\\.]+\\.[a-zA-Z]{2,}$";

    /**
     * 长度在6~18之间，只能包含字母、数字和下划线
     * The length should be between 6 and 18, and can only contain letters, numbers, and underscores.
     */
    public final static String PASSWORD = "^[a-zA-Z]\\w{5,17}$";

    /**
     * 必须包含大小写字母和数字的组合，可以使用特殊字符，长度在8-16之间
     * It must contain a combination of uppercase and lowercase letters, as well as numbers. Special characters are allowed, and the length should be between 8 and 16.
     */
    public final static String STRONG_PASSWORD = "^(?=.\\d)(?=.[a-z])(?=.*[A-Z]).{8,16}$";

    /**
     * 必须包含大小写字母、数字、特殊字符的组合，长度在8-16之间
     *
     * It must contain a combination of uppercase and lowercase letters, numbers, and special characters. The length should be between 8 and 16.
     */
    public final static String COMPLEX_PASSWORD = "^(?=(.*[0-9]))(?=.*[\\!@#$%^&*()\\\\[\\]{}\\-_+=~`|:;\"'<>,./?])(?=.*[a-z])(?=(.*[A-Z]))(?=(.*)).{8,16}$";

    /**
     * 整数
     */
    public final static String WHOLE_NUMBERS = "^[0-9]*$";


    /**
     * 汉字
     */
    public final static String CHINESE_CHARACTER = "^[\\u4e00-\\u9fa5]{0,}$";

    /**
     * 日语字符
     */
    public final static String JAPANESE_CHARACTER = "^[\\u3040-\\u309F\\u30A0-\\u30FF\\u31F0-\\u31FF]+$";

    /**
     * 校验日期格式（yyyy-mm-dd）
     */
    public final static String DATE = "^(?:(?!0000)[0-9]{4}-(?:(?:0[1-9]|1[0-2])-(?:0[1-9]|1[0-9]|2[0-8])|(?:0[13-9]|1[0-2])-(?:29|30)|(?:0[13578]|1[02])-31)|(?:[0-9]{2}(?:0[48]|[2468][048]|[13579][26])|(?:0[48]|[2468][048]|[13579][26])00)-02-29)$";

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
     * 中国固定电话号码
     */
    public final static String CHINESE_LANDLINE_NUMBER = "^0\\d{2}(?:-?\\d{8}|-?\\d{7})$";

    /**
     * 国际固定电话号码
     */
    public final static String INTERNATIONAL_PHONE_NUMBERS = "^(?:(?:\\(?(?:00|\\+)([1-4]\\d\\d|[1-9]\\d?)\\)?)?[\\-\\.\\ \\\\\\/]?)?((?:\\(?\\d{1,}\\)?[\\-\\.\\ \\\\\\/]?){0,})(?:[\\-\\.\\ \\\\\\/]?(?:#|ext\\.?|extension|x)[\\-\\.\\ \\\\\\/]?(\\d+))?$";

    /**
     * 中国身份证号
     */
    public final static String CHINESE_ID_NUMBER = "^[1-9]\\d{5}(18|19|20)\\d{2}((0[1-9])|(1[0-2]))(([0-2][1-9])|10|20|30|31)\\d{3}[0-9Xx]$";

    /**
     * xml文件名
     */
    public final static String XML_FILE_NAME = "^([^\\\\/]+)+\\.[x|X][m|M][l|L]$";

    /**
     * HTML
     */
    public final static String HTML = "<(\\S*?)[^>]*>.*?|<.*? />";

    /**
     * 中国邮政编码
     */
    public final static String CHINA_POSTAL_CODE = "\\d{6}";

    /**
     * 子网掩码
     */
    public final static String SUBNET_MASK = "^((?:(?:25[0-5]|2[0-4]\\d|[01]?\\d?\\d)\\.){3}(?:25[0-5]|2[0-4]\\d|[01]?\\d?\\d))$";

    /**
     * 腾讯QQ
     */
    public final static String TENCENT_QQ = "^[1-9][0-9]{4,}$";

    /**
     * 带后缀的网络文件路径
     */
    public final static String FILE_PATH = "^((\\/|\\\\|\\/\\/|https?:\\\\\\\\|https?:\\/\\/)[a-z0-9 _@\\-^!#$%&+={}.\\/\\\\\\[\\]]+)+\\.[a-z]+$";
    /**
     * 中国车牌号
     */
    public final static String CHINESE_LICENSE_PLATE_NUMBER = "^[京津沪渝冀豫云辽黑湘皖鲁新苏浙赣鄂桂甘晋蒙陕吉闽贵粤青藏川宁琼使领A-Z]{1}[A-Z]{1}[A-Z0-9]{4}[A-Z0-9挂学警港澳]{1}$";

    /**
     * MAC
     */
    public final static String MAC = "^([0-9A-Fa-f]{2}[:]){5}([0-9A-Fa-f]{2})|([0-9A-Fa-f]{2}[-]){5}([0-9A-Fa-f]{2})$";

    /**
     * ipv4
     */
    public final static String IPv4 = "^((2(5[0-5]|[0-4]\\d))|[0-1]?\\d{1,2})(\\.((2(5[0-5]|[0-4]\\d))|[0-1]?\\d{1,2})){3}$";

    /**
     * 提取ipv4
     */
    public final static String EXTRACT_IPv4 = "\\d+\\.\\d+\\.\\d+\\.\\d+";

    /**
     * ipv6
     */
    public final static String IPv6 = "^(([0-9a-fA-F]{1,4}:){7,7}[0-9a-fA-F]{1,4}|([0-9a-fA-F]{1,4}:){1,7}:|([0-9a-fA-F]{1,4}:){1,6}:[0-9a-fA-F]{1,4}|([0-9a-fA-F]{1,4}:){1,5}(:[0-9a-fA-F]{1,4}){1,2}|([0-9a-fA-F]{1,4}:){1,4}(:[0-9a-fA-F]{1,4}){1,3}|([0-9a-fA-F]{1,4}:){1,3}(:[0-9a-fA-F]{1,4}){1,4}|([0-9a-fA-F]{1,4}:){1,2}(:[0-9a-fA-F]{1,4}){1,5}|[0-9a-fA-F]{1,4}:((:[0-9a-fA-F]{1,4}){1,6})|:((:[0-9a-fA-F]{1,4}){1,7}|:)|fe80:(:[0-9a-fA-F]{0,4}){0,4}%[0-9a-zA-Z]{1,}|::(ffff(:0{1,4}){0,1}:){0,1}((25[0-5]|(2[0-4]|1{0,1}[0-9]){0,1}[0-9])\\.){3,3}(25[0-5]|(2[0-4]|1{0,1}[0-9]){0,1}[0-9])|([0-9a-fA-F]{1,4}:){1,4}:((25[0-5]|(2[0-4]|1{0,1}[0-9]){0,1}[0-9])\\.){3,3}(25[0-5]|(2[0-4]|1{0,1}[0-9]){0,1}[0-9]))$";
}

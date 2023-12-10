package com.codingcube.simpleauth.validated.util;

import com.codingcube.simpleauth.exception.ValidateException;

import java.util.Date;
import java.util.List;
import java.util.regex.Pattern;

import static com.codingcube.simpleauth.validated.util.SVU.notNull;

/**
 * 非必选项的处理（为null也返回true且不抛出异常）
 */
public class SVOU {

    /**
     * 检查base 是否在 start 与 end 之间(Integer)*
     */
    public static boolean range(Integer base, Integer start, Integer end){
        final boolean aNull = notNull(base, start, end);
        if(!aNull){
            return true;
        }
        return (base >= start && base <= end);
    }

    public static BooleanCompute rangeDelay(Integer base, Integer start, Integer end){
        return ()-> range(base, start, end);
    }
    public static boolean range(String message, Integer base, Integer start, Integer end){
        final boolean aNull = notNull(base, start, end);
        if(!aNull){
            return true;
        }
        if (!(base >= start && base <= end)){
            throw new ValidateException(message);
        }
        return true;
    }

    public static BooleanCompute rangeDelay(String message, Integer base, Integer start, Integer end){
        return ()-> range(message, base, start, end);
    }

    /**
     * 检查base 是否在 start 与 end 之间 (Long)*
     */
    public static boolean range(Long base, Long start, Long end){
        final boolean aNull = notNull(base, start, end);
        if(!aNull){
            return true;
        }
        return (base >= start && base <= end);
    }

    public static BooleanCompute rangeDelay(Long base, Long start, Long end){
        return ()-> range(base, start, end);
    }
    public static boolean range(String message, Long base, Long start, Long end){
        final boolean aNull = notNull(base, start, end);
        if(!aNull){
            return true;
        }
        if (!(base >= start && base <= end)){
            throw new ValidateException(message);
        }
        return true;
    }

    public static BooleanCompute rangeDelay(String message, Long base, Long start, Long end){
        return ()-> range(message, base, start, end);
    }

    /**
     * 检查时间是否在在 start 与 end 之间(Date)*
     */
    public static boolean range(Date base, Date start, Date end){
        final boolean aNull = notNull(base, start, end);
        if(!aNull){
            return true;
        }
        return base.compareTo(start) >= 0 && end.compareTo(base) >= 0;
    }

    public static BooleanCompute rangeDelay(Date base, Date start, Date end){
        return ()-> SVU.range(base, start, end);
    }
    public static boolean range(String message, Date base, Date start, Date end){
        final boolean aNull = notNull(base, start, end);
        if(!aNull){
            return true;
        }
        final boolean result = base.compareTo(start) >= 0 && end.compareTo(base) >= 0;
        if (!result){
            throw new ValidateException(message);
        }
        return true;
    }

    public static BooleanCompute rangeDelay(String message, Date base, Date start, Date end){
        return ()-> range(message, base, start, end);
    }

    /**
     * 检查字符串长度 *
     */
    public static boolean lengthRange(String base, Integer start, Integer end){
        final boolean aNull = notNull(base, start, end);
        if (!aNull){
            return true;
        }
        final int length = base.length();
        return length >= start && length <= end;
    }

    public static BooleanCompute lengthRangeDelay(String base, Integer start, Integer end){
        return ()-> lengthRange(base, start, end);
    }
    public static boolean lengthRange(String message, String base, Integer start, Integer end){
        final boolean aNull = notNull(base, start, end);
        if (!aNull){
            return true;
        }
        final int length = base.length();
        final boolean result = length >= start && length <= end;
        if (!result){
            throw new ValidateException(message);
        }
        return true;
    }

    public static BooleanCompute lengthRangeDelay(String message, String base, Integer start, Integer end){
        return ()-> SVU.lengthRange(message, base, start, end);
    }

    /**
     * 正则校验
     */
    public static boolean pattern(String content, String regex){
        final boolean notNull = notNull(content, regex);
        if (!notNull){
            return true;
        }
        Pattern pattern = Pattern.compile(regex);
        return pattern.matcher(content).matches();
    }
    public static BooleanCompute patternDelay(String content, String regex){
        return ()-> pattern(content, regex);
    }
    public static boolean pattern(String message, String content, String regex){
        final boolean notNull = notNull(message, content, regex);
        if (!notNull){
            return true;
        }
        Pattern pattern = Pattern.compile(regex);
        boolean matches = pattern.matcher(content).matches();
        if (!matches){
            throw new ValidateException(message);
        }
        return true;
    }
    public static BooleanCompute patternDelay(String message, String content, String regex){
        return ()-> pattern(message, content, regex);
    }

    public static <T> boolean checkList(List<T> list, BooleanGenericCompute<T> bc){
        if (list == null || list.size()==0){
            return true;
        }
        for (T t : list) {
            if (!bc.run(t)) {
                return false;
            }
        }
        return true;
    }

    public static <T> boolean checkList(String message, List<T> list, BooleanGenericCompute<T> bc){
        if (list == null || list.size()==0){
            return true;
        }
        for (T t : list) {
            if (!bc.run(t)) {
                throw new ValidateException(message);
            }
        }
        return true;
    }

    public static <T> BooleanCompute checkListDelay(List<T> list, BooleanGenericCompute<T> bc){
        return () -> checkList(list, bc);
    }

    public static <T> BooleanCompute checkListDelay(String message, List<T> list, BooleanGenericCompute<T> bc){
        return () -> checkList(message, list, bc);
    }

}

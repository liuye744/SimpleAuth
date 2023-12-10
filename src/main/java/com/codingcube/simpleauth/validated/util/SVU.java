package com.codingcube.simpleauth.validated.util;


import com.codingcube.simpleauth.exception.ValidateException;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.regex.Pattern;

/**
 * @author CodingCube<br>
 * SVU(SimpleValidateUtils)
 */
public class SVU {
    /**
     * 检查参数中是否存在null值
     */
    public static boolean notNull(Object ...args){
        for (Object arg : args) {
            if (arg == null) return false;
        }
        return true;
    }

    /**
     * 检查参数中是否存在null值
     */
    public static BooleanCompute notNullDelay(Object ...args){
        return ()-> notNull(args);
    }

    /**
     * 检查参数中是否存在null值，若存在则抛出异常
     */
    public static boolean notNullThrow(String message, Object ...args){
        for (Object arg : args) {
            if (arg == null) throw new ValidateException(message);
        }
        return true;
    }
    /**
     * 检查参数中是否存在null值，若存在则抛出异常
     */
    public static BooleanCompute notNullDelayThrow(String message, Object ...args){
        return ()-> notNullThrow(message, args);
    }

    /**
     * 检查base 是否在 start 与 end 之间(Integer)
     */
    public static boolean range(Integer base, Integer start, Integer end){
        return SVU.notNull(base, start ,end) && (base >= start && base <= end);
    }
    /**
     * 检查base 是否在 start 与 end 之间(Integer)
     */
    public static BooleanCompute rangeDelay(Integer base, Integer start, Integer end){
        return ()-> range(base, start, end);
    }
    /**
     * 检查base 是否在 start 与 end 之间(Integer)。 不成立则抛出异常
     */
    public static boolean range(String message, Integer base, Integer start, Integer end){
        final boolean result = SVU.notNull(base, start ,end) && (base >= start && base <= end);
        if (!result){
            throw new ValidateException(message);
        }
        return true;
    }

    /**
     * 检查base 是否在 start 与 end 之间(Integer)。 不成立则抛出异常
     */
    public static BooleanCompute rangeDelay(String message, Integer base, Integer start, Integer end){
        return ()-> range(message, base, start, end);
    }

    /**
     * 检查base 是否在 start 与 end 之间 (Long)
     */
    public static boolean range(Long base, Long start, Long end){
        return SVU.notNull(base, start ,end) && (base >= start && base <= end);
    }

    /**
     * 检查base 是否在 start 与 end 之间 (Long)
     */
    public static BooleanCompute rangeDelay(Long base, Long start, Long end){
        return ()-> range(base, start, end);
    }

    /**
     * 检查base 是否在 start 与 end 之间 (Long)，不成立则抛出异常
     */
    public static boolean range(String message, Long base, Long start, Long end){
        final boolean result = SVU.notNull(base, start ,end) && (base >= start && base <= end);
        if (!result){
            throw new ValidateException(message);
        }
        return true;
    }

    /**
     * 检查base 是否在 start 与 end 之间 (Long)，不成立则抛出异常
     */
    public static BooleanCompute rangeDelay(String message, Long base, Long start, Long end){
        return ()-> range(message, base, start, end);
    }

    /**
     * 检查时间是否在在 start 与 end 之间(Date)
     */
    public static boolean range(Date base, Date start, Date end){
        return notNull(base, start, end) && base.compareTo(start) >= 0 && end.compareTo(base) >= 0;
    }

    /**
     * 检查时间是否在在 start 与 end 之间(Date)
     */
    public static BooleanCompute rangeDelay(Date base, Date start, Date end){
        return ()-> SVU.range(base, start, end);
    }

    /**
     * 检查时间是否在在 start 与 end 之间(Date)，不成立则抛出异常
     */
    public static boolean range(String message, Date base, Date start, Date end){
        final boolean result = notNull(base, start, end) && base.compareTo(start) >= 0 && end.compareTo(base) >= 0;
        if (!result){
            throw new ValidateException(message);
        }
        return true;
    }

    /**
     * 检查时间是否在在 start 与 end 之间(Date)，不成立则抛出异常
     */
    public static BooleanCompute rangeDelay(String message, Date base, Date start, Date end){
        return ()-> range(message, base, start, end);
    }

    /**
     * 检查字符串长度
     */
    public static boolean lengthRange(String base, Integer start, Integer end){
        final int length;
        return SVU.notNull(base, start, end) && (length = base.length()) >= start && length <= end;
    }

    /**
     * 检查字符串长度
     */
    public static BooleanCompute lengthRangeDelay(String base, Integer start, Integer end){
        return ()-> lengthRange(base, start, end);
    }

    /**
     * 检查字符串长度 ，不成立则抛出异常
     */
    public static boolean lengthRange(String message, String base, Integer start, Integer end){
        final int length;
        final boolean result = SVU.notNull(base, start, end) && (length = base.length()) >= start && length <= end;
        if (!result){
            throw new ValidateException(message);
        }
        return true;
    }

    /**
     * 检查字符串长度 ，不成立则抛出异常
     */
    public static BooleanCompute lengthRangeDelay(String message, String base, Integer start, Integer end){
        return ()-> SVU.lengthRange(message, base, start, end);
    }

    /**
     * 正则校验
     */
    public static boolean pattern(String content, String regex){
        final boolean notNull = notNull(content, regex);
        if (!notNull){
            return false;
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
            throw new ValidateException(message);
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
    public static boolean isValueInRange(Serializable target, Serializable ...args){
        for (Serializable arg : args) {
            if (arg.equals(target)){
                return true;
            }
        }
        return false;
    }
    public static boolean isValueInRangeThrow(String message, Serializable target, Serializable ...args){
        for (Serializable arg : args) {
            if (arg.equals(target)){
                return true;
            }
        }
        throw new ValidateException(message);
    }

    public static BooleanCompute isValueInRangeDelay(Serializable target, Serializable ...args){
        return () -> isValueInRange(target, args);
    }
    public static BooleanCompute isValueInRangeThrowDelay(String message, Serializable target, Serializable ...args){
        return () -> isValueInRangeThrow(message, target, args);
    }

    /**
     * 检查传入的参数是否存在false
     * @param args boolean[]
     */
    public static boolean notFalse(boolean ...args){
        for (boolean arg: args){
            if (!arg) return false;
        }
        return true;
    }
    public static boolean notFalse(BooleanCompute ...booleanComputes){
        for (BooleanCompute bc: booleanComputes){
            if (!bc.run()) return false;
        }
        return true;
    }
    public static boolean notFalse(String message, boolean ...args){
        for (boolean arg: args){
            if (!arg) throw new ValidateException(message);
        }
        return true;
    }
    public static boolean notFalse(String message, BooleanCompute ...booleanComputes){
        for (BooleanCompute bc: booleanComputes){
            if (!bc.run()) throw new ValidateException(message);
        }
        return true;
    }

    /**
     * 若obj不为空则运行booleanCompute
     */
    public static void processIfNotEmpty(Object obj, BooleanCompute booleanCompute){
        if (obj != null){
            booleanCompute.run();
        }
    }

    /**
     * 检查传入的参数是否存在true
     * @param args boolean[]
     */
    public static boolean notTrue(boolean ...args){
        for (boolean arg: args){
            if (arg) return false;
        }
        return true;
    }

    public static boolean notTrue(BooleanCompute ...booleanComputes){
        for (BooleanCompute bc: booleanComputes){
            if (bc.run()) return false;
        }
        return true;
    }

    public static boolean notTrue(String message,boolean ...args){
        for (boolean arg: args){
            if (arg) throw new ValidateException(message);
        }
        return true;
    }
    public static boolean notTrue(String message, BooleanCompute ...booleanComputes){
        for (BooleanCompute bc: booleanComputes){
            if (bc.run()) throw new ValidateException(message);
        }
        return true;
    }

    public static <T> boolean checkList(List<T> list, BooleanGenericCompute<T> bc){
        for (T t : list) {
            if (!bc.run(t)) {
                return false;
            }
        }
        return true;
    }

    public static <T> boolean checkList(String message, List<T> list, BooleanGenericCompute<T> bc){
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

    public static boolean stringNotBlank(String ...args){
        for (String arg : args) {
            if (arg == null || "".equals(arg.trim())){
                return false;
            }
        }
        return true;
    }

    public static boolean stringNotBlankThrow(String message, String ...args){
        for (String arg : args) {
            if (arg == null || "".equals(arg.trim())){
                throw new ValidateException(message);
            }
        }
        return true;
    }

    public static BooleanCompute stringNotBlankDelay(String ...args){
        return () -> stringNotBlank(args);
    }

    public static BooleanCompute stringNotBlankThrowDelay(String message, String ...args){
        return () -> stringNotBlankThrow(message, args);
    }
}
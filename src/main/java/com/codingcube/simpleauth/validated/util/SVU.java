package com.codingcube.simpleauth.validated.util;


import com.codingcube.simpleauth.exception.ValidateException;

import java.util.Date;

/**
 * @author CodingCube<br>
 * SVU(SimpleValidateUtils)
 */
public class SVU {
    /**
     * 检查参数中是否存在null值 *
     */
    public static boolean notNull(Object ...args){
        for (Object arg : args) {
            if (arg == null) return false;
        }
        return true;
    }
    public static BooleanCompute notNullDelay(Object ...args){
        return ()-> notNull(args);
    }
    public static boolean notNullThrow(String message, Object ...args){
        for (Object arg : args) {
            if (arg == null) throw new ValidateException(message);
        }
        return true;
    }
    public static BooleanCompute notNullDelayThrow(String message, Object ...args){
        return ()-> notNullThrow(message, args);
    }

    /**
     * 检查base 是否在 start 与 end 之间(Integer)*
     */
    public static boolean range(Integer base, Integer start, Integer end){
        return SVU.notNull(base, start ,end) && (base > start && base < end);
    }

    public static BooleanCompute rangeDelay(Integer base, Integer start, Integer end){
        return ()-> range(base, start, end);
    }
    public static boolean range(String message, Integer base, Integer start, Integer end){
        final boolean result = SVU.notNull(base, start ,end) && (base > start && base < end);
        if (!result){
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
        return SVU.notNull(base, start ,end) && (base > start && base < end);
    }

    public static BooleanCompute rangeDelay(Long base, Long start, Long end){
        return ()-> range(base, start, end);
    }
    public static boolean range(String message, Long base, Long start, Long end){
        final boolean result = SVU.notNull(base, start ,end) && (base > start && base < end);
        if (!result){
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
        return notNull(base, start, end) && base.compareTo(start) > 0 && end.compareTo(base) > 0;
    }

    public static BooleanCompute rangeDelay(Date base, Date start, Date end){
        return ()-> SVU.range(base, start, end);
    }
    public static boolean range(String message, Date base, Date start, Date end){
        final boolean result = notNull(base, start, end) && base.compareTo(start) > 0 && end.compareTo(base) > 0;
        if (!result){
            throw new ValidateException(message);
        }
        return true;
    }

    public static BooleanCompute rangeDelay(String message, Date base, Date start, Date end){
        return ()-> {
            final boolean result = notNull(base, start, end) && base.compareTo(start) > 0 && end.compareTo(base) > 0;
            if (!result){
                throw new ValidateException(message);
            }
            return true;
        };
    }

    /**
     * 检查字符串长度 *
     */
    public static boolean lengthRange(String base, Integer start, Integer end){
        final int length;
        return SVU.notNull(base, start, end) && (length = base.length()) > start && length < end;
    }

    public static BooleanCompute lengthRangeDelay(String base, Integer start, Integer end){
        return ()-> lengthRange(base, start, end);
    }
    public static boolean lengthRange(String message, String base, Integer start, Integer end){
        final int length;
        final boolean result = SVU.notNull(base, start, end) && (length = base.length()) > start && length < end;
        if (!result){
            throw new ValidateException(message);
        }
        return true;
    }

    public static BooleanCompute lengthRangeDelay(String message, String base, Integer start, Integer end){
        return ()-> SVU.lengthRange(message, base, start, end);
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
}
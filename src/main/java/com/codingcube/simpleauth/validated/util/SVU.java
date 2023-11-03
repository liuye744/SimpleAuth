package com.codingcube.simpleauth.validated.util;


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
        return ()-> {
            for (Object arg : args) {
                if (arg == null) return false;
            }
            return true;
        };
    }

    /**
     * 检查base 是否在 start 与 end 之间 *
     */
    public static boolean range(Integer base, Integer start, Integer end){
        return base > start && base < end;
    }

    public static BooleanCompute rangeDelay(Integer base, Integer start, Integer end){
        return ()-> base > start && base < end;
    }

    /**
     * 检查字符串长度 *
     */
    public static boolean lengthRange(String base, Integer start, Integer end){
        final int length = base.length();
        return length > start && length < end;
    }

    public static BooleanCompute lengthRangeDelay(String base, Integer start, Integer end){
        return ()-> {
            final int length = base.length();
            return length > start && length < end;
        };
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
}

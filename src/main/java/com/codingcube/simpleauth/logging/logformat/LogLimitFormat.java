package com.codingcube.simpleauth.logging.logformat;

import com.codingcube.simpleauth.limit.LimitInfoUtil;
import com.codingcube.simpleauth.properties.LogProper;
import com.codingcube.simpleauth.limit.strategic.EffectiveStrategic;
import com.codingcube.simpleauth.auth.strategic.SignStrategic;

import java.util.Date;
import java.util.Deque;

public class LogLimitFormat {
    private final int times;
    private final int seconds;
    private final int ban;
    private final String item;
    private final Class<? extends SignStrategic> signStrategic;
    private final String sign;
    private final String source;
    private final boolean judgeAfterReturn;
    private final Class<? extends EffectiveStrategic> effectiveStrategic;
    private final boolean effective;
    private final boolean result;


    public LogLimitFormat(int times, int seconds, int ban, String item, Class<? extends SignStrategic> signStrategic, String sign, String source, boolean judgeAfterReturn, Class<? extends EffectiveStrategic> effectiveStrategic, boolean effective, boolean result) {
        this.times = times;
        this.seconds = seconds;
        this.ban = ban;
        this.item = item;
        this.signStrategic = signStrategic;
        this.sign = sign;
        this.source = source;
        this.judgeAfterReturn = judgeAfterReturn;
        this.effectiveStrategic = effectiveStrategic;
        this.effective = effective;
        this.result = result;
    }

    @Override
    public String toString() {
        final Deque<Date> optionList = LimitInfoUtil.getSignOptionList(item, sign);
        StringBuilder sb = new StringBuilder("SimpleAuth limit => \r\n" + "\tmax-times: " + times + "\r\n" +
                "\ttime: " + optionList.size() + "\r\n" +
                "\tseconds: " + seconds + "\r\n" +
                "\tban: " + ban + "\r\n" +
                "\titem: " + item + "\r\n" +
                "\tsignStrategic: " + signStrategic.getName() + "\r\n" +
                "\tsign: " + sign + "\r\n" +
                "\tsource: " + source + "\r\n" +
                "\tjudgeAfterReturn: "+ judgeAfterReturn +"\r\n" +
                "\teffectiveStrategic: " + effectiveStrategic.getName() + "\r\n" +
                "\teffective: " + effective + "\r\n");
        if (LogProper.getStaticShowOptList()){
            sb.append("\toptionList: ").append(optionList).append("\r\n");
        }
        sb.append("\tPass or not: ").append(result);
        return sb.toString();
    }
}

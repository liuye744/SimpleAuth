package com.codingcube.simpleauth.logging.logformat;

import com.codingcube.simpleauth.limit.LimitInfoUtil;
import com.codingcube.simpleauth.limit.strategic.RejectedStratagem;
import com.codingcube.simpleauth.limit.util.CompleteLimit;
import com.codingcube.simpleauth.limit.util.TokenLimit;
import com.codingcube.simpleauth.properties.LogProper;
import com.codingcube.simpleauth.limit.strategic.EffectiveStrategic;
import com.codingcube.simpleauth.auth.strategic.SignStrategic;
import com.codingcube.simpleauth.util.AuthHandlerUtil;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
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
    private final Class<? extends RejectedStratagem> rejectedClass;
    private final DateTimeFormatter dateFormatter;


    public LogLimitFormat(int times,
                          int seconds,
                          int ban,
                          String item,
                          Class<? extends SignStrategic> signStrategic,
                          String sign,
                          String source,
                          boolean judgeAfterReturn,
                          Class<? extends EffectiveStrategic> effectiveStrategic,
                          boolean effective, boolean result,
                          Class<? extends RejectedStratagem> rejectedClass) {
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
        this.rejectedClass = rejectedClass;

        this.dateFormatter = DateTimeFormatter.ofPattern(LogProper.getDateFormatStatic());
    }

    @Override
    public String toString() {
        TokenLimit optionList = LimitInfoUtil.getSignOptionList(item, sign);
        if (optionList == null){
            optionList = new CompleteLimit();
        }
        StringBuilder sb = new StringBuilder("SimpleAuth limit => \r\n" + "\tmax-times: " + times + "\r\n" +
                "\ttime: "+dateFormatter.format(LocalDateTime.now())+"\r\n"+
                "\tnumber of times: " + optionList.size() + "\r\n" +
                "\tseconds: " + seconds + "\r\n" +
                "\tban: " + ban + "\r\n" +
                "\titem: " + item + "\r\n" +
                "\tsignStrategic: " +  signStrategic + "\r\n" +
                "\tsign: " + sign + "\r\n" +
                "\tsource: " + source + "\r\n" +
                "\tjudgeAfterReturn: "+ judgeAfterReturn +"\r\n" +
                "\teffectiveStrategic: " + effectiveStrategic + "\r\n" +
                "\teffective: " + effective + "\r\n" +
                "\trejected Class: " + rejectedClass + "\r\n");
        if (LogProper.getStaticShowOptList()){
            sb.append("\toptionList: ").append(optionList).append("\r\n");
        }
        sb.append("\tPass or not: ").append(result);
        return sb.toString();
    }

    public int getTimes() {
        return times;
    }

    public int getSeconds() {
        return seconds;
    }

    public int getBan() {
        return ban;
    }

    public String getItem() {
        return item;
    }

    public Class<? extends SignStrategic> getSignStrategic() {
        return signStrategic;
    }

    public String getSign() {
        return sign;
    }

    public String getSource() {
        return source;
    }

    public boolean isJudgeAfterReturn() {
        return judgeAfterReturn;
    }

    public Class<? extends EffectiveStrategic> getEffectiveStrategic() {
        return effectiveStrategic;
    }

    public boolean isEffective() {
        return effective;
    }

    public boolean isResult() {
        return result;
    }
}

package com.codingcube.logging.logformat;

import com.codingcube.properties.LimitInfoUtil;
import com.codingcube.properties.LogProper;
import com.codingcube.strategic.EffectiveStrategic;
import com.codingcube.strategic.SignStrategic;

import java.util.Date;
import java.util.Deque;

public class LogLimitFormat {
    private int times;
    private int seconds;
    private int ban;
    private String item;
    private Class<? extends SignStrategic> signStrategic;
    private String sign;
    private boolean judgeAfterReturn;
    private Class<? extends EffectiveStrategic> effectiveStrategic;
    private boolean effective;
    private boolean result;

    public LogLimitFormat(int times, int seconds, int ban, String item, Class<? extends SignStrategic> signStrategic, String sign, boolean judgeAfterReturn, Class<? extends EffectiveStrategic> effectiveStrategic, boolean effective, boolean result) {
        this.times = times;
        this.seconds = seconds;
        this.ban = ban;
        this.item = item;
        this.signStrategic = signStrategic;
        this.sign = sign;
        this.judgeAfterReturn = judgeAfterReturn;
        this.effectiveStrategic = effectiveStrategic;
        this.effective = effective;
        this.result = result;
    }

    public int getTimes() {
        return times;
    }

    public void setTimes(int times) {
        this.times = times;
    }

    public int getSeconds() {
        return seconds;
    }

    public void setSeconds(int seconds) {
        this.seconds = seconds;
    }

    public int getBan() {
        return ban;
    }

    public void setBan(int ban) {
        this.ban = ban;
    }

    public String getItem() {
        return item;
    }

    public void setItem(String item) {
        this.item = item;
    }

    public Class<? extends SignStrategic> getSignStrategic() {
        return signStrategic;
    }

    public void setSignStrategic(Class<? extends SignStrategic> signStrategic) {
        this.signStrategic = signStrategic;
    }

    public String getSign() {
        return sign;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }

    public boolean isJudgeAfterReturn() {
        return judgeAfterReturn;
    }

    public void setJudgeAfterReturn(boolean judgeAfterReturn) {
        this.judgeAfterReturn = judgeAfterReturn;
    }

    public Class<? extends EffectiveStrategic> getEffectiveStrategic() {
        return effectiveStrategic;
    }

    public void setEffectiveStrategic(Class<? extends EffectiveStrategic> effectiveStrategic) {
        this.effectiveStrategic = effectiveStrategic;
    }

    public boolean isEffective() {
        return effective;
    }

    public void setEffective(boolean effective) {
        this.effective = effective;
    }

    @Override
    public String toString() {
        final Deque<Date> optionList = LimitInfoUtil.getSignOptionList(item, sign);
        StringBuilder sb = new StringBuilder("SimpleAuth auth => \r\n" + "\tmax-times: " + times + "\r\n" +
                "\ttime: " + optionList.size() + "\r\n" +
                "\tseconds: " + seconds + "\r\n" +
                "\titem: " + item + "\r\n" +
                "\tsignStrategic: " + signStrategic.getName() + "\r\n" +
                "\tsign: " + sign + "\r\n" +
                "\tjudgeAfterReturn: true\r\n" +
                "\teffectiveStrategic: " + effectiveStrategic.getName() + "\r\n" +
                "\teffective: " + effective + "\r\n");
        if (LogProper.getStaticShowOptList()){
            sb.append("\toptionList: ").append(optionList).append("\r\n");
        }
        sb.append("\tresult: ").append(result);
        return sb.toString();
    }
}

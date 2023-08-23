package com.codingcube.scheduled;

import com.codingcube.properties.LimitInfoUtil;
import org.springframework.scheduling.annotation.Scheduled;


public class AutoClearExpiredRecordScheduled {
    @Scheduled(cron = "${simple-auth.cron:0 30 3 * * 1}")
    public void execute(){
        LimitInfoUtil.clearExpiredRecord();
    }
}

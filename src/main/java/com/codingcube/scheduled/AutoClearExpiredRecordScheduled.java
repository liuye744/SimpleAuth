package com.codingcube.scheduled;

import com.codingcube.properties.LimitInfoUtil;
import org.springframework.scheduling.annotation.Scheduled;

/**
 * @author CodingCube<br>*
 * Configuration Class for Scheduled Cleanup of Expired Access Records*
 */
public class AutoClearExpiredRecordScheduled {
    @Scheduled(cron = "${simple-auth.clear.cron:0 30 3 * * 1}")
    public void execute(){
        LimitInfoUtil.clearExpiredRecord();
    }
}

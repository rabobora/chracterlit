package com.vamos.characterlit.bid.component;

import com.vamos.characterlit.bid.service.ScheduledService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@RequiredArgsConstructor
public class BidStateUpdateTask {

    private final ScheduledService scheduledService;

    @Scheduled(cron = "0 0 * * * ?")
    public void bidOpen() {
        log.info("Bid open schedule running...");
        scheduledService.bidOpen();
    }

    @Scheduled(cron = "0 1 * * * ?")
    public void bidClose() {
        log.info("Bid close schedule running...");
        scheduledService.bidClose();
    }

}

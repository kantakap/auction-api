package com.kantakap.auction.quartz;

import com.kantakap.auction.model.Auction;
import com.kantakap.auction.quartz.job.AuctionProcessJob;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.quartz.*;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

@Component
@RequiredArgsConstructor
@Slf4j
public class QuartzTestSample {
    private final Scheduler scheduler;

    public void testQuartz(Auction auction) {
        try {
            JobDetail job = JobBuilder.newJob(AuctionProcessJob.class)
                    .withIdentity(new JobKey(auction.getId(), "AuctionProcessJob"))
                    .build();
            var afterFiveSeconds = LocalDateTime.now().plusSeconds(auction.getInitialTime() + 2);
            Trigger trigger = TriggerBuilder.newTrigger()
                    .forJob(job)
                    .withIdentity(auction.getId(), "AuctionProcessJob")
                    .startAt(Date.from(afterFiveSeconds.atZone(ZoneId.systemDefault()).toInstant()))
                    .build();
            scheduler.scheduleJob(job, trigger);
            scheduler.start();
        } catch (SchedulerException e) {
            throw new RuntimeException(e);
        }
    }

    public void stopJob(String jobId) {
        try {
            log.info("JobId: {}", jobId);
            var triggerKey = TriggerKey.triggerKey(jobId, "AuctionProcessJob");
            log.info("Exists: {}", scheduler.checkExists(triggerKey));
            scheduler.unscheduleJob(triggerKey);
        } catch (SchedulerException e) {
            throw new RuntimeException(e);
        }
    }
}

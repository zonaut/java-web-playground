package com.zonaut.quartz.jobs;

import lombok.extern.log4j.Log4j2;
import org.quartz.*;
import org.springframework.scheduling.quartz.QuartzJobBean;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.Date;

import static org.quartz.TriggerBuilder.newTrigger;

@Log4j2
@Component
public class CommunicationRequestJob extends QuartzJobBean {

    public static final String REQUEST_DATA_SIMULATE_ERROR = "simulate_error";
    public static final String REQUEST_DATA_SIMULATE_HARD_FAIL = "simulate_hard_fail";

    public static final String REQUEST_DATA_RETRY_COUNT = "count";
    public static final String REQUEST_DATA_SUBJECT = "subject";

    private final static int MAX_RETRIES = 2;

    @Override
    protected void executeInternal(JobExecutionContext context) throws JobExecutionException {
        log.info("Executing Job with key {}", context.getJobDetail().getKey());

        JobDataMap dataMap = context.getMergedJobDataMap();

        int retries = dataMap.containsKey(REQUEST_DATA_RETRY_COUNT) ? dataMap.getIntValue(REQUEST_DATA_RETRY_COUNT) : 0;
        String subject = dataMap.getString(REQUEST_DATA_SUBJECT);
        boolean simulateError = dataMap.getBoolean(REQUEST_DATA_SIMULATE_ERROR);
        boolean simulateHardFail = dataMap.getBoolean(REQUEST_DATA_SIMULATE_HARD_FAIL);

        try {
            log.info("=> Calling some API ...");

            if (simulateError || simulateHardFail) {
                log.error("===> FAIL: Trying to reschedule communication due to error");
                throw new RuntimeException();
            }

            log.info("SUCCESS Handling communication for: {}", subject);
            log.info("# {}", "-".repeat(50));
        } catch (Exception e) {
            checkIfRetryCountIsExceeded(retries);
            rescheduleJob(context, dataMap, ++retries, subject);
        }
    }

    private void checkIfRetryCountIsExceeded(int retries) throws JobExecutionException {
        boolean isCurrentRetryCountExceeded = retries >= MAX_RETRIES;
        log.info("Current retry count {} exceeded? {}", retries, isCurrentRetryCountExceeded);
        if (isCurrentRetryCountExceeded) {
            log.fatal("Not rescheduling");
            JobExecutionException jobExecutionException = new JobExecutionException("Retries exceeded");
            jobExecutionException.setUnscheduleAllTriggers(true);
            throw jobExecutionException;
        }
    }

    private void rescheduleJob(JobExecutionContext context, JobDataMap dataMap, int retryCount, String subject) throws JobExecutionException {
        log.info("Rescheduling communication with retryCount {} for: {}", retryCount, subject);
        try {
            dataMap.putAsString(REQUEST_DATA_RETRY_COUNT, retryCount);
            dataMap.put(REQUEST_DATA_SIMULATE_ERROR, false);

            final Scheduler scheduler = context.getScheduler();
            final TriggerKey triggerKey = context.getTrigger().getKey();

            Trigger newTrigger = newTrigger()
                    .withIdentity(triggerKey)
                    .forJob(context.getJobDetail().getKey())
                    .withDescription("retry #" + retryCount)
                    .withPriority(context.getTrigger().getPriority())
                    .startAt(Date.from(Instant.now().plusSeconds(2)))
                    .usingJobData(dataMap)
                    .build();

            scheduler.rescheduleJob(triggerKey, newTrigger);
        } catch (SchedulerException e) {
            log.fatal("Error rescheduling job for {}", subject);
            throw new JobExecutionException(e);
        }
    }

}

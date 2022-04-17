package com.zonaut.quartz.jobs;

import com.zonaut.quartz.domain.CommunicationRequest;
import com.zonaut.quartz.domain.ScheduledCommunicationResponse;
import lombok.extern.log4j.Log4j2;
import org.quartz.*;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Date;

import static com.zonaut.quartz.jobs.CommunicationRequestJob.*;

@Log4j2
@Service
public class CommunicationSchedulerService {

    public static final String COMMUNICATION_REQUEST_JOB = "communication-request-job";
    public static final String COMMUNICATION_REQUEST_JOB_DESCRIPTION = "communication-request-job-description";
    public static final String COMMUNICATION_REQUEST_JOB_TRIGGER = "communication-request-job-trigger";
    public static final String COMMUNICATION_REQUEST_JOB_TRIGGER_DESCRIPTION = "communication-request-job-trigger-description";

    private final Scheduler scheduler;

    public CommunicationSchedulerService(Scheduler scheduler) {
        this.scheduler = scheduler;
    }

    public ScheduledCommunicationResponse scheduleCommunicationRequest(CommunicationRequest communicationRequest) {
        try {
            JobDetail jobDetail = buildJobDetail(communicationRequest);
            Trigger trigger = buildJobTrigger(jobDetail, communicationRequest.getTriggerAt());

            scheduler.scheduleJob(jobDetail, trigger);

            return new ScheduledCommunicationResponse(
                    true,
                    jobDetail.getKey().getName(),
                    jobDetail.getKey().getGroup(),
                    "Communication request has been scheduled!"
            );
        } catch (SchedulerException ex) {
            log.error("Error scheduling communication request", ex);
            return new ScheduledCommunicationResponse(
                    false,
                    "Error scheduling communication request!"
            );
        }
    }

    private JobDetail buildJobDetail(CommunicationRequest communicationRequest) {
        JobDataMap jobDataMap = new JobDataMap();

        jobDataMap.put(REQUEST_DATA_SUBJECT, communicationRequest.getSubject());
        jobDataMap.put(REQUEST_DATA_SIMULATE_ERROR, communicationRequest.isSimulateOneErrorButRetryWithSuccessUnlessHardFailIsTrue());
        jobDataMap.put(REQUEST_DATA_SIMULATE_HARD_FAIL, communicationRequest.isSimulateRetryUntilMaxRetriesExceededAndHardFail());

        return JobBuilder.newJob(CommunicationRequestJob.class)
                .withIdentity(communicationRequest.getId().toString(), COMMUNICATION_REQUEST_JOB)
                .withDescription(COMMUNICATION_REQUEST_JOB_DESCRIPTION)
                .usingJobData(jobDataMap)
                .storeDurably()
                .build();
    }

    private Trigger buildJobTrigger(JobDetail jobDetail, Instant startAt) {
        return TriggerBuilder.newTrigger()
                .forJob(jobDetail)
                .withIdentity(jobDetail.getKey().getName(), COMMUNICATION_REQUEST_JOB_TRIGGER)
                .withDescription(COMMUNICATION_REQUEST_JOB_TRIGGER_DESCRIPTION)
                .startAt(Date.from(startAt))
                .withSchedule(SimpleScheduleBuilder.simpleSchedule().withMisfireHandlingInstructionFireNow())
                .build();
    }

}

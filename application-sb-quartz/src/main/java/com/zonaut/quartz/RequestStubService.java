package com.zonaut.quartz;

import com.zonaut.quartz.domain.CommunicationRequest;
import com.zonaut.quartz.domain.ScheduledCommunicationResponse;
import com.zonaut.quartz.jobs.CommunicationSchedulerService;
import lombok.extern.log4j.Log4j2;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.UUID;

import static com.zonaut.common.stubs.StubGeneratorUtil.randomBoolean;
import static com.zonaut.common.stubs.StubGeneratorUtil.randomWordsAsString;

@Log4j2
@Service
public class RequestStubService implements CommandLineRunner {

    private final CommunicationSchedulerService communicationSchedulerService;

    public RequestStubService(CommunicationSchedulerService communicationSchedulerService) {
        this.communicationSchedulerService = communicationSchedulerService;
    }

    @Override
    public void run(String... args) {
        log.info("# {}", "-".repeat(50));
        log.info("# Triggering a new communication request ...");

        ScheduledCommunicationResponse response = communicationSchedulerService.scheduleCommunicationRequest(
                CommunicationRequest.builder()
                        .id(UUID.randomUUID())
                        .subject(randomWordsAsString(5))
                        .triggerAt(Instant.now().plusSeconds(3))
                        .simulateOneErrorButRetryWithSuccessUnlessHardFailIsTrue(randomBoolean())
                        .simulateRetryUntilMaxRetriesExceededAndHardFail(randomBoolean())
                        .build()
        );

        log.info("Schedule job status success? {}", response.isSuccess());
        log.info("Schedule job status message: {}", response.getMessage());
    }

}

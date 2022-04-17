package com.zonaut.quartz.domain;

import lombok.Builder;
import lombok.Getter;

import java.time.Instant;
import java.util.UUID;

@Getter
@Builder
public class CommunicationRequest {

    private UUID id;
    private String subject;
    private Instant triggerAt;

    private boolean simulateOneErrorButRetryWithSuccessUnlessHardFailIsTrue;
    private boolean simulateRetryUntilMaxRetriesExceededAndHardFail;

}

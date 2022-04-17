package com.zonaut.quartz.domain;

import lombok.Getter;

@Getter
public class ScheduledCommunicationResponse {

    private final boolean success;
    private final String message;

    private String jobId;
    private String jobGroup;

    public ScheduledCommunicationResponse(boolean success, String message) {
        this.success = success;
        this.message = message;
    }

    public ScheduledCommunicationResponse(boolean success, String jobId, String jobGroup, String message) {
        this.success = success;
        this.jobId = jobId;
        this.jobGroup = jobGroup;
        this.message = message;
    }
}

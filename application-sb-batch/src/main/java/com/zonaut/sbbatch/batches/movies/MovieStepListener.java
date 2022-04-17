package com.zonaut.sbbatch.batches.movies;

import lombok.extern.log4j.Log4j2;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.StepExecutionListener;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;

import java.util.Date;

@Log4j2
@Component
public class MovieStepListener implements StepExecutionListener {

    private final String stepStartTime = "start";

    @Override
    public void beforeStep(@NonNull StepExecution stepExecution) {
        stepExecution.getExecutionContext().put(stepStartTime, new Date().getTime());

        log.info("Step name: {} started", stepExecution.getStepName());
    }

    @Override
    public ExitStatus afterStep(@NonNull StepExecution stepExecution) {
        long elapsedTime = new Date().getTime() - stepExecution.getExecutionContext().getLong(stepStartTime);

        log.info("Step name: {} ended.", stepExecution.getStepName());
        log.info("Running time is {} milliseconds.", elapsedTime);
        log.info("Read count: {}", stepExecution.getReadCount());
        log.info("Write count: {}", stepExecution.getWriteCount());

        return ExitStatus.COMPLETED;
    }
}

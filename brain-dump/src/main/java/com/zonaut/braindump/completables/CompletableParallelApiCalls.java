package com.zonaut.braindump.completables;

import java.time.Duration;
import java.time.Instant;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class CompletableParallelApiCalls {

    public static final int RESPONSE_DELAY_IN_MILLISECONDS = 500;

    public static void main(String[] args) throws ExecutionException, InterruptedException {
        var service = new CompletableParallelApiCalls();

        var start = Instant.now();

        example1(service);
        example2(service);

        var duration = Duration.between(start, Instant.now());
        System.out.println("Total time taken for all examples: " + duration.toMillis());
    }

    private static void example1(CompletableParallelApiCalls service) throws InterruptedException, ExecutionException {
        var start = Instant.now();

        service.fetchAllResponsesAsFutures("example 1").get()
                .forEach(System.out::println);

        var duration = Duration.between(start, Instant.now());
        System.out.println("Total time taken for example 1: " + duration.toMillis());
    }

    private static void example2(CompletableParallelApiCalls service) {
        Instant start = Instant.now();

        service.fetchAllResponsesAsCalled("example 2")
                .forEach(System.out::println);

        Duration duration = Duration.between(start, Instant.now());
        System.out.println("Total time taken for example 2: " + duration.toMillis());
    }

    public CompletableFuture<List<String>> fetchAllResponsesAsFutures(String example) {
        var future1 = getFuture1(example);
        var future2 = getFuture2(example);
        var future3 = getFuture3(example);

        var futures = List.of(future1, future3, future2);

        return CompletableFuture.allOf(future1, future2, future3)
                .thenApply(future -> futures.stream().map(CompletableFuture::join).toList());
    }

    public List<String> fetchAllResponsesAsCalled(String example) {
        return Stream.of(getFuture1(example), getFuture2(example), getFuture3(example))
                .map(CompletableFuture::join)
                .collect(Collectors.toList());
    }

    private CompletableFuture<String> getFuture1(String example) {
        return CompletableFuture.supplyAsync(() -> stubbedApi1(example));
    }

    private CompletableFuture<String> getFuture2(String example) {
        return CompletableFuture.supplyAsync(() -> stubbedApi2(example));
    }

    private CompletableFuture<String> getFuture3(String example) {
        return CompletableFuture.supplyAsync(() -> stubbedApi3(example));
    }

    private String stubbedApi1(String example) {
        sleep(RESPONSE_DELAY_IN_MILLISECONDS);
        return "Response from api 1 for " + example;
    }

    private String stubbedApi2(String example) {
        sleep(RESPONSE_DELAY_IN_MILLISECONDS);
        return "Response from api 2 for " + example;
    }

    private String stubbedApi3(String example) {
        sleep(RESPONSE_DELAY_IN_MILLISECONDS);
        return "Response from api 3 for " + example;
    }

    private void sleep(int milliseconds) {
        try {
            TimeUnit.MILLISECONDS.sleep(milliseconds);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}

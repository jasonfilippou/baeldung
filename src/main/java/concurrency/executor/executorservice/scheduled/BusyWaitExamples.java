package concurrency.executor.executorservice.scheduled;

import java.util.concurrent.*;

public class BusyWaitExamples {

    private static String getCurrentThreadName(){
        return Thread.currentThread().getName();
    }

    public static void main(String[] args) {
        ScheduledExecutorService scheduledExecutorService = Executors.newSingleThreadScheduledExecutor();
        // ScheduledExecutorService scheduledExecutorService = Executors.newScheduledThreadPool(3);
        System.out.println("Thread " + getCurrentThreadName() + " will now submit scheduled jobs to ScheduledExecutor instance.");
        ScheduledFuture<String> stringScheduledFuture = scheduledExecutorService.schedule(() -> {
            System.out.println("Thread " + getCurrentThreadName() + " about to busy-wait for 1 second.");
            Thread.sleep(1000);
            System.out.println("Thread " + getCurrentThreadName() + " busy-waited for 1 second and will now return value.");
            return "Hello World!";
        }, 4, TimeUnit.SECONDS);
        ScheduledFuture<Integer> integerScheduledFuture = scheduledExecutorService.schedule(() -> {
            System.out.println("Thread " + getCurrentThreadName() + " about to busy-wait for 1 second.");
            Thread.sleep(1000);
            System.out.println("Thread " + getCurrentThreadName() + " busy-waited for 1 second and will now return value.");
            return 50;
        }, 6, TimeUnit.SECONDS);
        try {
            System.out.println("Thread " + getCurrentThreadName() + " will now busy-wait for 1 second.");
            Thread.sleep(1000);
            System.out.println("Thread " + getCurrentThreadName() + " busy-waited for 1 second.");
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // Await the execution...
        try {
            scheduledExecutorService.awaitTermination(20, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // Checking stringScheduledFuture return value....
        if(stringScheduledFuture.isDone() && !stringScheduledFuture.isCancelled()) {
            try {
                System.out.println(stringScheduledFuture.get());
            } catch (InterruptedException | ExecutionException e) {
                e.printStackTrace();
            }
        }

        // Checking integerScheduledFuture return value....
        if(integerScheduledFuture.isDone() && !integerScheduledFuture.isCancelled()) {
            try {
                System.out.println(integerScheduledFuture.get());
            } catch (InterruptedException | ExecutionException e) {
                e.printStackTrace();
            }
        }
        scheduledExecutorService.shutdown();
    }
}

package concurrency.executor.executorservice.scheduled;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;

import static java.util.concurrent.TimeUnit.SECONDS;

// Adapted from official Javadocs: https://docs.oracle.com/en/java/javase/11/docs/api/java.base/java/util/concurrent/ScheduledExecutorService.html
public class BeeperControl {

    public static void main(String[] args) {
        ScheduledExecutorService scheduler =   Executors.newSingleThreadScheduledExecutor();
        Runnable beeper = () -> System.out.println("beep");
        ScheduledFuture<?> beeperHandle = scheduler.scheduleAtFixedRate(beeper, 2, 3, SECONDS);
        Runnable canceller = () -> {
            beeperHandle.cancel(false);
            scheduler.shutdown();
        };
        scheduler.schedule(canceller, 30, SECONDS);
    }
}
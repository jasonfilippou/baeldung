package concurrency.executor.executorservice;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class Main {

    private final static Logger LOG = LoggerFactory.getLogger(Main.class);

    public static void main(String[] args) {
        ExecutorService executorService = Executors.newFixedThreadPool(3);
        // ExecutorService executorService = Executors.newSingleThreadExecutor(Executors.defaultThreadFactory());
        execute(executorService);
    }

    private static void execute(ExecutorService executorService){
        LOG.info("Thread " + Thread.currentThread().getName() + " about to submit a new task to executor.");
        executorService.submit(new Task());
        executorService.submit(new Task());
        executorService.submit(new Task());
        executorService.shutdown();
        // The following call, in conjunction with the use of awaitTermination(), will cause the main thread to
        // forcefully shutdown the ExecutorService. If awaitTermination() is **not** used, the interrupt() will **not**
        // trigger the shutdown of the ExecutorService.
        // Thread.currentThread().interrupt();
        try {
            boolean allTasksExecutedWithinTimeout = executorService.awaitTermination(Task.MILLISECS_TO_SLEEP + 100, TimeUnit.MILLISECONDS);
            if(allTasksExecutedWithinTimeout){
                LOG.info("All tasks terminated within the allotted time.");
            } else {
                LOG.info("Not all tasks terminated within the allotted time.");
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        LOG.info("Thread " +  Thread.currentThread().getName() + " about to do something required after all threads are done.");
    }
}

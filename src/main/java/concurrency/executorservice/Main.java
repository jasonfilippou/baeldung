package concurrency.executorservice;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class Main {

    public static void main(String[] args) {
        ExecutorService executorService = Executors.newFixedThreadPool(3);
        // ExecutorService executorService = Executors.newSingleThreadExecutor(Executors.defaultThreadFactory());
        execute(executorService);
    }

    private static void execute(ExecutorService executorService){
        System.out.println("Thread " + Thread.currentThread().getName() + " about to submit a new task to executor.");
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
                System.out.println("All tasks terminated within the allotted time.");
            } else {
                System.out.println("Not all tasks terminated within the allotted time.");
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("Thread " +  Thread.currentThread().getName() + " about to do something required after all threads are done.");
    }
}

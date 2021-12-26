package concurrency.executor.executorservice;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class MWE {

    private static String currThreadName(){
        return Thread.currentThread().getName();
    }

    private static class Task implements Runnable {

        public static long MILLIS_TO_WAIT = 2000;

        @Override
        public void run() {
            try {
                System.out.println("Thread " + currThreadName() + " is about to busy-wait. ");
                Thread.sleep(MILLIS_TO_WAIT);
                System.out.println("Thread " + currThreadName() + " busy - waited.");
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
    public static void main(String[] args) {
        ExecutorService executorService = Executors.newFixedThreadPool(3);
        System.out.println("Thread " + currThreadName() +  " about to submit tasks to ExecutorService.");
        executorService.submit(new Task());
        executorService.submit(new Task());
        executorService.submit(new Task());
        try {
            boolean allTasksExecutedWithinTimeout = executorService.awaitTermination(concurrency.executor.executorservice.Task.MILLISECS_TO_SLEEP + 100, TimeUnit.MILLISECONDS);
            if(allTasksExecutedWithinTimeout){
                System.out.println("All tasks terminated within the allotted time.");
            } else {
                System.out.println("Not all tasks terminated within the allotted time.");
            }
            // Let's see if this stuff gets printed now that we have called awaitTermination().
            System.out.println("Thread " + currThreadName() +  " about to busy-wait before shutting down ExecutorService.");
            Thread.sleep(Task.MILLIS_TO_WAIT);
            System.out.println("Thread " + currThreadName() + " busy-waited and will now shut the ExecutorService down.");
            executorService.shutdown();
            System.out.println("Thread " + currThreadName() + " just shut down the ExecutorService.");
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("Thread " +  currThreadName() + " about to do something required after all threads are done.");
    }
}

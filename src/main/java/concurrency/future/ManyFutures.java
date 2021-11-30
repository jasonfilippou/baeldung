package concurrency.future;

import org.apache.commons.lang3.RandomStringUtils;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.*;

public class ManyFutures {

    private static final long MILLIS = 5000;

    private static class StringTask implements Callable<String>{
        @Override
        public String call(){
            System.out.println("Thread " + currentThreadName() + " about to sleep for " + MILLIS + " milliseconds.");
            try {
                Thread.sleep(MILLIS);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println("Thread " + currentThreadName() + " slept " + MILLIS + " milliseconds and will now return a random String.");
            return RandomStringUtils.randomAlphanumeric(10);
        }
    }

    private static String currentThreadName(){
        return Thread.currentThread().getName();
    }

    private static <T> T processFuture(Future<T> future){
        if (future.isDone() && !future.isCancelled()) {
            try {
                return future.get();
            } catch (InterruptedException | ExecutionException e) {
                e.printStackTrace();
            }
        }
        return null;
    }
    public static void main(String[] args) {
        ExecutorService executorService = Executors.newFixedThreadPool(3);
        System.out.println("Thread " + currentThreadName() + " about to submit three StringTasks to ExecutorService instance.");
        List<Future<String>> futureList = Arrays.asList(
                                                executorService.submit(new StringTask()),
                                                executorService.submit(new StringTask()),
                                                executorService.submit(new StringTask())
                                            );
        executorService.shutdown();
        System.out.println("Thread " + currentThreadName() + " just shutdown ExecutorService instance and will sleep for " + MILLIS + " milliseconds.");
        try {
            boolean allTerminatedBeforeTimeout = executorService.awaitTermination(MILLIS + 100, TimeUnit.MILLISECONDS); // Change the value to see some futures down below return null :)
            if(allTerminatedBeforeTimeout){
                System.out.println("Thread " + currentThreadName() + ": All submitted jobs terminated before the provided timeout.");
            } else {
                System.out.println("Thread " + currentThreadName() + ": *Not* all submitted jobs terminated before the provided timeout.");
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        futureList.forEach(f-> System.out.println("Thread " + currentThreadName() + " processing value from Future instance: " + processFuture(f)));
    }
}

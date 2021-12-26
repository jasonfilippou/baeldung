package concurrency.executor.executorservice;

public class Task implements Runnable {

    public static final long MILLISECS_TO_SLEEP = 5000;

    @Override
    public void run() {
        try {
            Thread.sleep(MILLISECS_TO_SLEEP);
            System.out.println("Task executed after thread " + Thread.currentThread().getName() +
                    " waited for " + (double) MILLISECS_TO_SLEEP / 1000 + " seconds.");
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }
}

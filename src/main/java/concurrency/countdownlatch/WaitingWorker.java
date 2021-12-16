package concurrency.countdownlatch;

import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

/** A version of {@link Worker} with three more {@link CountDownLatch} dependencies:
 *
 *  <ul>
 *      <li>{@code callingThreadBlocker}, which does not allow the instance to execute {@code run()}
 *          until it is &quot; released &quot; by the main thread.</li>
 *       <li>{@code readyWorkersCounter}, which the main thread {@code await}s on until all threads are ready to work.</li>
 *      <li>{@code workersDoneCounter}, which notifies the calling thread that all workers have completed their work.
 *      This {@link CountDownLatch} is actually provided to the {@link Worker} subobject
 *      at construction.
 *      This latter behavior is similar to {@link java.util.concurrent.ExecutorService#awaitTermination(long, TimeUnit)}</li>.
 *  </ul>
 *
 *
 *
 *
 * Check <a href = "https://www.baeldung.com/java-countdown-latch">The Baeuldung tutorial</a>  for more. */
public class WaitingWorker extends Worker {

    private final CountDownLatch callingThreadBlocker;
    private final CountDownLatch readyWorkersCounter;


    public WaitingWorker(List<String> outputScraper, CountDownLatch callingThreadBlocker,
                         CountDownLatch readyWorkersCounter, CountDownLatch workersDoneCounter){
        super(outputScraper, workersDoneCounter);
        this.callingThreadBlocker = callingThreadBlocker;
        this.readyWorkersCounter = readyWorkersCounter;
    }

    @Override
    public void run(){
        readyWorkersCounter.countDown();
        // Wait on semaphore from main thread.
        try {
            callingThreadBlocker.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        super.run();    // Calls countDown()
    }


}

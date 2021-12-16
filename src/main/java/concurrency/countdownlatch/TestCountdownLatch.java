package concurrency.countdownlatch;

import org.junit.Test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.junit.Assert.assertEquals;

public class TestCountdownLatch {

    public static final int NUM_THREADS = 5;
    public static final String COUNTDOWN_MSG = "Counted down";
    public static final String RELEASE_MSG = "Latch Released";

    @Test
    public void blockingMainThread_UntilAllWorkersDone()
            throws InterruptedException {

        List<String> outputScraper = Collections.synchronizedList(new ArrayList<>());
        CountDownLatch countDownLatch = new CountDownLatch(NUM_THREADS);
        List<Thread> workers = Stream
                .generate(() -> new Thread(new Worker(outputScraper, countDownLatch)))
                .limit(NUM_THREADS)
                .collect(Collectors.toList());

        workers.forEach(Thread::start);
        countDownLatch.await(); // Waiting on all workers to be done.
        outputScraper.add(RELEASE_MSG);
        assertEquals(NUM_THREADS, Collections.frequency(outputScraper, COUNTDOWN_MSG));
        assertEquals(1, Collections.frequency(outputScraper, RELEASE_MSG));
        assertEquals(NUM_THREADS + 1, outputScraper.size());
    }

    /* In the following test, notice how the main thread waits until all workers are
        ready to begin, releases the semaphore, and then waits until they are all done.
        We are achieving reasonably complex synchronization with the CountDownLatch!
     */
    @Test
    public void blockingWaitingWorkers_untilMainThread_releasesLatch()
            throws InterruptedException {

        List<String> outputScraper = Collections.synchronizedList(new ArrayList<>());
        CountDownLatch blocker = new CountDownLatch(1);
        CountDownLatch readyWorkersCounter = new CountDownLatch(NUM_THREADS);
        CountDownLatch workersDoneCounter = new CountDownLatch(NUM_THREADS);
        List<Thread> workers = Stream
                .generate(() -> new Thread(new WaitingWorker(outputScraper, blocker,
                        readyWorkersCounter, workersDoneCounter)))
                .limit(NUM_THREADS)
                .collect(Collectors.toList());

        workers.forEach(Thread::start);
        readyWorkersCounter.await();
        outputScraper.add("Workers ready, releasing semaphore...");
        blocker.countDown();
        workersDoneCounter.await(); // Waiting for all workers to be done.
        outputScraper.add(RELEASE_MSG);

        assertEquals(NUM_THREADS, Collections.frequency(outputScraper, COUNTDOWN_MSG));
        assertEquals(1, Collections.frequency(outputScraper, RELEASE_MSG));
        assertEquals(NUM_THREADS + 2, outputScraper.size());
    }
}

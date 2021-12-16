package concurrency.cyclicbarrier;

import org.apache.commons.lang3.RandomUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

public class Task implements Runnable{

    private final CyclicBarrier barrier;
    private final Logger LOG = LoggerFactory.getLogger(Task.class);
    private final long workMillis;
    private final long waitMillis;
    public static final long MAX_WORK_MILLIS = 1000L;
    public static final long MAX_WAIT_MILLIS = 2000L; // Tune this up and down to trigger BrokenBarrierException and TimeOutException thrown instances.

    public Task(CyclicBarrier barrier){
        this.barrier = barrier;
        this.workMillis = RandomUtils.nextLong(100L, MAX_WORK_MILLIS);
        this.waitMillis = RandomUtils.nextLong(100L, MAX_WAIT_MILLIS);
    }

    @Override
    public void run() {
        try {
            busyWait();
            LOG.info("About to wait on barrier. Workers currently waiting on barrier: " + barrier.getNumberWaiting());
            barrier.await(waitMillis, TimeUnit.MILLISECONDS); // Waits for all threads to come to the barrier, but not more than the specified time.
            LOG.info("Waited on the barrier.");
            doSomeWork();
            return;
        } catch (InterruptedException | BrokenBarrierException | TimeoutException e) {
            LOG.error("Received a " + e.getClass().getSimpleName() + " while waiting on the barrier.");
            e.printStackTrace();
        }
        LOG.error("Got tired of waiting on the CyclicBarrier to be released.");
    }

    private void busyWait(){
        try {
            Thread.sleep(workMillis);
        } catch (InterruptedException e) {
            LOG.error("Received an interruption.");
            e.printStackTrace();
        }
    }

    private void doSomeWork(){
        LOG.info("About to do some work... ");
        busyWait();
        LOG.info("Done with work.");
    }

}

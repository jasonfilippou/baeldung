package concurrency.countdownlatch;

import java.util.List;
import java.util.concurrent.CountDownLatch;

public class Worker implements Runnable {

    public static final long MILLIS = 1000L;

    private final List<String> outputScraper; // Every thread will append a message to this scraper.
    private final CountDownLatch countDownLatch; // Will be counted down after every thread finishes.

    public Worker(List<String> outputScraper, CountDownLatch countDownLatch) {
        this.outputScraper = outputScraper;
        this.countDownLatch = countDownLatch;
    }

    @Override
    public void run() {
        doSomeWork();
        outputScraper.add(TestCountdownLatch.COUNTDOWN_MSG);
        countDownLatch.countDown();
    }

    protected void doSomeWork(){
        try {
            Thread.sleep(MILLIS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
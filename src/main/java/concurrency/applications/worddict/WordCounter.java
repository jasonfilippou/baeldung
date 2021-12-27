package concurrency.applications.worddict;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.Map;
import java.util.Queue;
import java.util.Scanner;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.Semaphore;

public class WordCounter implements Runnable {

    private final Semaphore mapMutex;
    private final Semaphore queueMutex;
    private String word;
    private final CyclicBarrier barrier;
    private final Queue<String> wordQueue;
    private final Logger LOG = LoggerFactory.getLogger(WordCounter.class);
    private long wordCount;
    private final String inputFilePath;
    private final Map<String, Long> dictionary;

    public WordCounter(Queue<String> wordQueue, Semaphore queueMutex, Semaphore mapMutex, CyclicBarrier barrier, String inputFilePath, Map<String, Long> dictionary){
        this.wordQueue = wordQueue;
        this.mapMutex = mapMutex;
        this.queueMutex = queueMutex;
        this.barrier = barrier;
        this.inputFilePath = inputFilePath;
        this.dictionary = dictionary;
    }

    @Override
    public void run() {
        try {
            LOG.info("Waiting on barrier...");
            barrier.await();
            LOG.info("Barrier tripped.");
            while(!wordQueue.isEmpty()) {
                LOG.info("Waiting on queue mutex to retrieve next word...");
                queueMutex.acquire();
                LOG.info("Acquired queue mutex.");
                word = wordQueue.poll();
                LOG.info("Word pulled: \"" + word + "\"");
                queueMutex.release();
                LOG.info("Queue mutex released."); // Releasing the queue after we are done with it; now our focus will be on calculating given word count.
                calcWordCount();
                LOG.info("Finished counting. We will now wait on the map mutex so that we can write results.");
                mapMutex.acquire();
                LOG.info("Acquired map mutex.");
                writeWordCount();
                LOG.info("Wrote word, count pair <" + word + ", " + wordCount + "> pair to map.");
                mapMutex.release();
                LOG.info("Map mutex released.");
            }
        } catch (BrokenBarrierException | InterruptedException e) {
            e.printStackTrace();
        }
        LOG.info("Queue empty. Exiting!");
    }

    private void calcWordCount(){
        Scanner sc = null;
        try {
            sc = new Scanner(new FileInputStream(inputFilePath));
            while(sc.hasNext()){    // Expensive loop
                if(sc.next().equals(word)){
                    wordCount++;
                }
            }
            sc.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    private void writeWordCount(){

        // Or maybe use a SynchronizedMap or some other method to first place counts
        // in a map and then drop them sorted later. Can even attempt to find how to graph
        // the results through GNUPlot or something.
        dictionary.put(word, wordCount);
    }
}

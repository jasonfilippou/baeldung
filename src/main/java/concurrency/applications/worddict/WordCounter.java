package concurrency.applications.worddict;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.Map;
import java.util.Scanner;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.Semaphore;

public class WordCounter implements Runnable {

    private final Semaphore mutex;
    private final CyclicBarrier barrier;
    private final String word;
    private final Logger LOG = LoggerFactory.getLogger(WordCounter.class);
    private long wordCount;
    private String inputFilePath;
    private String outputFilePath;
    private Map<String, Long> dictionary;

    public WordCounter(String word, Semaphore mutex, CyclicBarrier barrier, String inputFilePath, Map<String, Long> dictionary){
        this.word = word;
        this.mutex = mutex;
        this.barrier = barrier;
        this.inputFilePath = inputFilePath;
        this.dictionary = dictionary;
    }

    @Override
    public void run() {
        try {
            LOG.info("Waiting on barrier...");
            barrier.await();
            LOG.info("Barrier tripped. Now we will calculate the count for word: " + word + ".");
            calcWordCount();
            LOG.info("Finished counting. We will now wait on the mutex so that we can write results to dict.");
            mutex.acquire();
            dumpWordCount();
            mutex.release();
        } catch (BrokenBarrierException | InterruptedException e) {
            e.printStackTrace();
        }
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

    private void dumpWordCount(){

        // Or maybe use a SynchronizedMap or some other method to first place counts
        // in a map and then drop them sorted later. Can even attempt to find how to graph
        // the results through GNUPlot or something.
        dictionary.put(word, wordCount);
    }
}

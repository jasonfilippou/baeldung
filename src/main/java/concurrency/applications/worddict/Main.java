package concurrency.applications.worddict;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.file.Paths;
import java.util.*;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.Semaphore;
import java.util.stream.Collectors;
import java.util.stream.Stream;


public class Main {

    private static final int NUM_WORKERS = 10;
    private static final Logger LOG = LoggerFactory.getLogger(Main.class);

    private static String getRelativeFilePath(String fileName){
        return Paths.get(System.getProperty("user.dir"), "src", "main", "java", "concurrency", "applications", "worddict", fileName).toString();
    }

    public static void main(String[] args) {

        // Setup
        CyclicBarrier barrier = new CyclicBarrier(NUM_WORKERS, ()-> LOG.info("All barriers tripped!"));
        Semaphore mutex = new Semaphore(1, true);
        Map<String, Long> dictionary = new HashMap<>();
        ArrayDeque<String> words = new ArrayDeque<>(Arrays.asList("the", "an", "a", "of", "yes", "no", "can", "do", "you", "had"));
        List<Thread> wordCounters = Stream
                .generate(() -> new Thread(new WordCounter(words.pop(), mutex, barrier, getRelativeFilePath("input.txt"), dictionary)))
                .limit(NUM_WORKERS)
                .collect(Collectors.toList());
        assert words.isEmpty() : "We should have exhausted the queue";

        // Start workers and join them.
        wordCounters.forEach(Thread::start);
        wordCounters.forEach(wordCounter ->
                {
                    try {
                        wordCounter.join();
                    } catch (InterruptedException e){
                        LOG.error("Thread" + wordCounter.getName() + " failed to join.");
                    }
                }
        );

        // Print results
        dictionary.forEach((k, v) -> LOG.info(k + " appears " + v + " times " + " in text"));
    }
}

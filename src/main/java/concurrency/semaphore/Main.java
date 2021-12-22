package concurrency.semaphore;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.nio.file.Paths;
import java.util.List;
import java.util.concurrent.Semaphore;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Main {
    private static final String FNAME = "dump.txt";
    private static final int NUM_THREADS = 10;
    private static final Logger LOG = LoggerFactory.getLogger(Main.class);

    public static File getRelativeFile(String fileName){
        return Paths.get(System.getProperty("user.dir"), "src", "main", "java", "concurrency", "semaphore", fileName).toFile();
    }

    public static void main(String[] args) {
        getRelativeFile(FNAME).delete();
        Semaphore fairMutex = new Semaphore(1, true);
        List<Thread> workers = Stream
                .generate(() -> new Thread(new SemaphoreBlockedTask(fairMutex, FNAME)))
                .limit(NUM_THREADS)
                .collect(Collectors.toList());
        LOG.info("About to start workers.");
        workers.forEach(Thread::start);
        workers.forEach(worker ->
                {
                    try {
                        worker.join();
                    } catch (InterruptedException e){
                        LOG.error("Thread" + worker.getName() + " failed to join.");
                    }
                }
        );
        LOG.info("Waited for all threads to join, exiting...");
    }
}

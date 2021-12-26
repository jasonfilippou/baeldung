package concurrency.semaphore;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileWriter;
import java.io.IOException;
import java.util.concurrent.Semaphore;

public class SemaphoreBlockedTask implements Runnable{

    private final Semaphore mutex;
    private final String file;
    private final Logger LOG = LoggerFactory.getLogger(SemaphoreBlockedTask.class);

    public SemaphoreBlockedTask(Semaphore mutex, String file){
        this.mutex = mutex;
        this.file = file;
    }

    @Override
    public void run() {
        try {
            // Acquire lock:
            LOG.info("Waiting on mutex.");
            mutex.acquire();
            LOG.info("Mutex acquired, about to write to file.");

            // Do work:
            writeToFile();

            // Release lock:
            LOG.info("File written to, releasing mutex.");
            mutex.release();
            LOG.info("Mutex released.");
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private void writeToFile(){
        try {
            //FileWriter fileWriter = new FileWriter(Objects.requireNonNull(this.getClass().getClassLoader().getResource("")).getPath() + file, true);
            FileWriter fileWriter = new FileWriter(Main.getRelativeFile(file), true);
            fileWriter.write("Thread " + Thread.currentThread().getName() + " writes to file.\n");
            fileWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

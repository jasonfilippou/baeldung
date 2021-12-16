package concurrency.cyclicbarrier;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.concurrent.CyclicBarrier;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Main {

    public static final int NUM_TASKS = 5;
    private final Logger LOG = LoggerFactory.getLogger(Main.class);

    public static void main(String[] args) {
        Logger LOG = LoggerFactory.getLogger(Main.class);
        // Create a CyclicBarrier which will get tripped when NUM_TASKS - many workers
        // call await() on it. The barrier has a Runnable action that gets executed when it's tripped.
        CyclicBarrier barrier = new CyclicBarrier(NUM_TASKS, ()-> LOG.info("All threads reached barrier!"));
        List<Thread> workers = Stream
                .generate(() -> new Thread(new Task(barrier)))
                .limit(NUM_TASKS)
                .collect(Collectors.toList());
        LOG.info("About to start workers. We require " + barrier.getParties() + " workers to trip this barrier.");
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
        if(!barrier.isBroken()){
            LOG.info("Barrier did not break, all good!");
        } else {
            LOG.info("Barrier broke. :( ");
        }

    }
}

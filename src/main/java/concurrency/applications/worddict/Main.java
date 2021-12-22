package concurrency.applications.worddict;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.CyclicBarrier;


public class Main {

    private static final int NUM_WORKERS = 10;
    private static final Logger LOG = LoggerFactory.getLogger(Main.class);

    public static void main(String[] args) {
        CyclicBarrier barrier = new CyclicBarrier(NUM_WORKERS, ()-> LOG.info("All barriers tripped!"));
    }
}

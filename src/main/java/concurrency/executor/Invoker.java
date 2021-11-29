package concurrency.executor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.Executor;

public class Invoker implements Executor {

    private Logger logger = LoggerFactory.getLogger(Invoker.class);

    @Override
    public void execute(Runnable command) {
        System.out.println("Invoker about to run task: " + command);
//        logger.info("Invoker about to run task: " + command.toString());
        command.run();
//        logger.info("Invoker ran task: " + command.toString());
        System.out.println("Invoker ran task: " + command);
    }
}

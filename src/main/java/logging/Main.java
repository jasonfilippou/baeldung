package logging;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Main {

    private static void helper(Logger logger){
        logger.info("Log 1");
        System.out.println("A print!");
        logger.info("Log 2");
    }
    public static void main(String[] args) {
        System.setProperty("org.slf4j.simpleLogger.logFile", "System.out");
        Logger logger = LoggerFactory.getLogger(Main.class);
        for(int i = 0; i < 100; i++){
            System.out.println("=================================");
            helper(logger);
            System.out.println("=================================");
        }

    }
}

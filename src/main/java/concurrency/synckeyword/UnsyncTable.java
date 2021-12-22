package concurrency.synckeyword;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Adapted from <a href = "https://www.javatpoint.com/synchronization-in-java>this tutorial</a>.
 */

public class UnsyncTable{

    private static class Table {

        private final Logger LOG = LoggerFactory.getLogger(Table.class);

        // Following method not synchronized, so output non-deterministic.
         void printTable(int n){
            for(int i=1;i<=5;i++){
                LOG.info("" + n*i);    // Prints first 5 non-zero multiples of the argument.
                try {
                    Thread.sleep(400);
                } catch(Exception e){
                    LOG.error("" + e);
                }
            }

        }
    }

    /* Some Threads that will receive a Table instance in their constructor,
     * which means that they will be operating on a common piece of memory...
     */

    static class MyThread1 extends Thread {

        private Table t;
        public MyThread1(Table t){
            this.t=t;
        }
        public void run(){
            t.printTable(5);    // Calling a method on the provided Table instance.
        }

    }
    static class MyThread2 extends Thread{
        private Table t;

        public MyThread2(Table t){
            this.t=t;
        }
        public void run(){
            t.printTable(100); // Calling a method on the provided Table instance.
        }
    }
    public static void main(String[] args){
        Table obj = new Table(); // Will be shared between Threads.
        MyThread1 t1= new MyThread1(obj);
        MyThread2 t2= new MyThread2(obj);
        t1.start();
        t2.start();
    }
}  
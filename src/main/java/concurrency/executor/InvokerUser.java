package concurrency.executor;

import java.util.concurrent.Executor;

public class InvokerUser {

    public static void main(String[] args) {
        Executor invoker = new Invoker();
        invoker.execute(() -> System.out.println("Some task..."));
    }
}

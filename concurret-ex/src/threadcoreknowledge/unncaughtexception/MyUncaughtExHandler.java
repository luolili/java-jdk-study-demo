package threadcoreknowledge.unncaughtexception;

import java.util.logging.Level;
import java.util.logging.Logger;

public class MyUncaughtExHandler implements
        Thread.UncaughtExceptionHandler {

    private String name;

    public MyUncaughtExHandler(String name) {
        this.name = name;
    }

    @Override
    public void uncaughtException(Thread t, Throwable e) {

        Logger logger = Logger.getAnonymousLogger();// from java.util.logging
        logger.log(Level.WARNING, "thread 异常，stop:" + t.getName());
        System.out.println(name + "got the ex:" + t.getName() + "ex:");
    }
}

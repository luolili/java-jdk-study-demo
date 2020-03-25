package bf.iv;

/**
 * java 如何创建一个进程；一个应用程序对应一个jvm实例
 * java 采用的 是单线程编程，若没有主动创建线程，就只有一个主线程
 */
public class ProcessCreation {

    public static void main(String[] args) throws Throwable {
        Runtime runtime = Runtime.getRuntime();
        //runtime.exec("notepad");
        //runtime.exec("cmd /k start https://www.baidu.com/");
        //runtime.exec("cmd /k services.msc");
        //runtime.exec("cmd /k services.msc");
        runtime.exec("cmd /k osk");
    }
}

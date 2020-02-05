package base.jvm.ch07;

import java.io.IOException;
import java.net.URL;
import java.util.Enumeration;

/**
 * sys:sun.misc.Launcher$AppClassLoader@b4aac2
 * file:/F:/githubpro/java-jdk-study-demo/src/class/production/java-jdk-study-demo/%20
 * ext:sun.misc.Launcher$ExtClassLoader@16f6e28
 * path:D:\Java\jdk1.8.0_111\jre\lib\ext;C:\Windows\Sun\Java\lib\ext
 * ext parent:null
 * ext parent 是C++实现的，所以获取不到
 */
public class ClassLoaderPropTest {
    public static void main(String[] args) throws IOException {
        ClassLoader systemClassLoader = ClassLoader.getSystemClassLoader();
        System.out.println("sys:" + systemClassLoader);

        Enumeration<URL> eml = systemClassLoader.getResources(" ");
        while (eml.hasMoreElements()) {
            System.out.println(eml.nextElement());
        }
        ClassLoader extClassLoader = systemClassLoader.getParent();
        System.out.println("ext:" + extClassLoader);
        System.out.println("path:" + System.getProperty("java.ext.dirs"));
        System.out.println("ext parent:" + extClassLoader.getParent());


    }
}

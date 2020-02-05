package base.jvm.ch07;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.lang.reflect.Method;

public class MyClassLoader extends ClassLoader {
    private byte[] getByetes(String fileName) throws IOException {
        File file = new File(fileName);
        long len = file.length();
        byte[] raw = new byte[(int) len];
        try (FileInputStream fin = new FileInputStream(file)) {
            //输入流 读入到byte[]
            int read = fin.read(raw);
            if (read != len) {
                throw new IOException("无法读取全部文件");
            }
        }

        return raw;
    }

    @Override
    protected Class<?> findClass(String name) throws ClassNotFoundException {
        Class clazz = null;
        String fileSub = name.replace(".", "/");
        String classFileName = fileSub + ".class";
        File classFile = new File(classFileName);
        if (classFile.exists()) {
            try {
                byte[] raw = getByetes(classFileName);
                clazz = defineClass(name, raw, 0, raw.length);
            } catch (Exception e) {
                e.printStackTrace();
            }

        }

        if (clazz == null) {
            throw new ClassNotFoundException(name);
        }
        return clazz;
    }

    public static void main(String[] args) throws Exception {
        String path = "base.jvm.ch07.Halo";
        MyClassLoader myClassLoader = new MyClassLoader();
        Class<?> aClass = myClassLoader.loadClass("");

        Method method = aClass.getMethod("test", String.class);
        System.out.println(method);
        method.invoke(aClass.newInstance(), "hh");
    }
}

package com.luo.util;

import com.luo.lang.Nullable;

import java.io.*;

/**
 * seri and deseri
 */
public abstract class SerializationUtils {


    @Nullable
    public static byte[] serialize(@Nullable Object object) {

        if (object == null) {
            return null;
        }

        //-1 prepare a byte output stream
        ByteArrayOutputStream baos = new ByteArrayOutputStream(1024);

        try {

            ObjectOutputStream oos = new ObjectOutputStream(baos);
            oos.writeObject(object);
            oos.flush();
        } catch (IOException ex) {
            throw new IllegalArgumentException("Failed to serialize object of type: " + object.getClass(), ex);
        }
        return baos.toByteArray();
    }

    public static Object deserialize(@Nullable byte[] bytes) {
        if (bytes == null) {
            return null;
        }

        try {
            ObjectInputStream ois = new ObjectInputStream(new ByteArrayInputStream(bytes));

            return ois.readObject();
        } catch (IOException ex) {
            throw new IllegalArgumentException("Failed to deserialize object", ex);
        } catch (ClassNotFoundException ex) {
            throw new IllegalStateException("Failed to deserialize object type", ex);
        }
    }
}

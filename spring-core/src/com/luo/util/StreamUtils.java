package com.luo.util;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * abstract class too like others
 * it is about inputStream and etc
 */
public abstract class StreamUtils {
    //attrs
    private static final int BUFFER_SIZE = 4096;//1024*4


    private static final byte[] EMPTY_CONTENT = new byte[0];


    public static int copy(InputStream in, OutputStream out) throws IOException {

        int byteCount = 0;
        //prepare  the space to write for out
        byte[] buffer = new byte[BUFFER_SIZE];
        int byteRead = -1;
        //inputStream and outputStream simple use:read and write
        while ((byteRead = in.read()) != -1) {
            out.write(buffer, 0, byteRead);
            byteCount += byteRead;
        }
        out.flush();

        return byteCount;
    }
}

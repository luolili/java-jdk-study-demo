package com.luo.util;

import java.io.*;
import java.nio.charset.Charset;

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

    public static byte[] copyToByteArray(InputStream in) throws IOException {
        if (in == null) {
            return new byte[0];
        }

        ByteArrayOutputStream out = new ByteArrayOutputStream(BUFFER_SIZE);
        copy(in, out);//copy in into byte array out
        return out.toByteArray();

    }


    public static String copyToString(InputStream in, Charset charset) throws IOException {
        if (in == null) {
            return "";
        }

        StringBuilder out = new StringBuilder();
        int byteRead = -1;
        char[] buffer = new char[BUFFER_SIZE];
        InputStreamReader reader = new InputStreamReader(in, charset);
        while ((byteRead = reader.read()) != -1) {
            out.append(buffer, 0, byteRead);
        }
        return out.toString();

    }
}

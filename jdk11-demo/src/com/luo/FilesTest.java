package com.luo;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

public class FilesTest {

    public static void main(String[] args) throws IOException {
        String path = "jdk11-demo/test.txt";
        Files.writeString(Path.of(path), "jdk11 new feature001", StandardCharsets.UTF_8);
        System.out.println(Files.readString(Path.of(path), StandardCharsets.UTF_8));
        //list
        List<String> list = List.of("java", "j2");
        System.out.println(list);
        String[] oldWay = list.toArray(new String[0]);

        String[] newWay = list.toArray(String[]::new);

    }

}

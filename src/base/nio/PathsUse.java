package base.nio;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

/**
 * nio Paths
 */
public class PathsUse {
    public static void main(String[] args) throws Exception {
        Path path =
                Paths.get("E:\\电子书\\SharingMaterial\\BJLive.md");


        Path path2 = Paths.get("E:\\电子书", "SharingMaterial", "BJLive.md");
        Path fileName = path.getFileName();
        Path parent = path.getParent();
        //文件层级数
        int nameCount = path.getNameCount();
        Path name = path.getName(0);
        Path resolveSibling = path.resolveSibling("BJLive.md");


        System.out.println(fileName);
        System.out.println(parent);
        System.out.println(nameCount);//3
        System.out.println(name);//电子书
        //E:\电子书\SharingMaterial\BJLive.md
        System.out.println("resolveSibling:" + resolveSibling);
        System.out.println("-----");
        //---Files
        boolean exists = Files.exists(path);
        boolean isDirectory = Files.isDirectory(path);
        try {
            Path path3 = Paths.get("E:\\电子书", "SharingMaterial");
            //Files.createFile(path3.resolve("aa.txt"));
            //Files.createFile(path3.resolve("bb.txt"));
            //Files.createDirectories(path3.resolve("xxx"));
            //Files.createDirectory(path3.resolve("xxx"));
            //创建多个目录
            //Files.createDirectories(path3.resolve("xxx/cc"));
            Files.copy(path3.resolve("aa.txt"), path3.resolve("bb.txt"));
        } catch (IOException e) {
            e.printStackTrace();
        }

        System.out.println("file exists:" + exists);
        System.out.println("file isDirectory:" + isDirectory);

        System.out.println("------read");
        List<String> lines = Files.readAllLines(path);
        byte[] bytes = Files.readAllBytes(path);
        System.out.println(lines);
        System.out.println(bytes);

        Path path4 =
                Paths.get("E:\\电子书\\SharingMaterial\\aa.txt");
        Files.newBufferedWriter(path4).write("for good");
        List<String> strings = Files.readAllLines(path4);
        System.out.println(strings);

    }
}

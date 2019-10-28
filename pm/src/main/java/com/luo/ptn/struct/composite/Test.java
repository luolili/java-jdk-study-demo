package com.luo.ptn.struct.composite;

public class Test {
    public static void main(String[] args) {
        CatalogComponent linuxCourse = new Course("linux", 11);
        CatalogComponent winCourse = new Course("linux", 2);

        CatalogComponent javaCourseCata = new CourseCatalog("java cata");

        CatalogComponent mmall1 = new Course("mmall1", 2);
        CatalogComponent mmall2 = new Course("mmall2", 3);
        CatalogComponent pm = new Course("pm", 3);
        javaCourseCata.add(mmall1);
        javaCourseCata.add(mmall2);
        javaCourseCata.add(pm);

        CatalogComponent mainCourseCata = new CourseCatalog("main cata");
        mainCourseCata.add(linuxCourse);
        mainCourseCata.add(winCourse);
        mainCourseCata.add(javaCourseCata);

        mainCourseCata.print();
    }
}

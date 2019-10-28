package com.luo.ptn.struct.composite;

import java.util.ArrayList;
import java.util.List;

public class CourseCatalog extends CatalogComponent {

    private List<CatalogComponent> items = new ArrayList<>();
    private String name;

    public CourseCatalog(String name) {
        this.name = name;
    }

    @Override
    public void add(CatalogComponent catalogComponent) {
        items.add(catalogComponent);
    }

    @Override
    public void remove(CatalogComponent catalogComponent) {
        items.remove(catalogComponent);
    }

    @Override
    public void print() {

        for (CatalogComponent item : items) {
            System.out.println("");
            item.print();
        }
    }

    @Override
    public String getName(CatalogComponent catalogComponent) {
        return this.name;
    }
}

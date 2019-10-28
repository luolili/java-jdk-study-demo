package com.luo.ptn.struct.composite;

public abstract class CatalogComponent {

    public void add(CatalogComponent catalogComponent) {
        throw new UnsupportedOperationException("not add");
    }

    public void remove(CatalogComponent catalogComponent) {
        throw new UnsupportedOperationException("not remove");
    }

    public String getName(CatalogComponent catalogComponent) {
        throw new UnsupportedOperationException("not name");
    }

    public double getPrice(CatalogComponent catalogComponent) {
        throw new UnsupportedOperationException("not price");
    }

    public void print() {
        throw new UnsupportedOperationException("not print");
    }

}

package com.luo.function;

import com.luo.lang.Nullable;

import java.util.function.Supplier;

public class SingletonSupplier<T> implements Supplier<T> {

    //--
    @Nullable
    private Supplier<? extends T> instanceSupplier;

    @Nullable
    private Supplier<? extends T> defaultSupplier;

    private volatile T singletonInstance;

    //construtor

    public SingletonSupplier(Supplier<? extends T> defaultSupplier, T singletonInstance) {
        this.instanceSupplier = null;//default value
        this.defaultSupplier = defaultSupplier;
        this.singletonInstance = singletonInstance;
    }

    public SingletonSupplier(Supplier<? extends T> instanceSupplier, Supplier<? extends T> defaultSupplier) {
        this.instanceSupplier = instanceSupplier;
        this.defaultSupplier = defaultSupplier;
    }


    public SingletonSupplier(T singletonInstance) {
        this.instanceSupplier = null;//default value
        this.defaultSupplier = null;
        this.singletonInstance = singletonInstance;
    }

    @Override
    @Nullable
    public T get() {
        //-1 get instance from the class
        T instance = this.singletonInstance;
        //-2 instance is null
        if (instance == null) {
            //-3 sync get,get instance from the class first
            synchronized (this) {
                instance = this.singletonInstance;
                //-4 if it is null, get from instanceSupplier first
                if (instance == null) {
                    if (this.instanceSupplier != null) {
                        instance = this.instanceSupplier.get();
                    }
                    //-5 get from defaultSupplier at last
                    if (instance == null && this.defaultSupplier != null) {
                        instance = this.defaultSupplier.get();
                    }
                }
            }
        }
        return instance;
    }

    
}

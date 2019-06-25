package com.luo.comparator;

import com.luo.lang.Nullable;
import com.luo.util.Assert;

import java.util.Comparator;

/**
 * Compares objs based on an arbitrary class order,
 * sorted based on the types of class they inherit
 * for example: this comparator can be used to
 * sort a list {@code Number}s such that {@code Long}s occur before {@code Integer}s.
 *
 * @param <T>
 */
public class InstanceComparator<T> implements Comparator<T> {

    private final Class<?>[] instanceOrder;

    public InstanceComparator(Class<?>... instanceOrder) {
        Assert.notNull(instanceOrder, "'instanceOrder' array must not be null");
        this.instanceOrder = instanceOrder;
    }


    @Override
    public int compare(T o1, T o2) {
        int i1 = getOrder(o1);
        int i2 = getOrder(o2);

        return (i1 < i2 ? -1 : (i1 == i2) ? 0 : 1);
    }

    public int getOrder(@Nullable Object object) {
        if (object != null) {
            for (int i = 0; i < this.instanceOrder.length; i++) {
                if (this.instanceOrder[i].isInstance(object)) {
                    return i;
                }
            }
        }
        //else the length,the last one
        return this.instanceOrder.length;
    }
}

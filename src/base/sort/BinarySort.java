package base.sort;

import java.util.Comparator;

public class BinarySort {
    public static void main(String[] args) {

        Comparator c = new MyComparator();
        Integer[] a1 = {2, 1, 3};//case 1
        Integer[] a0 = {1, 2, 3};//case 0
        Integer[] a2 = {3, 2, 1, 0};//case 2
        //binarySort01(a1,0,2,0,c);
        //binarySort01(a0,0,2,0,c);
        binarySort01(a2, 0, 3, 0, c);


    }

    static void binarySort01(Object[] a, int lo, int hi,
                             int start, Comparator c) {

        assert lo <= start && start <= hi;
        if (start == lo) {
            start++;
        }
        for (; start < hi; start++) {
            Object pivot = a[start];

            int left = lo;
            int right = start;
            assert left <= right;

            while (left < right) {
                int mid = (left + right) >>> 1;
                if (c.compare(pivot, a[mid]) < 0) {
                    right = mid;
                } else {
                    left = mid + 1;
                }

            }
            assert left == right;

            int n = start - left;
            switch (n) {
                case 2:
                    a[left + 2] = a[left + 1];
                case 1:
                    a[left + 1] = a[left];
                    break;
                default:
                    System.arraycopy(a, left, a, left + 1, n);
            }

            a[left] = pivot;

        }


    }
}

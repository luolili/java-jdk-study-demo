package base.alg;

public class BinarySearch {

    public static int bsRecurse(int[] arr, int key, int low, int high) {
        if (key < arr[low] || key > arr[high] || low > high) {
            return -1;
        }
        int mid = (low + high) / 2;
        if (arr[mid] > key) {
            return bsRecurse(arr, key, low, mid - 1);
        } else if (arr[mid] < key) {
            return bsRecurse(arr, key, mid + 1, high);
        } else {
            return mid;
        }
    }

    public static int bsByCommon(int[] arr, int key, int low, int high) {
        if (key < arr[low] || key > arr[high] || low > high) {
            return -1;
        }

        while (low <= high) {
            int mid = (low + high) / 2;
            if (arr[mid] > key) {
                high = mid - 1;

            } else if (arr[mid] < key) {
                low = mid + 1;
            } else {
                return mid;
            }
        }
        return -1;
    }
}

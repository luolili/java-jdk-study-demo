package base.string;

/**
 * a string includes another string?
 */
public class StringUse01 {
    public static void main(String[] args) {
        String src = "hello hu";
        String tar = "hu";
        indexOf01(src.toCharArray(), 0, src.toCharArray().length,
                tar.toCharArray(), 0, tar.toCharArray().length, 0);

    }

    static int indexOf01(char[] source, int sourceOffset, int sourceCount,
                         char[] target, int targetOffset, int targetCount,
                         int fromIndex) {

        if (fromIndex >= sourceCount) {
            return (targetCount == 0 ? sourceCount : -1);
        }
        if (fromIndex < 0) {
            fromIndex = 0;
        }

        if (targetCount == 0) {
            return fromIndex;
        }

        char firstChar = target[targetOffset];
        //the times of recurse
        int max = sourceOffset + (sourceCount - targetCount);
        // deal with non-matching conditions
        for (int i = sourceOffset + fromIndex; i <= max; i++) {
            if (source[i] != firstChar) {
                while (++i <= max && source[i] != firstChar) ;
            }

            if (i <= max) {
                int j = i + 1;
                //the num of target elements except its first element
                int end = j + targetCount - 1;
                //k for target, j for source
                for (int k = targetOffset + 1; j < end && source[j] == target[k]; j++, k++) ;


                //match the whole string
                if (j == end) {
                    //i is the index of the first char in source
                    //sourceOffset is the start index in source array when we match
                    return i - sourceOffset;
                }
            }
        }


        return -1;
    }

}

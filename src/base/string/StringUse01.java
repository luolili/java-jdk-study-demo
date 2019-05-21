package base.string;

/**
 * a string includes another string?
 */
public class StringUse01 {
    public static void main(String[] args) {
        String src = "hello hu";
        String tar = "hi";
        indexOf01(src.toCharArray(), 0, src.toCharArray().length,
                tar.toCharArray(), 0, tar.toCharArray().length, 0);

        lastIndexOf01(src.toCharArray(), 0, src.toCharArray().length,
                tar.toCharArray(), 0, tar.toCharArray().length, 6);

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


    static int lastIndexOf01(char[] source, int sourceOffset, int sourceCount,
                             char[] target, int targetOffset, int targetCount,
                             int fromIndex) {

        int rightIndex = sourceCount - targetCount;
        if (fromIndex < 0) {
            return -1;
        }
        // empty string match all
        if (targetCount == 0) {
            return fromIndex;
        }

        //decrease the fromIndex
        if (fromIndex > rightIndex) {
            fromIndex = rightIndex;
        }

        //the first index from right side of target
        int strLastIndex = targetOffset + targetCount - 1;
        int lastChar = target[strLastIndex];
        int min = sourceOffset + targetCount - 1;
        int i = min + fromIndex;//traverse from fromIndex to the left side of source
        startSearchFromLastChar:

        while (true) {

            //deal with non-matching conditions
            while (i >= min && source[i] != lastChar) {
                i--;
            }

            //no char can match
            if (i < min) {
                return -1;
            }

            int j = i - 1;//the index of previous element of source[i]
            int start = j - (targetCount - 1);//num of traverse
            int k = strLastIndex - 1;
            while (j > start) {
                if (source[j--] != target[k--]) {
                    i--;
                    continue startSearchFromLastChar;
                }
            }
            return start - sourceOffset + 1;
        }


    }

}

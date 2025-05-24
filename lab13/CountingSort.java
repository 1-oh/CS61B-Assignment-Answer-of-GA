import java.util.HashMap;
import java.util.Map;

/**
 * Class with 2 ways of doing Counting sort, one naive way and one "better" way
 *
 * @author Akhil Batra, Alexander Hwang
 *
 **/
public class CountingSort {
    /**
     * Counting sort on the given int array. Returns a sorted version of the array.
     * Does not touch original array (non-destructive method).
     * DISCLAIMER: this method does not always work, find a case where it fails
     *
     * @param arr int array that will be sorted
     * @return the sorted array
     */
    public static int[] naiveCountingSort(int[] arr) {
        // find max
        int max = Integer.MIN_VALUE;
        for (int i : arr) {
            max = max > i ? max : i;
        }

        // gather all the counts for each value
        int[] counts = new int[max + 1];
        for (int i : arr) {
            counts[i]++;
        }

        // when we're dealing with ints, we can just put each value
        // count number of times into the new array
        int[] sorted = new int[arr.length];
        int k = 0;
        for (int i = 0; i < counts.length; i += 1) {
            for (int j = 0; j < counts[i]; j += 1, k += 1) {
                sorted[k] = i;
            }
        }

        // however, below is a more proper, generalized implementation of
        // counting sort that uses start position calculation
        int[] starts = new int[max + 1];
        int pos = 0;
        for (int i = 0; i < starts.length; i += 1) {
            starts[i] = pos;
            pos += counts[i];
        }

        int[] sorted2 = new int[arr.length];
        for (int i = 0; i < arr.length; i += 1) {
            int item = arr[i];
            int place = starts[item];
            sorted2[place] = item;
            starts[item] += 1;
        }

        // return the sorted array
        return sorted;
    }

    /**
     * Counting sort on the given int array, must work even with negative numbers.
     * Note, this code does not need to work for ranges of numbers greater
     * than 2 billion.
     * Does not touch original array (non-destructive method).
     *
     * @param arr int array that will be sorted
     */
    public static int[] betterCountingSort(int[] arr) {
        // TODO make counting sort work with arrays containing negative numbers.
        //To find the max element and the min element
        int min = Integer.MAX_VALUE;
        int max = Integer.MIN_VALUE;
        for(int i : arr){
            if(i < min){
                min = i;
            }
            if(i > max){
                max = i;
            }
        }

       /*To get the Map whose key represent the numbers while
       * the value represent the counts*/
        Map<Integer, Integer> numOfElements = new HashMap<>();
        for(Integer i : arr){
            if(numOfElements.containsKey(i)){
                int oldNum = numOfElements.get(i);
                numOfElements.put(i, oldNum + 1);
            }
            else numOfElements.put(i, 1);
        }

        /*The array used to store where an element 's starting
        * position is, NOTICE THAT:the value at index "i - min" represent
        * the number "i" 's starting position*/
        int[] start = new int[max - min + 1];

        //To start from the second least number
        for(int i = min + 1; i <= max; i += 1){
            if(numOfElements.containsKey(i - 1)){
                start[i - min] = numOfElements.get(i - 1);
            }
            start[i - min] += start[i - min - 1];
        }

        int[] ret = new int[arr.length];
        for(int i : arr){
            int pos = start[i - min];
            ret[pos] = i;
            start[i - min] += 1;
        }
        return ret;
    }
}

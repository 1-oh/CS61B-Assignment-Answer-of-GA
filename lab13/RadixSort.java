import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Class for doing Radix sort
 *
 * @author Akhil Batra, Alexander Hwang
 *
 */
public class RadixSort {
    /**
     * Does LSD radix sort on the passed in array with the following restrictions:
     * The array can only have ASCII Strings (sequence of 1 byte characters)
     * The sorting is stable and non-destructive
     * The Strings can be variable length (all Strings are not constrained to 1 length)
     *
     * @param asciis String[] that needs to be sorted
     *
     * @return String[] the sorted array
     */
    public static String[] sort(String[] asciis) {
        // TODO: Implement LSD Sort
        String[] ret = asciis.clone();

        //To find to maximum length of the string
        int lengthMax = 0;
        for(String s : asciis){
            if(s.length() > lengthMax){
                lengthMax = s.length();
            }
        }
        //To make the length of strings equal to lengthMax
        for(int k = 0; k < ret.length; k += 1){
            int addTimes = lengthMax - ret[k].length();
            for(int i = 0; i < addTimes; i += 1){
                ret[k] = ret[k] + "_";
            }
        }
        for(int i = lengthMax - 1; i >= 0; i -= 1){
            ret = sortHelperLSD(ret, i);
        }

        //To delete "_"
        for(int k = 0; k < ret.length; k += 1){
            while (ret[k].charAt(ret[k].length() - 1) == '_'){
                ret[k] = ret[k].substring(0, ret[k].length() - 1);
            }
        }

        return ret;
    }

    /**
     * LSD helper method that performs a non-destructive counting sort the array of
     * Strings based off characters at a specific index.
     * @param asciis Input array of Strings
     * @param index The position to sort the Strings on.
     */
    private static String[] sortHelperLSD(String[] asciis, int index) {
        // Optional LSD helper method for required LSD radix sort
        int asciiSize = 256;
        String[] ret = new String[asciis.length];

        int[] count = new int[asciiSize];
        for(String s: asciis){
            count[(int)s.charAt(index)] += 1;
        }

        int[] start = new int[asciiSize];
        for(int i = 1; i < asciiSize; i += 1){
            start[i] = start[i - 1] + count[i - 1];
        }

        for(String s : asciis){
            int pos = start[(int)s.charAt(index)];
            ret[pos] = s;
            start[(int)s.charAt(index)] += 1;
        }

        return ret;
    }

//   @Test
//   public void testsortHelper(){
//        String[] sarray = new String[5];
//        sarray[0] = "good";
//        sarray[1] = "hello";
//        sarray[2] = "zero";
//        sarray[3] = "wikipedia";
//        sarray[4] = "numpy";
//        String[] result = sortHelperLSD(sarray, 1);
//        assertEquals(result[4], "numpy");
//   }
    @Test
    public void testLSD(){
        String[] sarray = new String[10];
        sarray[0] = "good";
        sarray[1] = "hello";
        sarray[2] = "zero";
        sarray[3] = "wikipedia";
        sarray[4] = "numpy";
        sarray[5] = "wiki racer";
        sarray[6] = "fudan.edu.cn";
        sarray[7] = "^(O w O)^";
        sarray[8] = "stO";
        sarray[9] = "hi there";
        String[] result = sortMSD(sarray);
    }
    private static String[] sortMSD(String[] asciis) {
        // TODO: Implement MSD Sort
        String[] ret = asciis.clone();
        sortHelperMSD(ret, 0, asciis.length, 0);
        return ret;
    }
    /**
     * MSD radix sort helper function that recursively calls itself to achieve the sorted array.
     * Destructive method that changes the passed in array, asciis.
     *
     * @param asciis String[] to be sorted
     * @param start int for where to start sorting in this method (includes String at start)
     * @param end int for where to end sorting in this method (does not include String at end)
     * @param index the index of the character the method is currently sorting on
     *
     **/
    private static void sortHelperMSD(String[] asciis, int start, int end, int index) {
        // Optional MSD helper method for optional MSD radix sort
        if(start == end || start == end - 1) {
            return;
        }
        String[] arrayToSort = new String[end - start];
        if (end - start >= 0) {
            System.arraycopy(asciis, start, arrayToSort, 0, end - start);
        }

        String[] sortedArray = sortHelperLSD(arrayToSort, index);
        if (end - start >= 0) {
            System.arraycopy(sortedArray, 0, asciis, start, end - start);
        }

        int index1 = start;
        int index2;
        for(index2 = index1 + 1; index2 < end + 1 && index1 < end; index2 ++){
            if(index2 == end || asciis[index1].charAt(index) != asciis[index2].charAt(index)){
                sortHelperMSD(asciis, index1, index2, index + 1);
                index1 = index2;
            }
        }
        return;
    }
}

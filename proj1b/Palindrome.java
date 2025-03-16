public class Palindrome {
    public static Deque<Character> wordToDeque(String word){
        int LengthOfWords=word.length();
        Deque<Character> lld=new LinkedListDeque<Character>();
        for(int i=0;i<LengthOfWords;i+=1){
            lld.addLast(word.charAt(i));
        }
        return lld;
    }

    public static boolean isPalindrome(String word){
        OffByN StandardN=new OffByN(1);
        int LengthOfWords=word.length();
        Deque<Character> WordDeque=wordToDeque(word);
        boolean ret=true;
        for(int i=0;i<LengthOfWords/2+1&&i<LengthOfWords;i+=1){
            if(!StandardN.equalChars(WordDeque.get(i), WordDeque.get(LengthOfWords - 1 - i))){
                ret=false;
            };
        }
        return ret;
    }

    public static boolean Helper(int i,int j,String word){
        if(i>=j) return true;
        if(word.charAt(i)==word.charAt(j)) return Helper(i+1,j-1,word);
        else return false;
    }

    public static boolean isPalindrome2(String word){
        return Helper(0,word.length()-1,word);
    }
}

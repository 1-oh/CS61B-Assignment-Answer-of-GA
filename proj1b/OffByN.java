public class OffByN implements CharacterComparator{
    int n;
    @Override
    public boolean equalChars(char x,char y){
        if(x-y==n||y-x==n){
            return true;
        }
        else return false;
    }

    OffByN(int N){
        n=N;
    }
}

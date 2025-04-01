package byog.lab5;


    public class Position{
        public int px;
        public int py;
        public int timesofvisit;

        public Position(int X, int Y){
            this.px=X;
            this.py=Y;
            timesofvisit=0;
        }
        Position(){
            px=0;
            py=0;
            timesofvisit=0;
        }
    }


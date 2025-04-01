import java.sql.Array;

public class NBody {
    public static int readN(String str){
        In inclass=new In(str);
        int n= inclass.readInt();
        return n;
    }
    public static double readRadius(String str){
        In inclass=new In(str);
        inclass.readInt();
        double r=inclass.readDouble();
        return r;
    }
    public static Planet[] readPlanets(String str,int N){
        int cnt=N;
        In in2=new In(str);
        in2.readInt();
        in2.readDouble();
         Planet[] ret=new Planet[cnt];
         for(int i=0;i<cnt;i++){
             double x,y,vx,vy,m;
             String path;
             x=in2.readDouble();
             y=in2.readDouble();
             vx=in2.readDouble();
             vy=in2.readDouble();
             m=in2.readDouble();
             path=in2.readString();
             ret[i]=new Planet(x,y,vx,vy,m,path);
         }
         return ret;
    }

    public static void main(String[] args){
        double T=Double.parseDouble(args[0]);
        double dt=Double.parseDouble(args[1]);
        String filename=args[2];
        int N=readN(filename);
        double spaceR=readRadius(filename);
        Planet[] Allplanets=new Planet[N];
        Allplanets=readPlanets(filename,N);

        StdDraw.setScale(-spaceR,spaceR);
        StdDraw.clear();
        StdDraw.picture(0, 0,"images/starfield.jpg");
        for(Planet p:Allplanets){
            p.draw();
        }

        StdDraw.enableDoubleBuffering();
        int TIME=0;
        for(;TIME<T;TIME+=dt){
            double[] xForces=new double[N];
            double[] yForces=new double[N];
            for(int i=0;i<N;i++){
                xForces[i]=Allplanets[i].calcNetForceExertedByX(Allplanets);
                yForces[i]=Allplanets[i].calcNetForceExertedByY(Allplanets);
            }
            for(int i=0;i<N;i++) Allplanets[i].update(TIME,xForces[i],yForces[i]);
            StdDraw.clear();
            StdDraw.picture(0, 0,"images/starfield.jpg");
            for(Planet p:Allplanets){
                p.draw();
            }
            StdDraw.show();
            StdDraw.pause(10);
        }
        StdOut.printf("%d\n", N);
        StdOut.printf("%.2e\n", spaceR);
        for (int i = 0; i < N; i++) {
            StdOut.printf("%11.4e %11.4e %11.4e %11.4e %11.4e %12s\n",
                    Allplanets[i].xxPos, Allplanets[i].yyPos, Allplanets[i].xxVel,
                    Allplanets[i].yyVel, Allplanets[i].mass, Allplanets[i].imgFileName);
        }
    }
}

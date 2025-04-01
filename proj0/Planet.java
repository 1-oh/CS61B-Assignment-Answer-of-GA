public class Planet {
    public double xxPos;
    public double yyPos;
    public double xxVel;
    public double yyVel;
    public double mass;
    public String imgFileName;

    public Planet(double xP, double yP, double xV, double yV, double m, String img){
        xxPos=xP;
        yyPos=yP;
        xxVel=xV;
        yyVel=yV;
        mass=m;
        imgFileName=img;
    }

    public Planet(Planet p){
        xxPos=p.xxPos;
        yyPos=p.yyPos;
        xxVel=p.xxVel;
        yyVel=p.yyVel;
        mass=p.mass;
        imgFileName=p.imgFileName;
    }

    public double calcDistance(Planet Another){
        double result=Math.sqrt((xxPos-Another.xxPos)*(xxPos-Another.xxPos)
                +(yyPos-Another.yyPos)*(yyPos-Another.yyPos)
                );
        return result;
    }

    public double calcForceExertedBy(Planet Another){
        double force=(6.67*1e-11)*mass*Another.mass/(calcDistance(Another)*calcDistance(Another));
        return force;
    }

    public double calcForceExertedByX(Planet Another){
        double forcex=calcForceExertedBy(Another)*(Another.xxPos-xxPos)/calcDistance(Another);
        return forcex;
    }

    public double calcForceExertedByY(Planet Another){
        double forcey=calcForceExertedBy(Another)*(Another.yyPos-yyPos)/calcDistance(Another);
        return forcey;
    }

    public double calcNetForceExertedByX(Planet[] others){
        double totalforcex=0;
        for(int i=0;i< others.length;i++){
            if(others[i]!=this) totalforcex+=calcForceExertedByX(others[i]);
        }
        return totalforcex;
    }

    public double calcNetForceExertedByY(Planet[] others){
        double totalforcey=0;
        for(int i=0;i< others.length;i++){
            if(others[i]!=this)totalforcey+=calcForceExertedByY(others[i]);
        }
        return totalforcey;
    }

    public void update(double time,double xforce,double yforce){
        double ax=xforce/mass;double ay=yforce/mass;
        xxVel+=ax*time;yyVel+=ay*time;
        xxPos+=xxVel*time;yyPos+=yyVel*time;
    }

    public void draw(){
        StdDraw.picture(xxPos,yyPos,"images/"+imgFileName);
    }
}

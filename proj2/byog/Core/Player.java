package byog.Core;

import byog.lab5.Position;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Player {
    public Position pos;
    public int x;
    public int y;
    public Integer PoliticLife;
    public List<String> experiences;
    public String Job;
    public Integer status;/*The Status Ranges from 1 to 6*/
    Random random;
    public static String[] Jobs={"Mayor of Xiamen","Secretary of Dalian","Secretary of Communist Youth League","Headmaster of Fudan","Manager of CASC","Mayor of Mianyang",
                                 "Mayor of Communist Youth League","Mayor of Chengdu","Deputy Secretary of Shanghai","Mayor of Hangzhou","Vice minister of Technology","Governor of Hainan",
                                 "Governor of Liaoning","Minister of Agriculture","Mayor of Shenzhen","Mayor of Chongqing","Mayor of Tianjin","Minister of Industry",
                                 "Secretary of Zhejiang","Minister of Finance","Mayor of Beijing","Mayor of Shanghai","Secretary of Henan","Secretary of Jiangsu",
                                 "Vice premier","Secretary of Shanghai","Minister of Organization of CPC","Secretary of Xinjiang","Vice Speaker","Minister of Public Security",
                                 "Chairman","Premier","Speaker","Vice Chairman","Chairman of the CPPCC","Central Commission for Discipline Inspection"};

    public Player(Position p){
        random=new Random(System.currentTimeMillis());
        pos=p;
        x=p.px;
        y=p.py;

        PoliticLife=20;
        Job=Jobs[random.nextInt(6)];
        experiences=new ArrayList<>();
        experiences.add(Job);
        status=1;
    }

    public Player(int X,int Y){
        random=new Random(System.currentTimeMillis());

        x=X;
        y=Y;
        pos= new Position(x,y);
        PoliticLife=15;
        Job=Jobs[random.nextInt(6)];
        experiences=new ArrayList<>();
        experiences.add(Job);
        status=1;
    }

    public void upgrade(){
        if (status<6)status+=1;
        PoliticLife+=15;
        Job=Jobs[6*(status-1)+random.nextInt(6)];
        experiences.add(Job);
    }
}

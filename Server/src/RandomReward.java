import com.google.gson.Gson;
import untils.Map;

import java.awt.*;
import java.io.PrintStream;
import java.util.Random;

/**
 * 此线程随机生成奖励在地图上
 */
public class RandomReward extends Thread{
    private PrintStream sendstream;
    private Random random;
    private Point[] points;
    Gson gson;
    int x;
    int y;

    RandomReward(PrintStream sendstream)
    {
        this.sendstream=sendstream;
        this.random=new Random();
        gson=new Gson();
    }
    public void run()
    {
        while(!this.isInterrupted())
        {
            boolean right=false;
            try {
                Thread.sleep(20000);//每20秒发送随机坐标
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            while(!right)
            {
                 x = random.nextInt(30);
                 y = random.nextInt(20);
                if(Map.oringemap[y][x]==0) right=true;
            }
            String pointStr=gson.toJson(new Point(x*40,y*40));
            int z=random.nextInt(8);
            sendstream.println(Sign.RandomReward+z+Sign.SplitSign+pointStr);
        }
    }
    public void stopThisThread(){
        this.interrupt();
    }
}

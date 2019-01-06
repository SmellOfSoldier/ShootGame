import com.google.gson.Gson;

import java.io.PrintStream;

/**
 * 用于计时多人游戏开始后多久停止
 */
public class MultiPlayTimeStop extends Thread {
    private ServerGameRoom currentgame;
    private boolean destroy=false;
    private RandomReward randomReward;
    private int time;
    private Gson gson;
    String allclientsStr;
    String roomStr;
    MultiPlayTimeStop(ServerGameRoom currentgame,RandomReward randomReward,int time)
    {
        this.currentgame=currentgame;
        this.time=time;
        this.randomReward=randomReward;
        gson=new Gson();
        allclientsStr = gson.toJson(StartServer.onlineClients);
        roomStr = gson.toJson(StartServer.allGameRoom);
    }
    @Override
    public void run() {
        try {
            Thread.sleep(time*1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        if(!destroy)
        {
            for (Client c : currentgame.getAllClients()) {
                if (c.isPlaying()) {
                    PrintStream sendstream = StartServer.clientPrintStreamMap.get(c);
                    sendstream.println(Sign.GameOver + allclientsStr + Sign.SplitSign + roomStr);
                    //设置玩家不在玩耍状态
                    c.setPlaying(false);
                }
            }
            System.out.println("已经通知完" + currentgame.getId() + "所有玩家游戏结束。");
        }
    }
    public void stopThisThread()
    {
        try {
            randomReward.stopThisThread();
            destroy = true;
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
        }
    }
}

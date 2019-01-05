import java.io.PrintStream;

/**
 * 用于计时多人游戏开始后多久停止
 */
public class MultiPlayTimeStop extends Thread {
    private ServerGameRoom currentgame;
    private int time;
    MultiPlayTimeStop(ServerGameRoom currentgame,int time)
    {
        this.currentgame=currentgame;
        this.time=time;
    }
    @Override
    public void run() {
        try {
            Thread.sleep(time*1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        for(Client c:currentgame.getAllClients())
        {
            PrintStream sendstream=StartServer.clientPrintStreamMap.get(c);
            sendstream.println(Sign.GameOver);
            //设置玩家不在玩耍状态
            //c.setPlaying(false);
        }
        System.out.println("已经通知完"+currentgame.getId()+"所有玩家游戏结束。");
    }
}

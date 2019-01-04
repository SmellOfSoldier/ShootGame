import java.io.PrintStream;

/**
 * 玩家复活延时线程
 */
public class PlayerReliveThread extends Thread
{
    private ServerGameRoom serverGameRoom;
    private int playerIndex;
    public PlayerReliveThread(ServerGameRoom serverGameRoom, int playerIndex)
    {
        this.serverGameRoom=serverGameRoom;
        this.playerIndex = playerIndex;
    }
    public void run()
    {
        try {
            //睡眠三秒后发送玩家复活消息
            sleep(3000);
             //生成初始化出生地址的数组
            Integer[] randomEntrance= AllInfo.randomArray(0,4,1);
            int i=randomEntrance[0];
            /**
             * 给所有房间内的玩家发送玩家编号为clientnum重新复活在i位置
             */
            for(Client c:serverGameRoom.getAllClients())
            {
                System.out.println("向玩家"+ playerIndex +"发送复活消息");
                PrintStream sendstream= StartServer.clientPrintStreamMap.get(c);
                sendstream.println(Sign.OnePlayerRelive+ playerIndex +Sign.SplitSign+i);
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}

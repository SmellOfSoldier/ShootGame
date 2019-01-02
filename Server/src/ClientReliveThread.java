import java.io.BufferedReader;
import java.io.PrintStream;
import java.net.Socket;
import java.util.List;

/**
 * 玩家复活延时线程
 */
public class ClientReliveThread extends Thread
{
    private Client client;
    public ClientReliveThread(Client client)
    {
        this.client=client;
    }
    public void run()
    {
        try {
            sleep(3000);
             PrintStream sendstream=CreatServer.clientPrintStreamMap.get(client);
             //生成初始化出生地址的数组
            int[] randomEntrance=Info.randomArray(0,4,1);
            int i=randomEntrance[0];
                sendstream.println(Sign.GameStart+i);//为死亡的的玩家发送初始化的出生坐标

        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}

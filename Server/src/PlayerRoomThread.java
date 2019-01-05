import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintStream;
import java.util.List;

public class PlayerRoomThread extends Thread
{
    private Client client;//当前游戏内线程服务玩家对象
    private PrintStream sendStream;//发送消息通道
    private BufferedReader getStream;//获取消息通道
    private ServerGameRoom currentGameRoom;//当前所在房间
    private List<Client> allClients;//所在房间内的玩家链表
    boolean isPlaying;

    /**
     * 游戏内服务线程构造
     * @param client 服务玩家
     * @param sendStream 发送流
     * @param getStream 接收流
     * @param currentGameRoom 房间
     */
    public PlayerRoomThread(Client client,PrintStream sendStream,BufferedReader getStream,ServerGameRoom currentGameRoom)
    {
        this.client=client;
        this.sendStream=sendStream;
        this.getStream=getStream;
        this.currentGameRoom=currentGameRoom;
        this.allClients=currentGameRoom.getAllClients();
        isPlaying=client.isPlaying();
    }

    /**
     * 覆写RUN
     */
    public void run()
    {
        String line = null;//接收到的初始字符串（信息）
        String command = null;//当前获取的信息需要执行的命令
        String realMessage = null;//去除头部命令的信息
        try {

            while (isPlaying && (line = getStream.readLine()) != null)
            {
                if (client.isPlaying() && line.startsWith(Sign.PlayerMove))
                {
                    //直接获取玩家移动的消息
                    realMessage = line;
                    System.out.println(allClients.size());
                    for (Client c : allClients)
                    {
                        //直接转发给房间内所有的在线玩家
                        if (!c.equals(client))
                        {
                            PrintStream sendstream = StartServer.clientPrintStreamMap.get(c);
                            sendstream.println(realMessage);
                        }
                    }
                }
                /**
                 * 游戏内地雷爆炸的消息
                 */
                else if (client.isPlaying() && line.startsWith(Sign.MineBoom))
                {
                    //获取爆炸的地雷的下标
                    realMessage = check.getRealMessage(line, Sign.MineBoom);
                    int mineflag = Integer.parseInt(realMessage.split(Sign.SplitSign)[0]);
                    //转发给房间内其他玩家
                    for (Client c : allClients)//给房间内所有玩家发送mineflag号地雷爆炸的消息
                    {
                        PrintStream sendstream = StartServer.clientPrintStreamMap.get(c);
                        sendstream.println(Sign.MineBoom + mineflag);//发送地雷爆炸消息
                    }


                }
                /**
                 * 游戏内手雷爆炸消息
                 */
                else if (client.isPlaying() && line.startsWith(Sign.GrenadeBoom)) {
                    //转发给房间内其他玩家
                    for (Client c : allClients)//给房间内所有玩家发送nadeflag号手雷爆炸的消息
                    {
                        PrintStream sendstream = StartServer.clientPrintStreamMap.get(c);
                        sendstream.println(line);//发送手雷爆炸消息
                    }
                }

                /**
                 * 游戏内玩家开火消息
                 */
                else if (client.isPlaying() && line.startsWith(Sign.CreateBullet)) {
                    for (Client c : allClients) {
                        PrintStream sendstream = StartServer.clientPrintStreamMap.get(c);
                        sendstream.println(line);
                    }
                }
                /**
                 * 游戏内玩家放置地雷消息
                 */
                else if (client.isPlaying() && line.startsWith(Sign.CreateMine)) {
                    for (Client c : allClients) {
                        PrintStream sendstream = StartServer.clientPrintStreamMap.get(c);
                        sendstream.println(line);
                    }
                }
                /**
                 * 游戏内玩家丢出手雷消息
                 */
                else if (client.isPlaying() && line.startsWith(Sign.CreateGrenade)) {
                    for (Client c : allClients) {
                        PrintStream sendstream = StartServer.clientPrintStreamMap.get(c);
                        sendstream.println(line);
                    }
                }
                /**
                 * 玩家死亡消息
                 */
                else if (client.isPlaying() && line.startsWith(Sign.OnePlayerDie)) {
                    //获取爆炸的地雷的下标
                    realMessage = check.getRealMessage(line, Sign.OnePlayerDie);
                    int diePlayerFlag = Integer.parseInt(realMessage);

                    //转发给房间内其他玩家
                    for (Client c : allClients)//给房间内所有玩家发送flag玩家死亡的消息
                    {
                        PrintStream sendstream = StartServer.clientPrintStreamMap.get(c);
                        sendstream.println(line);//发送玩家死亡消息
                    }

                    //创建复活信息发送线程
                    new PlayerReliveThread(currentGameRoom, diePlayerFlag).start();
                }
            }
        }catch (IOException e)
        {
            e.printStackTrace();
        }
    }
    /**
     * 获取sendStream
     * @return sendStream
     */
    public PrintStream getSendStream()
    {
        return sendStream;
    }

    /**
     * 获取当前getStream
     * @return getStream
     */
    public BufferedReader getGetStream()
    {
        return getStream;
    }
}

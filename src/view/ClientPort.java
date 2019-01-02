package view;

import java.io.BufferedReader;
import java.io.PrintStream;
import java.net.Socket;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

public class ClientPort {
    public static Socket socket;
    public static PrintStream sendStream;
    public static BufferedReader getStream;
    public static List<Client> allOnlineClient= Collections.synchronizedList(new LinkedList<>());//所有在线的玩家
    public static List<ServerGameRoom> allServerRoom=Collections.synchronizedList(new LinkedList<>());//所有存在的房间
    public static List<GameHall.GameRoom> allGameRoom=Collections.synchronizedList(new LinkedList<>());

    public static void main(String[] args)
    {
        //开启登陆界面
        new LoginFrame();
    }
}

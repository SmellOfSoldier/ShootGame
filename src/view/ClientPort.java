package view;

import java.io.BufferedReader;
import java.io.PrintStream;
import java.net.Socket;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class ClientPort {
    public static Socket socket;
    public static PrintStream sendStream;
    public static BufferedReader getStream;
    public static List<Client> allOnlineClient= Collections.synchronizedList(new LinkedList<>());//所有在线的玩家
    public static List<ServerGameRoom> allServerRoom=Collections.synchronizedList(new LinkedList<>());//所有存在的房间
    public static List<GameHall.GameRoom> allGameRoom=Collections.synchronizedList(new LinkedList<>());
    public static Lock threadLock=new ReentrantLock();     //同步锁，用来锁游戏线程和业务线程
    public static Condition threadCondition=threadLock.newCondition();   //用来使线程通信的condition
    public static GameHall gameHall;
    public static void main(String[] args)
    {
        //开启登陆界面
       new GameStart();
    }
}

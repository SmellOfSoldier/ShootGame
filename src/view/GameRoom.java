package view;


import person.Player;

import javax.swing.*;
import java.net.Socket;
import java.util.ArrayList;

/**
 * 游戏房间：用于多人游戏时，将在同一局游戏中的所有玩家联系起来
 * 房间相当于一个小服务器，它承上启下，作为服务器和玩家的中转站
 * 传输玩家在游戏中的各种操作信息：射击、移动、击杀等
 */
public class GameRoom extends JFrame
{
    private String id;                                  //房间的编号
    private String name;                                //房间的名称
    public static final int maxPlayerNum=4;             //房间最大玩家数目
    private int playerNum;                              //房间目前玩家数目
    private ArrayList<Player> playerArrayList =new ArrayList<Player>();         //存放该房间中的player
    private Player roomMaster;                          //房间的创建者

    private JLabel masterName;                          //房主用户名
    private JButton beging;                             //开始游戏
    private RoomArea roomArea;                          //游戏显示区域
    private JTextArea receiveArea=new JTextArea();      //消息接收区
    private JTextArea sendArea=new JTextArea();         //消息发送区

    public GameRoom()
    {
        roomArea=new RoomArea();
        this.add(roomArea);
        this.setResizable(false);
        this.setSize(200,400);

    }
    //房间显示区域
    private class RoomArea extends JPanel
    {
        RoomArea()
        {
            this.setLayout(null);           //绝对布局



        }
    }

}

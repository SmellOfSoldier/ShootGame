package view;


import person.Player;

import javax.swing.*;
import java.util.ArrayList;

/**
 * 游戏房间：用于多人游戏时，将在同一局游戏中的所有玩家联系起来
 * 房间相当于一个小服务器，它承上启下，作为服务器和玩家的中转站
 * 传输玩家在游戏中的各种操作信息：射击、移动、击杀等
 */
public class GameRoom extends JFrame
{
    private static int Width=300;
    private static int Height=800;
    private String id;                                  //房间的编号
    private String name;                                //房间的名称
    public static final int maxPlayerNum=4;             //房间最大玩家数目
    private int playerNum;                              //房间目前玩家数目
    private ArrayList<Player> playerList =new ArrayList<Player>();         //存放该房间中的玩家
    private ArrayList<Client> clientList =new ArrayList<>();            //存放该房间的用户，玩家和用户是数组下标一一对应
    private Client roomMaster;                          //房间的创建者
    private JLabel masterName=new JLabel();                                  //房主用户名
    private JButton startGame=new JButton("开始");  //开始游戏
    private JButton leaveRoom=new JButton("退出");  //退出房间
    private JButton sendMessage =new JButton("发送");     //发送消息
    private RoomArea roomArea;                              //游戏显示区域
    private JTextArea receiveArea=new JTextArea();          //消息接收区
    private JTextArea sendArea=new JTextArea();             //消息发送区
    private JList<String> clientJList;                       //房间内的玩家的用户名,用于显示在房间中

    public GameRoom()
    {
        roomArea=new RoomArea();
        this.add(roomArea);
        this.setResizable(false);
        this.setSize(Width,Height);
        this.setLocationRelativeTo(null);
        this.setVisible(true);
    }
    //房间显示区域
    private class RoomArea extends JPanel
    {
        RoomArea()
        {
            this.setSize(Width,Height);
            this.setLayout(null);           //绝对布局
            masterName.setText("房主：");
            masterName.setSize(100,50);
            masterName.setLocation(20,20);
            this.add(masterName);

            startGame.setSize(80,40);
            startGame.setLocation(200,80);
            this.add(startGame);

            leaveRoom.setSize(80,40);
            leaveRoom.setLocation(200,160);
            this.add(leaveRoom);

            sendMessage.setSize(60,40);
            sendMessage.setLocation(230,720);
            this.add(sendMessage);

            DefaultListModel<String> defaultListModel=new DefaultListModel<String>();
            clientJList=new JList<String>(defaultListModel);
            JScrollPane clientJListJsp=new JScrollPane(clientJList);
            clientJListJsp.setSize(Width,200);
            clientJListJsp.setLocation(0,200);
            this.add(clientJListJsp);

            receiveArea.setSize(Width,200);
            JScrollPane receiveAreaJsp=new JScrollPane(receiveArea);
            receiveAreaJsp.setSize(Width,200);
            receiveAreaJsp.setLocation(0,400);
            this.add(receiveAreaJsp);

            sendArea.setSize(Width,200);
            JScrollPane sendAreaJsp=new JScrollPane(sendArea);
            sendAreaJsp.setSize(Width,200);
            sendAreaJsp.setLocation(0,600);
            this.add(sendAreaJsp);

        }
    }

    public static void main(String[] args) {
        new GameRoom();
    }

}

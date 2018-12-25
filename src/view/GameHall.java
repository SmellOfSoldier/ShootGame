package view;

import com.google.gson.Gson;
import person.Player;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintStream;
import java.net.Socket;
import java.util.ArrayList;

/**
 * 游戏大厅类bylijie
 */
public class GameHall {
    private static Client currentClient=null;//当前用户

    private JFrame gameHallJFrame;//大厅frame
    private JButton killList;//击杀榜
    private JButton logout;//注销
    private JButton gameTutorial;//游戏教程
    private JButton offlineGame;//离线游戏
    private JButton createRoom;//创建游戏房间
    private JLabel gamerAccount;//玩家账号
    private JLabel gamerName;//玩家名字
    private JTextPane gamerIcon;//玩家头像
    private  DefaultListModel onlinePlayer;//在线玩家列表
    private DefaultListModel rooms;//所有房间列表
    private  JScrollPane rpane;//左
    private  JScrollPane lpanel;//右
    private JList currentOnlinePlayer;//当前在线用户
    private JList currentRoom;//当前可用房间
    private boolean isCreatRoom=false;//是否创建房间
    private GameRoom currentGameRoom=null;//当前房间
    GameHall(Client currentClient){
        this.currentClient=currentClient;
        gameHallJFrame=new JFrame("游戏大厅");
        gameHallJFrame.setSize(1000,800);
        gameHallJFrame.setLocationRelativeTo(null);
        gameHallJFrame.setLayout(null);//设置绝对布局
        gameHallJFrame.setResizable(false);//设置不可更改
        gameHallJFrame.setVisible(true);
        JPanel jPanel=new hallJPanel();
        gameHallJFrame.add(jPanel);
        jPanel.repaint();//重画
        /**
         * 创建房间监听
         */
        createRoom.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(!isCreatRoom) {
                    ClientPort.sendStream.println(Sign.CreateRoom + currentClient.getId());//发送创建房间的命令
                    createGameRoom(currentClient);
                    
                }else {
                    JOptionPane.showMessageDialog(gameHallJFrame, "您已经创建过房间请关闭已经创建的房间再尝试", "提示", JOptionPane.ERROR_MESSAGE);//弹出警告框
                }
            }
        });
        /**
         * 注销监听
         */
        logout.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ClientPort.sendStream.println(Sign.Logout);
                ClientPort.sendStream.flush();
            }
        });

        /**
         * 单人游戏
         */
        offlineGame.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new singlePersonModel();
            }
        });
        //关闭事件
        gameHallJFrame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                super.windowClosing(e);
                ClientPort.sendStream.println(Sign.Logout);
                ClientPort.sendStream.flush();
                System.exit(0);
            }
        });
    }
    class hallJPanel extends JPanel{
        hallJPanel(){
            this.setSize(1000,800);
            this.setLayout(null);//设置绝对布局
            //killList=new JButton("击杀榜");
            logout=new JButton("注销");
            gameTutorial=new JButton("游戏教程");
            offlineGame=new JButton("离线游戏");
            createRoom =new JButton("创建房间");
            gamerAccount=new JLabel("玩家账号");
            gamerName=new JLabel("玩家昵称");
            gamerIcon=new JTextPane();
            onlinePlayer=new DefaultListModel();
            rooms=new DefaultListModel();
            currentOnlinePlayer=new JList(onlinePlayer);
            currentOnlinePlayer.setBorder(new TitledBorder("当前在线玩家"));
            currentRoom=new JList(rooms);
            currentRoom.setBorder(new TitledBorder("当前房间"));
            rpane=new JScrollPane(currentOnlinePlayer);
            lpanel=new JScrollPane(currentRoom);
            //设置两个列表的位置
            rpane.setSize(495,600);
            rpane.setLocation(0,200);
            lpanel.setSize(495,600);
            lpanel.setLocation(495,200);
            //设置上层菜单左边布局
            gamerIcon.setSize(150,150);
            gamerIcon.setLocation(25,25);
            gamerIcon.setEditable(false);
            gamerAccount.setSize(200,70);
            gamerAccount.setLocation(225,25);
            gamerName.setSize(200,70);
            gamerName.setLocation(225,105);
            //设置上层菜单右边布局
            createRoom.setSize(180,60);
            createRoom.setLocation(525,15);
            createRoom.setBackground(new Color(0xFFEFDB));
            logout.setSize(180,60);
            logout.setLocation(725,15);
            logout.setBackground(new Color(0xFFEFDB));
            gameTutorial.setSize(180,60);
            gameTutorial.setLocation(525,115);
            gameTutorial.setBackground(new Color(0xFFEFDB));
            offlineGame.setSize(180,60);
            offlineGame.setLocation(725,115);
            offlineGame.setBackground(new Color(0xFFEFDB));
            this.add(logout);
            this.add(gameTutorial);
            this.add(offlineGame);
            this.add(createRoom);
            this.add(gamerAccount);
            this.add(gamerName);
            this.add(gamerIcon);
            this.add(rpane);
            this.add(lpanel);
            //初始化在线玩家列表
            for(Client c:ClientPort.allOnlineClient){
                onlinePlayer.addElement(c.getId());
            }
            //初始化在线房间列表
            for(ServerGameRoom r:ClientPort.allServerRoom){
                rooms.addElement(r.getId());
            }
        }
    }
    /**
     * 设置是否创建房间
     * @param flag
     */
    public void setIsCreateRoom(boolean flag){
        this.isCreatRoom=flag;
    }

    /**
     * 游戏房间：用于多人游戏时，将在同一局游戏中的所有玩家联系起来
     * 房间相当于一个小服务器，它承上启下，作为服务器和玩家的中转站
     * 传输玩家在游戏中的各种操作信息：射击、移动、击杀等
     */
    public class GameRoom extends JFrame
    {
        private  int Width=300;
        private  int Height=800;
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
        //private utils.Client.ClientThread


        public GameRoom(Client roomMaster,String roomname)
        {
            roomArea=new RoomArea();
            this.add(roomArea);
            this.setResizable(false);
            this.setSize(Width,Height);
            this.setLocation(1458,115);
            this.setVisible(true);
            this.roomMaster=roomMaster;
            this.name=roomname;
        }

        //房间显示区域
         class RoomArea extends JPanel
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
    }

    /**
     * 创建游戏房间时之后所有于服务器之间进行关于房间信息和游戏中的数据
     * 传输都分配给该线程的实例对象完成
     */
    public class ClientThread extends  Thread
    {
        private Socket socket;
        private PrintStream sendstream;
        private BufferedReader getstream;
        ClientThread(Socket socket,PrintStream sendstream,BufferedReader getstream)
        {
            this.socket=socket;
            this.sendstream=sendstream;
            this.getstream=getstream;
        }
        public void run()
        {
            String line=null;//接收到的初始字符串（信息）
            String command = null;//当前获取的信息需要执行的命令
            String realMessage = null;//去除头部命令的信息
            while (!this.isInterrupted())
            {
                try
                {
                    line=getstream.readLine();
                    System.out.println("客户端收到来自服务器的消息"+line);
                    {

                    }
                } catch (IOException e)
                {
                    e.printStackTrace();
                }
            }
        }
        /**
         *  停止该连接线程
         * @return
         */
        public boolean stopThisThread()
        {
            this.interrupt();
            return true;
        }
    }
    /**
     * 用于依据标志符号切割掉客户端发送来的字符串开始的命令
     * @param line 每次客户端发送过来的字符串
     * @param cmd 字符串中开头包含的命令
     * @return 返回命令后的字符串
     */
    public static String getRealMessage(String line,String cmd)
    {
        String realMessage=line.substring(cmd.length(),line.length());
        return realMessage;
    }
    /**
     * 创建房间
     * @param roomMaster
     */
    public void createGameRoom(Client roomMaster)
    {
        String roomname=null;

        ClientPort.sendStream.println(Sign.CreateRoom+roomname);
        currentGameRoom=new GameRoom(roomMaster,roomname);
    }

}

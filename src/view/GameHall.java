package view;

import Arsenal.AWM;
import Arsenal.M4A1;
import Weapon.Grenade;
import Weapon.Mine;
import com.google.gson.Gson;
import javafx.scene.transform.Rotate;
import person.Person;
import person.Player;
import utils.MusicPlayer;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;

/**
 * 游戏大厅类bylijie
 */
public class GameHall
{
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
    private JList currentOnlinePlayerJList;//当前在线用户
    private JList currentRoomJList;//当前可用房间
    private String ip;//服务器ip
    private boolean isCreatRoom=false;//是否创建房间
    private boolean isEnterRoom=false;//是否加入房间
    private  GameRoom currentGameRoom=null;//当前房间
    private  ClientThread clientThread=null;
    private Point[] entrance=new Point[]{new Point(800,20),new Point(1040,280),new Point(1160,600), new Point(320,760),new Point(20,520)};         //刷怪位置
    GameHall(Client currentClient,String ip)
    {
        this.currentClient=currentClient;
        this.ip=ip;
        gameHallJFrame=new JFrame("游戏大厅");
        gameHallJFrame.setSize(1000,800);
        gameHallJFrame.setLocationRelativeTo(null);
        gameHallJFrame.setLayout(null);//设置绝对布局
        gameHallJFrame.setResizable(false);//设置不可更改
        gameHallJFrame.setVisible(true);
        JPanel jPanel=new hallJPanel();
        gameHallJFrame.add(jPanel);
        MusicPlayer.playGameHallBGM();
        jPanel.repaint();//重画
        /**
         * 游戏帮助监听
         */
        gameTutorial.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new GameTutorialFrame();
            }
        });


        /**
         * 创建房间监听
         */
        createRoom.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(currentGameRoom==null) {
                    new createRoomGui(currentClient);//创建创建房间gui
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
                //关闭大厅界面
                if(currentClient!=null)
                {

                    if(currentGameRoom!=null)
                    {
                        ClientPort.sendStream.println(Sign.LeaveRoom);
                        currentGameRoom.dispose();
                    }
                }
                ClientPort.sendStream.println(Sign.Logout);
                ClientPort.sendStream.flush();
                new LoginFrame(gameHallJFrame,ip);
                MusicPlayer.stopGameHallBGM();
                gameHallJFrame.dispose();
            }
        });
        //加入房间事件
        currentRoomJList.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e)
            {
                super.mousePressed(e);
                if(e.getClickCount()==2)
                {
                    System.out.println(currentClient.getRoomID()+"当前玩家所在房间的id为：");
                    if(currentGameRoom==null)
                    {
                        System.out.println("加入房间中。");
                        JList currentRooms=(JList)e.getSource();
                        int index=currentRooms.getSelectedIndex();
                        Object obj=currentRooms.getModel().getElementAt(index);
                        System.out.println("点击加入 "+obj.toString()+" 房间");
                        System.out.println("房间名："+obj.toString());
                        //发送加入房间请求
                        ClientPort.sendStream.println(Sign.EnterRoom+currentClient.getId()+Sign.SplitSign+obj.toString());
                    }
                    else{
                        JOptionPane.showMessageDialog(gameHallJFrame,"你已加入了房间","警告",JOptionPane.OK_OPTION);
                    }

                }
            }
        });

        /**
         * 单人游戏
         */
        offlineGame.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new SinglePersonModel(gameHallJFrame);
            }
        });
        //关闭事件
        gameHallJFrame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                super.windowClosing(e);
                //先使大厅不可见
                gameHallJFrame.setVisible(false);
                //离开大厅（注销账号）
                //如果在房间则发送离开房间命令
                if(currentGameRoom!=null) leaveRoom();
                //发送注销命令
                ClientPort.sendStream.println(Sign.Disconnect);
                //睡眠一会将最后的命令发送到服务器
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
            gamerAccount=new JLabel("玩家账号："+currentClient.getId());
            gamerName=new JLabel("玩家昵称：xxx");
            gamerIcon=new JTextPane();
            onlinePlayer=new DefaultListModel();
            rooms=new DefaultListModel();
            currentOnlinePlayerJList =new JList(onlinePlayer);
            currentOnlinePlayerJList.setBorder(new TitledBorder("当前在线玩家"));
            currentRoomJList =new JList(rooms);
            currentRoomJList.setBorder(new TitledBorder("当前房间"));
            rpane=new JScrollPane(currentOnlinePlayerJList);
            lpanel=new JScrollPane(currentRoomJList);
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
            for(Client client:ClientPort.allOnlineClient)
            {
                onlinePlayer.addElement(client.getId());
            }
            for(ServerGameRoom room:ClientPort.allServerRoom)
            {
                rooms.addElement(room.getId());
            }
            this.add(logout);
            this.add(gameTutorial);
            this.add(offlineGame);
            this.add(createRoom);
            this.add(gamerAccount);
            this.add(gamerName);
            this.add(gamerIcon);
            this.add(rpane);
            this.add(lpanel);
            clientThread=new ClientThread(ClientPort.socket,ClientPort.sendStream,ClientPort.getStream,ClientPort.threadLock,ClientPort.threadCondition);
            clientThread.start();


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
        private Client roomMaster=null;                          //房间的创建者
        private JTextField masterName=new JTextField();                                  //房主用户名
        private JButton startGame=new JButton("开始");  //开始游戏
        private JButton leaveRoom=new JButton("退出");  //退出房间
        private JButton sendMessage =new JButton("发送");     //发送消息
        private JButton tickPerson=new JButton("踢出房间");     //踢出玩家
        private RoomArea roomArea;                              //游戏显示区域
        private JTextArea receiveArea=new JTextArea();          //消息接收区
        private JTextArea sendArea=new JTextArea();             //消息发送区

        private JList<String> clientIdJList;                       //房间内的玩家的用户名,用于显示在房间中
        private DefaultListModel<String> clientIdModel =new DefaultListModel<String>();
        private String roomTips=null;                           //房间提示语(在游戏大厅中显示的对应房间的简单信息)

        private JList<String> clientJList;                       //房间内的玩家的用户名,用于显示在房间中
        private DefaultListModel<String> defaultListModel=new DefaultListModel<String>();


        //private utils.Client.ClientThread


        public GameRoom(Client roomMaster,String roomname,List<Client> clients)
        {
            this.roomMaster=roomMaster;
            for(Client client:clients)
            {
                clientList.add(client);
            }
            roomArea=new RoomArea();
            this.id=roomMaster.getId();
            this.name=roomname;
            this.add(roomArea);
            this.setResizable(false);
            this.setSize(Width,Height);
            this.setLocation(1458,115);
            this.setVisible(true);
            this.name=roomname;

            this.addWindowListener(new WindowAdapter() {
                @Override
                public void windowClosing(WindowEvent e) {
                    super.windowClosing(e);
                    //如果当前玩家处在房间中则退出当前房间
                    leaveRoom();
                    GameRoom.this.dispose();
                }
            });

        }
        //获取房间的id
        public String getId()
        {
            return id;
        }
        //获取房间名
        public String getName()
        {
            return name;
        }
        //获取房间内用户id的列表
        public JList<String> getClientIdJList()
        {
            return clientIdJList;
        }
        //获取用户id列表的defaultListModel
        public DefaultListModel<String> getClientIdModel()
        {
            return clientIdModel;
        }
        //获取房间的简单信息
        public String getRoomTips(){return roomTips;}
        //设置房间的简单信息
        public void setRoomTips(String roomTips){this.roomTips=roomTips;}
        //设置房主
        public void setRoomMaster(Client roomMaster){this.roomMaster=roomMaster;}
        public Client getRoomMaster(){return roomMaster;}
        //添加消息接收框的消息
        public void putMessage(String message)
        {
            receiveArea.append(message+"\r\n");
        }
        //房间显示区域
         class RoomArea extends JPanel
        {
            public RoomArea()
            {
                this.setSize(Width,Height);
                this.setLayout(null);           //绝对布局
                masterName.setText("房主："+roomMaster.getId());
                masterName.setEditable(false);
                masterName.setFont(new Font(null,Font.BOLD,16));
                masterName.setBorder(null);
                masterName.setSize(100,50);
                masterName.setLocation(20,20);
                this.add(masterName);
                //初始化开始游戏按钮
                startGame.setSize(80,40);
                startGame.setLocation(200,80);
                this.add(startGame);
                //初始化离开房间按钮
                leaveRoom.setSize(80,40);
                leaveRoom.setLocation(200,160);
                this.add(leaveRoom);
                //初始化发送消息按钮
                sendMessage.setSize(60,40);
                sendMessage.setLocation(230,720);
                this.add(sendMessage);
                //初始化踢人按钮
                tickPerson.setSize(100,40);
                tickPerson.setLocation(20,160);
                this.add(tickPerson);
                //初始化用户显示列表
                clientIdJList =new JList<String>(clientIdModel);
                JScrollPane clientJListJsp=new JScrollPane(clientIdJList);
                clientJListJsp.setSize(Width,200);
                clientJListJsp.setLocation(0,200);
                this.add(clientJListJsp);
                //初始化房间聊天消息接收框
                receiveArea.setSize(Width,200);
                receiveArea.setFont(new Font(null,Font.BOLD,16));
                JScrollPane receiveAreaJsp=new JScrollPane(receiveArea);
                receiveAreaJsp.setSize(Width,200);
                receiveAreaJsp.setLocation(0,400);
                this.add(receiveAreaJsp);
                //初始化房间聊天编辑框
                sendArea.setSize(Width,200);
                sendArea.setFont(new Font(null,Font.BOLD,16));
                JScrollPane sendAreaJsp=new JScrollPane(sendArea);
                sendAreaJsp.setSize(Width,200);
                sendAreaJsp.setLocation(0,600);
                this.add(sendAreaJsp);

                for(Client client:clientList)
                {
                    clientIdModel.addElement(client.getId());
                }
                initialListenThread();
            }

            /**
             * 初始化房间按钮的监听器
             */

            private void initialListenThread()
            {
                //初始化踢出人物按钮
                tickPerson.addMouseListener(new MouseAdapter() {
                    @Override
                    public void mousePressed(MouseEvent e) {
                        super.mousePressed(e);
                        List<String> selectList= clientIdJList.getSelectedValuesList();
                        String selectPersonId=selectList.get(0);
                        if(currentClient.getId().equals(selectPersonId))
                        {
                            JOptionPane.showMessageDialog(null, "你自己就是房主，怎么踢自己？", "秀啊", JOptionPane.OK_OPTION);
                            return;
                        }
                        PrintStream ps=ClientPort.sendStream;
                        ps.println(Sign.TickFromRoom+selectPersonId+Sign.SplitSign+id);

                    }
                });
                //初始化离开房间按钮
                leaveRoom.addMouseListener(new MouseAdapter() {
                    @Override
                    public void mousePressed(MouseEvent e)
                    {
                        super.mousePressed(e);
                        leaveRoom();
                    }
                });
                //初始化发送消息按钮
                sendMessage.addMouseListener(new MouseAdapter() {
                    @Override
                    public void mousePressed(MouseEvent e)
                    {
                        super.mousePressed(e);
                        String message=sendArea.getText();
                        sendArea.setText(null);
                        PrintStream ps=ClientPort.sendStream;
                        //将消息发送给服务端
                        ps.println(Sign.SendPublicMessage+message);
                    }
                });
                startGame.addMouseListener(new MouseAdapter() {
                    @Override
                    public void mousePressed(MouseEvent e) {
                        super.mousePressed(e);
                        PrintStream ps=ClientPort.sendStream;
                        ps.println(Sign.StartGame);
                    }
                });
            }
        }

    }
    /**
     * 创建房间的GUI界面类
     */
    public class createRoomGui {
        private JFrame creatRoomJFrame;
        private createRoomJPanel createRoomjpanel;
        private JLabel roomnamelable;
        private JLabel maxclientnumlable;
        private JButton btn_creat;
        private JButton btn_exit;
        private JTextField txt_room_name;
        private JTextField txt_maxclients;
        private PrintStream sendStream;
        private BufferedReader getStream;
        private Client currentclient;
        createRoomGui(Client currentclient){
            this.currentclient=currentclient;
            createRoomjpanel =new createRoomJPanel();
            creatRoomJFrame=new JFrame();
            this.sendStream=ClientPort.sendStream;
            this.getStream=ClientPort.getStream;
            creatRoomJFrame.setTitle("创建房间");
            creatRoomJFrame.add(createRoomjpanel);
            creatRoomJFrame.setSize(300,200);
            creatRoomJFrame.setResizable(false);
            creatRoomJFrame.setLayout(null);
            creatRoomJFrame.setVisible(true);
            //设置相对屏幕绝对位置
            creatRoomJFrame.setLocationRelativeTo(null);
            creatRoomJFrame.setVisible(true);
            btn_creat.addActionListener(new ActionListener()
            {
                @Override
                public void actionPerformed(ActionEvent e)
                {
                        sendStream.println(Sign.CreateRoom + currentclient.getId());//发送创建房间的命令
                        creatRoomJFrame.dispose();
                        //置为已经开启房间状态和已经加入房间状态
                        isCreatRoom=true;

                }
            });
            btn_exit.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    creatRoomJFrame.dispose();
                }
            });
            creatRoomJFrame.addWindowListener(new WindowAdapter() {
                @Override
                public void windowClosing(WindowEvent e) {
                    super.windowClosing(e);
                    creatRoomJFrame.dispose();
                }
            });
        }
        /**
         * 内部类定义房间创建GUI的布局
         */
        class createRoomJPanel extends  JPanel{
            createRoomJPanel(){
                this.setSize(300,200);
                this.setLayout(null);
                this.setLocation(0,0);
                initial();
            }
            private void initial(){
                roomnamelable=new JLabel("房间名字");//暂时不考虑
                maxclientnumlable =new JLabel("最大人数");//暂时只能为4
                btn_creat=new JButton("开启");
                btn_exit=new JButton("取消");
                txt_room_name =new JTextField();
                txt_room_name.setText(currentclient.getId());
                txt_room_name.setEditable(false);
                txt_maxclients=new JTextField();
                txt_maxclients.setText("4");
                txt_maxclients.setEditable(false);
                this.add(roomnamelable);
                this.add(maxclientnumlable);
                this.add(btn_creat);
                this.add(btn_exit);
                this.add(txt_room_name);
                this.add(txt_maxclients);
                //TODO:注册帮助
                //设置位置
                //公告栏位置大小
                //昵称位置大小
                roomnamelable.setSize(70,30);
                roomnamelable.setLocation(30,35);
                maxclientnumlable.setSize(70,30);
                maxclientnumlable.setLocation(30,70);
                //账号位置大小
                txt_room_name.setSize(120,30);
                txt_room_name.setLocation(110,35);
                txt_maxclients.setSize(120,30);
                txt_maxclients.setLocation(110,70);
                //注册
                btn_creat.setSize(60,30);
                btn_creat.setLocation(80,110);
                //取消
                btn_exit.setSize(60,30);
                btn_exit.setLocation(170,110);
            }
        }

    }

    /**
     * 创建游戏房间时之后所有于服务器之间进行关于房间信息和游戏中的数据
     * 传输都分配给该线程的实例对象完成
     */
    public class ClientThread extends  Thread
    {
        private Lock lock=null;
        Condition condition=null;
        private Socket socket;
        private PrintStream sendstream;
        private BufferedReader getstream;
        ClientThread(Socket socket,PrintStream sendstream,BufferedReader getstream,Lock lock,Condition condition)
        {
            this.lock=lock;
            this.socket=socket;
            this.sendstream=sendstream;
            this.getstream=getstream;
            this.condition=condition;
        }

        public void run()
        {
            lock.lock();
            String line=null;//接收到的初始字符串（信息）
            String command = null;//当前获取的信息需要执行的命令
            String realMessage = null;//去除头部命令的信息
            sign:while (!this.isInterrupted())
            {
                try {
                     if((line=getstream.readLine())!=null) {
                         System.out.println("客户端收到来自服务器的消息" + line);

                         //如果有用户上线
                         if (line.startsWith(Sign.OneClientOnline)) {
                             realMessage = getRealMessage(line, Sign.OneClientOnline);
                             onlinePlayer.addElement(realMessage);
                         }
                         //如果有用户下线
                         else if (line.startsWith(Sign.OneClientOffline)) {
                             realMessage = getRealMessage(line, Sign.OneClientOffline);
                             onlinePlayer.removeElement(realMessage);
                         }
                         //如果有用户加入当前房间
                         else if (line.startsWith(Sign.NewClientEnter)) {
                             realMessage = getRealMessage(line, Sign.NewClientEnter);
                             //进入房间的用户ID
                             String clientId = realMessage;
                             //获取大厅房间中用户id的列表;   （客户端大厅显示的房间类）
                             DefaultListModel<String> clientIdInRoom = currentGameRoom.getClientIdModel();
                             clientIdInRoom.addElement(clientId);
                         }
                         //如果有用户离开当前房间或被踢出房间 （非房主）
                         else if (line.startsWith(Sign.ClientLeaveRoom)) {
                             realMessage = getRealMessage(line, Sign.ClientLeaveRoom);
                             //进入房间的用户ID
                             String clientId = realMessage.split(Sign.SplitSign)[0];
                             //进入的房间ID
                             String roomId = realMessage.split(Sign.SplitSign)[1];
                             //获取大厅房间中用户id的列表;   （客户端大厅显示的房间类）
                             DefaultListModel<String> clientIdInRoom = currentGameRoom.getClientIdModel();
                             //将用户从当前房间用户id列表中删除
                             System.out.println(clientIdInRoom.size());
                             System.out.println(clientIdInRoom.contains(clientId) + clientId + "\n");
                             for (int i = 0; i < clientIdInRoom.size(); i++) {
                                 System.out.println(clientIdInRoom.get(i));
                             }
                             clientIdInRoom.removeElement(clientId);
                             System.out.println(clientIdInRoom.size());
                         }
                         //如果用户注销，并且收到服务端关闭业务线程的命令
                         else if (line.startsWith(Sign.CloseLocalThread)) {
                             break sign;
                         }
                         //如果房间被解散（房主离开房间）
                         else if (line.startsWith(Sign.RoomDismiss)) {
                             //该用户目前所处房间的id
                             String roomId = currentGameRoom.getId();
                             System.out.println("需要移除的房间id为" + roomId);
                             //将该房间从房间链表中删除
                             ClientPort.allServerRoom.remove(new ServerGameRoom(roomId, new Client(null, null), null));
                             //将该房间从大厅房间列表中删除
                             System.out.println("delete position");
                             rooms.removeElement(roomId);
                             System.out.println("rooms .remove");
                             currentClient.setRoomNull();
                             System.out.println("set null");
                             currentGameRoom.setVisible(false);
                             System.out.println("dispose");
                             if (currentGameRoom != null)
                                 currentGameRoom = null;
                             System.out.println("roomdis run out");
                         }
                         //如果被房主踢出房间
                         else if (line.startsWith(Sign.BeenTicked)) {
                             DefaultListModel<String> clientIdInRoom = currentGameRoom.getClientIdModel();
                             //将用户从当前房间用户id列表中删除
                             clientIdInRoom.removeElement(currentClient.getId());
                             currentGameRoom.setVisible(false);
                             currentClient.setRoomNull();
                             currentGameRoom = null;
                             JOptionPane.showConfirmDialog(null, "你被房主踢出了房间", "提示", JOptionPane.OK_OPTION);
                         }
                         //如果有房间新建
                         else if (line.startsWith(Sign.NewRoomCreate)) {
                             realMessage = getRealMessage(line, Sign.NewRoomCreate);
                             Gson gson = new Gson();
                             ServerGameRoom serverGameRoom = gson.fromJson(realMessage, ServerGameRoom.class);
                             String roomId = serverGameRoom.getId();
                             //更新大厅房间信息列表
                             rooms.addElement(roomId);
                             ClientPort.allServerRoom.add(serverGameRoom);
                         }
                         //如果有房间被解散
                         else if (line.startsWith(Sign.DeleteRoom)) {
                             String roomId = getRealMessage(line, Sign.DeleteRoom);
                             for (int i = 0; i < ClientPort.allServerRoom.size(); i++) {
                                 if (ClientPort.allServerRoom.get(i).getId().equals(roomId)) {
                                     ClientPort.allServerRoom.remove(i);
                                     break;
                                 }
                             }
                             rooms.removeElement(roomId);
                         }
                         //如果收到房间里面其他玩家发来的消息
                         else if (line.startsWith(Sign.FromServerMessage) && currentClient.getRoomID() != null) {
                             realMessage = getRealMessage(line, Sign.FromServerMessage);
                             currentGameRoom.putMessage(realMessage);
                         }
                         //如果收到服务器退出的消息
                         else if (line.startsWith(Sign.ServerExit)) {
                             getstream.close();
                             sendstream.close();
                             socket.close();
                             JOptionPane.showMessageDialog(null, "服务器已经关闭，客户端即将退出", "警告", JOptionPane.OK_OPTION);
                             System.exit(0);
                         }

                         //如果创建房间成功
                         else if (line.startsWith(Sign.PermissionCreateRoom)) {
                             realMessage = getRealMessage(line, Sign.PermissionCreateRoom);
                             Gson gson = new Gson();
                             ServerGameRoom serverGameRoom = gson.fromJson(realMessage, ServerGameRoom.class);
                             //当前玩家的房间设置为自己创建的房间
                             currentClient.setGameRoomID(serverGameRoom.getId());
                             currentGameRoom = new GameRoom(currentClient, currentClient.getId(), serverGameRoom.getAllClients());

                         }
                         //如果允许进入房间
                         else if (line.startsWith(Sign.PermissionEnterRoom)) {
                             String roomStr = getRealMessage(line, Sign.PermissionEnterRoom);
                             //将玩家所在房间设为加入的房间
                             Gson gson = new Gson();
                             ServerGameRoom serverGameRoom = gson.fromJson(roomStr, ServerGameRoom.class);
                             currentClient.setGameRoomID(serverGameRoom.getId());
                             currentGameRoom = new GameRoom(serverGameRoom.getMaster(), serverGameRoom.getName(), serverGameRoom.getAllClients());
                         }
                         //如果房间已满
                         else if (line.startsWith(Sign.RoomFull)) {
                             JOptionPane.showMessageDialog(null, "房间已满", "提示", JOptionPane.OK_OPTION);
                         }
                         //如果收到游戏已经开始的命令
                         else if (line.startsWith(Sign.GameStart)) {
                             realMessage = getRealMessage(line, Sign.GameStart);
                             //本地玩家的下标
                             int myPlayerIndex = Integer.parseInt(realMessage.split(Sign.SplitSign)[1]);
                             Gson gson = new Gson();
                             String pointsIndexStr = realMessage.split(Sign.SplitSign)[0];
                             System.out.println(pointsIndexStr);
                             Integer[] pointIndex = gson.fromJson(pointsIndexStr, Integer[].class);
                             List<Player> players = Collections.synchronizedList(new ArrayList<Player>());
                             for (int i = 0; i < pointIndex.length; i++) {
                                 players.add(createPlayer(i + "", entrance[pointIndex[i]], currentGameRoom.getClientIdModel().get(i), currentGameRoom.getClientIdModel().get(i)));
                             }
                             sendstream.println(Sign.GameReadyStart);
                             gameHallJFrame.setVisible(false);
                             currentGameRoom.setVisible(false);
                             new MultiPlayerModel(players.get(myPlayerIndex), players, gameHallJFrame, currentGameRoom, GameHall.this);
                             //停止播放大厅背景音乐
                             MusicPlayer.stopGameHallBGM();
                             //业务线程睡眠，等待游戏结束后被唤醒
                             condition.await();

                         }
                         //TODO:客户端线程
                     }
                }
                catch (IOException e)
                {
                    e.printStackTrace();
                }
                catch (Exception ex)
                {
                    ex.printStackTrace();
                }

            }
            System.out.println("线程关闭");
            lock.unlock();
        }
        /**
         *  停止该连接线程
         * @return
         */
        public boolean stopThisThread(){
            this.interrupt();
            return true;
        }
    }
    /**
     * 玩家离开房间
     */
    private void leaveRoom()
    {
        PrintStream ps=ClientPort.sendStream;
        //将人物的id与将要离开的房间的id发送给服务端
        ps.println(Sign.LeaveRoom);
        if(currentGameRoom!=null && !currentGameRoom.getId().equals(currentClient.getId()))
        {
            currentGameRoom.setVisible(false);
            currentGameRoom=null;
        }
    }
    /**
     * 创建玩家
     * @param id：玩家对应链表中的下标
     * @param startPoint：玩家出生位置
     * @return
     */
    private Player createPlayer(String id,Point startPoint,String name,String clientId)
    {
        Player player = null;
        try
        {
            Rotate rotate = new Rotate();
            player = new Player(id, name,clientId);
            int size = 2 * (player.getRadius());
            player.setSize(size, size);
            InputStream is = startGame.class.getResourceAsStream("/images/player/"+ Person.playerImageFile[Integer.parseInt(id)]);
            BufferedImage bufferedImage = ImageIO.read(is);
            ImageIcon icon = new ImageIcon();
            icon.setImage(bufferedImage);
            icon.setImage(icon.getImage().getScaledInstance(size, size, Image.SCALE_DEFAULT));
            player.setIcon(icon);
            //player.peekWeapon(new AWM(), 100);
            player.peekWeapon(new Mine(), 5);
            player.peekWeapon(new Grenade(), 10);
            player.peekWeapon(new M4A1(), 210);
            player.setLocation(startPoint);
        }
        catch (IOException ioe)
        {
            ioe.printStackTrace();
        }
        finally
        {
            return player;
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
     * 刷新gamehall列表
     * @param allclient
     * @param allgameroom
     */
    public void refreshList(List<Client> allclient,List<ServerGameRoom> allgameroom)
    {
        onlinePlayer.clear();
        rooms.clear();
        for(Client c:allclient)
        {
            onlinePlayer.addElement(c.getId());
        }
        for(ServerGameRoom s:allgameroom)
        {
            rooms.addElement(s.getId());
        }
    }
}

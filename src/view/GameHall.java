package view;

import com.sun.security.ntlm.Server;
import com.sun.xml.internal.bind.v2.model.core.ID;
import person.Player;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.awt.event.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

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
        private JButton tickPerson=new JButton("踢出房间");     //踢出玩家
        private RoomArea roomArea;                              //游戏显示区域
        private JTextArea receiveArea=new JTextArea();          //消息接收区
        private JTextArea sendArea=new JTextArea();             //消息发送区
        private JList<String> clientJList;                       //房间内的玩家的用户名,用于显示在房间中
        private DefaultListModel<String> defaultListModel=new DefaultListModel<String>();
        private String roomTips=null;                           //房间提示语(在游戏大厅中显示的对应房间的简单信息)
        //private utils.Client.ClientThread


        public GameRoom(Client roomMaster,String roomname)
        {
            roomArea=new RoomArea();
            this.id=roomMaster.getId();
            this.name=roomname;
            this.add(roomArea);
            this.setResizable(false);
            this.setSize(Width,Height);
            this.setLocation(1458,115);
            this.setVisible(true);
            this.roomMaster=roomMaster;
            this.name=roomname;
            this.addWindowListener(new WindowAdapter() {
                @Override
                public void windowClosing(WindowEvent e) {
                    super.windowClosing(e);
                    //离开房间

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
        public JList<String> getClientJList()
        {
            return clientJList;
        }
        //获取用户id列表的defaultListModel
        public DefaultListModel<String> getDefaultListModel()
        {
            return defaultListModel;
        }
        //获取房间的简单信息
        public String getRoomTips(){return roomTips;}
        //设置房间的简单信息
        public void setRoomTips(String roomTips){this.roomTips=roomTips;}
        //设置房主
        public void setRoomMaster(Client roomMaster){this.roomMaster=roomMaster;}
        //返回房主
        public Client getRoomMaster(){return roomMaster;}
        //房间显示区域
         class RoomArea extends JPanel
        {
            public RoomArea()
            {
                this.setSize(Width,Height);
                this.setLayout(null);           //绝对布局
                masterName.setText("房主：");
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
                clientJList=new JList<String>(defaultListModel);
                JScrollPane clientJListJsp=new JScrollPane(clientJList);
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
                    public void mouseClicked(MouseEvent e) {
                        super.mouseClicked(e);
                        List<String> selectList=clientJList.getSelectedValuesList();
                        String selectPersonId=selectList.get(0);
                        PrintStream ps=ClientPort.sendStream;
                        ps.println(Sign.TickFromRoom+selectPersonId+Sign.SplitSign+id);
                    }
                });
                //初始化离开房间按钮
                leaveRoom.addMouseListener(new MouseAdapter() {
                    @Override
                    public void mouseClicked(MouseEvent e)
                    {
                        super.mouseClicked(e);
                        leaveRoom();
                    }
                });
                //初始化发送消息按钮
                sendMessage.addMouseListener(new MouseAdapter() {
                    @Override
                    public void mouseClicked(MouseEvent e)
                    {
                        super.mouseClicked(e);
                        String message=sendArea.getText();
                        sendArea.setText(null);
                        PrintStream ps=ClientPort.sendStream;
                        //将消息发送给服务端
                        ps.println(Sign.SendPublicMessage+message);
                    }
                });
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
        public void run(){
            String line=null;//接收到的初始字符串（信息）
            String command = null;//当前获取的信息需要执行的命令
            String realMessage = null;//去除头部命令的信息
            while (!this.isInterrupted())
            {
                try {
                    line=getstream.readLine();
                    System.out.println("客户端收到来自服务器的消息"+line);

                    //如果有用户加入当前房间
                    if(line.startsWith(Sign.NewClientEnter))
                    {
                        realMessage=getRealMessage(line,Sign.NewClientEnter);
                        //进入房间的用户ID
                        String clientId=realMessage;
                        //获取大厅房间中用户id的列表;   （客户端大厅显示的房间类）
                        DefaultListModel<String> clientIdInRoom=currentGameRoom.getDefaultListModel();
                        clientIdInRoom.addElement(clientId);
                        String roomName=currentGameRoom.getName();
                        //目前该房间中的人
                        int clientNum=clientIdInRoom.size();
                        //该房间最大的人数
                        int maxNum=GameRoom.maxPlayerNum;
                        //修改大厅房间的简单信息
                        String roomTips=currentGameRoom.getRoomTips();
                        rooms.removeElement(roomTips);
                        roomTips=roomName+"("+clientNum+"/"+maxNum+")"+"\t等待中";
                        rooms.addElement(roomTips);
                        currentGameRoom.setRoomTips(roomTips);
                    }
                    //如果有用户加入非当前房间
                    else if(line.startsWith(Sign.OtherClientEnterRoom))
                    {
                       //String roomId=
                    }
                    //如果有用户离开当前房间或被踢出房间 （非房主）
                    else if(line.startsWith(Sign.ClientLeaveRoom))
                    {
                        realMessage=getRealMessage(line,Sign.NewClientEnter);
                        //进入房间的用户ID
                        String clientId=realMessage.split(Sign.SplitSign)[0];
                        //进入的房间ID
                        String roomId=realMessage.split(Sign.SplitSign)[1];
                        //获取大厅房间中用户id的列表;   （客户端大厅显示的房间类）
                        DefaultListModel<String> clientIdInRoom=currentGameRoom.getDefaultListModel();
                        //将用户从当前房间用户id列表中删除
                        clientIdInRoom.removeElement(clientId);
                        //房间名
                        String roomName=currentGameRoom.getName();
                        //目前该房间中的人
                        int clientNum=clientIdInRoom.size();
                        //该房间最大的人数
                        int maxNum=GameRoom.maxPlayerNum;
                        //修改大厅房间的简单信息
                        String roomTips=currentGameRoom.getRoomTips();
                        rooms.removeElement(roomTips);
                        roomTips=roomName+"("+clientNum+"/"+maxNum+")"+"\t等待中";
                        rooms.addElement(roomTips);
                        currentGameRoom.setRoomTips(roomTips);
                    }
                    //如果房间被解散（房主离开房间）
                    else if(line.startsWith(Sign.RoomDismiss))
                    {
                        //该用户目前所处房间的id
                        String roomId=currentGameRoom.getId();
                        //将该房间从房间链表中删除
                        ClientPort.allServerRoom.remove(new ServerGameRoom(roomId,new Client(null,null),null));
                        //获取将要被删除的房间的信息
                        String roomTips=currentGameRoom.getRoomTips();
                        rooms.removeElement(roomTips);
                        currentClient.setRoomNull();
                        currentGameRoom.dispose();
                    }
                    //如果被房主踢出房间
                    else if(line.startsWith(Sign.BeenTicked))
                    {
                        DefaultListModel<String> clientIdInRoom=currentGameRoom.getDefaultListModel();
                        //将用户从当前房间用户id列表中删除
                        clientIdInRoom.removeElement(currentClient.getId());
                        //房间名
                        String roomName=currentGameRoom.getName();
                        //目前该房间中的人
                        int clientNum=clientIdInRoom.size();
                        //该房间最大的人数
                        int maxNum=GameRoom.maxPlayerNum;
                        //修改大厅房间的简单信息
                        String roomTips=currentGameRoom.getRoomTips();
                        rooms.removeElement(roomTips);
                        roomTips=roomName+"("+clientNum+"/"+maxNum+")"+"\t等待中";
                        rooms.addElement(roomTips);
                        currentGameRoom.setRoomTips(roomTips);
                        currentGameRoom.dispose();
                        JOptionPane.showConfirmDialog(null,"你被房主踢出了房间","提示",JOptionPane.OK_OPTION);
                    }

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
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
        currentClient.setRoomNull();
        currentGameRoom.dispose();
    }

    /**
     * 创建房间
     * @param roomMaster
     */
    public void createGameRoom(Client roomMaster){
        String roomname=null;
        ClientPort.sendStream.println(Sign.CreateRoom+roomname);
        currentGameRoom=new GameRoom(roomMaster,roomname);
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

    public static void main(String[] args) {
        Client client=new Client("1","13");
        new GameHall(client).new GameRoom(client,"123");
    }

}

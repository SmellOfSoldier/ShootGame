import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.net.BindException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

/**
 * 服务器类
 * 用于生成服务器为客户端提供联机服务
 * BY:Lijie
 */
public class creatServer {
    private JFrame frame;
    private JTextArea contentArea;
    private  JTextField txt_mes;
    private  JTextField txt_max;
    private  JTextField txt_port;
    private JButton btn_start;
    private JButton btn_stop;
    private JButton btn_send;
    private JPanel upjpanel;
    private JPanel downjpanel;
    private  JScrollPane rpane;
    private  JScrollPane lpanel;
    private  JSplitPane centerSplit;
    private  JList users;
    private ServerSocket serverSocket;//ServerSocket线程
    private ServerThread serverThread;
    private ArrayList<clientThread> playerclients;//List用于储存所有玩家服务线程
    private DefaultListModel listModel;
    private boolean isStart=false;
    public static void main(String[] args) {
        new creatServer();
    }
    /**
     * 服务端GUI界面构造区域
     */
    creatServer(){
        frame=new JFrame("游戏服务器");
        frame.add(new ServerJPanel());
        frame.setSize(600,500);
        //设置服务器在屏幕正中央
        int screen_width = Toolkit.getDefaultToolkit().getScreenSize().width;
        int screen_height = Toolkit.getDefaultToolkit().getScreenSize().height;
        frame.setLocation((screen_width - frame.getWidth()) / 2,
                (screen_height - frame.getHeight()) / 2);
        frame.setVisible(true);
        //设置关闭事件
        frame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                if(isStart) closeServer();//关闭服务器
                System.exit(0);//退出程序
            }
        });
        //TODO:设置服务器开启监听
        btn_start.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(isStart) {//如果服务器已经启动则进行警告
                    contentArea.append("服务器已经开启。"+"\r\n");
                    JOptionPane.showMessageDialog(frame, "服务器已经开启。",
                            "错误操作", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                int maxplayer=Integer.parseInt(txt_max.getText());//获取输入得最大玩家数目
                int port=Integer.parseInt(txt_port.getText());//获取输入的服务器开启监听端口
                try{
                    startServer(maxplayer,port);//尝试开启服务器
                }catch (BindException e1){
                    e1.printStackTrace();
                }
                //开启成功进行开启扫尾工作
                contentArea.append("服务器开启成功");
            }
        });
        //TODO:命令行输入确认监听
        btn_send.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String inputCommand=txt_mes.getText().trim();//获取输入的命令
                txt_mes.setText(null);//将命令行置空
                contentArea.append(inputCommand+"\r\n");//信息框里面显示输入命令

            }
        });
    }
    /**
     * @param maxPlayer 最大可连接的玩家数目
     * @param port 服务器开启的监听端口
     * @throws BindException 抛出开启失败的错误情况
     */
    public void startServer(int maxPlayer,int port) throws BindException {
        try {
            playerclients=new ArrayList<clientThread>();//创建玩家列表
            serverSocket=new ServerSocket(port);//创建服务Socket
            serverThread=new ServerThread(serverSocket,30);//创建服务器线程
            serverThread.start();//服务器线程开启
            isStart=true;
        } catch (BindException e) {
            e.printStackTrace();
            isStart=false;
            throw new BindException("端口被占用无法开启。");
        }
        catch (Exception e){
            e.printStackTrace();
            isStart=false;
            throw new BindException("服务器开启错误。");
        }
    }

    /**
     * 关闭服务器函数进行关闭和扫尾工作
     */
    public void closeServer(){
        try{
        if(serverThread!=null) serverThread.interrupt();//服务端主线程如果存在则停止服务端主线程
        System.out.println("服务端线程成功关闭。");//测试使用
        contentArea.append("服务器已经成功关闭"+"\r\n");
        for(int i=0;i<playerclients.size();i++){
            //TODO:转发给所有玩家服务端被关闭
        }
        if(serverSocket!=null) serverSocket.close();//如果serverSocket存在则关闭它
            listModel.removeAllElements();//清空用户列表
            isStart=false;//服务器状态置为关闭
        }
        catch (IOException e){
            e.printStackTrace();
            isStart=true;
        }
    }
    public void sendToOther(){
        //TODO:转发给其它玩家函数

    }
    //服务器线程
    class ServerThread extends Thread{
        private ServerSocket serverSocket;
        private int maxplayer;
        /**
         * 服务器线程构造函数
         * @param serverSocket 传入的serverSocket
         * @param maxplayer 传入的最大在线人数
         */
        public ServerThread(ServerSocket serverSocket,int maxplayer){
            this.serverSocket=serverSocket;
            this.maxplayer=maxplayer;
        }
        /**
         * 服务器线程不断循环等待客户端的连接
         */
        public void run(){
            while(!this.isInterrupted()){
                try {
                    Socket socket=serverSocket.accept();//得到一个客户端与服务器的Socket连接对象并保存
                    if(playerclients.size()==maxplayer){//当前人数已满
                        //TODO:服务器人数已满返回给现要连接的客户端信息
                        socket.close();
                        continue;//拒绝连接后跳出本次循环等待下次连接
                    }
                    clientThread aplayerClient=new clientThread(socket);//创建一个服务线程
                    playerclients.add(aplayerClient);//将此服务线程压入playerclients中保存
                    aplayerClient.start();//启动该服务线程
                    listModel.addElement(aplayerClient.getGamer().getName());//在在线用户中添加新的用户
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * 玩家服务线程
     */
    class clientThread extends Thread{
        private Gamer player;
        private Socket socket;
        /**
         * 获取此线程实例对象的Gamer
         * @return
         */
        public Gamer getGamer(){
            return player;
        }
        /**
         * 玩家服务线程构造
         * @param socket 属于实例对象(一位玩家)的Socket通道
         */
        public clientThread(Socket socket){
            this.socket=socket;
            //TODO:服务线程构造函数待完成部分
            System.out.println("服务器成功建立与"+player.getName()+"的连接。");
        }
        public void run(){
            //TODO:服务线程run待完成
        }
    }

    /**
     * ServerJPanel统一管理服务器界面布局
     */
    class ServerJPanel extends JPanel{
        ServerJPanel(){
            initial();
        }
        private  void initial(){
            contentArea= new JTextArea();
            //设置为不可输入
            contentArea.setEditable(false);
            txt_max=new JTextField("20");
            txt_mes=new JTextField();
            txt_port=new JTextField("10086");
            btn_send=new JButton("输入");
            btn_start=new JButton("开启");
            btn_stop=new JButton("停止");
            listModel = new DefaultListModel();
            users = new JList(listModel);
            lpanel=new JScrollPane(users);
            lpanel.setBorder(new TitledBorder("在线玩家"));
            rpane=new JScrollPane(contentArea);
            rpane.setBorder(new TitledBorder("服务器运行信息"));
            centerSplit=new JSplitPane(JSplitPane.HORIZONTAL_SPLIT,lpanel,rpane);
            centerSplit.setDividerLocation(100);
            downjpanel=new JPanel(new BorderLayout());
            downjpanel.setBorder(new TitledBorder("命令输入"));
            downjpanel.add(txt_mes, "Center");
            downjpanel.add(btn_send,"East");
            upjpanel=new JPanel();
            upjpanel.setLayout(new GridLayout(1,6));
            upjpanel.add(new JLabel("最大玩家数"));
            upjpanel.add(txt_max);
            upjpanel.add(new JLabel("端口"));
            upjpanel.add(txt_port);
            upjpanel.add(btn_start);
            upjpanel.add(btn_stop);
            upjpanel.setBorder(new TitledBorder("服务器参数"));
            this.setLayout(new BorderLayout());
            this.add(upjpanel,"North");
            this.add(downjpanel,"South");
            this.add(centerSplit,"Center");
        }
    }
}

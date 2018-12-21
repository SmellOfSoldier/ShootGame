import javax.swing.*;
import java.io.IOException;
import java.net.BindException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

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
    private creatServer(){

    }

    /**
     *
     * @param maxPlayer 最大可连接的玩家数目
     * @param port 服务器开启的监听端口
     * @throws BindException 抛出开启失败的错误情况
     */
    public void startServer(int maxPlayer,int port) throws BindException {
        playerclients=new ArrayList<clientThread>();
        try {
            serverSocket=new ServerSocket(port);
            serverThread=new ServerThread(serverSocket,30);
            serverThread.start();
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

    public void closeServer(){
        try{
        if(serverThread!=null) serverThread.interrupt();//服务端主线程如果存在则停止服务端主线程
        System.out.println("服务端线程成功关闭。");
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
        public ServerThread(ServerSocket serverSocket,int maxplayer){
            this.serverSocket=serverSocket;
            this.maxplayer=maxplayer;
        }
        public void run(){
            while(!this.isInterrupted()){
                try {
                    Socket socket=serverSocket.accept();
                    
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
    //服务线程每个玩家分配一个
    class clientThread extends Thread{
        private Gamer player;
        private Socket socket;
        public clientThread(Socket socket){
            this.socket=socket;
            //TODO:服务线程构造函数待完成部分
            System.out.println("服务器成功建立与"+player.getName()+"的连接。");
        }
        public void run(){
            //TODO:服务线程run待完成
        }
    }
}

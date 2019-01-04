import javax.swing.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;

//服务器线程
class ClientThreadCreator extends Thread{
    private ServerSocket serverSocket;
    private int maxplayer;
    private JTextArea GuiShowMes;
    /**
     * 服务器线程构造函数
     * @param serverSocket 传入的serverSocket
     * @param maxplayer 传入的最大在线人数
     */
    public ClientThreadCreator(ServerSocket serverSocket, int maxplayer, JTextArea GuiShowMes){
        this.serverSocket=serverSocket;
        this.maxplayer=maxplayer;
        this.GuiShowMes=GuiShowMes;
        System.out.println("服务器线程已经创建。" );//测试
    }
    /**
     * 服务器线程不断循环等待客户端的连接
     */
    public void run(){
        while(!this.isInterrupted()){
            try {
                Socket socket=serverSocket.accept();//得到一个客户端与服务器的Socket连接对象并保存
                PrintStream sendStream=new PrintStream(socket.getOutputStream());//获取写出流
                BufferedReader getStream=new BufferedReader(new InputStreamReader(socket.getInputStream()));//获取写入流

                System.out.println("尝试连接成功消息");
                sendStream.println(Sign.SuccessConnected);//返回给尝试连接的客户端成功信息
                sendStream.flush();
                ClientThread aplayerClient=new ClientThread(socket,sendStream,getStream,GuiShowMes);//创建一个服务线程
                aplayerClient.setisConnected(true);//设置此玩家服务线程连接状态为true
                aplayerClient.start();//启动该服务线程
                System.out.println("成功建立一个玩家连接。");
            }
            catch (SocketException se){
            }
            catch (IOException IOE){
                AllInfo.saveAllClientInfo(StartServer.allPlayer);//保存所有注册用户信息到文件
                IOE.printStackTrace();
            }
        }
    }
}
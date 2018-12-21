import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.ServerSocket;
import java.net.Socket;

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
                /**
                 * 判断服务器是否已经满人
                 */
                if(creatServer.playerclientThreads.size()==maxplayer){//当前人数已满
                    //TODO:服务器人数已满返回给现要连接的客户端信息
                    socket.close();
                    continue;//拒绝连接后跳出本次循环等待下次连接
                }
                System.out.println("尝试连接成功消息");
                sendStream.println(Sign.SuccessConnected);//返回给尝试连接的客户端成功信息
                sendStream.flush();
                clientThread aplayerClient=new clientThread(socket,sendStream,getStream);//创建一个服务线程
                creatServer.playerclientThreads.add(aplayerClient);//将此服务线程压入playerclientThreads中保存
                aplayerClient.start();//启动该服务线程
                System.out.println("成功建立一个玩家连接。");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
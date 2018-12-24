package Client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintStream;
import java.net.Socket;

/**
 * 创建游戏房间时之后所有于服务器之间进行关于房间信息和游戏中的数据
 * 传输都分配给该线程的实例对象完成
 */
public class ClientThread extends  Thread {
    private Socket socket;
    private PrintStream sendstream;
    private BufferedReader getstream;
    ClientThread(Socket socket,PrintStream sendstream,BufferedReader getstream){
        this.socket=socket;
        this.sendstream=sendstream;
        this.getstream=getstream;
    }
    public void run(){
        String line=null;//接收到的初始字符串（信息）
        String command = null;//当前获取的信息需要执行的命令
        String realMessage = null;//去除头部命令的信息
        while (!this.isInterrupted()){
            try {
                line=getstream.readLine();
                System.out.println("客户端收到来自服务器的消息"+line);

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     *
     * @return
     */
    public boolean stopThisThread(){
        this.interrupt();
        return true;
    }
}

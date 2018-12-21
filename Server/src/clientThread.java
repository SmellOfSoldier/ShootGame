import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.Socket;

/**
 * 玩家服务线程
 */
class clientThread extends Thread{
    private Gamer player;
    private Socket socket;
    private PrintStream sendStream;
    private BufferedReader getStream;
    private boolean isLogin=false;//是否登陆
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
    public clientThread(Socket socket,PrintStream sendStream,BufferedReader getStream){
        this.socket=socket;
        this.sendStream=sendStream;//获取写出流
        this.getStream=getStream;//获取写入流
    }

    /**
     * 玩家服务线程run函数
     */
    public void run(){
        //线程不被interrupted则持续接收玩家发来的信息
        while(!this.isInterrupted()){
        //TODO:服务线程run待完成
        if(!isLogin){
            try {
                boolean successLogin=check.checkLoginInfo(getStream);
                if(successLogin) isLogin=true;//密码成功则将当前玩家的服务线程登陆置为true
                else stopThisClient(Sign.WrongPassword,sendStream,getStream);//否则停止服务线程
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        
        }
    }
    /**
     * 停止当前服务线程实例对象的运行并进行扫尾工作
     * @param reson 停止（拒绝连接或被T除的原因)
     * @param sendStream 获取输出流以回复客户端消息和扫尾停止
     * @param getStream 获取输入流进行扫尾停止
     */
    private void stopThisClient(String reson,PrintStream sendStream,BufferedReader getStream) throws IOException {
                //发送拒绝登陆消息和停止线程
                sendStream.print(reson);
                //扫尾工作
                sendStream.flush();
                sendStream.close();
                getStream.close();
                this.interrupt();//停止玩家服务线程
    }
}

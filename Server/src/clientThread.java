import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.Socket;

/**
 * 玩家服务线程
 */
class clientThread extends Thread{
    private Player player;
    private Socket socket;
    private PrintStream sendStream;
    private BufferedReader getStream;
    private boolean isLogin=false;//是否登陆
    /**
     * 获取此线程实例对象的Gamer
     * @return
     */
    public Player getPlayer(){
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
        System.out.println("成功创建一个玩家服务线程");
    }

    /**
     * 玩家服务线程run函数
     */
    public void run(){
        String line=null;//接收到的初始字符串（信息）
        String command = null;//当前获取的信息需要执行的命令
        String realMessage = null;//去除头部命令的信息
        //线程不被interrupted则持续接收玩家发来的信息
        while(!this.isInterrupted()) {
            try {
                //TODO:服务线程run待完成
                line = getStream.readLine();//线程堵塞  读取发来的消息
                /**
                 * 如果是登陆则采取如下操作
                 */
                if (!isLogin && line.startsWith(Sign.Login)) {
                    try {
                        int loginResult = check.checkLoginInfo(line);
                        if (loginResult == 1) {
                            isLogin = true;//密码成功则将当前玩家的服务线程登陆置为true
                            player=check.creatPlayer(line);//根据接收到的登陆信息建立player对象
                        }
                        else if (loginResult == -1) sendCommand(Sign.IsNotRegistered);//返回账号还未注册的消息
                        else sendCommand(Sign.WrongPassword);//返回密码错误的消息
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                //TODO:玩家服务线程待完成
                else if(line.startsWith("TODO"));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 发送命令函数
     * @param command 发送的命令
     */
    private void sendCommand(String command){
        sendStream.print(command);
        sendStream.flush();
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

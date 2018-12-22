import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintStream;
import java.net.Socket;

/**
 * 玩家服务线程
 */
class clientThread extends Thread{
    private Client player;
    private Socket socket;
    private PrintStream sendStream;
    private BufferedReader getStream;
    private boolean isConnected=false;//是否连接
    private boolean isLogin=false;//是否登陆

    /**
     * 获取此线程实例对象的Gamer
     * @return
     */
    public Client getPlayer(){
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
            if (isConnected) {
                try {
                    //TODO:服务线程run待完成
                    System.out.println("线程阻塞中等待命令。");
                    line = getStream.readLine();//线程堵塞  读取发来的消息
                    System.out.println("收到一个命令信息"+line);
                    /**
                     * 如果是登陆则采取如下操作
                     */
                    if (!isLogin && line.startsWith(Sign.Login)) {
                        try {
                            System.out.println("进度登陆函数");
                            int loginResult = check.checkLoginInfo(line);
                            System.out.println("登陆结果为"+loginResult);//1为成功 -1为账号未注册  为密码错误
                            switch (loginResult){
                                case 1:{
                                    isLogin = true;//密码成功则将当前玩家的服务线程登陆置为true
                                    player = check.creatPlayer(line);//创建依据line的player对象
                                    sendCommand(Sign.LoginSuccess);//发送成功登陆的信息
                                    break;
                                }
                                case -1:{
                                    sendCommand(Sign.IsNotRegistered);//返回账号还未注册的消息
                                    break;
                                }
                                case 0:{
                                    sendCommand(Sign.WrongPassword);//返回密码错误的消息
                                    break;
                                }
                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                    /**
                     * 如果接收到注册请求
                     */
                    else if (!isLogin && line.startsWith(Sign.Register)) {
                        System.out.println("收到注册请求开始注册流程。");
                        //分割命令与内容
                        command = Sign.Register;
                        realMessage = check.getRealMessage(line, Sign.Register);
                        String playerid = realMessage.split(Sign.SplitSign)[0];
                        String playerPassword = realMessage.split(Sign.SplitSign)[1];
                        if(!check.isRegistered(playerid)){
                             saveorreadInfo.savePlayerInfo(new Client(playerid, playerPassword));//注册一个Player并保存到文件
                             sendCommand(Sign.RegisterSuccess);//返回注册成功信息
                        }
                        else sendCommand(Sign.IsRegistered);//否则返回已经注册过的消息
                    }

                    /**
                     * 如果收到注销请求(玩家返回到登陆界面)
                     */
                    else if (isLogin&&line.startsWith(Sign.Logout)){

                    }
                    /**
                     * 如果收到断开连接请求（返回到单人与多人游戏选择界面)
                     */
                    else if(isLogin&&line.startsWith(Sign.Disconnect)){
                        stopThisClient(Sign.SuccessDisconnected,sendStream,getStream);//关闭此服务线程 tips:原因：玩家请求断开连接
                    }
                    //TODO:待完成的玩家服务线程
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     *
     * @param flag
     */
    public void setisConnected(boolean flag){
        isConnected=flag;
    }

    /**
     * 发送命令函数
     * @param command 发送的命令
     */
    public void sendCommand(String command){
        sendStream.println(command);
        sendStream.flush();
    }

    /**
     * 用于注销当前服务线程服务的玩家账号
     * //TODO:
     */
    public void LogoutPlayer(){

    }
    /**
     * 停止当前服务线程实例对象的运行并进行扫尾工作
     * @param reson 停止（拒绝连接或被T除的原因)
     * @param sendStream 获取输出流以回复客户端消息和扫尾停止
     * @param getStream 获取输入流进行扫尾停止
     */
    private void stopThisClient(String reson,PrintStream sendStream,BufferedReader getStream) throws IOException {
                //发送拒绝登陆消息和停止线程
                sendStream.println(reson);
                //扫尾工作
                sendStream.flush();
                sendStream.close();
                getStream.close();
                this.interrupt();//停止玩家服务线程
    }
}

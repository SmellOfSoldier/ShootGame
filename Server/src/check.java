import java.io.BufferedReader;
import java.io.IOException;

/**
 * check类用于集合所有检查信息的格式与内容的函数
 */
public class check {
    //private BufferedReader getStream;

    /**
     * 登陆信息检查函数
     * @param line 登陆信息
     * @return 检查无误返回1 未注册返回 -1   否则返回0
     */
    public static int checkLoginInfo(String line) throws IOException {
        //解析出包含的玩家id和发送过来的登陆密码
        String realMessage=getRealMessage(line,Sign.Login);
        String playerid=realMessage.split(Sign.SplitSign)[0];
        String password=realMessage.split(Sign.SplitSign)[1];
        //TODO：密码判定函数
        if(!isRegistered(playerid)) return -1;
        else if(checkPassword(playerid,password)) return 1;
        else return 0;
    }

    /**
     * 通过登陆的line信息创建一个player
     * @param secondline  第二次信息（接收到的玩家的登陆的账号密码信息）
     * @return 返回一个Playe实例对象
     */
    public static Player creatPlayer(String secondline){
        String realMessage=getRealMessage(secondline,Sign.Login);
        String playerid=realMessage.split(Sign.SplitSign)[0];
        String password=realMessage.split(Sign.SplitSign)[1];
        return  new Player(playerid,password);
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
     * 检查ID是否被注册过
     * @param id 玩家发送过来的玩家ID
     * @return 注册过返回 true 否则返回false
     */
    private static boolean isRegistered(String id){
        //TODO:检查是否已经注册bylijie
        return true;
    }

    /**
     * 密码检查函数判断玩家密码是否正确
     * @param id 玩家id
     * @param password 玩家密码
     * @return 密码正确返回true 否则返回false
     */
    private static boolean checkPassword(String id,String password){
        //TODO:密码检查函数bylijie
        return true;
    }


}

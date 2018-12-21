import java.io.BufferedReader;
import java.io.IOException;

/**
 * check类用于集合所有检查信息的格式与内容的函数
 */
public class check {
    //private BufferedReader getStream;

    /**
     * 登陆信息检查函数
     * @param getStream 获取客户端到服务端的输入流
     * @return 检查无误返回true 否则返回false
     */
    public static boolean checkLoginInfo(BufferedReader getStream) throws IOException {
        try {
            String LoginLine=getStream.readLine();//读取一条登陆信息
            if(LoginLine.startsWith(Sign.login)){
                //去掉开头的登陆命令串
                String LoginInfo=getRealMessage(LoginLine,Sign.login);
                //分割出登陆者id和密码
                String playerid=LoginInfo.split(Sign.SplitSign)[0];
                String password=LoginInfo.split(Sign.SplitSign)[1];
                //TODO：密码判定函数
                if(checkPassword(playerid,password)) return true;
                else return false;
            }
            else return false;
        } catch (IOException e) {
            e.printStackTrace();
            throw new IOException("读取的此字符串并非登陆信息流串，函数使用位置可能有错。");
        }
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
     * 密码检查函数判断玩家密码是否正确
     * @param id 玩家id
     * @param password 玩家密码
     * @return 密码正确返回true 否则返回false
     */
    private static boolean checkPassword(String id,String password){
        //TODO:密码检查函数
        return true;
    }
}

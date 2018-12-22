import java.io.Serializable;

/**
 * 游戏玩家类
 */
public class Player implements Serializable {
    private String id;
    private String password;
    private boolean isOnline=false;
    /**
     * Player构造函数
     * @param id 玩家ID（唯一标识）
     * @param password 玩家账号密码
     */
    public  Player(String id,String password) {
        this.id = id;
        this.password = password;
    }

    /**
     * 获取Player实例对象ID
     * @return 返回Player实例对象ID
     */
    public String getId(){
        return id;
    }

    /**
     * 密码比对函数
     * @param password 需要对比的密码
     * @return 密码正确返回true 否则返回false
     */
    public boolean passwordEquals(String password){
        if(this.password.equals(password)) return true;
        else return false;
    }

    /**
     * 设置是否在线
     * @param flag true在线 反之
     */
    public void setOline(boolean flag){
        isOnline=flag;
    }

    /**
     * 检查是否在线
     * @return true在线 反之
     */
    public boolean isOline(){
        return isOnline;
    }
}

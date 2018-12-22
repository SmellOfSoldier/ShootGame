import view.GameRoom;

import java.io.Serializable;

/**
 * 游戏玩家(客户)类
 */
public class Client implements Serializable {
    private String id;
    private String password;
    private boolean isOnline=false;
    private  boolean isPlaying=false;
    private GameRoom room;
    /**
     * Player构造函数
     * @param id 玩家ID（唯一标识）
     * @param password 玩家账号密码
     */
    public Client(String id, String password) {
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
     * 设置玩家所在房间
     * @param gameRoom 房间对象
     */
    public void setGameRoom(GameRoom gameRoom){
        this.room=gameRoom;
    }
    /**
     * 设置是否在线
     * @param flag true在线 反之
     */
    public void setOline(boolean flag){
        isOnline=flag;
    }

    /**
     * 设置是否在游戏中（房间中）
     * @param flag true游戏中 反之
     */
    public void setPlaying(boolean flag){
        isPlaying=flag;
    }
    /**
     * 检查是否在线
     * @return true在线 反之
     */
    public boolean isOline(){
        return isOnline;
    }

    /**
     * 检查是否在游戏中（房间中）
     * @return
     */
    public boolean isPlaying(){
        return isPlaying;
    }

    /**
     * 返回房间实例对象
     * @return 房间实例索引
     */
    public GameRoom getRoom(){
        return room;
    }
}

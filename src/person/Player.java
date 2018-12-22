package person;

import java.io.Serializable;

/**
 * 游戏玩家
 */
public class Player extends Person implements Serializable
{
    private int lSpeed=0;                           //人物每次左边方向移动的像素
    private int rSpeed=0;                           //人物每次右边方向移动的像素
    private int uSpeed=0;                           //人物每次上边方向移动的像素
    private int dSpeed=0;                           //人物每次下边方向移动的像素
    private String id;
    private String password;
    private boolean isOnline=false;
    public Player(int id,String name,int healthPoint)
    {
        super(id,name,healthPoint,10,20);
    }
    public Player(){}
    public void setlSpeed(int lSpeed){this.lSpeed=lSpeed;}
    public void setrSpeed(int rSpeed){this.rSpeed=rSpeed;}
    public void setuSpeed(int uSpeed){this.uSpeed=uSpeed;}
    public void setdSpeed(int dSpeed){this.dSpeed=dSpeed;}
    public int getlSpeed(){return lSpeed;}
    public int getrSpeed(){return rSpeed;}
    public int getuSpeed(){return uSpeed;}
    public int getdSpeed(){return dSpeed;}
    public void stop(){lSpeed=0;rSpeed=0;uSpeed=0;dSpeed=0;}    //人物停止运动

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

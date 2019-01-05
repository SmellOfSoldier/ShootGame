package person;


import Weapon.*;
import utils.MusicPlayer;
import view.SinglePersonModel;

import javax.swing.*;
import java.awt.*;
import java.io.Serializable;

/**
 * 游戏玩家
 */
public class Player extends Person implements Serializable
{
    public static final int maxHealthPoint=1000;    //最大生命值
    private int lSpeed=0;                           //人物每次左边方向移动的像素
    private int rSpeed=0;                           //人物每次右边方向移动的像素
    private int uSpeed=0;                           //人物每次上边方向移动的像素
    private int dSpeed=0;                           //人物每次下边方向移动的像素
    private String clientId=null;                   //玩家的用户id
    public Player(String id,String name,String clientId)
    {
        super(id,name,maxHealthPoint,10,20);
        this.clientId=clientId;
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
    public String getClientId(){return clientId;}

    /**
     * 重写父类的方法,增添修改屏幕上显示击杀/死亡数目的功能
     * @param killNum
     */
    public void addKillNum(int killNum)
    {
        super.addKillNum(killNum);
    }
    public void addDieNum(int dieNum)
    {
        super.addDieNum(dieNum);
    }

    /**
     * 重写父类的方法，增添修改屏幕上显示血条的功能
     * @param die
     */
    public void setDie(boolean die)
    {
        super.setDie(die);
        if(die)
        {
            MusicPlayer.playDieMusic();     //播放死亡音效
            addDieNum(1);
        }
    }

    /**
     * 重写父类的方法，增添播放被攻击的时候人物音效
     * @param hp
     */
    public void reduceHealthPoint(int hp)
    {
        MusicPlayer.playBeenHitMusic();
        super.reduceHealthPoint(hp);
    }
    public int hashCode()
    {
        return Integer.parseInt(id);
    }
    public void addHealthPoint(int hp)
    {
        int healthPoint =getHealthPoint();
        if(healthPoint+hp>maxHealthPoint)
        {
            super.setHealthPoint(hp);
        }
        else
        {
            super.addHealthPoint(hp);
        }
    }
}
package person;

import view.GameFrame;

import javax.swing.*;
import java.io.Serializable;

/**
 * 游戏玩家
 */
public class Player extends Person implements Serializable
{
    public static final int maxHealthPoint=10000;
    private int lSpeed=0;                           //人物每次左边方向移动的像素
    private int rSpeed=0;                           //人物每次右边方向移动的像素
    private int uSpeed=0;                           //人物每次上边方向移动的像素
    private int dSpeed=0;                           //人物每次下边方向移动的像素
    public Player(int id,String name,int healthPoint)
    {
        super(id,name,healthPoint,10,20);
        GameFrame.healthLevel.setValue(healthPoint);
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
}

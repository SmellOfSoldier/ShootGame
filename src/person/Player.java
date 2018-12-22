package person;

import view.SinglePersonModel;

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
    private int killNum=0;                          //击杀数
    private int dieNum=0;                           //死亡数
    public Player(int id,String name)
    {
        super(id,name,maxHealthPoint,10,20);
        SinglePersonModel.healthLevel.setValue(maxHealthPoint);
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
    public int getKillNum(){return killNum;}
    public int getDieNum(){return dieNum;}
    //重写父类的设置死亡
    public void setDie(boolean die)
    {
        if (die) {
            super.setDie(true);
            dieNum++;
        }
    }
    public void stop(){lSpeed=0;rSpeed=0;uSpeed=0;dSpeed=0;}    //人物停止运动
}

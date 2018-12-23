package person;


import Weapon.*;
import utils.MusicPlayer;
import view.GameFrame;

import javax.swing.*;
import java.awt.*;
import java.io.Serializable;

/**
 * 游戏玩家
 */
public class Player extends Person implements Serializable
{
    public static final int maxHealthPoint=5000;    //最大生命值
    private int lSpeed=0;                           //人物每次左边方向移动的像素
    private int rSpeed=0;                           //人物每次右边方向移动的像素
    private int uSpeed=0;                           //人物每次上边方向移动的像素
    private int dSpeed=0;                           //人物每次下边方向移动的像素
    public Player(String id,String name)
    {
        super(id,name,maxHealthPoint,10,20);
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

    /**
     * 重写父类的方法,增添修改屏幕上显示击杀/死亡数目的功能
     * @param killNum
     */
    public void addKillNum(int killNum)
    {
        super.addKillNum(killNum);
        GameFrame.killAndDieField.setText("击杀/死亡："+getKillNum()+"/"+getDieNum());
    }
    public void setDieNum(int dieNum)
    {
        super.setDieNum(dieNum);
        GameFrame.killAndDieField.setText("击杀/死亡："+getKillNum()+"/"+getDieNum());
    }

    /**
     * 重写父类的方法，增添修改屏幕上显示血条的功能
     * @param die
     */
    public void setDie(boolean die)
    {
        MusicPlayer.playDieMusic();     //播放死亡音效
        if (die) {
            super.setDie(true);
            setDieNum(1);
            GameFrame.healthLevel.setValue(0);
        }
    }

    /**
     * 重写父类的方法，增添修改屏幕上显示子弹数目的功能
     * @param type
     */
    public void changeWeapon(int type, JPanel gameArea)                  //切换武器
    {
        super.changeWeapon(type,gameArea);
        Weapon weapon=getUsingWeapon();
        //如果是枪类武器
        if(weapon instanceof Gun)
        {
            int bulletLeftInGun=((Gun)weapon).getBulletLeft();
            int bulletLeftOnPerson=getBulletLeftOnPerson();
            GameFrame.bulletLeft.setText("子弹："+bulletLeftInGun+"/"+bulletLeftOnPerson);
        }
        else
        {
            int left=getBulletLeftOnPerson();
            GameFrame.bulletLeft.setText("子弹："+left);
        }
        Point point=GameFrame.flagPoint[getUsingWeaponType()-1];
        GameFrame.usingWeaponFlag.setLocation(point);
        gameArea.repaint();
    }
    /**
     * 重写Person的reLoad方法，增添修改显示子弹的bulletLeft这个JTextField功能
     */
    public void reLoad()
    {
        super.reLoad();
        Weapon weapon=getUsingWeapon();
        //如果是枪类武器
        if(weapon instanceof Gun)
        {
            int bulletLeftInGun=((Gun)weapon).getBulletLeft();
            int bulletLeftOnPerson=getBulletLeftOnPerson();
            GameFrame.bulletLeft.setText("子弹："+bulletLeftInGun+"/"+bulletLeftOnPerson);
        }
        else
        {
            int left=getBulletLeftOnPerson();
            GameFrame.bulletLeft.setText("子弹："+left);
        }
    }
    public void reduceHealthPoint(int hp)
    {
        MusicPlayer.playBeenHitMusic();
        super.reduceHealthPoint(hp);
    }
}

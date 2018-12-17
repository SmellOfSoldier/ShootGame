package person;

import javax.swing.*;
import java.io.Serializable;

import Weapon.*;
import utils.MusicPlayer;

/**
 * 游戏人物
 */
public class Person extends JLabel implements Serializable
{
    private boolean isReload=false;                 //人物是否正在装子弹
    private int lSpeed=0;                           //人物每次左边方向移动的像素
    private int rSpeed=0;                           //人物每次右边方向移动的像素
    private int uSpeed=0;                           //人物每次上边方向移动的像素
    private int dSpeed=0;                           //人物每次下边方向移动的像素
    private int speed;                      //人物的移动速度
    protected int id;                       //编号
    protected String name;                  //姓名
    private int usingWeaponType=3;              //目前使用的武器的种类
    private int healthPoint;                //生命值
    private   int radius;                   //人物的半径
    private int [] bulletNum=new int[Weapon.weaponsTypeNum];      //武器中子弹的数目
    private Weapon []weapons=new Weapon[Weapon.weaponsTypeNum];     //人物持有的武器
    protected Person(){}
    protected Person(int id,String name,int healthPoint,int radius,int speed)
    {
        this.name=name;
        this.id=id;
        this.healthPoint=healthPoint;
        this.radius=radius;
        this.speed=speed;
    }
    public void setlSpeed(int lSpeed){this.lSpeed=lSpeed;}
    public void setrSpeed(int rSpeed){this.rSpeed=rSpeed;}
    public void setuSpeed(int uSpeed){this.uSpeed=uSpeed;}
    public void setdSpeed(int dSpeed){this.dSpeed=dSpeed;}
    public int getlSpeed(){return lSpeed;}
    public int getrSpeed(){return rSpeed;}
    public int getuSpeed(){return uSpeed;}
    public int getdSpeed(){return dSpeed;}
    public void stop(){lSpeed=0;rSpeed=0;uSpeed=0;dSpeed=0;}    //人物停止运动
    public int getHealthPoint(){return healthPoint;}       //获取人物当前血量
    public int getId(){return id;}                          //获取人物编号
    public int getRadius(){return radius;}                  //获取人物半径
    public String getName(){return name;}                   //获取人物名称
    public int getSpeed(){return speed;}                    //获取人物的速度
    public void reLoad()            //装填子弹
    {
            MusicPlayer.playReloadMusic(WeaponType.automaticRifle);
            isReload = true;
            Gun gun = (Gun) weapons[usingWeaponType];
            int bulletLeft = gun.getBulletLeft();
            int addBullet;          //预计加装的子弹量
            int maxBulletNum = 0;       //弹夹中最大子弹量
            switch (usingWeaponType) {
                case WeaponType.automaticRifle:
                    maxBulletNum = AutomaticRifle.maxBulletNum;
                    break;
                case WeaponType.sniperRifle:
                    maxBulletNum = SniperRifle.maxBulletNum;
                    break;
                case WeaponType.pistol:
                    maxBulletNum = Pistol.maxBulletNum;
                    break;
            }
            addBullet = maxBulletNum - bulletLeft;

            if (bulletNum[usingWeaponType] < addBullet)        //如果人物携带的子弹量小于预计加装的子弹量
            {
                addBullet = bulletNum[usingWeaponType];
            }
            gun.addBulletNum(addBullet);
            bulletNum[usingWeaponType] -= addBullet;
            new Thread(new Runnable() {
                @Override
                public void run()
                {
                    try
                    {
                        Thread.currentThread().sleep(2000);
                    }
                    catch (Exception ex)
                    {
                        ex.printStackTrace();
                    }
                    isReload=false;
                }
            }).start();
    }
    public boolean ifReloading()
    {
        return isReload;
    }
    public void addHealthPoint(int hp)                      //给人物加血
    {
        if(hp+healthPoint>100)
        {
            healthPoint=100;
        }
        else
        {
            healthPoint+=hp;
        }
    }
    public void reduceHealthPoint(int hp)               //给人物扣血
    {
        if(hp+healthPoint<0)
        {
            healthPoint=0;
        }
        else
        {
            healthPoint-=hp;
        }
    }
    public void dicardWeapon(int type)              //丢弃武器,type为要丢弃武器的种类
    {
        weapons[type]=null;
    }
    public void peekWeapon(Weapon weapon,int bn)                //捡起武器
    {
        int type=weapon.getType();
        weapons[type]=weapon;
        bulletNum[type]=bn;
    }
    public boolean ifHaveWeapon(int type)               //判断是否拥有该武器
    {
        if(weapons[type]==null)
            return false;
        return true;
    }
    public void changeWeapon(int type)                  //切换武器
    {
        usingWeaponType=type;
    }
    public int getUsingWeaponType(){return usingWeaponType;}    //获取当前使用的武器类型
    public Weapon getUsingWeapon(){return weapons[usingWeaponType];}     //获取当前使用的武器
}


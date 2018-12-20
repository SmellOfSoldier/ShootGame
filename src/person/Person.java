package person;

import javax.swing.*;
import java.io.Serializable;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import Weapon.*;
import utils.MusicPlayer;

/**
 * 游戏人物
 */
public class Person extends JLabel implements Serializable
{
    private Lock reLoadLock=new ReentrantLock();    //装子弹的锁
    private boolean isReload=false;                 //人物是否正在装子弹
    private int speed;                      //人物的移动速度
    protected int id;                       //编号
    protected String name;                  //姓名
    private int usingWeaponType=3;              //目前使用的武器的种类
    private int healthPoint;                //生命值
    private   int radius;                   //人物的半径
    private int [] bulletNum=new int[Weapon.weaponsTypeNum];      //武器中子弹的数目
    private Weapon []weapons=new Weapon[Weapon.weaponsTypeNum+1];     //人物持有的武器
    protected Person(){}
    protected Person(int id,String name,int healthPoint,int radius,int speed)
    {
        this.name=name;
        this.id=id;
        this.healthPoint=healthPoint;
        this.radius=radius;
        this.speed=speed;
    }
    public int getHealthPoint(){return healthPoint;}       //获取人物当前血量
    public int getId(){return id;}                          //获取人物编号
    public int getRadius(){return radius;}                  //获取人物半径
    public String getName(){return name;}                   //获取人物名称www
    public  void  reLoad()            //装填子弹
    {
        //如果已经在装填中
        if(isReload)
            return;
            //如果没有子弹可以装填
            if(bulletNum[usingWeaponType]==0)
            {
                MusicPlayer.playBulletUseOutMusic();
                return;
            }
            MusicPlayer.playReloadMusic(weapons[usingWeaponType].getWeaponName());
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
            //这个线程用于计算换弹夹的时间
            new Thread(new Runnable() {
                @Override
                public void run()
                {
                    try
                    {
                        Thread.currentThread().sleep(gun.getReloadTime());
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
        bulletNum[type]+=bn;
        usingWeaponType=type;
    }
    public boolean ifHaveWeapon(int type)               //判断是否拥有该武器
    {
        if(weapons[type]==null)
            return false;
        return true;
    }
    public void changeWeapon(int type)                  //切换武器
    {
        if(type!=usingWeaponType)
        {
            if (weapons[type] != null)    //如果这把武器存在
            {//TODO:
                usingWeaponType = type;
                MusicPlayer.playChangeWeaponMusic(weapons[type].getWeaponName());
            } else                       //不存在
            {
                MusicPlayer.playBulletUseOutMusic();
            }
        }
    }
    public int getUsingWeaponType(){return usingWeaponType;}    //获取当前使用的武器类型
    public Weapon getUsingWeapon(){return weapons[usingWeaponType];}     //获取当前使用的武器
    public int getSpeed(){return speed;}
}


package person;

import javax.swing.*;
import java.awt.*;
import java.io.Serializable;
import java.net.URL;

import Weapon.*;
import utils.MusicPlayer;
import view.SinglePersonModel;

/**
 * 游戏人物
 */
public class Person extends JLabel implements Serializable
{
    private boolean isAttacking=false;              //人物是否正在攻击
    private boolean isReload=false;                 //人物是否正在装子弹
    private int speed;                      //人物的移动速度
    protected String id;                       //编号
    protected String name;                  //姓名
    private int usingWeaponType;              //目前使用的武器的种类
    private int healthPoint;                //生命值
    private   int radius;                   //人物的半径
    private int killNum=0;                          //击杀数
    private int dieNum=0;                           //死亡数
    private int [] bulletNum=new int[WeaponType.typeNum+1];      //武器中子弹的数目
    private Weapon []weapons=new Weapon[WeaponType.typeNum+1];     //人物持有的武器
    private boolean isDie=false;        //是否死亡
    private JLabel dieSpecialEffect=new JLabel();   //死亡特效
    protected Person(){}
    protected Person(String id,String name,int healthPoint,int radius,int speed)
    {
        this.name=name;
        this.id=id;
        this.healthPoint=healthPoint;
        this.radius=radius;
        this.speed=speed;
        URL url=Person.class.getResource("/images/specialEffect/blood.png");
        ImageIcon icon=new ImageIcon(url);
        icon.setImage(icon.getImage().getScaledInstance(30,30,Image.SCALE_DEFAULT));
        dieSpecialEffect.setIcon(icon);
    }
    public int getHealthPoint(){return healthPoint;}       //获取人物当前血量
    public String getId(){return id;}                          //获取人物编号
    public int getRadius(){return radius;}                  //获取人物半径
    public String getName(){return name;}                   //获取人物名称www
    public boolean ifDie(){return isDie;}                   //电脑是否死亡
    public boolean isAttacking(){return isAttacking;}       //电脑是否正在攻击
    public void setAttacking(boolean isAttacking){this.isAttacking=isAttacking;}        //设置人物攻击状态
    public int getKillNum(){return killNum;}
    public int getDieNum(){return dieNum;}
    public void addKillNum(int killNum)
    {
        this.killNum+=killNum;
    }
    public void addDieNum(int dieNum)
    {
        this.dieNum+=dieNum;
    }
    public void setDie(boolean isDie)                       //设置人物的死亡状态
    {
        if(isDie)
        {
            healthPoint=0;
        }
        this.isDie=isDie;
    }

    /**
     * 播放人物死亡特效
     * @param gameArea
     */
    public void dieSpecialEffect(JPanel gameArea)
    {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    dieSpecialEffect.setSize(30,30);
                    dieSpecialEffect.setLocation(Person.this.getLocation());
                    gameArea.add(dieSpecialEffect);
                    Thread.sleep(6000);
                    gameArea.remove(dieSpecialEffect);
                    gameArea.repaint();
                }
                catch(Exception ex)
                {
                    ex.printStackTrace();
                }

            }
        }).start();
    }

    /**
     * 人物状态子弹
     */
    public  void  reLoad()            //装填子弹
    {
        Gun gun = (Gun) weapons[usingWeaponType];
        //如果已经在装填中，或子弹是满的
        if(isReload || gun.getBulletLeft()==gun.getMaxBulletNum())
            return;
            //如果没有子弹可以装填
            if(bulletNum[usingWeaponType]==0)
            {
                MusicPlayer.playBulletUseOutMusic();
                return;
            }
            MusicPlayer.playReloadMusic(weapons[usingWeaponType].getWeaponName());
            isReload = true;
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

    /**
     * 人物是否在装弹中
     * @return
     */
    public boolean ifReloading()
    {
        return isReload;
    }
    public void addHealthPoint(int hp)                      //给人物加血
    {
        healthPoint+=hp;
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

    /**
     * 切换武器
     * @param type:武器类型
     * @param gameArea:游戏显示区域
     */
    public void changeWeapon(int type,JPanel gameArea)                  //切换武器
    {

        if(type!=usingWeaponType)
        {
            if (weapons[type] != null)    //如果这把武器存在
            {
                if(isAttacking)
                {
                    MusicPlayer.stopContinueAttackMusic();
                }
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
    public int getSpeed(){return speed;}                                //获取人物的移动速度
    public void reduceMineNum(int num){bulletNum[WeaponType.mine]-=num;}        //减少人物携带的地雷数量
    public void reduceGrenadeNum(int num){bulletNum[WeaponType.grenade]-=num;}  //减少人物携带的手雷数量
    public boolean ifEmptyMine(){return bulletNum[WeaponType.mine]==0;}         //判断人物是否还有地雷
    public boolean ifEmptyGrenade(){return bulletNum[WeaponType.grenade]==0;}   //判断人物是否还有手雷
    public int getBulletLeftOnPerson(){return bulletNum[usingWeaponType];}      //放回人物身上携带的子弹数量
}


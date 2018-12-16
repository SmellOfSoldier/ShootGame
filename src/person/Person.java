package person;

import javax.swing.*;
import java.io.Serializable;

import Weapon.Weapon;

/**
 * 游戏人物
 */
public class Person extends JLabel implements Serializable
{
    private int speed;                      //人物的移动速度
    protected int id;                       //编号
    protected String name;                  //姓名
    private int usingWeapon=3;              //目前使用的武器
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
    public int getHealthPoint(){return healthPoint;}       //获取人物当前血量
    public int getId(){return id;}                          //获取人物编号
    public int getRadius(){return radius;}                  //获取人物半径
    public String getName(){return name;}                   //获取人物名称
    public int getSpeed(){return speed;}                    //获取人物的速度
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
    }
    public boolean ifHaveWeapon(int type)               //判断是否拥有该武器
    {
        if(weapons[type]==null)
            return false;
        return true;
    }
    public void changeWeapon(int type)                  //切换武器
    {
        usingWeapon=type;
    }
    public int getUsingWeaponType(){return usingWeapon;}    //获取当前使用的武器类型
    public Weapon getUsingWeapon(){return weapons[usingWeapon];}     //获取当前使用的武器
}


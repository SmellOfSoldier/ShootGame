package Weapon;


import javax.swing.*;
import java.io.Serializable;

/**
 * 武器
 */
public  class Weapon extends JLabel implements Serializable
{
    private String weaponName=null;   //武器的名称
    private int type;                 //武器的种类
    private int damageValue;          //武器伤害大小
    protected Weapon(int type,String weaponName,int damageValue)
    {
        this.type=type;
        this.damageValue=damageValue;
        this.weaponName=weaponName;
    }
    public int getType(){return type;}      //获取武器种类
    public int getDamageValue(){return damageValue; }  //获取武器的伤害大小
    public String getWeaponName(){return weaponName;}   //获取武器的名称
}



package Weapon;

import java.io.Serializable;

/**
 * 枪类
 */
public class Gun extends Weapon implements Serializable
{
    private boolean continuedShot;      //能否持续射击
    private int fireRate;               //武器的射速
    private int bulletType;             //使用的子弹类型
    protected Gun(int weaponType,int bulletType,String weaponName,int damageValue,int fireRate,boolean continuedShot)
    {
        super(weaponType,weaponName,damageValue);
        this.bulletType=bulletType;
        this.continuedShot=continuedShot;
        this.fireRate=fireRate;
    }
    public int getFireRate(){return fireRate;}          //返回武器的射速
    public int getBulletType(){return bulletType;}      //返回子弹的类型
    public boolean ifContinuedShot(){return continuedShot;}//是否能连续射击
}

package Weapon;


import java.io.Serializable;


/**
 * 狙击步枪
 */
public class SniperRifle extends Gun implements Serializable
{
    public static int maxBulletNum=5;      //弹夹中最大的子弹量
    public static boolean continuedShot=false;               //能否连续射击
    protected SniperRifle(int bulletType,String weaponName,int damageValue,int fireRate,boolean continuedShot,int reloadeTime)
    {
        super(WeaponType.sniperRifle,bulletType,weaponName,damageValue,fireRate,continuedShot,5,reloadeTime);
    }
}
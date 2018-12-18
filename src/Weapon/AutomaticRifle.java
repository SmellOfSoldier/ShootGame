package Weapon;


import java.io.Serializable;

/**
 * 自动步枪
 */
public class AutomaticRifle extends Gun implements Serializable
{
    public static int maxBulletNum=30;      //弹夹中最大的子弹量
    protected AutomaticRifle(int bulletType,String weaponName,int damageValue,int fireRate,boolean continuedShot,int reloadTime)
    {
        super(WeaponType.automaticRifle,bulletType,weaponName,damageValue,fireRate,continuedShot,maxBulletNum,reloadTime);
    }
}

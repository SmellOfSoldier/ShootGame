package Weapon;

import Arsenal.WeaponType;

import java.io.Serializable;


/**
 * 狙击步枪
 */
public class SniperRifle extends Gun implements Serializable
{
    public static boolean continuedShot=false;               //能否连续射击
    protected SniperRifle(int bulletType,String weaponName,int damageValue,int fireRate,boolean continuedShot)
    {
        super(WeaponType.sniperRifle,bulletType,weaponName,damageValue,fireRate,continuedShot);
    }
}
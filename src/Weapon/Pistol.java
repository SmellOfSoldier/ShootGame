package Weapon;

import Arsenal.WeaponType;

import java.io.Serializable;

/**
 * 手枪
 */
public class Pistol extends Gun implements Serializable
{
    protected Pistol(int bulletType,String weaponName,int damageValue,int fireRate,boolean continuedShot)
    {
        super(WeaponType.pistol,bulletType,weaponName,damageValue,fireRate,continuedShot);
    }
}
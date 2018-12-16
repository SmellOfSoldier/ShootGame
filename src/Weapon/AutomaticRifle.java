package Weapon;

import Arsenal.WeaponType;

import java.io.Serializable;

/**
 * 自动步枪
 */
public class AutomaticRifle extends Gun implements Serializable
{
    protected AutomaticRifle(int bulletType,String weaponName,int damageValue,int fireRate,boolean continuedShot)
    {
        super(WeaponType.automaticRifle,bulletType,weaponName,damageValue,fireRate,continuedShot);
    }
}

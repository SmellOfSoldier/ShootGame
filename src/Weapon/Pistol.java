package Weapon;



import java.io.Serializable;

/**
 * 手枪
 */
public class Pistol extends Gun implements Serializable
{
    public static int maxBulletNum=10;      //弹夹中最大的子弹量
    protected Pistol(int bulletType,String weaponName,int damageValue,int fireRate,boolean continuedShot,int reloadTime)
    {
        super(WeaponType.pistol,bulletType,weaponName,damageValue,fireRate,continuedShot,10,reloadTime);
    }
}
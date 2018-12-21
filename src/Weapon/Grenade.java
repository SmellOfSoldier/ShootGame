package Weapon;


import java.io.Serializable;

/**
 * 手榴弹
 */
public class Grenade extends Weapon implements Serializable
{

    private static int throwDistance;       //投掷距离
    private static int damageRadius=60;        //伤害半径
    private static int damageValue=200;     //爆炸伤害
    protected Grenade(String weaponName,int throwDistance,int damageRadius)
    {
        super(WeaponType.grenade,weaponName,damageValue);
        this.throwDistance=throwDistance;
        this.damageRadius=damageRadius;
    }
    public int getThrowDistance(){return throwDistance;}        //获取投掷距离
    public int getDamageRadius(){return damageRadius;}          //获取爆炸半径
}
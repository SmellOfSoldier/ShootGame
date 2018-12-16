package Weapon;

import Arsenal.WeaponType;

import java.io.Serializable;
 /**
  * 近战武器
  */
 public class ClosedWeapon extends Weapon implements Serializable
{
    private int attackRange;     //攻击范围
    protected ClosedWeapon(int damageValue,String weaponName,int attackRange)
    {
        super(WeaponType.closedWeapon,weaponName,damageValue);
        this.attackRange=attackRange;
    }
    public int getAttackRange(){return attackRange;}
}

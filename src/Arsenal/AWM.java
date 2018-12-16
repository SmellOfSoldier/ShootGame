package Arsenal;

import Weapon.SniperRifle;
import bullet.BulletType;

public class AWM extends SniperRifle
{
    public final static String name="AWM";       //武器名称
    public final static int fireRate=1000;       //武器射速
    public final static boolean continuedShot=false;     //能否连续射击
    public final static int damageValue=100;     //武器伤害
    public final static int bulletType= BulletType.k127;    //使用12.7毫米子弹
    public AWM()
    {
        super(bulletType,name,damageValue,fireRate,continuedShot);
    }
}

package Arsenal;

import Weapon.SniperRifle;
import bullet.BulletType;

public class AWM extends SniperRifle
{
    private  static int reloadTime=3000;    //换弹夹时间
    private static String name="AWM";       //武器名称
    private static int fireRate=1000;       //武器射速
    private static boolean continuedShot=false;     //能否连续射击
    private static int damageValue=200;     //武器伤害
    private static int bulletType= BulletType.k127;    //使用12.7毫米子弹
    public AWM()
    {
        super(bulletType,name,damageValue,fireRate,continuedShot,reloadTime);
    }
}

package Arsenal;

import Weapon.Gun;
import Weapon.SniperRifle;
import bullet.BulletType;

//巴雷特反器材狙击步枪
public class Barret extends SniperRifle
{
    private static int reloadTime=3500;    //换弹夹时间
    private static String name="Barret";       //武器名称
    private static int fireRate=2000;       //武器射速
    private static boolean continuedShot=false;     //能否连续射击
    private static int damageValue=1100;     //武器伤害
    private static int bulletType= BulletType.k127;    //使用12.7毫米子弹
    public Barret()
    {
        super(bulletType,name,damageValue,fireRate,continuedShot,reloadTime);
    }
}

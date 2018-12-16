package Arsenal;

import Weapon.Gun;
import Weapon.SniperRifle;
import bullet.BulletType;

//巴雷特反器材狙击步枪
public class Barret extends SniperRifle
{
    public final static String name="Barret";       //武器名称
    public final static int fireRate=2000;       //武器射速
    public final static boolean continuedShot=false;     //能否连续射击
    public final static int damageValue=200;     //武器伤害
    public final static int bulletType= BulletType.k127;    //使用12.7毫米子弹
    public Barret()
    {
        super(bulletType,name,damageValue,fireRate,continuedShot);
    }
}

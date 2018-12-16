package Arsenal;

import Weapon.AutomaticRifle;
import Weapon.Gun;
import bullet.BulletType;

//m4a1自动步枪
public class M4A1 extends AutomaticRifle
{
    public final static String name="M4A1";       //武器名称
    public final static int fireRate=100;       //武器射速
    public final static boolean continuedShot=true;     //能否连续射击
    public final static int damageValue=60;     //武器伤害
    public final static int bulletType= BulletType.k556;    //5.56子弹
    public M4A1()
    {
        super(bulletType,name,damageValue,fireRate,continuedShot);
    }
}

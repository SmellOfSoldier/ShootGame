package Arsenal;

import Weapon.AutomaticRifle;
import Weapon.Gun;
import bullet.BulletType;

//AKM
public class AKM extends AutomaticRifle
{
    public final static String name="AKM";       //武器名称
    public final static int fireRate=120;       //武器射速
    public final static boolean continuedShot=true;     //能否连续射击
    public final static int damageValue=60;     //武器伤害
    public final static int bulletType= BulletType.k762;    //子弹为7.62毫米
    public AKM()
    {
        super(bulletType,name,damageValue,fireRate,continuedShot);
    }
}

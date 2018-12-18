package Arsenal;

import Weapon.AutomaticRifle;
import Weapon.Gun;
import bullet.BulletType;

//AKM
public class AKM extends AutomaticRifle
{
    private static int reloadTime=2500;    //换弹夹时间
    private  static String name="AKM";       //武器名称
    private  static int fireRate=120;       //武器射速
    private  static boolean continuedShot=true;     //能否连续射击
    private  static int damageValue=60;     //武器伤害
    private  static int bulletType= BulletType.k762;    //子弹为7.62毫米
    public AKM()
    {
        super(bulletType,name,damageValue,fireRate,continuedShot,reloadTime);
    }
}

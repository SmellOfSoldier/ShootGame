package Arsenal;

import Weapon.AutomaticRifle;
import Weapon.Gun;
import bullet.BulletType;

//m4a1自动步枪
public class M4A1 extends AutomaticRifle
{
    private  static int reloadTime=2000;    //换弹夹时间
    private  static String name="M4A1";       //武器名称
    private  static int fireRate=100;       //武器射速
    private  static boolean continuedShot=true;     //能否连续射击
    private  static int damageValue=60;     //武器伤害
    private  static int bulletType= BulletType.k556;    //5.56子弹
    public M4A1()
    {
        super(bulletType,name,damageValue,fireRate,continuedShot,reloadTime);
    }
}

package Arsenal;

import Weapon.Pistol;
import bullet.BulletType;

//p92手枪
public class P92 extends Pistol
{
    public final static String name="P92";       //武器名称
    public final static int fireRate=400;       //武器射速
    public final static boolean continuedShot=false;     //能否连续射击
    public final static int damageValue=40;     //武器伤害
    public final static int bulletType= BulletType.k900;    //使用9毫米子弹
    public P92()
    {
        super(bulletType,name,damageValue,fireRate,continuedShot);
    }
}

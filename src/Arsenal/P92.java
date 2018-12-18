package Arsenal;

import Weapon.Pistol;
import bullet.BulletType;

//p92手枪
public class P92 extends Pistol
{
    private static int reloadTime=1500;    //换弹夹时间
    private  static String name="P92";       //武器名称
    private  static int fireRate=400;       //武器射速
    private  static boolean continuedShot=false;     //能否连续射击
    private  static int damageValue=40;     //武器伤害
    private  static int bulletType= BulletType.k900;    //使用9毫米子弹
    public P92()
    {
        super(bulletType,name,damageValue,fireRate,continuedShot,reloadTime);
    }
}

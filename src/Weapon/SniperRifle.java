package Weapon;


import java.io.Serializable;


/**
 * 狙击步枪
 */
public class SniperRifle extends Gun implements Serializable
{
    private boolean isPollBolt  =false;     //武器正在拉栓
    public static int maxBulletNum=5;      //弹夹中最大的子弹量
    public static boolean continuedShot=false;               //能否连续射击
    protected SniperRifle(int bulletType,String weaponName,int damageValue,int fireRate,boolean continuedShot,int reloadeTime)
    {
        super(WeaponType.sniperRifle,bulletType,weaponName,damageValue,fireRate,continuedShot,5,reloadeTime);
    }
   public void setPollBolt(boolean isPollBolt)
    {
        this.isPollBolt=isPollBolt;
        if(isPollBolt==true)
        {
            new Thread(new Runnable() {
                @Override
                public void run()
                {
                    try
                    {
                        Thread.currentThread().sleep(2000);
                    }
                    catch (Exception ex)
                    {
                        ex.printStackTrace();
                    }
                    setPollBolt(false);
                }
            }).start();
        }
    }
    public boolean isPollBolt(){return isPollBolt;}
}
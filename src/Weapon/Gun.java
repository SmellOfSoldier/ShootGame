package Weapon;

import java.io.Serializable;

/**
 * 枪类
 */
public class Gun extends Weapon implements Serializable
{
    private int reloadTime;             //换弹夹时间
    private boolean continuedShot;      //能否持续射击
    private int fireRate;               //武器的射速
    private int bulletType;             //使用的子弹类型
    private int bulletLeft ;  //弹夹中的子弹
    protected Gun(int weaponType,int bulletType,String weaponName,int damageValue,int fireRate,boolean continuedShot,int bulletNumInCartridgeClip,int reloadTime)
    {
        super(weaponType,weaponName,damageValue);
        this.bulletType=bulletType;
        this.continuedShot=continuedShot;
        this.fireRate=fireRate;
        this.bulletLeft=bulletNumInCartridgeClip;
        this.reloadTime=reloadTime;
    }

    public int getFireRate(){return fireRate;}          //返回武器的射速
    public int getBulletType(){return bulletType;}      //返回子弹的类型
    public boolean ifContinuedShot(){return continuedShot;}//是否能连续射击
    public int getBulletLeft(){return bulletLeft;}  //返回当前弹夹数目
    public boolean emptyBulletNum(){return bulletLeft==0;}        //弹夹中的子弹是否为空
    public void addBulletNum(int bulletNum){bulletLeft+=bulletNum;}    //给枪加子弹
    public void reduceBulletNum(int bulletNum){bulletLeft-=bulletNum;}    //开枪后减少弹夹中子弹的数目
    public int getReloadTime(){return reloadTime;}              //返回换弹夹的时间
}

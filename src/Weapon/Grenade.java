package Weapon;


import person.Person;
import person.TravelSpeed;
import view.SinglePersonModel;

import javax.swing.*;
import java.applet.Applet;
import java.applet.AudioClip;
import java.awt.*;
import java.io.Serializable;
import java.net.URL;

/**
 * 手榴弹334
 */
public class Grenade extends Weapon implements Serializable
{
    public static final int speed=80;            //手雷移动刷新周期
    private int xSpeed;                     //手雷每次x轴方向移动像素
    private int ySpeed;                     //手雷每次y轴方向移动的像素
    private static int maxThrowDistance=200;    //最远投掷距离
    private int throwDistance;       //投掷距离
    private Point endPoint;               //投掷最终坐标
    private Point startPoint;             //开始坐标
    private static int damageRadius=60;        //伤害半径
    private static int damageValue=200;     //爆炸伤害
    private static JLabel boomEffect=new JLabel(); //爆炸特效
    private static URL boomMusicURL=Grenade.class.getResource("/musics/boom/grenadeBoom.wav");      //手雷爆炸音效路径
    private static URL grenadeImageUrl=Grenade.class.getResource("/images/Weapon/BoomWeapon/grenade.png");  //手雷的图片路径
    private AudioClip boomMusicPlayer= Applet.newAudioClip(boomMusicURL);
    private String fromPersonId=null;           //手雷持有者的id
    {
        //为手雷设置爆炸特效
        URL url=Mine.class.getResource("/images/specialEffect/boomEffect.gif");
        boomEffect.setSize(damageRadius*2,damageRadius*2);
        ImageIcon icon=new ImageIcon(url);
        boomEffect.setIcon(icon);
    }

    /**
     * 构造函数
     */
    public Grenade()
    {
        super(WeaponType.grenade,"Grenade",damageValue);
        this.setSize(SinglePersonModel.CELL, SinglePersonModel.CELL);
        ImageIcon icon=new ImageIcon(grenadeImageUrl);
        icon.setImage(icon.getImage().getScaledInstance(SinglePersonModel.CELL, SinglePersonModel.CELL,Image.SCALE_DEFAULT));
        this.setIcon(icon);
    }
    public int getThrowDistance(){return throwDistance;}        //获取投掷距离
    public int getDamageRadius(){return damageRadius;}          //获取爆炸半径
    public String getFromPersonId(){return fromPersonId;}           //获取这个手雷的所有者
    /**
     * 判断手雷是否到达最终坐标
     */
    public boolean ifArrive()
    {
        Point nowPoint= SinglePersonModel.getCentralPoint(this.getLocation());
        double nowDistance=nowPoint.distance(startPoint);
        return nowDistance>=throwDistance;
    }

    /**
     * 将这颗手雷扔出
     * @param endPoint：手雷的最终坐标
     * @param startPoint：手雷的起始坐标
     */
    public void calculateGrenadeSpeed(Point startPoint, Point endPoint)
    {
        //计算手雷的xspeed、yspeed、throwDistance
        double distance=startPoint.distance(endPoint);
        if(distance>maxThrowDistance)
        {
            distance=maxThrowDistance;
        }
        throwDistance=(int)distance;
        this.endPoint=endPoint;
        this.startPoint=startPoint;
        this.setLocation(startPoint);
        double travelSpeed= TravelSpeed.grenadeTravelSpeed;
        double xs=startPoint.x;
        double ys=startPoint.y;
        double xe=endPoint.x;
        double ye=endPoint.y;
        double xRate=(xe-xs)/distance;
        double yRate=(ye-ys)/distance;

        xSpeed=(int)(xRate*travelSpeed);
        ySpeed=(int)(yRate*travelSpeed);
    }
    /**
     * 设置手雷的使用者
     */
    public void setFromPersonId(String fromPersonId)
    {
        this.fromPersonId=fromPersonId;
    }
    /**
     * 手雷继续移动
     */
    public void next()
    {
        Point oldPoint=this.getLocation();
        Point newPoint=new Point(oldPoint.x+xSpeed,oldPoint.y+ySpeed);
        this.setLocation(newPoint);
    }
    /**
     * 手雷爆炸，将手雷图片换成爆炸特效
     * @param gameArea
     */
    public void boom(JPanel gameArea)
    {
        Point point=this.getLocation();
        Point boomPoint =new Point(point.x+(SinglePersonModel.CELL/2)-damageRadius,point.y+(SinglePersonModel.CELL/2)-damageRadius);
        boomEffect.setLocation(boomPoint);
        boomMusicPlayer.play();
        new Thread(new Runnable() {
            @Override
            public void run()
            {
                try {
                    gameArea.add(boomEffect);
                    gameArea.repaint();
                    Thread.sleep(1500);
                    gameArea.remove(boomEffect);
                    gameArea.repaint();
                }
                catch (Exception ex)
                {
                    ex.printStackTrace();
                }
            }
        }).start();
    }

}
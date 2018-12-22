package person;

import Arsenal.AWM;
import Weapon.WeaponType;
import bullet.Bullet;
import bullet.BulletSize;
import view.SinglePersonModel;
import view.startGame;

import javax.swing.*;
import java.applet.Applet;
import java.applet.AudioClip;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.Serializable;
import java.net.URL;

/**
 * 隐匿者,使用AWM
 */
public class Hider extends AI implements Serializable
{
    private static int vision=450;
    public static final int speed=30;
    private int shootSpeed=3000;
    private Timer shotThread=null;
    private  AudioClip shotPlayer=null;
    private static URL shotUrl=Hider.class.getResource("/musics/shot/AWM.wav");
    AWM awm=new AWM();
    public Hider(String id)
    {
        super(id,"隐匿者",100,10,speed,vision);
        changeWeapon(WeaponType.automaticRifle);
        URL url=startGame.class.getResource("/images/orange.png");
        ImageIcon icon=new ImageIcon(url);
        icon.setImage(icon.getImage().getScaledInstance(SinglePersonModel.CELL, SinglePersonModel.CELL,Image.SCALE_DEFAULT));
        this.setSize(SinglePersonModel.CELL, SinglePersonModel.CELL);
        this.setIcon(icon);
        this.peekWeapon(awm,100);
    }
    //射击
    public void shot(Point endPoint, JPanel gameArea)
    {
        if(shotThread==null)
        {
            Point p=this.getLocation();
            shotThread=new Timer(shootSpeed, new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e)
                {
                    int radius= BulletSize.getBulletRadius(awm.getBulletType());
                    java.util.List bulletList= SinglePersonModel.getSniperBulletList();
                    Bullet bullet=new Bullet(Hider.this,awm.getBulletType(),radius,awm.getDamageValue(), TravelSpeed.bulletTravelSpeed,Hider.this.getLocation(), SinglePersonModel.getPlayer().getLocation());
                    int bulletRadius=bullet.getRadius();
                    bullet.setSize(bulletRadius,bulletRadius);
                    URL url= startGame.class.getResource("/images/bullet/Bullet.png");
                    ImageIcon icon=new ImageIcon(url);
                    icon.setImage(icon.getImage().getScaledInstance(bulletRadius,bulletRadius,Image.SCALE_DEFAULT));
                    bullet.setIcon(icon);
                    bulletList.add(bullet);
                    gameArea.add(bullet);
                    playShotMusic();
                }
            });
        }
        shotThread.start();
    }
    //停止射击
    public void stopShot()
    {
        if(shotThread!= null && shotThread.isRunning())
            shotThread.stop();
        reLoad();
    }
    private void playShotMusic()
    {
        if(shotPlayer!=null)
        {
            shotPlayer.stop();
        }
        shotPlayer= Applet.newAudioClip(shotUrl);
        shotPlayer.play();
    }
    public boolean isIfFindPlayer(Point playerPoint)
    {
        Point aiPoint=this.getLocation();
        if (aiPoint.distance(playerPoint) < vision)
        {
            return true;
        }
        return false;
    }
}

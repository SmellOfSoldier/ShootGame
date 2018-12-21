package person;

import Arsenal.AKM;
import Weapon.WeaponType;
import bullet.Bullet;
import bullet.BulletSize;
import bullet.BulletType;
import utils.MusicPlayer;
import view.GameFrame;
import view.TravelSpeed;
import view.startGame;

import java.applet.Applet;
import java.applet.AudioClip;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.Serializable;
import java.net.URL;
import javax.swing.*;

/**
 * 精英战士，拿着自动步枪
 */
public class EliteSoldier extends AI implements Serializable
{
    public final static int vision=400;
    public static final int speed=40;
    private Timer shotThread=null;
    public final static int shotSpeed=2000;
    public final static int healthPoint=140;
    private AudioClip shotPlayer=null;
    private URL shotUrl= MusicPlayer.class.getResource("/musics/shot/AKM.wav");
    private AKM akm=new AKM();
    public EliteSoldier(int id)
    {
        super(id,"精英战士",healthPoint,10,speed,vision);
        this.peekWeapon(akm,240);
        changeWeapon(WeaponType.automaticRifle);
        URL url=startGame.class.getResource("/images/apple.png");
        ImageIcon icon=new ImageIcon(url);
        icon.setImage(icon.getImage().getScaledInstance(GameFrame.CELL,GameFrame.CELL,Image.SCALE_DEFAULT));
        this.setSize(GameFrame.CELL,GameFrame.CELL);
        this.setIcon(icon);
    }
    //射击
    public void shot(Point endPoint, JPanel gameArea)
    {
        if(shotThread==null)
        {
            Point p=this.getLocation();
            AKM akm=(AKM)this.getUsingWeapon();
            shotThread=new Timer(shotSpeed, new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e)
                {
                    int radius=BulletSize.getBulletRadius(akm.getBulletType());
                    java.util.List bulletList=GameFrame.getAutomaticBulletList();
                    Bullet bullet=new Bullet(EliteSoldier.this,akm.getBulletType(),radius,akm.getDamageValue(), TravelSpeed.bulletTravelSpeed,EliteSoldier.this.getLocation(),GameFrame.getPlayer().getLocation());
                    int bulletRadius=bullet.getRadius();
                    bullet.setSize(bulletRadius,bulletRadius);
                    URL url= startGame.class.getResource("/images/bullet/Bullet.png");
                    ImageIcon icon=new ImageIcon(url);
                    icon.setImage(icon.getImage().getScaledInstance(bulletRadius,bulletRadius,Image.SCALE_DEFAULT));
                    bullet.setIcon(icon);
                    bulletList.add(bullet);
                    gameArea.add(bullet);
                    //playShotMusic();
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
}

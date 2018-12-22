package Weapon;

import person.Person;
import utils.MusicPlayer;

import javax.swing.*;
import java.awt.*;
import java.net.URL;

//地雷
public class Mine extends Weapon
{
    private static int damageValue=300; //爆炸伤害
    private static int damageRadius=40;   //杀伤半径
    private static int radius=10;       //地雷半径
    private static JLabel boomEffect=new JLabel(); //爆炸特效
    private Person fromPerson;      //安装这个地雷的人
    static
    {
        //为地雷设置爆炸特效
        URL url=Mine.class.getResource("/images/specialEffect/boomEffect.gif");
        boomEffect.setSize(damageRadius*2,damageRadius*2);
        ImageIcon icon=new ImageIcon(url);
        boomEffect.setIcon(icon);
    }
    public Mine()
    {
        super(WeaponType.mine,"Mine",damageValue);
    }
    public void setFromPerson(Person person)
    {
        this.fromPerson=fromPerson;
    }
    public void boom(JPanel gameArea, Point point)       //爆炸
    {
        Point boomPoint =new Point(point.x-damageRadius,point.y-damageRadius);
        boomEffect.setLocation(boomPoint);
        MusicPlayer.playBoomMusic("mineBoom");
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
    //获取地雷爆炸半径
    public int getDamageRadius(){return damageRadius;}
    //获取地雷半径
    public int getRadius(){return radius;}
    //获取安装这个地雷的person
    public Person getFromPerson(){return fromPerson;}
}

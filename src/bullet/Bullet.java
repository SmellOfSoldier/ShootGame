package bullet;

import Arsenal.AKM;
import person.Person;
import person.Player;

import javax.swing.*;
import java.awt.*;
import java.io.Serializable;

/**
 * 子弹类
 *
 */
public class Bullet extends JLabel implements Serializable
{
    private Person fromPerson;      //射出该子弹的人物
    private String fromGunName;     //射出该子弹的枪名字
    private int damageValue;        //射出该子弹的枪的的伤害
    private int radius;             //子弹半径
    int bulletType;                 //子弹类型
    private int xSpeed;             //子弹的x方向的速度
    private int ySpeed;             //子弹的y方向的速度
    public Bullet(){}
    public Bullet(Person fromPerson,int bulletType, int radius, int damageValue, int speed, Point start, Point end)
    {
        this.damageValue=damageValue;
        this.radius=radius;
        this.bulletType=bulletType;
        double x1=start.x;
        double y1=start.y;
        double y2=end.y;
        double x2=end.x;
        int flag=1;
        if(start.x>end.x&&start.y>end.y){flag=2;}//y反向
        if(start.x>end.x&&start.y<end.y){flag=3;}
        double k=(y2-y1)/(x2-x1);
        double angle=Math.atan(k);
        ySpeed=(int)(Math.sin(angle)*speed);
        xSpeed=(int)(Math.cos(angle)*speed);
        if(flag==2){xSpeed=-xSpeed;ySpeed=-ySpeed;}
        if(flag==3){xSpeed=-xSpeed;ySpeed=-ySpeed;}
        this.setLocation(start);
        this.fromPerson=fromPerson;
    }
    public int getxSpeed(){return xSpeed;}
    public int getySpeed(){return ySpeed;}
    public int getBulletType(){return bulletType;}
    public int getRadius(){return radius;}
    public int getDamageValue(){return damageValue;}
    public Person getFromPerson(){return fromPerson;}
}

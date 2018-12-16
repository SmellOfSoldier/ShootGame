package view;

import Arsenal.*;
import Weapon.Weapon;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.net.URL;
import java.util.Collections;
import java.util.LinkedList;
import java.util.Random;
import Weapon.*;
import bullet.*;

public class startGame {
    public static void main(String[] args)
    {
        new GameFrame();
    }
}

class GameFrame extends JFrame
{
    private Random random=new Random();
    private final static int CELL=20;
    private final static int width=800;
    private final static int high=600;
    private final static int bulletTravalSpeed=5;      //子弹每次移动的格数
    private LinkedList<Player> otherPlayer=new LinkedList<Player>();    //存放其他玩家
    private java.util.List<AI> aiList= Collections.synchronizedList(new LinkedList<AI>());    //存放游戏AI
    private java.util.List<Bullet> automaticBulletList =Collections.synchronizedList(new LinkedList<Bullet>());
    private Player player;                      //游戏玩家
    private Timer shotThread=null;              //开火线程
    private GameArea gameArea=new GameArea();
    private Timer automaticBulletThread=null;   //自动步枪子弹飞行线程
    private Timer sniperBulletThread=null;      //狙击步枪子弹飞行线程
    private Timer pistolBulletThread=null;      //手枪子弹的飞行线程
    GameFrame()
    {
        gameArea=new GameArea();
        createPlayer();
        createNpc();
        this.setSize(width+10,high+34);
        this.setResizable(false);
        this.setLocationRelativeTo(null);
        this.add(gameArea);
        this.setVisible(true);

    }
    /**
     * 游戏的画面显示区域
     */
    class GameArea extends JPanel
    {
        GameArea()
        {
            initial();
            this.setSize(GameFrame.width,GameFrame.high);
            this.setLayout(null);           //设置为绝对布局
            this.addMouseListener(new MouseAdapter()
            {
                @Override
                public void mouseClicked(MouseEvent mouseEvent)
                {
                    Point endPoint=getCentralPoint(mouseEvent.getLocationOnScreen());
                    Point startPoint=getCentralPoint(player.getLocation());
                    attack(startPoint,endPoint,player);    //攻击
                }
                public void mousePressed(MouseEvent mouseEvent)         //持续开火
                {
                    int fireRate=((Gun)player.getUsingWeapon()).getFireRate();         //获取枪的射速
                    int weaponType=player.getUsingWeaponType();                 //获取枪的类型
                    shotThread= new Timer(fireRate, new ActionListener() {       //玩家开火
                        @Override
                        public void actionPerformed(ActionEvent e)
                        {
                            continuouslyAttack();
                        }
                    });
                    shotThread.start();
                }
                @Override
                public void mouseReleased(MouseEvent e)
                {
                    shotThread.stop();
                }       //玩家停止开火
            });
        }
        public void initial()
        {
            automaticBulletThread=new Timer(BulletSpeed.automaticBulletSpeed, new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e)
                {
                    if(!automaticBulletList.isEmpty())
                    {
                        int i=-1;
                        Bullet[] deleteBullet=new Bullet[automaticBulletList.size()];
                        for(Bullet bullet:automaticBulletList)
                        {
                            boolean flag=false;             //标记该子弹是否击中人
                            Point oldPoint=bullet.getLocation();
                            Point newPoint=new Point(oldPoint.x+bullet.getxSpeed(),oldPoint.y+bullet.getySpeed());
                            bullet.setLocation(newPoint);
                            for(AI ai:aiList)          //判断每个敌人是否被子弹击中
                            {
                                if((ifHitPerson(bullet,ai)) && !ai.ifDie())      //如果击中
                                {
                                    flag=true;
                                    bullet.setVisible(false);                   //击中目标后子弹消失
                                    int healthPoint =ai.getHealthPoint();
                                    if(healthPoint-bullet.getDamageValue()<=0)
                                    {
                                        ai.setVisible(false);
                                        ai.setDie(true);            //设置AI死亡
                                    }
                                    else
                                    {
                                        ai.reduceHealthPoint(bullet.getDamageValue());
                                    }
                                    deleteBullet[++i]=bullet;       //将击中目标的子弹保存起来
                                    break;
                                }
                            }
                            if(!flag && (newPoint.x<0 || newPoint.x>800 || newPoint.y<0 || newPoint.y>600))    //判断子弹是否撞墙
                            {
                                   deleteBullet[++i]=bullet ;           //将撞墙的子弹保存起来
                            }
                        }
                        if(i!=-1)
                        {
                            for(int j=0;j<=i;j++)
                            {
                                gameArea.remove(deleteBullet[j]);  //将子弹从gameArea中删除
                                automaticBulletList.remove(deleteBullet[j]);                //将子弹从自动步枪子弹中删除
                            }
                        }
                        gameArea.repaint();
                    }
                }
            });
            automaticBulletThread.start();
        }
        public void paintComponent(Graphics g)
        {
            URL url=startGame.class.getResource("/images/bg.png");
            ImageIcon icon=new ImageIcon(url);
            g.drawImage(icon.getImage(),0,0,GameFrame.width,GameFrame.high,null);
        }
    }
    //创建子弹
    public void createBullet(Point start,Point end,int bulletType,int damageValue,int gunType)
    {
        Bullet bullet=new Bullet(bulletType,5,damageValue,bulletTravalSpeed,start,end);
        bullet.setSize(5,5);
        URL url=startGame.class.getResource("/images/red.png");

        ImageIcon icon=new ImageIcon(url);
        icon.setImage(icon.getImage().getScaledInstance(5,5,Image.SCALE_DEFAULT));
        bullet.setIcon(icon);
        switch (gunType)
        {
            case WeaponType.automaticRifle:
                automaticBulletList.add(bullet);
        }
        this.add(bullet);
    }
    //间断性攻击
    private void attack(Point startPoint,Point endPoint,Person person)
    {
        Weapon weapon=person.getUsingWeapon();
        //如果是近战武器
        if(weapon.getType()==WeaponType.closedWeapon)
        {

        }
        //如果是投掷类武器
        else if(weapon.getType()==WeaponType.grenade)
        {

        }
        //如果是枪类武器
        else
        {
            createBullet(startPoint,endPoint,((Gun)weapon).getBulletType(),weapon.getDamageValue(),weapon.getType());
        }
    }
    //持续性攻击
    private void continuouslyAttack(Point startPoint ,Point endPoint ,Person person)
    {

    }
    //创建玩家
    private void createPlayer()
    {
        player=new Player(1,"DJF",100,10);
        player.setSize(CELL,CELL);
        URL url=startGame.class.getResource("/images/header_b.png");
        ImageIcon icon=new ImageIcon(url);
        icon.setImage(icon.getImage().getScaledInstance(CELL,CELL,Image.SCALE_DEFAULT));
        player.setIcon(icon);
        player.setLocation(400,300);
        gameArea.add(player);
        player.peekWeapon(new Weapon(WeaponType.automaticRifle,"ak-47",WeaponDamageValue.automaticRifleDamageValue,FireRate.automaticRifleFireRate,BulletSpeed.automaticRifleBulletSpeed),100);
    }
    //创建AI
    private void createNpc()
    {
        URL url=startGame.class.getResource("/images/apple.png");
        ImageIcon icon=new ImageIcon(url);
        icon.setImage(icon.getImage().getScaledInstance(CELL,CELL,Image.SCALE_DEFAULT));
        for(int i=0;i<5;i++)
        {
            AI ai=new AI(i,"精英战士",100,10);
            ai.setSize(CELL,CELL);
            ai.setLocation(randomPoint());
            ai.setIcon(icon);
            gameArea.add(ai);
            aiList.add(ai);
        }
    }
    //随机生成一个坐标
    private Point randomPoint()
    {
        int x=random.nextInt(GameFrame.width/CELL)*CELL;
        int y=random.nextInt(GameFrame.high/CELL)*CELL;
        return new Point(x,y);
    }

    //获取人物的中心坐标
    private Point getCentralPoint(Point topLeftCornerPoint)
    {
        return new Point(topLeftCornerPoint.x+CELL/2,topLeftCornerPoint.y+CELL/2);
    }
    private boolean ifHitPerson(Bullet bullet,Person person)
    {
        Point bulletCentralPoint=getCentralPoint(bullet.getLocation());
        Point personCentralPoint=getCentralPoint(bullet.getLocation());
        if(bulletCentralPoint.distance(personCentralPoint) < Math.abs(bullet.getRadius()-person.getRadius()))
            return true;
        return false;
    }

}




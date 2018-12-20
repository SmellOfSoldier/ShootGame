package view;

import Arsenal.AKM;
import Arsenal.AWM;
import Weapon.Gun;
import Weapon.Weapon;
import Weapon.WeaponType;
import bullet.Bullet;
import bullet.BulletSize;
import bullet.BulletSpeed;
import person.*;
import utils.MusicPlayer;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.net.URL;
import java.util.Collections;
import java.util.LinkedList;
import java.util.Random;

public class GameFrame extends JFrame
{
    private Point[] entrance=new Point[]{new Point(800,20),new Point(1040,280),new Point(1160,600), new Point(320,760),new Point(20,520)};         //刷怪位置
    private Point endPoint=new Point();
    private Random random=new Random();
    private final static int CELL=20;   //每个人物的大小
    private final static int width=1200;
    private final static int high=800;
    private LinkedList<Player> otherPlayer=new LinkedList<Player>();    //存放其他玩家
    //存放自动步枪子弹的链表
    private java.util.List<Bullet> automaticBulletList = Collections.synchronizedList(new LinkedList<Bullet>());
    //存放狙击枪子弹的链表
    private java.util.List<Bullet> sniperBulletList =Collections.synchronizedList(new LinkedList<Bullet>());
    //存放精英战士的链表
    private java.util.List<EliteSoldier> eliteSoldierList=Collections.synchronizedList(new LinkedList<EliteSoldier>());
    //存放所有AI的链表
    private java.util.List<AI> aiList= Collections.synchronizedList(new LinkedList<AI>());
    private static Player player;                      //游戏玩家
    private Timer shotThread=null;              //开火线程
    private GameArea gameArea=new GameArea();
    private Timer automaticBulletThread=null;   //自动步枪子弹飞行线程
    private Timer sniperBulletThread=null;      //狙击步枪子弹飞行线程
    private Timer pistolBulletThread=null;      //手枪子弹的飞行线程
    private Timer playerMoveThread=null;        //玩家移动的线程
    private Timer eliteSoldierMoveThread=null;  //精英战士移动线程
    private Timer reLiveAiThread=null;          //复活AI线程

    GameFrame()
    {

        gameArea=new GameArea();
        createPlayer();
        createAI();
        initial();
        //关闭窗口推出程序
        this.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                System.exit(0);
            }
        });
        this.setSize(width+6,high+35);
        this.setResizable(false);
        this.setLocationRelativeTo(null);
        this.add(gameArea);
        MusicPlayer.playBgm();
        this.setVisible(true);
    }
    private void initial()
    {
        /**
         * 控制玩家移动的线程
         */
        playerMoveThread=new Timer(player.getSpeed(), new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                Point oldPoint=player.getLocation();
                Point newPoint=new Point(oldPoint.x+player.getrSpeed()-player.getlSpeed(),oldPoint.y+player.getdSpeed()-player.getuSpeed());
                if(!ifhitWall(newPoint,player.getRadius()))
                    player.setLocation(newPoint);
            }
        });
        /**
         * 控制精英战士移动的线程
         */
        eliteSoldierMoveThread=new Timer(EliteSoldier.speed, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                for (EliteSoldier eliteSoldier:eliteSoldierList)
                {
                    if(eliteSoldier.hasNext())
                    {
                        eliteSoldier.nextStep();
                    }
                }
            }
        });
        /**
         * 初始化复活Ai线程
         */
        reLiveAiThread=new Timer(3000, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                for(EliteSoldier eliteSoldier:eliteSoldierList)
                {
                    if (eliteSoldier.ifDie())
                    {
                        eliteSoldier.setLocation(entrance[random.nextInt(entrance.length)]);
                        eliteSoldier.setPath(eliteSoldier.getLocation(),player.getLocation());
                        eliteSoldier.setDie(false);
                        eliteSoldier.setVisible(true);
                    }
                }
            }
        });
        eliteSoldierMoveThread.start();
        playerMoveThread.start();
        reLiveAiThread.start();
    }
    private void reLiveAi(AI ai)
    {

    }
    /**
     * 内部类
     * 游戏的画面显示区域
     *
     */
    class GameArea extends JPanel
    {
        GameArea()
        {
            initial();
            this.setSize(GameFrame.width,GameFrame.high);
            this.setLayout(null);           //设置为绝对布局
        }
        public void initial()
        {
            /**
             * 键盘监听器监听玩家移动，玩家切换武器，装填子弹
             */
            this.addKeyListener(new KeyAdapter() {
                @Override
                public void keyPressed(KeyEvent e)
                {
                    switch (e.getKeyCode())
                    {
                        case KeyEvent.VK_W:
                            player.setuSpeed(TravelSpeed.personTravelSpeed);
                            break;
                        case KeyEvent.VK_S:
                            player.setdSpeed(TravelSpeed.personTravelSpeed);
                            break;
                        case KeyEvent.VK_D:
                            player.setrSpeed(TravelSpeed.personTravelSpeed);
                            break;
                        case KeyEvent.VK_A:
                            player.setlSpeed(TravelSpeed.personTravelSpeed);
                            break;
                        case KeyEvent.VK_R:             //装填子弹
                            int weaponType=player.getUsingWeaponType();
                            //如果玩家当前使用的是枪,则可以换子弹
                            if(weaponType== WeaponType.automaticRifle || weaponType==WeaponType.sniperRifle || weaponType==WeaponType.pistol)
                            {
                                player.reLoad();
                            }
                            break;
                            //切换武器
                        case KeyEvent.VK_1:
                            player.changeWeapon(WeaponType.closedWeapon);
                            break;
                        case KeyEvent.VK_2:
                            player.changeWeapon(WeaponType.pistol);
                            break;
                        case KeyEvent.VK_3:
                            player.changeWeapon(WeaponType.automaticRifle);
                            break;
                        case KeyEvent.VK_4:
                            player.changeWeapon(WeaponType.sniperRifle);
                            break;
                        case KeyEvent.VK_5:
                            player.changeWeapon(WeaponType.grenade);
                            break;
                    }
                }
                @Override
                public void keyReleased(KeyEvent e)
                {
                    switch (e.getExtendedKeyCode())
                    {
                        case KeyEvent.VK_W:
                            player.setuSpeed(0);
                            break;
                        case KeyEvent.VK_S:
                            player.setdSpeed(0);
                            break;
                        case KeyEvent.VK_D:
                            player.setrSpeed(0);
                            break;
                        case KeyEvent.VK_A:
                            player.setlSpeed(0);
                            break;
                    }
                }
            });
            //将JPanel设置成可焦点化
            this.setFocusable(true);

            this.addMouseMotionListener(new MouseMotionAdapter() {
                @Override
                public void mouseDragged(MouseEvent e)
                {
                    endPoint=e.getPoint();
                }
                public void mouseMoved(MouseEvent e)
                {
                    endPoint=e.getPoint();
                }
            });
            //玩家射击
            this.addMouseListener(new MouseAdapter()
            {
                @Override
                public void mousePressed(MouseEvent mouseEvent)         //自动步枪连续扫射
                {
                    int fireRate = ((Gun) player.getUsingWeapon()).getFireRate();         //获取枪的射速sa
                    Point startPoint = getCentralPoint(player.getLocation());
                    attack(startPoint, endPoint, player);

                    shotThread = new Timer(fireRate, new ActionListener() {       //玩家开火
                        @Override
                        public void actionPerformed(ActionEvent e)
                        {
                            Point startPoint = getCentralPoint(player.getLocation());
                            attack(startPoint, endPoint, player);
                        }
                    });
                        shotThread.start();
                }
                @Override
                public void mouseReleased(MouseEvent e)
                {
                    MusicPlayer.stopContinueousShotMusic();
                    if(shotThread!=null && shotThread.isRunning())
                        shotThread.stop();
                }       //玩家停止开火
            });
            /**
             * 控制狙击步枪移动的线程
             */
            sniperBulletThread=new Timer(BulletSpeed.sniperBulletSpeed, new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e)
                {
                    if(!sniperBulletList.isEmpty())
                    {
                        int i=-1;
                        Bullet[] deleteBullet=new Bullet[sniperBulletList.size()];
                        for(Bullet bullet:sniperBulletList)
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
                                        bullet.setVisible(false);
                                    }
                                    else
                                    {
                                        ai.reduceHealthPoint(bullet.getDamageValue());
                                    }
                                    deleteBullet[++i]=bullet;       //将击中目标的子弹保存起来
                                    break;
                                }
                            }
                            if(!flag && (ifhitWall(bullet.getLocation(),bullet.getRadius())))    //判断子弹是否撞墙
                            {
                                deleteBullet[++i]=bullet ;           //将撞墙的子弹保存起来
                                bullet.setVisible(false);
                                // MusicPlayer.playBulletHitWallMusic();
                            }
                        }
                        if(i!=-1)
                        {
                            for(int j=0;j<=i;j++)
                            {
                                gameArea.remove(deleteBullet[j]);  //将子弹从gameArea中删除
                                sniperBulletList.remove(deleteBullet[j]);                //将子弹从自动步枪子弹中删除
                            }
                            gameArea.repaint();
                        }
                    }
                }
            });
            sniperBulletThread.start();
            /**
             *
             * 控制自动步枪自动移动的线程
             * 判断每个子弹是否撞到人，
             */
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
                            if(!flag && (ifhitWall(bullet.getLocation(),bullet.getRadius())))    //判断子弹是否撞墙
                            {
                                    bullet.setVisible(false);
                                   deleteBullet[++i]=bullet ;           //将撞墙的子弹保存起来
                               // MusicPlayer.playBulletHitWallMusic();
                            }
                        }
                        if(i!=-1)
                        {
                            for(int j=0;j<=i;j++)
                            {
                                gameArea.remove(deleteBullet[j]);  //将子弹从gameArea中删除
                                automaticBulletList.remove(deleteBullet[j]);                //将子弹从自动步枪子弹中删除
                            }
                            gameArea.repaint();
                        }
                    }
                }
            });
            automaticBulletThread.start();
        }
        public void paintComponent(Graphics g)
        {
            URL url=startGame.class.getResource("/images/backGround.png");
            ImageIcon icon=new ImageIcon(url);
            g.drawImage(icon.getImage(),0,0,GameFrame.width,GameFrame.high,null);
        }
    }
    //判断子弹是否撞墙,radius为物体的半径
    public boolean ifhitWall(Point point,int radius)
    {
        //
        try {
            if (point.x < 0 || point.y < 0 || point.x + radius > GameFrame.width || point.y + radius > GameFrame.high)
                return true;
            //分别判断物体的左上、右上、左下、右下角是与墙重
            int y = point.x / 20;       //物体在map中的纵坐标
            int x = point.y / 20;       //物体在map中的横坐标
            if (Wall.map[x][y] == 1) {
                return true;
            }
            y = (point.x + 2 * radius) / 20;
            x = point.y / 20;
            if (Wall.map[x][y] == 1) {
                return true;
            }
            y = (point.x / 20);
            x = (point.y + 2 * radius) / 20;
            if (Wall.map[x][y] == 1) {
                return true;
            }
            y = (point.x + 2 * radius) / 20;
            x = (point.y + 2 * radius) / 20;
            if (Wall.map[x][y] == 1) {
                return true;
            }
            return false;
        }
        catch (Exception ex)
        {
            return true;
        }
    }

    //创建子弹
    public void createBullet(Point start,Point end,int bulletType,int damageValue,int gunType)
    {
        int bulletRadius= BulletSize.getBulletRadius(bulletType);        //根据子弹的类型获取子弹的大小
        Bullet bullet=new Bullet(bulletType,bulletRadius,damageValue,TravelSpeed.bulletTravelSpeed,start,end);
        bullet.setSize(bulletRadius,bulletRadius);
        URL url=startGame.class.getResource("/images/red.png");
        ImageIcon icon=new ImageIcon(url);
        icon.setImage(icon.getImage().getScaledInstance(bulletRadius,bulletRadius,Image.SCALE_DEFAULT));
        bullet.setIcon(icon);
        switch (gunType)
        {
            case WeaponType.automaticRifle:
                automaticBulletList.add(bullet);
                break;
            case WeaponType.sniperRifle:
                sniperBulletList.add(bullet);
                break;
        }
        gameArea.add(bullet);
    }
    //间断性攻击
    private void attack(Point startPoint, Point endPoint, Person person)
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
            Gun gun=(Gun)weapon;
            if(!gun.emptyBulletNum() && !player.ifReloading()) //如果还有子弹
            {
                createBullet(startPoint, endPoint, ((Gun) weapon).getBulletType(), weapon.getDamageValue(), weapon.getType());
                gun.reduceBulletNum(1);     //子弹里面的弹夹减1
                MusicPlayer.playShotMusic(weapon.getWeaponName());
            }
            else                    //没有子弹
            {
                MusicPlayer.playBulletUseOutMusic();        //播放没有子弹的声音
                shotThread.stop();
                //MusicPlayer.stopContinueousShotMusic();
            }
        }
    }
    //创建玩家
    private void createPlayer()
    {
        player=new Player(1,"DJF",100);
        int size=2*(player.getRadius());
        player.setSize(size,size);
        URL url=startGame.class.getResource("/images/header_b.png");
        ImageIcon icon=new ImageIcon(url);
        icon.setImage(icon.getImage().getScaledInstance(size,size,Image.SCALE_DEFAULT));
        player.setIcon(icon);
        player.setLocation(400,300);
        gameArea.add(player);
        player.peekWeapon(new AKM(),100);
        player.peekWeapon(new AWM(),10);
    }
    //创建AI
    private void createAI()
    {
        int size=2*(new EliteSoldier(0).getRadius());
        URL url=startGame.class.getResource("/images/apple.png");
        ImageIcon icon=new ImageIcon(url);
        icon.setImage(icon.getImage().getScaledInstance(size,size,Image.SCALE_DEFAULT));
        for(int i=0;i<5;i++)
        {
            EliteSoldier eliteSoldier=new EliteSoldier(1);
            eliteSoldier.setSize(size,size);
            eliteSoldier.setLocation(entrance[random.nextInt(entrance.length)]);
            //eliteSoldier.setLocation(randomPoint());
            eliteSoldier.setIcon(icon);

            Point endPoint=player.getLocation();        //获取玩家坐标作为寻路终点
            Point startPoint=eliteSoldier.getLocation();    //AI当前坐标为寻路起点
            eliteSoldier.setPath(startPoint,endPoint);
            gameArea.add(eliteSoldier);
            aiList.add(eliteSoldier);
            eliteSoldierList.add(eliteSoldier);
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
        Point personCentralPoint=getCentralPoint(person.getLocation());
        if(bulletCentralPoint.distance(personCentralPoint) < bullet.getRadius()+person.getRadius())
            return true;
        return false;
    }
    public static Player getPlayer(){return player;}
}

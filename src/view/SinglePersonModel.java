package view;

import Arsenal.AKM;
import Arsenal.AWM;
import Arsenal.M4A1;
import reward.MedicalPackage;
import reward.RewardProp;
import reward.RewardType;
import Weapon.Gun;
import Weapon.*;
import Weapon.WeaponType;
import bullet.Bullet;
import bullet.BulletSize;
import bullet.BulletSpeed;
import bullet.BulletType;
import javafx.scene.transform.Rotate;
import person.*;
import utils.MusicPlayer;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Collections;
import java.util.LinkedList;
import java.util.Random;

/**
 * 该模式为单人模式
 */
public class SinglePersonModel extends JFrame
{
    private Point[] entrance=new Point[]{new Point(800,20),new Point(1040,280),new Point(1160,600), new Point(320,760),new Point(20,520)};         //刷怪位置
    private Point endPoint=new Point();
    private Random random=new Random();
    public final static int CELL=20;   //每个方格的大小
    public final static int gameAreaWidth =1200;
    public final static int gameAreaHeight=800;
    public final static int gameFrameWidth=1206;
    public final static int gameFrameHeight=974;
    private JLabel healthLevelTip=new JLabel("生命值");
    public static JProgressBar healthLevel=new JProgressBar(0,Player.maxHealthPoint);        //显示玩家生命的进度条
    private static LinkedList<Player> otherPlayer=new LinkedList<Player>();    //存放其他玩家
    //存放自动步枪子弹的链表
    private static java.util.List<Bullet> automaticBulletList = Collections.synchronizedList(new LinkedList<Bullet>());
    //存放狙击枪子弹的链表
    private static java.util.List<Bullet> sniperBulletList =Collections.synchronizedList(new LinkedList<Bullet>());
    //存放地雷的链表
    private static java.util.List<Mine> mineList=Collections.synchronizedList(new LinkedList<Mine>());
    //存放精英战士的链表
    private static java.util.List<EliteSoldier> eliteSoldierList=Collections.synchronizedList(new LinkedList<EliteSoldier>());
    //存放隐匿者的链表
    private static java.util.List<Hider> hiderList=Collections.synchronizedList(new LinkedList<Hider>());
    //存放掉落物品的链表
    private static java.util.List<RewardProp> rewardPropList=Collections.synchronizedList(new LinkedList<RewardProp>());
    //存放所有Person的链表
    private static java.util.List<Person> personList=Collections.synchronizedList(new LinkedList<Person>());
    //存放物品栏
    public static JLabel[] itemBars=new JLabel[]{new JLabel(),new JLabel(),new JLabel(),new JLabel()};
    private static Player player;                      //游戏玩家
    private Timer shotThread=null;              //开火线程
    private GameArea gameArea=new GameArea();
    private Timer automaticBulletThread=null;   //自动步枪子弹飞行线程
    private Timer sniperBulletThread=null;      //狙击步枪子弹飞行线程
    private Timer pistolBulletThread=null;      //手枪子弹的飞行线程
    private Timer playerMoveThread=null;        //玩家移动的线程
    private Timer eliteSoldierMoveThread=null;  //精英战士移动线程
    private Timer hiderMoveThread=null;         //隐匿者线程
    private Timer reLiveAiThread=null;          //复活AI线程


    SinglePersonModel()
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
        this.setSize(gameFrameWidth,gameFrameHeight);
        this.setResizable(false);
        this.setLocationRelativeTo(null);
        this.add(gameArea);
        MusicPlayer.playActionMusic();
        this.setVisible(true);
    }

    /**
     * 初始化单人模式
     */
    private void initialSinglePersonModel()
    {

    }
    private void initial()
    {
        /**
         * 初始化控制玩家移动的线程
         */
        playerMoveThread=new Timer(player.getSpeed(), new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                Point oldPoint=player.getLocation();
                Point newPoint=new Point(oldPoint.x+player.getrSpeed()-player.getlSpeed(),oldPoint.y+player.getdSpeed()-player.getuSpeed());
                if(!ifHitWall(newPoint,player.getRadius(),false))
                    player.setLocation(newPoint);
            }
        });
        /**
         * 初始化控制精英战士移动的线程
         */
        eliteSoldierMoveThread=new Timer(EliteSoldier.speed, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                boolean gameOver=false;
                java.util.List<Mine> list=new LinkedList<Mine>();   //存放爆炸的地雷
                for (EliteSoldier eliteSoldier:eliteSoldierList)
                {
                    //判断精英战士是否踩了地雷
                    for(Mine mine :mineList)
                    {
                        int damageValue=mine.getDamageValue();
                        Point minePoint=getCentralPoint(mine.getLocation());
                        Point eliteSoldierPoint=getCentralPoint(eliteSoldier.getLocation());
                        //如果精英战士踩到地雷,并且这个地雷不是自己埋下的
                        if(ifStepMine(mine,eliteSoldier))
                        {
                            list.add(mine);
                            mine.boom(gameArea,eliteSoldierPoint);
                            mine.setVisible(false);
                            //寻找在爆炸半径内的所有人
                            for(Person person:personList)
                            {
                                //精英战士的中心位置
                                Point cp=getCentralPoint(person.getLocation());
                                //如果在爆炸范围内,并且精英战士不是死亡状态
                                if(cp.distance(minePoint)<mine.getDamageRadius() && !person.ifDie())
                                {
                                    person.dieSpecialEffect(gameArea);
                                    person.setDie(true);
                                    //如果被炸到的是玩家
                                    if(person instanceof Player)
                                    {
                                        int choice= JOptionPane.showConfirmDialog(null,"你扑街了！再来一把？","",JOptionPane.YES_NO_CANCEL_OPTION);
                                        if(choice==0)
                                        {
                                            restStart();
                                            gameOver=true;
                                            break;
                                        }
                                        else
                                        {
                                            System.exit(0);
                                        }
                                    }
                                }
                            }
                        }
                        if (gameOver)
                            break;
                    }
                    for(Mine mine:list)
                    {
                        mineList.remove(mine);
                    }

                    //判断精英战士是否发现玩家，如果发现则开火
                    if(eliteSoldier.isIfFindPlayer(player.getLocation()) && !eliteSoldier.ifDie())
                    {
                        eliteSoldier.shot(player.getLocation(),gameArea);
                    }
                    else
                    {
                        eliteSoldier.stopShot();
                    }
                    //判断精英战士当前路径是否完成，如果完成则生成新的路径，否则继续走
                    if(eliteSoldier.hasNext())
                    {
                        eliteSoldier.nextStep();
                    }
                    else
                    {
                        eliteSoldier.setPath(eliteSoldier.getLocation(),player.getLocation());
                    }
                    if (gameOver)
                        break;
                }
            }
        });
        hiderMoveThread=new Timer(Hider.speed, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                boolean gameOver=false;
                java.util.List<Mine> list=new LinkedList<Mine>();   //存放爆炸的地雷
                for (Hider hider:hiderList)
                {
                    //判断隐匿者是否踩到地雷s
                    for(Mine mine :mineList)
                    {
                        Point minePoint=getCentralPoint(mine.getLocation());
                        Point hiderSoldierPoint=getCentralPoint(hider.getLocation());
                        //如果隐匿者踩到雷,并且这个地雷不是自己埋下的
                        if(ifStepMine(mine,hider))
                        {
                            list.add(mine);
                            mine.boom(gameArea,hiderSoldierPoint);
                            mine.setVisible(false);
                            //寻找在爆炸半径内的所有人
                            for(Person person:personList)
                            {
                                person.dieSpecialEffect(gameArea);
                                person.setDie(true);
                                //如果被炸到的是玩家
                                if(person instanceof Player)
                                {
                                    int choice= JOptionPane.showConfirmDialog(null,"你扑街了！再来一把？","",JOptionPane.YES_NO_CANCEL_OPTION);
                                    if(choice==0)
                                    {
                                        restStart();
                                        gameOver=true;
                                        break;
                                    }
                                    else
                                    {
                                        System.exit(0);
                                    }
                                }
                            }
                        }
                        if (gameOver)
                            break;
                    }
                    for(Mine mine:list)
                    {
                        mineList.remove(mine);
                    }

                    //判断精英战士是否发现玩家，如果发现则开火
                    if(hider.isIfFindPlayer(player.getLocation()) && !hider.ifDie())
                    {
                        hider.shot(player.getLocation(),gameArea);
                    }
                    else
                    {
                        hider.stopShot();
                    }
                    //判断精英战士当前路径是否完成，如果完成则生成新的路径，否则继续走
                    if(hider.hasNext() && !hider.isIfFindPlayer(player.getLocation()))
                    {
                        hider.nextStep();
                    }
                    else
                    {
                        hider.setPath(hider.getLocation(),player.getLocation());
                    }
                    if (gameOver)
                        break;
                }
            }
        });
        hiderMoveThread.start();

        /**
         * 初始化复活Ai线程
         */
        reLiveAiThread=new Timer(3000, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                for(Person person:personList)
                {
                    if (person.ifDie() && !(person instanceof Player))
                    {
                        AI ai=(AI)person;
                        ai.setLocation(entrance[random.nextInt(entrance.length)]);
                        ai.setPath(ai.getLocation(),player.getLocation());
                        ai.setDie(false);
                        ai.setVisible(true);
                        ai.addHealthPoint(EliteSoldier.healthPoint);
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
            initialThread();
            healthLevel.setStringPainted(true); //显示玩家生命值百分比
            healthLevel.setSize(120,30);
            healthLevel.setLocation(50,10);
            healthLevelTip.setLocation(10,10);
            healthLevelTip.setSize(60,30);
            healthLevelTip.setFont(new Font(null,Font.BOLD,20));
            healthLevelTip.setBackground(new Color(0xFD2016));

            this.add(healthLevel);
            this.add(healthLevelTip);
            this.setSize(SinglePersonModel.gameFrameWidth, SinglePersonModel.gameFrameHeight);
            this.setLayout(null);           //设置为绝对布局
        }

        /**
         * 初始化物品栏
         */
        private void initialItemBars()
        {
            itemBars[0].setSize(300,150);
            itemBars[1].setSize(300,150);
            itemBars[2].setSize(150,150);
            itemBars[3].setSize(150,150);
            itemBars[0].setLocation(0,800);
            itemBars[1].setLocation(400,800);
            itemBars[2].setLocation(800,800);
            itemBars[3].setLocation(1050,800);
           for(int i=0;i<itemBars.length;i++)
           {
               int k=i+1;
               int width=itemBars[i].getWidth();
               int height=itemBars[i].getHeight();
               URL url= SinglePersonModel.class.getResource("/images/itemBars/itemBar"+k+".png");
               ImageIcon icon=new ImageIcon(url);
               icon.setImage(icon.getImage().getScaledInstance(width,height,Image.SCALE_DEFAULT));
               itemBars[i].setIcon(icon);
               this.add(itemBars[i]);
           }
        }

        /**
         * 初始化子弹移动线程
         */
        private void initialThread()
        {
            initialItemBars();
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
                            if(weaponType== WeaponType.automaticRifle || weaponType==WeaponType.sniperRifle)
                            {
                                player.reLoad();
                            }
                            break;
                            //玩家捡起道具
                        case KeyEvent.VK_F:
                            Point playerPoint=player.getLocation();
                            int index=-1;
                            int i=0;
                            for(RewardProp rewardProp:rewardPropList)
                            {
                                if (playerPoint.distance(rewardProp.getLocation()) < CELL)
                                {
                                    //如果道具是医疗包
                                    MusicPlayer.playPeekRewardPropMusic();
                                    rewardProp.setVisible(false);
                                    if(rewardProp.getType()==0)
                                    {
                                        MedicalPackage medicalPackage=(MedicalPackage) rewardProp.getReward();
                                        player.addHealthPoint(medicalPackage.getHealthPoint());
                                    }
                                    //如果的hi地雷
                                    else if(rewardProp.getType()== RewardType.Mine)
                                    {
                                        player.peekWeapon((Weapon) rewardProp.getReward(),1);
                                    }
                                    //如果道具是枪
                                    else
                                    {
                                        player.peekWeapon((Weapon) rewardProp.getReward(),0);
                                    }
                                    index=i;
                                }
                                i++;
                            }
                            if(index!=-1)
                            {
                                gameArea.remove(rewardPropList.get(index));
                                rewardPropList.remove(index);
                            }
                            break;
                            //切换武器
                        case KeyEvent.VK_1:
                            player.changeWeapon(WeaponType.automaticRifle);
                            if (shotThread!=null &&shotThread.isRunning())
                                shotThread.stop();
                            break;
                        case KeyEvent.VK_2:
                            player.changeWeapon(WeaponType.sniperRifle);
                            if (shotThread!=null &&shotThread.isRunning())
                                shotThread.stop();
                            break;
                        case KeyEvent.VK_3:
                            player.changeWeapon(WeaponType.grenade);
                            if (shotThread!=null &&shotThread.isRunning())
                                shotThread.stop();
                            break;
                        case KeyEvent.VK_4:
                            player.changeWeapon(WeaponType.mine);
                            if(shotThread!=null && shotThread.isRunning())
                                shotThread.stop();
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
                    Point oldPoint=endPoint;
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
                public void mouseClicked(MouseEvent mouseEvent)        //非枪类武器攻击
                {
                    Weapon weapon=player.getUsingWeapon();
                    Point startPoint=getCentralPoint(player.getLocation());
                    if(!(weapon instanceof Gun))
                    {
                        attack(startPoint, endPoint, player);
                    }

                }
                public void mousePressed(MouseEvent mouseEvent)         //枪类武器攻击
                {
                    Weapon weapon=player.getUsingWeapon();
                    Point startPoint = getCentralPoint(player.getLocation());
                    if(weapon instanceof Gun)
                    {
                        int fireRate = ((Gun) weapon).getFireRate();
                        attack(startPoint, endPoint, player);
                        shotThread = new Timer(fireRate, new ActionListener() {       //玩家开火
                            @Override
                            public void actionPerformed(ActionEvent e) {
                                Point startPoint = getCentralPoint(player.getLocation());
                                attack(startPoint, endPoint, player);
                            }
                        });
                        shotThread.start();
                    }
                }
                @Override
                public void mouseReleased(MouseEvent e)
                {
                    Weapon weapon=player.getUsingWeapon();
                    if(weapon instanceof Gun && ((Gun)weapon).ifContinuedShot())
                    {
                        MusicPlayer.stopContinueAttackMusic();
                    }
                    player.setAttacking(false);
                    if(shotThread!=null && shotThread.isRunning())
                        shotThread.stop();
                }       //玩家停止开火
            });
            /**
             * 初始化控制狙击步枪子弹移动的线程
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
                            for(Person person:personList)          //判断每个人是否被子弹击中
                            {
                                if((ifHitPerson(bullet,person)) && !person.ifDie() && !person.equals(bullet.getFromPerson()))    //如果击中AI
                                {
                                    flag=true;
                                    bullet.setVisible(false);                   //击中目标后子弹消失
                                    int healthPoint =person.getHealthPoint();
                                    if(healthPoint-bullet.getDamageValue()<=0)      //如果目标死亡
                                    {
                                        person.dieSpecialEffect(gameArea);
                                        person.setVisible(false);
                                        person.setDie(true);            //设置AI死亡
                                        if(person instanceof Player)
                                        {
                                            int choice= JOptionPane.showConfirmDialog(null,"你扑街了！再来一把？","",JOptionPane.YES_NO_CANCEL_OPTION);
                                            if(choice==0)
                                            {
                                                restStart();
                                            }
                                            else
                                            {
                                                System.exit(0);
                                            }
                                        }
                                        createRewardProp(person.getLocation());     //掉落物品
                                    }
                                    else
                                    {
                                        person.reduceHealthPoint(bullet.getDamageValue());
                                    }
                                    deleteBullet[++i]=bullet;       //将击中目标的子弹保存起来
                                    break;
                                }
                            }
                            if(!flag && (ifHitWall(bullet.getLocation(),bullet.getRadius(),true)))    //判断子弹是否撞墙
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
                            for(Person person:personList)          //判断每个敌人是否被子弹击中
                            {
                                if((ifHitPerson(bullet,person)) && !person.ifDie() && !person.equals(bullet.getFromPerson()))    //如果击中AI
                                {
                                    flag=true;
                                    bullet.setVisible(false);                   //击中目标后子弹消失
                                    int healthPoint =person.getHealthPoint();
                                    if(healthPoint-bullet.getDamageValue()<=0)
                                    {
                                        person.setDie(true);            //设置人物死亡
                                        person.dieSpecialEffect(gameArea);
                                        if(person instanceof Player)
                                        {
                                            int choice= JOptionPane.showConfirmDialog(null,"你扑街了！再来一把？","",JOptionPane.YES_NO_CANCEL_OPTION);
                                            if(choice==0)
                                            {
                                                restStart();
                                            }
                                            else
                                            {
                                                System.exit(0);
                                            }
                                        }
                                        person.setVisible(false);
                                        createRewardProp(person.getLocation());     //掉落物品
                                    }
                                    else
                                    {
                                        person.reduceHealthPoint(bullet.getDamageValue());
                                    }
                                    deleteBullet[++i]=bullet;       //将击中目标的子弹保存起来
                                    break;
                                }
                            }
                            if(!flag && (ifHitWall(bullet.getLocation(),bullet.getRadius(),true)))    //判断子弹是否撞墙
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
            //初始化地雷监听线程
        }
        public void paintComponent(Graphics g)
        {
            URL url=startGame.class.getResource("/images/backGround.png");
            ImageIcon icon=new ImageIcon(url);
            g.drawImage(icon.getImage(),0,0, SinglePersonModel.gameAreaWidth, SinglePersonModel.gameAreaHeight,null);
        }
    }

    /**
     * 判断物体是否撞墙，子弹可以穿过河，人不可以，所以判别要区分人和子弹
     * @param point：物体的左上角的坐标
     * @param radius：物体的半径
     * @param isBullet：该物体是子弹的话，则isBullet=true
     * @return
     */
    public boolean ifHitWall(Point point,int radius,boolean isBullet)
    {
        //
        try {
            //先判断物体是否超出地图边界
            if (point.x < 0 || point.y < 0 || point.x + radius > SinglePersonModel.gameAreaWidth || point.y + radius > SinglePersonModel.gameAreaHeight)
                return true;
            //分别判断物体的左上、右上、左下、右下角是与墙重
            if(!isBullet)
            {
                int y = point.x / 20;       //物体在map中的纵坐标
                int x = point.y / 20;       //物体在map中的横坐标
                if (Wall.map[x][y] !=0) {
                    return true;
                }
                y = (point.x + 2 * radius) / 20;
                x = point.y / 20;
                if (Wall.map[x][y] != 0) {
                    return true;
                }
                y = (point.x / 20);
                x = (point.y + 2 * radius) / 20;
                if (Wall.map[x][y] != 0) {
                    return true;
                }
                y = (point.x + 2 * radius) / 20;
                x = (point.y + 2 * radius) / 20;
                if (Wall.map[x][y] != 0) {
                    return true;
                }
            }
            else
            {
                int y = point.x / 20;       //物体在map中的纵坐标
                int x = point.y / 20;       //物体在map中的横坐标
                if (Wall.map[x][y] ==1) {
                    return true;
                }
                y = (point.x + 2 * radius) / 20;
                x = point.y / 20;
                if (Wall.map[x][y] ==1) {
                    return true;
                }
                y = (point.x / 20);
                x = (point.y + 2 * radius) / 20;
                if (Wall.map[x][y] ==1) {
                    return true;
                }
                y = (point.x + 2 * radius) / 20;
                x = (point.y + 2 * radius) / 20;
                if (Wall.map[x][y] ==1) {
                    return true;
                }
            }
            return false;
        }
        catch (Exception ex)
        {
            return true;
        }
    }

    /**
     * 创建子弹
     * @param fromPerson：射出这个子弹的人
     * @param start：子弹移动的初始坐标
     * @param end：与start构建成一次函数关系
     * @param bulletType：子弹种类
     * @param damageValue：子弹伤害大小
     * @param gunType：射出该子弹的武器类型
     */
    public void createBullet(Person fromPerson,Point start,Point end,int bulletType,int damageValue,int gunType)
    {
        int bulletRadius= BulletSize.getBulletRadius(bulletType);        //根据子弹的类型获取子弹的大小
        Bullet bullet=new Bullet(fromPerson,bulletType,bulletRadius,damageValue,TravelSpeed.bulletTravelSpeed,start,end);
        bullet.setSize(bulletRadius,bulletRadius);
        URL url=startGame.class.getResource("/images/bullet/Bullet.png");
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

    /**
     * 创建掉落在地上的奖励道具
     * @param point：道具在地图上显示的位置
     */
    public void createRewardProp(Point point)
    {
        //进行一次抽签，看脸掉不掉道具
        int result=random.nextInt(100);
        if(result%10==0)
        {
            int type=random.nextInt(RewardType.typeNum);
            RewardProp rewardProp=new RewardProp(type,point);
            rewardPropList.add(rewardProp);
            gameArea.add(rewardProp);
            gameArea.repaint();
        }
    }

    /**
     * person进行攻击
     * @param startPoint：子弹移动的初始位置
     * @param endPoint：
     * @param person
     */
    private void attack(Point startPoint, Point endPoint, Person person)
    {
        Weapon weapon=person.getUsingWeapon();
        //如果是投掷类武器
        if(weapon.getType()==WeaponType.grenade)
        {

        }
        //如果是地雷
        else if(weapon.getType()==WeaponType.mine)
        {
            if(!person.ifEmptyMine())
            {
                MusicPlayer.playDiscontinueAttackMusic(weapon.getWeaponName());
                stepMine(player.getLocation(), person);
            }
            else
            {
                MusicPlayer.playBulletUseOutMusic();
            }
        }
        //如果是狙击步枪
        else if(weapon.getType()==WeaponType.sniperRifle)
        {
            SniperRifle gun=(SniperRifle) weapon;
            //判断狙击枪是否在上膛或拉栓状态，如果不是则可以开枪
            if(!player.ifReloading() && !gun.isPollBolt())
            {
                if (!gun.emptyBulletNum() ) //如果还有子弹
                {
                    gun.setPollBolt(true);      //狙击枪进入拉栓状态
                    createBullet(player,startPoint, endPoint, BulletType.k127, weapon.getDamageValue(), weapon.getType());
                    gun.reduceBulletNum(1);     //子弹里面的弹夹减1
                    MusicPlayer.playDiscontinueAttackMusic(weapon.getWeaponName());
                } else                    //没有子弹
                {
                    MusicPlayer.playBulletUseOutMusic();        //播放没有子弹的声音
                    shotThread.stop();
                }
            }
        }
        //如果是自动步枪
        else if(weapon.getType()==WeaponType.automaticRifle)
        {
            AutomaticRifle gun=(AutomaticRifle) weapon;
            if(!player.ifReloading() )
            {
                if (!gun.emptyBulletNum() ) //如果还有子弹
                {
                    createBullet(player,startPoint, endPoint, ((Gun) weapon).getBulletType(), weapon.getDamageValue(), weapon.getType());
                    gun.reduceBulletNum(1);     //子弹里面的弹夹减1
                    if(!person.isAttacking())
                    {
                        MusicPlayer.playContinueAttackMusic(gun.getWeaponName());
                        person.setAttacking(true);
                    }
                } else                    //没有子弹
                {
                    if(person.isAttacking())
                    {
                        MusicPlayer.stopContinueAttackMusic();
                    }
                    MusicPlayer.playBulletUseOutMusic();        //播放没有子弹的声音
                    shotThread.stop();
                }
            }
        }
    }
    //重新开始游戏
    public void restStart()
    {
        MusicPlayer.stopActionMusic();
        for(Person person:personList)
        {
            gameArea.remove(person);
        }
        for(EliteSoldier eliteSoldier:eliteSoldierList)
        {
            eliteSoldier.stopShot();
        }
        for(Hider hider :hiderList)
        {
            hider.stopShot();
        }
        for(Bullet bullet:automaticBulletList)
        {
            gameArea.remove(bullet);
        }
        for(Bullet bullet:sniperBulletList)
        {
            gameArea.remove(bullet);
        }
        for(RewardProp rewardProp:rewardPropList)
        {
            gameArea.remove(rewardProp);
        }
        for(Mine mine:mineList)
        {
            gameArea.remove(mine);
        }
        mineList.clear();
        automaticBulletList.clear();
        eliteSoldierList.clear();
        hiderList.clear();
        sniperBulletList.clear();
        rewardPropList.clear();
        personList.clear();
        System.gc();
        createPlayer();
        createAI();
        MusicPlayer.playActionMusic();
        gameArea.repaint();
    }
    //创建玩家
    private void createPlayer()
    {
        try {
            Rotate rotate = new Rotate();
            player = new Player("1", "DJF");
            int size = 2 * (player.getRadius());
            player.setSize(size, size);
            InputStream is = startGame.class.getResourceAsStream("/images/header_b.png");
            BufferedImage bufferedImage = ImageIO.read(is);
            ImageIcon icon = new ImageIcon();
            icon.setImage(bufferedImage);
            icon.setImage(icon.getImage().getScaledInstance(size, size, Image.SCALE_DEFAULT));
            player.setIcon(icon);
            player.setLocation(400, 300);
            gameArea.add(player);
            player.peekWeapon(new AWM(), 100);
            player.peekWeapon(new Mine(),5);
            player.peekWeapon(new M4A1(), 10000);
            personList.add(player);
            healthLevel.setValue(player.getHealthPoint());

        }
        catch (IOException ioe)
        {
            ioe.printStackTrace();
        }
    }
    //安装地雷
    private void stepMine(Point point,Person fromPerson)
    {
        fromPerson.reduceMineNum(1);
        Mine mine=new Mine();
        mine.setFromPerson(fromPerson);
        mine.setLocation(point);
        URL url= SinglePersonModel.class.getResource("/images/Weapon/BoomWeapon/Mine.png");
        ImageIcon icon=new ImageIcon(url);
        icon.setImage(icon.getImage().getScaledInstance(20,20,Image.SCALE_DEFAULT));
        mine.setSize(20,20);
        mine.setIcon(icon);
        mineList.add(mine);
        gameArea.add(mine);

    }
    //创建AI
    private void createAI()
    {
        //创建精英战士
        for(int i=0;i<5;i++)
        {
            EliteSoldier eliteSoldier=new EliteSoldier(personList.size()+"");
            eliteSoldier.setLocation(entrance[random.nextInt(entrance.length)]);
            Point endPoint=player.getLocation();        //获取玩家坐标作为寻路终点
            Point startPoint=eliteSoldier.getLocation();    //AI当前坐标为寻路起点
            eliteSoldier.setPath(startPoint,endPoint);
            gameArea.add(eliteSoldier);
            eliteSoldierList.add(eliteSoldier);
            personList.add(eliteSoldier);
        }
        for(int i=0;i<2;i++)
        {
            Hider hider=new Hider(personList.size()+"");
            hider.setLocation(entrance[random.nextInt(entrance.length)]);
            Point endPoint=player.getLocation();        //获取玩家坐标作为寻路终点
            Point startPoint=hider.getLocation();    //AI当前坐标为寻路起点
            hider.setPath(startPoint,endPoint);
            gameArea.add(hider);
            hiderList.add(hider);
            personList.add(hider);
        }
    }
    //是否踩到地雷
    private boolean ifStepMine(Mine mine,Person person)
    {
        Point minePoint=getCentralPoint(mine.getLocation());
        Point eliteSoldierPoint=getCentralPoint(person.getLocation());
        if(minePoint.distance(eliteSoldierPoint) < mine.getRadius() && !person.equals(mine.getFromPerson()))
        {
            return true;
        }
        return false;
    }
    //随机生成一个坐标
    private Point randomPoint()
    {
        int x=random.nextInt(SinglePersonModel.gameAreaWidth /CELL)*CELL;
        int y=random.nextInt(SinglePersonModel.gameAreaHeight/CELL)*CELL;
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
    public static java.util.List getAutomaticBulletList()
    {
        return automaticBulletList;
    }
    public static java.util.List getSniperBulletList()
    {
        return sniperBulletList;
    }

}

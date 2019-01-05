package view;

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
    private JFrame superiorMenu;
    private Point[] entrance=new Point[]{new Point(800,20),new Point(1040,280),new Point(1160,600), new Point(320,760),new Point(20,520)};         //刷怪位置
    private Point mousePoint =new Point();      //鼠标当前制作位置
    private Random random=new Random();
    public final static int CELL=20;   //每个方格的大小
    public final static int gameAreaWidth =1200;
    public final static int gameAreaHeight=950;
    public final static int gameFrameWidth=1206;
    public final static int gameFrameHeight=974;
    private JTextField healthLevelTip=new JTextField("生命值");
    public  JProgressBar healthLevel=new JProgressBar(0,Player.maxHealthPoint);        //显示玩家生命的进度条
    public  JTextField bulletLeft=new JTextField();                           //显示武器子弹剩余量
    public  JTextField killAndDieField =new  JTextField();                             //显示玩家击杀死亡数
    public  JLabel usingWeaponFlag=new JLabel();                   //玩家当前使用武器的标记
    public  Point []flagPoint=new Point[4];
    //存放自动步枪子弹的链表
    private  java.util.List<Bullet> automaticBulletList = Collections.synchronizedList(new LinkedList<Bullet>());
    //存放狙击枪子弹的链表
    private  java.util.List<Bullet> sniperBulletList =Collections.synchronizedList(new LinkedList<Bullet>());
    //存放地雷的链表
    private  java.util.List<Mine> mineList=Collections.synchronizedList(new LinkedList<Mine>());
    //存放手雷的链表
    private  java.util.List<Grenade> grenadeList=Collections.synchronizedList(new LinkedList<Grenade>());
    //存放精英战士的链表
    private  java.util.List<EliteSoldier> eliteSoldierList=Collections.synchronizedList(new LinkedList<EliteSoldier>());
    //存放隐匿者的链表
    private  java.util.List<Hider> hiderList=Collections.synchronizedList(new LinkedList<Hider>());
    //存放掉落物品的链表
    private  java.util.List<RewardProp> rewardPropList=Collections.synchronizedList(new LinkedList<RewardProp>());
    //存放所有Person的链表
    private  java.util.List<Person> personList=Collections.synchronizedList(new LinkedList<Person>());
    //存放物品栏
    public  JLabel[] itemBars=new JLabel[]{new JLabel(),new JLabel(),new JLabel(),new JLabel()};
    private static Player player;                      //游戏玩家
    private Timer shotThread=null;              //开火线程
    private GameArea gameArea=null;             //游戏显示区域
    private Timer automaticBulletThread=null;   //自动步枪子弹飞行线程
    private Timer sniperBulletThread=null;      //狙击步枪子弹飞行线程
    private Timer playerMoveThread=null;        //玩家移动的线程
    private Timer eliteSoldierMoveThread=null;  //精英战士移动线程
    private Timer hiderMoveThread=null;         //隐匿者线程
    private Timer reLiveAiThread=null;          //复活AI线程
    private Timer grenadeMoveThread=null;       //控制手雷移动的线程
    SinglePersonModel( JFrame superiorMenu)
    {
        gameArea=new GameArea();
        this.superiorMenu=superiorMenu;
        createPlayer();
        createAI();
        initialPersonMoveThread();
        //关闭窗口推出程序
        this.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e)
            {
                leaveGame();
            }
        });
        this.setSize(gameFrameWidth,gameFrameHeight);
        this.setResizable(false);
        this.setLocationRelativeTo(null);
        this.add(gameArea);
        MusicPlayer.playActionBGM();
        this.setVisible(true);
    }

    /**
     * 初始化人物移动线程
     */
    private void initialPersonMoveThread()
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
                for (EliteSoldier eliteSoldier:eliteSoldierList)
                {
                    //判断精英战士是否踩了地雷

                   for(int i=0;i<mineList.size();i++)
                    {
                        Mine mine=mineList.get(i);
                        int damageValue=mine.getDamageValue();
                        Point minePoint=getCentralPoint(mine.getLocation());
                        Point eliteSoldierPoint=getCentralPoint(eliteSoldier.getLocation());
                        //如果精英战士踩到地雷,并且这个地雷不是自己埋下的
                        if(ifStepMine(mine,eliteSoldier))
                        {
                            mineList.remove(i);
                            mine.boom(gameArea,eliteSoldierPoint);
                            gameArea.remove(mine);
                            mine.setVisible(false);
                            //寻找在爆炸半径内的所有人
                            for(Person person:personList)
                            {
                                //精英战士的中心位置
                                Point cp=getCentralPoint(person.getLocation());
                                //如果在爆炸范围内,并且人物不是死亡状态
                                if(cp.distance(minePoint)<mine.getDamageRadius() && !person.ifDie())
                                {
                                    person.dieSpecialEffect(gameArea);
                                    person.setDie(true);
                                    person.setVisible(false);
                                    //如果被炸到的是玩家
                                    if(person instanceof Player)
                                    {
                                        int choice= JOptionPane.showConfirmDialog(null,"你扑街了！再来一把？","",JOptionPane.YES_NO_CANCEL_OPTION);
                                        if(choice==0)
                                        {
                                            restStart();
                                            gameOver=true;
                                            healthLevel.setValue(0);
                                            break;
                                        }
                                        else
                                        {
                                            leaveGame();
                                        }
                                    }
                                    Person fromPerson=null;
                                    for(Person p:personList)
                                    {
                                        if(p.getId().equals(mine.getFromPersonId()))
                                            fromPerson=p;
                                    }
                                    fromPerson.addKillNum(1);
                                    if(fromPerson.equals(player))
                                    {
                                        killAndDieField.setText("击杀/死亡：（"+player.getKillNum()+"/"+player.getDieNum()+")");
                                    }
                                }
                            }
                        }
                        if (gameOver)
                            break;
                    }

                    //判断精英战士是否发现玩家，如果发现则开火
                    if(eliteSoldier.isIfFindPlayer(player.getLocation()) && !eliteSoldier.ifDie())
                    {
                        eliteSoldier.shot(player.getLocation(),gameArea,automaticBulletList);
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
        /**
         * 初始化隐匿者移动线程
         * 隐匿者是狙击手，当然不可能踩地雷啦
         */
        hiderMoveThread=new Timer(Hider.speed, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                boolean gameOver=false;
                java.util.List<Mine> list=new LinkedList<Mine>();   //存放爆炸的地雷
                for (Hider hider:hiderList)
                {

                    //判断精英战士是否发现玩家，如果发现则开火
                    if(hider.isIfFindPlayer(player.getLocation()) && !hider.ifDie())
                    {
                        hider.shot(player.getLocation(),gameArea,automaticBulletList);
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
    /**
     * 内部类
     * 游戏的画面显示区域
     *
     */
    class GameArea extends JPanel
    {
        GameArea()
        {
            initialBulletMoveThread();
            healthLevel.setStringPainted(true); //显示玩家生命值百分比
            healthLevel.setSize(120,30);
            healthLevel.setLocation(100,10);
            healthLevel.setForeground(new Color(0xFD2016));
            healthLevel.setValue(Player.maxHealthPoint);

            healthLevelTip.setLocation(10,10);
            healthLevelTip.setSize(70,30);
            healthLevelTip.setFont(new Font(null,Font.BOLD,20));
            healthLevelTip.setBackground(new Color(0xFD2016));
            healthLevelTip.setEditable(false);

            bulletLeft.setFont(new Font(null,Font.BOLD,20));
            bulletLeft.setSize(180,30);
            bulletLeft.setLocation(10,50);
            bulletLeft.setEditable(false);
            bulletLeft.setBackground(new Color(0x75BCE2));

            killAndDieField.setText("击杀/死亡：(0/0)");
            killAndDieField.setFont(new Font(null,Font.BOLD,20));
            killAndDieField.setSize(180,30);
            killAndDieField.setLocation(1000,10);
            killAndDieField.setBackground(new Color(0xED4078));
            killAndDieField.setEditable(false);

            URL url= SinglePersonModel.class.getResource("/images/logo/usingWeaponFlag.png");
            ImageIcon icon=new ImageIcon(url);
            icon.setImage(icon.getImage().getScaledInstance(20,20,Image.SCALE_DEFAULT));
            usingWeaponFlag.setSize(20, 20);
            usingWeaponFlag.setIcon(icon);
            flagPoint[0]=new Point(10,820);
            flagPoint[1]=new Point(400,820);
            flagPoint[2]=new Point(790,820);
            flagPoint[3]=new Point(1020,820);

            this.add(usingWeaponFlag);
            this.add(healthLevel);
            this.add(healthLevelTip);
            this.add(bulletLeft);
            this.add(killAndDieField);
            this.setSize(1200, 950);
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
        private void initialBulletMoveThread()
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
                            Weapon usingWeapon=player.getUsingWeapon();
                            //如果玩家当前使用的是枪,则可以换子弹
                            if(usingWeapon instanceof Gun)
                            {
                                player.reLoad();
                                int bulletLeftInGun=((Gun)usingWeapon).getBulletLeft();
                                int bulletLeftOnPerson=player.getBulletLeftOnPerson();
                                bulletLeft.setText("子弹："+bulletLeftInGun+"/"+bulletLeftOnPerson);
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
                                        healthLevel.setValue(player.getHealthPoint());
                                    }
                                    //如果是地雷或者是手雷
                                    else if(rewardProp.getType()== RewardType.Mine || rewardProp.getType()==RewardType.Grenade)
                                    {
                                        player.peekWeapon((Weapon) rewardProp.getReward(),1);
                                        int left=player.getBulletLeftOnPerson();
                                        bulletLeft.setText("子弹："+left);
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
                        case KeyEvent.VK_1: {
                            player.changeWeapon(WeaponType.automaticRifle, gameArea);
                            if (shotThread != null && shotThread.isRunning())
                                shotThread.stop();
                            int bulletLeftInGun = ((Gun) player.getUsingWeapon()).getBulletLeft();
                            int bulletLeftOnPerson = player.getBulletLeftOnPerson();
                            bulletLeft.setText("子弹：" + bulletLeftInGun + "/" + bulletLeftOnPerson);
                            Point point= flagPoint[player.getUsingWeaponType()-1];
                            usingWeaponFlag.setLocation(point);
                            break;
                        }
                        case KeyEvent.VK_2: {
                            player.changeWeapon(WeaponType.sniperRifle, gameArea);
                            if (shotThread != null && shotThread.isRunning())
                                shotThread.stop();
                            int bulletLeftInGun = ((Gun) player.getUsingWeapon()).getBulletLeft();
                            int bulletLeftOnPerson = player.getBulletLeftOnPerson();
                            bulletLeft.setText("子弹：" + bulletLeftInGun + "/" + bulletLeftOnPerson);
                            Point point= flagPoint[player.getUsingWeaponType()-1];
                            usingWeaponFlag.setLocation(point);
                            break;
                        }
                        case KeyEvent.VK_3: {
                            player.changeWeapon(WeaponType.grenade, gameArea);
                            if (shotThread != null && shotThread.isRunning())
                                shotThread.stop();
                            int grenadeLeft=player.getBulletLeftOnPerson();
                            bulletLeft.setText("手雷："+grenadeLeft);
                            Point point= flagPoint[player.getUsingWeaponType()-1];
                            usingWeaponFlag.setLocation(point);
                            break;
                        }
                        case KeyEvent.VK_4: {
                            player.changeWeapon(WeaponType.mine, gameArea);
                            if (shotThread != null && shotThread.isRunning())
                                shotThread.stop();
                            int mineLeft=player.getBulletLeftOnPerson();
                            bulletLeft.setText("地雷："+mineLeft);
                            Point point= flagPoint[player.getUsingWeaponType()-1];
                            usingWeaponFlag.setLocation(point);
                            break;
                        }
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
                    Point oldPoint= mousePoint;
                    mousePoint =e.getPoint();
                }
                public void mouseMoved(MouseEvent e)
                {
                    mousePoint =e.getPoint();
                }
            });
            //玩家射击
            this.addMouseListener(new MouseAdapter()
            {
                public void mousePressed(MouseEvent mouseEvent)         //玩家攻击
                {
                    Weapon weapon=player.getUsingWeapon();
                    Point startPoint = getCentralPoint(player.getLocation());
                    //如果是非枪类武器攻击
                    if(!(weapon instanceof Gun))
                    {
                        attack(startPoint, mousePoint, player);
                    }
                    //如果是枪类武器攻击
                    else if(weapon instanceof Gun)
                    {
                        int fireRate = ((Gun) weapon).getFireRate();
                        attack(startPoint, mousePoint, player);
                        shotThread = new Timer(fireRate, new ActionListener() {       //玩家开火
                            @Override
                            public void actionPerformed(ActionEvent e) {
                                Point startPoint = getCentralPoint(player.getLocation());
                                attack(startPoint, mousePoint, player);
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

                       for(int i=0;i<sniperBulletList.size();i++)
                        {
                            Bullet bullet=sniperBulletList.get(i);
                            boolean flag=false;             //标记该子弹是否击中人
                            Point oldPoint=bullet.getLocation();
                            Point newPoint=new Point(oldPoint.x+bullet.getxSpeed(),oldPoint.y+bullet.getySpeed());
                            bullet.setLocation(newPoint);
                            for(Person person:personList)          //判断每个人是否被子弹击中
                            {
                                if((ifHitPerson(bullet,person)) && !person.ifDie() && !person.getId().equals(bullet.getFromPersonId()))    //如果击中AI
                                {
                                    flag=true;
                                    sniperBulletList.remove(i);
                                    gameArea.remove(bullet);
                                    int healthPoint =person.getHealthPoint();
                                    if(healthPoint-bullet.getDamageValue()<=0)      //如果目标死亡
                                    {
                                        Person fromPerson=personList.get(Integer.parseInt(bullet.getFromPersonId()));       //这颗子弹的所有者击杀数加1
                                        fromPerson.addKillNum(1);
                                        if(fromPerson.equals(player))
                                        {
                                            killAndDieField.setText("击杀/死亡：（"+player.getKillNum()+"/"+player.getDieNum()+")");
                                        }
                                        person.dieSpecialEffect(gameArea);
                                        person.setVisible(false);
                                        person.setDie(true);            //人物AI死亡
                                        if(person instanceof Player)
                                        {
                                            healthLevel.setValue(0);
                                            int choice= JOptionPane.showConfirmDialog(null,"你扑街了！再来一把？","",JOptionPane.YES_NO_CANCEL_OPTION);
                                            if(choice==0)
                                            {
                                                restStart();
                                            }
                                            else
                                            {
                                                leaveGame();
                                            }
                                        }
                                        createRewardProp(person.getLocation());     //掉落物品
                                    }
                                    else
                                    {
                                        person.reduceHealthPoint(bullet.getDamageValue());
                                    }
                                    if(person.equals(player))
                                    {
                                        healthLevel.setValue(player.getHealthPoint());
                                    }
                                    break;
                                }
                            }
                            if(!flag && (ifHitWall(bullet.getLocation(),bullet.getRadius(),true)))    //判断子弹是否撞墙
                            {
                                sniperBulletList.remove(i);
                                gameArea.remove(bullet);
                                // MusicPlayer.playBulletHitWallMusic();
                            }
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

                        for(int i=0;i<automaticBulletList.size();i++)
                            {
                                Bullet bullet=automaticBulletList.get(i);
                            boolean flag=false;             //标记该子弹是否击中人
                            Point oldPoint=bullet.getLocation();
                            Point newPoint=new Point(oldPoint.x+bullet.getxSpeed(),oldPoint.y+bullet.getySpeed());
                            bullet.setLocation(newPoint);
                            for(Person person:personList)          //判断每个敌人是否被子弹击中
                            {
                                if((ifHitPerson(bullet,person)) && !person.ifDie() && !person.getId().equals(bullet.getFromPersonId()))    //如果击中AI
                                {
                                    flag=true;
                                    gameArea.remove(bullet);
                                    automaticBulletList.remove(i);
                                    int healthPoint =person.getHealthPoint();
                                    if(healthPoint-bullet.getDamageValue()<=0)
                                    {

                                        Person fromPerson=personList.get(Integer.parseInt(bullet.getFromPersonId()));       //这颗子弹的所有者击杀数加1
                                        fromPerson.addKillNum(1);
                                        if(fromPerson.equals(player))
                                        {
                                            killAndDieField.setText("击杀/死亡：（"+player.getKillNum()+"/"+player.getDieNum()+")");
                                        }
                                        person.setDie(true);                        //设置人物死亡
                                        person.dieSpecialEffect(gameArea);
                                        if(person instanceof Player)
                                        {
                                            healthLevel.setValue(0);
                                            int choice= JOptionPane.showConfirmDialog(null,"你扑街了！再来一把？","",JOptionPane.YES_NO_CANCEL_OPTION);
                                            if(choice==0)
                                            {
                                                restStart();
                                            }
                                            else
                                            {
                                                leaveGame();
                                            }
                                        }
                                        person.setVisible(false);
                                        createRewardProp(person.getLocation());     //掉落物品
                                    }
                                    else
                                    {
                                        person.reduceHealthPoint(bullet.getDamageValue());
                                    }
                                    //如果被击中的人是玩家
                                    if(person instanceof Player)
                                    {
                                        healthLevel.setValue(person.getHealthPoint());
                                    }
                                    break;
                                }
                            }
                            if(!flag && (ifHitWall(bullet.getLocation(),bullet.getRadius(),true)))    //判断子弹是否撞墙
                            {
                                automaticBulletList.remove(i);
                                gameArea.remove(bullet);
                               // MusicPlayer.playBulletHitWallMusic();
                            }
                        }
                    }
                }
            });
            automaticBulletThread.start();
            //初始化手榴弹移动线程
            grenadeMoveThread=new Timer(Grenade.speed, new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    for(int i=0;i<grenadeList.size();i++)
                    {
                        Grenade grenade=grenadeList.get(i);
                        //如果手榴弹到达指定地点，或者超出地图范围
                        int x=grenade.getLocation().x;
                        int y=grenade.getLocation().y;
                        if(grenade.ifArrive() || x  > SinglePersonModel.gameAreaWidth ||x<=0 || y> SinglePersonModel.gameAreaHeight || y<=0)
                        {
                            gameArea.remove(grenade);
                            grenadeList.remove(i);
                            boolean gameOver=false;             //判断游戏是否结束
                            int damageRadius=grenade.getDamageRadius();     //爆炸杀伤半径
                            java.util.List<Person> pList=new LinkedList<Person>();   //用于存放被炸死的person
                            grenade.boom(gameArea);
                            Point grenadePoint=getCentralPoint(grenade.getLocation());
                            //获取手雷持有者的id
                            String fromPersonId=grenade.getFromPersonId();
                            //寻找手雷的持有者;
                            Person fromPerson=null;
                            for(Person person:personList)
                            {
                                if(person.getId().equals(fromPersonId))
                                    fromPerson=person;
                                break;
                            }
                            for(Person person:personList)           //遍历所有人，判断是否有人在爆炸半径中
                            {
                                Point personPoint=getCentralPoint(person.getLocation());
                                int personRadius=person.getRadius();
                                if(personPoint.distance(grenadePoint) < personRadius+damageRadius)      //如果人物在爆炸半径中
                                {
                                    person.setDie(true);
                                    person.setVisible(false);
                                    pList.add(person);
                                    person.dieSpecialEffect(gameArea);      //人物死亡特效
                                    if(person instanceof Player)        //如果炸到的是玩家，则游戏结束
                                    {
                                        gameOver=true;
                                        healthLevel.setValue(0);
                                        int choice= JOptionPane.showConfirmDialog(null,"你扑街了！再来一把？","",JOptionPane.YES_NO_CANCEL_OPTION);
                                        if(choice==0)
                                        {
                                            restStart();
                                            gameOver=true;
                                            break;
                                        }
                                        else
                                        {
                                            leaveGame();
                                        }
                                    }
                                    fromPerson.addKillNum(1);
                                    if(fromPerson.equals(player))
                                    {
                                        killAndDieField.setText("击杀/死亡：（"+player.getKillNum()+"/"+player.getDieNum()+")");
                                    }
                                }
                            }
                        }
                        else
                        {
                            grenade.next();
                        }
                    }
                    gameArea.repaint();

                }
            });
            grenadeMoveThread.start();
        }
        public void paintComponent(Graphics g)
        {
            URL url=startGame.class.getResource("/images/background.png");
            ImageIcon icon=new ImageIcon(url);
            g.drawImage(icon.getImage(),0,0, 1200, 950,null);
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
                if (Map.map[x][y] !=0) {
                    return true;
                }
                y = (point.x + 2 * radius) / 20;
                x = point.y / 20;
                if (Map.map[x][y] != 0) {
                    return true;
                }
                y = (point.x / 20);
                x = (point.y + 2 * radius) / 20;
                if (Map.map[x][y] != 0) {
                    return true;
                }
                y = (point.x + 2 * radius) / 20;
                x = (point.y + 2 * radius) / 20;
                if (Map.map[x][y] != 0) {
                    return true;
                }
            }
            else
            {
                int y = point.x / 20;       //物体在map中的纵坐标
                int x = point.y / 20;       //物体在map中的横坐标
                if (Map.map[x][y] ==1) {
                    return true;
                }
                y = (point.x + 2 * radius) / 20;
                x = point.y / 20;
                if (Map.map[x][y] ==1) {
                    return true;
                }
                y = (point.x / 20);
                x = (point.y + 2 * radius) / 20;
                if (Map.map[x][y] ==1) {
                    return true;
                }
                y = (point.x + 2 * radius) / 20;
                x = (point.y + 2 * radius) / 20;
                if (Map.map[x][y] ==1) {
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
        Bullet bullet=new Bullet(fromPerson.getId(),bulletType,bulletRadius,damageValue,TravelSpeed.bulletTravelSpeed,start,end);
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
            if(!person.ifEmptyGrenade()) {
                Grenade grenade = new Grenade();
                grenade.setFromPersonId(person.getId());              //将手雷的所有者设置为person
                grenade.calculateGrenadeSpeed(startPoint, endPoint);
                gameArea.add(grenade);
                grenadeList.add(grenade);
                person.reduceGrenadeNum(1);
                int bulletLeftOnPerson=player.getBulletLeftOnPerson();
                bulletLeft.setText("手雷："+bulletLeftOnPerson);
            }
            else
            {
                MusicPlayer.playBulletUseOutMusic();
            }
        }
        //如果是地雷
        else if(weapon.getType()==WeaponType.mine)
        {
            if(!person.ifEmptyMine())
            {
                MusicPlayer.playDiscontinueAttackMusic(weapon.getWeaponName());
                Mine mine=new Mine();
                mine.setFromPersonId(person.getId());
                mine.setLocation(person.getLocation());
                person.reduceMineNum(1);

                gameArea.add(mine);
                mineList.add(mine);

                if(person.equals(player))
                {
                    int bulletLeftOnPerson = player.getBulletLeftOnPerson();
                    bulletLeft.setText("地雷：" + bulletLeftOnPerson);
                }
                gameArea.repaint();
                int bulletLeftOnPerson=player.getBulletLeftOnPerson();
                bulletLeft.setText("地雷："+bulletLeftOnPerson);
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
                    //修改子弹的数目，在屏幕左上角的显示
                    int bulletLeftInGun=((Gun)weapon).getBulletLeft();
                    int bulletLeftOnPerson=player.getBulletLeftOnPerson();
                    bulletLeft.setText("子弹："+bulletLeftInGun+"/"+bulletLeftOnPerson);

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
                    int bulletLeftInGun=((Gun)weapon).getBulletLeft();
                    int bulletLeftOnPerson=player.getBulletLeftOnPerson();
                    bulletLeft.setText("子弹："+bulletLeftInGun+"/"+bulletLeftOnPerson);
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
        int killNum=player.getKillNum();
        int dieNum=player.getDieNum();
        MusicPlayer.stopActionMusic();
        for(EliteSoldier eliteSoldier:eliteSoldierList)
        {
            eliteSoldier.stopShot();
        }
        for(Hider hider:hiderList)
        {
            hider.stopShot();
        }
        for(Person person:personList)
        {
            gameArea.remove(person);
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
        player.addDieNum(dieNum);
        player.addKillNum(killNum);
        MusicPlayer.playActionBGM();
        eliteSoldierMoveThread.restart();
        hiderMoveThread.restart();
        grenadeMoveThread.restart();
        playerMoveThread.restart();
        automaticBulletThread.restart();
        sniperBulletThread.restart();
        reLiveAiThread.restart();
        gameArea.repaint();
    }
    //创建玩家
    private void createPlayer()
    {
        try {
            Rotate rotate = new Rotate();
            player = new Player("0", "DJF",null);
            int size = 2 * (player.getRadius());
            player.setSize(size, size);
           URL url= startGame.class.getResource("/images/player/"+Person.playerImageFile[0]);
            ImageIcon icon = new ImageIcon(url);
            icon.setImage(icon.getImage().getScaledInstance(size, size, Image.SCALE_DEFAULT));
            player.setIcon(icon);
            player.setLocation(400, 300);
            gameArea.add(player);
            player.peekWeapon(new M4A1(), 10000);
            player.peekWeapon(new AWM(), 100);
            player.peekWeapon(new Mine(),10);
            player.peekWeapon(new Grenade(),10);
            player.changeWeapon(1,gameArea);
            bulletLeft.setText("子弹："+30+"/"+10000);
            personList.add(player);
            healthLevel.setValue(player.getHealthPoint());
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
        }
    }

    /**
     * 仍手雷
     * @param fromPerson:手雷的使用者
     * @param startPoint：起始坐标
     * @param endPoint：终点坐标
     */
    private void throwGrenade(Person fromPerson,Point startPoint,Point endPoint)
    {

    }
    //创建AI
    private void createAI()
    {
        int id=1;
        //创建精英战士
        for(int i=0;i<5;i++)
        {
            EliteSoldier eliteSoldier=new EliteSoldier((id++) +"");
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
            Hider hider=new Hider((id++) +"");
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
        if(minePoint.distance(eliteSoldierPoint) < mine.getRadius() && !person.getId().equals(mine.getFromPersonId()))
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
    public static Point getCentralPoint(Point topLeftCornerPoint)
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
    public  java.util.List getAutomaticBulletList()
    {
        return automaticBulletList;
    }
    public  java.util.List getSniperBulletList()
    {
        return sniperBulletList;
    }

    /**
     * 停止所有线程
     */
    public void stopAllThread()
    {
        if(shotThread!=null&&shotThread.isRunning())
            shotThread.stop();             //开火线程
        for(EliteSoldier eliteSoldier:eliteSoldierList)
        {
            eliteSoldier.stopShot();
        }
        for(Hider hider :hiderList)
        {
            hider.stopShot();
        }
        automaticBulletThread.stop();     //自动步枪子弹飞行线程
        sniperBulletThread.stop();       //狙击步枪子弹飞行线程
        playerMoveThread.stop();         //玩家移动的线程
        eliteSoldierMoveThread.stop();   //精英战士移动线程
        hiderMoveThread.stop();         //隐匿者线程
        reLiveAiThread.stop();          //复活AI线程
        grenadeMoveThread.stop();        //控制手雷移动的线程
    }

    /**
     * 离开单人游戏
     */
    public void leaveGame()
    {
        MusicPlayer.stopActionMusic();
        stopAllThread();
        SinglePersonModel.this.dispose();
        superiorMenu.setVisible(true);
    }
}

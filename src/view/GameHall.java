package view;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

/**
 * 游戏大厅类bylijie
 */
public class GameHall {
    private static Client currentClient;//当前用户

    private JFrame gameHallJFrame;//大厅frame
    private JButton killList;//击杀榜
    private JButton logout;//注销
    private JButton gameTutorial;//游戏教程
    private JButton offlineGame;//离线游戏
    private JButton createRoom;//创建游戏房间
    private JLabel gamerAccount;//玩家账号
    private JLabel gamerName;//玩家名字
    private JTextPane gamerIcon;//玩家头像
    private  DefaultListModel onlinePlayer;//在线玩家列表
    private DefaultListModel rooms;//所有房间列表
    private  JScrollPane rpane;//左
    private  JScrollPane lpanel;//右
    private JList currentOnlinePlayer;//当前在线用户
    private JList currentRoom;//当前可用房间
    GameHall(Client currentClient){
        GameHall.currentClient=currentClient;
        gameHallJFrame=new JFrame("游戏大厅");
        gameHallJFrame.setSize(1000,800);
        gameHallJFrame.setLocationRelativeTo(null);
        gameHallJFrame.setLayout(null);//设置绝对布局
        gameHallJFrame.setResizable(false);//设置不可更改
        gameHallJFrame.setVisible(true);
        JPanel jPanel=new hallJPanel();
        gameHallJFrame.add(jPanel);
        jPanel.repaint();//重画
        /**
         * 注销监听
         */
        logout.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ClientPort.sendStream.println(Sign.Logout);
                ClientPort.sendStream.flush();
            }
        });

        /**
         * 单人游戏
         */
        offlineGame.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new singlePersonModel();
            }
        });
        //关闭事件
        gameHallJFrame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                super.windowClosing(e);
                ClientPort.sendStream.println(Sign.Logout);
                ClientPort.sendStream.flush();
                System.exit(0);
            }
        });
    }
    class hallJPanel extends JPanel{
        hallJPanel(){
            this.setSize(1000,800);
            this.setLayout(null);//设置绝对布局
            killList=new JButton("击杀榜");
            logout=new JButton("注销");
            gameTutorial=new JButton("游戏教程");
            offlineGame=new JButton("离线游戏");
            createRoom =new JButton("创建房间");
            gamerAccount=new JLabel("玩家账号");
            gamerName=new JLabel("玩家昵称");
            gamerIcon=new JTextPane();
            onlinePlayer=new DefaultListModel();
            rooms=new DefaultListModel();
            currentOnlinePlayer=new JList(onlinePlayer);
            currentOnlinePlayer.setBorder(new TitledBorder("当前在线玩家"));
            currentRoom=new JList(rooms);
            currentRoom.setBorder(new TitledBorder("当前房间"));
            rpane=new JScrollPane(currentOnlinePlayer);
            lpanel=new JScrollPane(currentRoom);
            //设置两个列表的位置
            rpane.setSize(495,600);
            rpane.setLocation(0,200);
            lpanel.setSize(495,600);
            lpanel.setLocation(495,200);
            //设置上层菜单左边布局
            gamerIcon.setSize(150,150);
            gamerIcon.setLocation(25,25);
            gamerIcon.setEditable(false);
            gamerAccount.setSize(200,70);
            gamerAccount.setLocation(225,25);
            gamerName.setSize(200,70);
            gamerName.setLocation(225,105);
            //设置上层菜单右边布局
            killList.setSize(180,60);
            killList.setLocation(525,15);
            logout.setSize(180,60);
            logout.setLocation(725,15);
            gameTutorial.setSize(180,60);
            gameTutorial.setLocation(525,115);
            offlineGame.setSize(180,60);
            offlineGame.setLocation(725,115);
            this.add(killList);
            this.add(logout);
            this.add(gameTutorial);
            this.add(offlineGame);
            this.add(createRoom);
            this.add(gamerAccount);
            this.add(gamerName);
            this.add(gamerIcon);
            this.add(rpane);
            this.add(lpanel);
        }
    }

    public static void main(String[] args) {
    }
}

package view;

import utils.MyButton;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

/**
 * 游戏开始界面bylijie
 */
public class GameStart {
    private JFrame gameStartFrame;
    private MyButton singlePlay;
    private MyButton multiPlay;
    private JTextArea contentArea;
    private JPanel size1;
    private JPanel size2;

    GameStart() {
        contentArea = new JTextArea();
        contentArea.setEditable(false);
        contentArea.setText("");
        gameStartFrame=new JFrame("游戏开始");
        String defaultmes="                                     射击大战\n";
        String singlePlayStr="                                单人游戏模式\n" +
                " 1.单人游戏模式敌人为电脑\n" +
                " 2.电脑敌人不断刷新难度随事件增加而增加\n" +
                " 3.血量有限没有获胜条件";
        String multiPlayStr="                                 多人游戏模式\n" +
                " 1.可以进行四人对战\n" +
                " 2.需局域网内服务端开启服务器\n" +
                " 3.先获得指定击杀数玩家获胜";
        singlePlay = new MyButton("单人模式",singlePlayStr, contentArea);
        multiPlay = new MyButton("多人模式",multiPlayStr, contentArea);
        gameStartFrame.setSize(600,435);
        gameStartFrame.setResizable(false);
        gameStartFrame.setLayout(null);
        gameStartFrame.setVisible(true);
        gameStartFrame.setLocationRelativeTo(null);
        gameStartFrame.add(contentArea);
        gameStartFrame.add(singlePlay);
        gameStartFrame.add(multiPlay);
        //位置设置
        contentArea.setSize(520,220);
        contentArea.setLocation(40,20);
        contentArea.setText(defaultmes);
        contentArea.setFont(new Font(Font.DIALOG,1,20));
        singlePlay.setBounds(100,260,150,90);
        multiPlay.setBounds(350,260,150,90);
        /**
         * 单人游戏侦听
         */
        singlePlay.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                gameStartFrame.setVisible(false);
                new SinglePersonModel(gameStartFrame);

            }
        });
        /**
         * 多人游戏侦测
         */
        multiPlay.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                gameStartFrame.setVisible(false);
                new LoginFrame(gameStartFrame);
            }
        });
        /**
         * 关闭监听
         */
        gameStartFrame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                super.windowClosing(e);
                System.exit(0);
            }
        });
    }
}

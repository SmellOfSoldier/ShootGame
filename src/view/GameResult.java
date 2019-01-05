package view;

import person.Player;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class GameResult extends JFrame {
    private List<Player> playerList;
    int length=200;
    int high=150;
    GameResult(List<Player> playerList)
    {
        JPanel jPanel=new JPanel();
        jPanel.setSize(300,150);
        jPanel.setLayout(new GridLayout(4,1));
        this.playerList=playerList;
        this.setTitle("击杀榜");
        this.setSize(300,150);
        this.setLocationRelativeTo(null);
        for(Player p:playerList)
        {
            System.out.println("玩家："+p.getClientId()+"(击杀/死亡):   "+"("+p.getKillNum()+" / "+p.getDieNum()+")");
            JTextField player=new JTextField("玩家："+p.getClientId()+"(击杀/死亡):   "+"("+p.getKillNum()+" / "+p.getDieNum()+")");
            player.setEditable(false);
            player.setFont(new Font(null,Font.BOLD,20));
            player.setSize(300,28);
            jPanel.add(player);
        }
        this.add(jPanel);
        this.setVisible(true);
    }
}

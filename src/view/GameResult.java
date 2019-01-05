package view;

import person.Player;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class GameResult extends JFrame {
    private List<Player> playerList;
    int length=200;
    int high=150;
    GameResult(List<Player> playerList){
        this.playerList=playerList;
        this.setTitle("击杀榜");
        this.setSize(200,150);
        this.setVisible(true);
        this.setLocationRelativeTo(null);
        this.setLayout(new GridLayout(4,1));
        for(Player p:playerList)
        {
            JLabel player=new JLabel("玩家："+p.getClientId()+"KD:"+p.getKillNum()+"/"+p.getDieNum());
            player.setSize(170,28);
            this.add(player);
        }
    }
}

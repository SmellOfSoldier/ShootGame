package view;


import person.Player;

import java.util.ArrayList;

/**
 * 游戏房间：用于多人游戏时，将在同一局游戏中的所有玩家联系起来
 *
 */
public class GameRoom
{
    //存放这个房间里面的所有玩家
    private ArrayList<Player> playerArrayList =new ArrayList<Player>();
    //房间的主人
    private Player roomMaster;
}

package person;

import java.io.Serializable;

/**
 * 游戏玩家
 */
public class Player extends Person implements Serializable
{
    public Player(int id,String name,int healthPoint,int radius)
    {
        super(id,name,healthPoint,radius,5);
    }
    public Player(){}

}

package person;

import java.io.Serializable;

/**
 * 游戏玩家
 */
public class Player extends Person implements Serializable
{
    public Player(int id,String name,int healthPoint)
    {
        super(id,name,healthPoint,10,20);
    }
    public Player(){}

}

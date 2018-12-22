package person;

import Weapon.Weapon;

import java.io.Serializable;

/**
 * 屠夫，使用砍刀，移动速度极快
 */
public class Butcher extends AI implements Serializable
{
    private static int vision=200;     //屠夫的视野
    public static final int speed=10;
    public Butcher(String id)
    {
        super(id,"屠夫",200,10,speed,vision);

    }

}

package person;

import Arsenal.P92;

import java.io.Serializable;

/**
 * 牛仔,持用手枪
 */
public class Cowboy extends AI implements Serializable
{
    private  static int vision=300;         //牛仔的视野
    public Cowboy(int id)
    {
        super(id,"牛仔",100,10,300,vision);
        this.peekWeapon(new P92(),144);
    }
}

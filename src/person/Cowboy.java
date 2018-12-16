package person;

import java.io.Serializable;

/**
 * 牛仔,持用手枪
 */
public class Cowboy extends AI implements Serializable
{
    public Cowboy(int id)
    {
        super(id,"牛仔",100,10,300);
    }
}

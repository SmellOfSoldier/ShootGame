package person;

import java.io.Serializable;

/**
 * 隐匿者,使用AWM
 */
public class Hider extends AI implements Serializable
{
    public Hider(int id)
    {
        super(id,"隐匿者",100,10,400);
    }
}

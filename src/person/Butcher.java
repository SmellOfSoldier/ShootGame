package person;

import java.io.Serializable;

/**
 * 屠夫，使用砍刀，移动速度极快
 */
public class Butcher extends AI implements Serializable
{
    public Butcher(int id)
    {
        super(id,"屠夫",200,10,200);
    }

}

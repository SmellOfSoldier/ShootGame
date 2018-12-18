package person;

import Arsenal.AWM;

import java.io.Serializable;

/**
 * 隐匿者,使用AWM
 */
public class Hider extends AI implements Serializable
{
    private static int vision=700;
    public Hider(int id)
    {
        super(id,"隐匿者",100,10,400,vision);
        this.peekWeapon(new AWM(),100);
    }

}

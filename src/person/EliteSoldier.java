package person;

import Arsenal.AKM;

import java.io.Serializable;

/**
 * 精英战士，拿着自动步枪
 */
public class EliteSoldier extends AI implements Serializable
{
    private static int vision=400;
    public EliteSoldier(int id)
    {
        super(id,"精英战士",140,10,300,vision);
        this.peekWeapon(new AKM(),240);
    }
}

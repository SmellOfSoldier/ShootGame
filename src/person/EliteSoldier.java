package person;

import Arsenal.AKM;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.Serializable;
import javax.swing.*;

/**
 * 精英战士，拿着自动步枪
 */
public class EliteSoldier extends AI implements Serializable
{
    private static int vision=400;
    public static final int speed=20;
    public Timer shotThread=null;
    private int shotSpeed=200;
    public EliteSoldier(int id)
    {
        super(id,"精英战士",140,10,speed,vision);
        AKM akm=new AKM();
        this.peekWeapon(akm,240);
    }
    public void shot(Point endPoint, JPanel gameArea)
    {
        if(shotThread==null)
        {

        }
    }
}

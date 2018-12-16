package person;

import java.io.Serializable;

/**
 * 电脑玩家
 */

public class AI extends Person implements Serializable
{

    private boolean isDie=false;        //是否死亡
    public AI(int id, String name, int healthPoint,int radius,int speed)
    {
        super(id,name,healthPoint,radius,speed);
    }
    public boolean ifDie(){return isDie;}
    public void setDie(boolean isDie){this.isDie=isDie;}    //设置AI死亡或复活
}

package person;

import java.io.Serializable;

/**
 * 电脑玩家
 */

public class AI extends Person implements Serializable
{
    private int vision;                 //AI视野，当玩家进入视野范围内开枪
    private AiPath path=null;           //ai移动的路径
    private boolean isDie=false;        //是否死亡
    public AI(int id, String name, int healthPoint,int radius,int speed)
    {
        super(id,name,healthPoint,radius,speed);
        //this.vision=vision;
        //this.path=path;
    }
    public boolean ifDie(){return isDie;}                   //电脑是否死亡
    public void setDie(boolean isDie){this.isDie=isDie;}    //设置AI死亡或复活
    public void testgit(){
        //用于测试github分支合并
    }
}

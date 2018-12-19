package person;

import view.GameFrame;

import java.awt.*;
import java.io.Serializable;

/**
 * 电脑玩家
 */

public class AI extends Person implements Serializable
{
    private boolean moveCommend=true;          //命令AI停止移动
    private boolean shotCommend=false;         //命令AI停止射击
    private Integer xspeed=0;                 //ai在x方向上每次移动的像素
    private Integer yspeed=0;                 //ai在y方向上每次移动的像素
    private int vision;                 //AI视野，当玩家进入视野范围内开枪
    private AiPath path=null;           //ai移动的路径
    private boolean isDie=false;        //是否死亡
    public AI(int id, String name, int healthPoint,int radius,int speed,int vision)
    {
        super(id,name,healthPoint,radius,speed);
        this.vision=vision;
    }
    public boolean ifDie(){return isDie;}                   //电脑是否死亡
    public void setDie(boolean isDie){this.isDie=isDie;}    //设置AI死亡或复活
    public void setPath(Point startPoint,Point endPoint)
    {
        int x1=startPoint.y/20;
        int y1=startPoint.x/20;
        int x2=endPoint.y/20;
        int y2=endPoint.x/20;
        path= FindPath.findPath(new MyPoint(x1,y1),new MyPoint(x2,y2));
    }
    public void setMoveCommend(boolean commend)
    {
        moveCommend=commend;
    }
    public void setShotCommend(boolean commend)
    {
        shotCommend=commend;
    }
    public boolean hasNext()            //判断Ai是否还有下一步
    {
        return path.hasNext();
    }
    public boolean nextStep()              //ai移动下一步，改变
    {
        if(!moveCommend)
            return false;
        path.next(this);
        return true;
    }
    /*public boolean ifFindPlayer(Player player)
    {

    }*/


}

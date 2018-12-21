package person;

import view.GameFrame;

import javax.swing.*;
import java.awt.*;
import java.io.Serializable;

/**
 * 电脑玩家
 */

public class AI extends Person implements Serializable
{
    private boolean moveCommend=true;          //命令AI停止移动
    private Integer xspeed=0;                 //ai在x方向上每次移动的像素
    private Integer yspeed=0;                 //ai在y方向上每次移动的像素
    private int vision;                 //AI视野，当玩家进入视野范围内开枪
    private AiPath path=null;           //ai移动的路径
    public AI(int id, String name, int healthPoint,int radius,int speed,int vision)
    {
        super(id,name,healthPoint,radius,speed);
        this.vision=vision;
    }
    public void setPath(Point startPoint,Point endPoint)
    {
        int x1=startPoint.y/20;
        int y1=startPoint.x/20;
        int x2=endPoint.y/20;
        int y2=endPoint.x/20;
        if(!(x1==x2 && y1==y2))
            path= FindPath.findPath(new MyPoint(x1,y1),new MyPoint(x2,y2));
    }
    public void setMoveCommend(boolean commend)
    {
        moveCommend=commend;
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
    public boolean isIfFindPlayer(Point playerPoint)
    {
        Point aiPoint=this.getLocation();
        if (aiPoint.distance(playerPoint) < vision)
        {
            return true;
        }
        return false;
    }

    /*public boolean ifFindPlayer(Player player)
    {

    }*/


}

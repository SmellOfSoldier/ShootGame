package person;

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

    /**
     * 给ai设置移动的命令
     * @param commend
     */
    public void setMoveCommend(boolean commend)
    {
        moveCommend=commend;
    }

    /**
     * 判断ai是否还有下一步（是否完成了路径）
     * @return
     */
    public boolean hasNext()            //判断Ai是否还有下一步
    {
        return path.hasNext();
    }

    /**
     * ai按照路径方向继续下一步移动
     * @return
     */
    public boolean nextStep()              //ai移动下一步，改变
    {
        if(!moveCommend)
            return false;
        path.next(this);
        return true;
    }

    /**
     * 判断ai的视野内有玩家出现
     * @param playerPoint
     * @return
     */
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

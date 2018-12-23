package person;

import com.google.gson.Gson;


import java.awt.*;
import java.io.Serializable;

/**
 * 电脑移动的路径
 */
public class AiPath implements Serializable {
    private final int processBetweenTwoPoint = 10;     //两个点直接移动的次数是4次
    private int process = 0;                      //两个点间已经移动的次数
    private int currentPosition = 0;  //再path中的当前位置
    private MyPoint[] path;

    /**
     * 构造函数，参数为json形式的字符串，便于网络传递AI路径
     * @param sPath
     */
    AiPath(String sPath) {
        Gson gson = new Gson();
        path = gson.fromJson(sPath, MyPoint[].class);
    }

    /**
     * 构造函数 参数为path
     * @param path
     */
    AiPath(MyPoint[] path) {
        this.path = path;
    }

    /**
     * 判断ai是否完成了整个路径
     * @return 完成返回true
     */
    public boolean ifFinishedAll() {
        return currentPosition == path.length - 1;
    }    //判断是否走完了整个路

    public boolean hasNext()            //如果AI还能按照地图继续走，返回true
    {
        if (path != null && !( currentPosition == path.length - 1 && process==processBetweenTwoPoint))
        {
            return true;
        }
        return false;
    }

    /**
     * AI按照path走下一步，xSpeed、ySpeed表示Ai下一步在横坐标和纵坐标分别移动的像素
     * @param ai
     */
    public void next(AI ai) {
            try {
                int xSpeed;
                int ySpeed;
                ySpeed = (path[currentPosition + 1].x - path[currentPosition].x) * TravelSpeed.personTravelSpeed;
                xSpeed = (path[currentPosition + 1].y - path[currentPosition].y) * TravelSpeed.personTravelSpeed;
                Point oldPoint = ai.getLocation();
                ai.setLocation(oldPoint.x + xSpeed, oldPoint.y + ySpeed);
                if (process == processBetweenTwoPoint - 1) {
                    currentPosition++;
                }
                process ++;
                if(currentPosition!=path.length-1)
                {
                    process=process % processBetweenTwoPoint;
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }


    }
}

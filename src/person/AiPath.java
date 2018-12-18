package person;

import com.google.gson.Gson;


import java.awt.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * 电脑移动的路径
 */
public class AiPath implements Serializable
{
    private int currentPosition=0;  //再path中的当前位置
    private MyPoint[] path=null;
    //利用Gson将字符串反序列化
    AiPath(String sPath)
    {
        Gson gson=new Gson();
        path=gson.fromJson(sPath,MyPoint[].class);
    }
    AiPath(MyPoint[] path)
    {
        this.path=path;
    }
    public void clearPath(){path=null;}
    public void setPath(MyPoint[] path){this.path=path;}
    public boolean ifFinishedAll(){return currentPosition==path.length-1;}    //判断是否走完了整个路
    /*public boolean ifFinishedCurrentPoint(Point position)                     //判断是否走完了这个
    {

    }*/
}


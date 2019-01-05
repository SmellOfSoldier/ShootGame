package person;

import java.awt.*;
import java.io.Serializable;

/**
 * 继承Point，并实现Serializable接口，使其可以序列化
 */
public class  MyPoint extends Point implements Serializable
{
   public MyPoint (int x,int y)
   {
       super(x,y);
   }
   public MyPoint(MyPoint p)
   {
       super(p);
   }
}

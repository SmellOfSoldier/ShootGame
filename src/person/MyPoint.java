package person;

import java.awt.*;
import java.io.Serializable;

public class MyPoint extends Point implements Serializable
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

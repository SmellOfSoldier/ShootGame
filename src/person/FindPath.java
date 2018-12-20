package person;

import view.Wall;

import java.awt.*;
import java.util.*;

public class FindPath {
    static int dp[][];

    static Vector<MyPoint>ans;
    static int n=40,m=60;
    public static Boolean check(int x,int y)
    {
        if(x<0||y<0||x>=n||y>=m)return false;
        return true;
    }
    public static AiPath findPath (MyPoint start,MyPoint end)
    {
        int xi=start.x,yi=start.y,xe=end.x,ye=end.y;
        dp=new int[100][100];
        //n=40,m=60;
        for(int i=0;i<n;i++)
        {
            for(int j=0;j<m;j++)
            {
                if(Wall.map[i][j]==0)dp[i][j]=100000000;
                else dp[i][j]=-1;
            }
        }
        Queue<MyPoint>s= new LinkedList<MyPoint>();
        dp[xi][yi]=1;
        s.offer(new MyPoint(xi,yi));
        while(!s.isEmpty())
        {
            MyPoint temp=s.peek();s.poll();
            int di=0,i = 0,j=0;
            while(di<8)
            {
                switch(di)
                {
                    case 0:i=temp.x+1;j=temp.y;break;
                    case 1:i=temp.x;j=temp.y+1;break;
                    case 2:i=temp.x-1;j=temp.y;break;
                    case 3:i=temp.x;j=temp.y-1;break;
                    case 4:i=temp.x+1;j=temp.y+1;break;
                    case 5:i=temp.x-1;j=temp.y-1;break;
                    case 6:i=temp.x-1;j=temp.y+1;break;
                    case 7:i=temp.x+1;j=temp.y-1;break;
                }
                if(di>=4&&check(i,j)&&dp[i][temp.y]!=-1&&dp[temp.x][j]!=-1&&dp[temp.x][temp.y]+1<dp[i][j])
                {

                    dp[i][j]=dp[temp.x][temp.y]+1;
                    s.offer(new MyPoint(i,j));
                }
                if(di<4&&check(i,j)&&dp[i][j]!=-1&&dp[temp.x][temp.y]+1<dp[i][j])
                {

                    dp[i][j]=dp[temp.x][temp.y]+1;
                    s.offer(new MyPoint(i,j));
                }
                di++;
            }
        }
       return new AiPath( print(xi,yi,xe,ye));


    }
    public static MyPoint[] print(int xi, int yi, int xe, int ye)
    {
        ans=new Vector<>(100);
        //if(dp[xe][ye]==10000000||dp[xe][ye]==-1){System.out.println("没有路径");return ;}
        ans.add(new MyPoint(xe,ye));
        while(xe!=xi||ye!=yi)
        {
            int di=0,i=0,j=0;
            while(di<8)
            {
                switch(di)
                {
                    case 0:i=xe+1;j=ye+1;break;
                    case 1:i=xe-1;j=ye-1;break;
                    case 2:i=xe-1;j=ye+1;break;
                    case 3:i=xe+1;j=ye-1;break;
                    case 4:i=xe+1;j=ye;break;
                    case 5:i=xe;j=ye+1;break;
                    case 6:i=xe-1;j=ye;break;
                    case 7:i=xe;j=ye-1;break;

                }
                di++;
                if(check(i,j)&&dp[i][j]==dp[xe][ye]-1)
                {
                    ans.add(new MyPoint(i,j));
                    xe=i;
                    ye=j;
                    break;
                }
            }

        }
        int len=ans.size();

        MyPoint[] path=new MyPoint[ans.size()];
        for(int i=0,j=ans.size()-1;i<ans.size();i++,j--)
        {
            path[i]=ans.get(j);
        }
        return path;
    }
}

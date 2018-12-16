package bullet;

public class BulletSize
{
    private final static int[][] bulletRadius=new int[][]{{0,5},{1,6},{2,4},{3,8}};
    public static int getBulletRadius(int bulletType)
    {
        return bulletRadius[bulletType][1];
    }
    private BulletSize(){}
}

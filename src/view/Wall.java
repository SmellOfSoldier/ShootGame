package view;

public class Wall
{
    public static int oringemap[][]=new int[20][30];
    //TODO:将40*40的地图转化为20*20的地图保存到map中
    public static int map[][]=null;

    static
    {
        int n = 20, m = 30;
        for (int i = 0; i < m; i++) {
            oringemap[0][i] = 1;
            oringemap[n - 1][i] = 1;
        }
        for (int i = 0; i < n; i++) {
            oringemap[i][0] = 1;
            oringemap[i][m - 1] = 1;
        }
        oringemap[2][22] = 1;
        oringemap[2][23] = 1;
        oringemap[3][22] = 1;
        oringemap[3][23] = 1;
        oringemap[2][22] = 1;
        oringemap[3][13] = 1;
        oringemap[3][14] = 1;
        oringemap[3][15] = 1;
        oringemap[4][14] = 1;
        oringemap[4][15] = 1;
        oringemap[5][8] = 1;
        oringemap[5][9] = 1;
        oringemap[5][14] = 1;
        oringemap[5][15] = 1;
        oringemap[5][24] = 1;
        oringemap[5][25] = 1;
        oringemap[5][26] = 1;
        oringemap[5][27] = 1;
        oringemap[6][8] = 1;
        oringemap[6][9] = 1;
        oringemap[6][13] = 1;
        oringemap[6][14] = 1;
        oringemap[6][15] = 1;
        oringemap[6][18] = 1;
        oringemap[6][19] = 1;
        oringemap[6][24] = 1;
        oringemap[6][25] = 1;
        oringemap[6][26] = 1;
        oringemap[6][27] = 1;
        oringemap[7][18] = 1;
        oringemap[7][13] = 1;
        oringemap[7][14] = 1;
        oringemap[8][13] = 1;
        oringemap[8][14] = 1;
        oringemap[9][13] = 1;
        oringemap[9][14] = 1;
        oringemap[9][15] = 1;
        oringemap[10][6] = 1;
        oringemap[10][7] = 1;
        oringemap[11][6] = 1;
        oringemap[11][7] = 1;
        oringemap[12][14] = 1;
        oringemap[12][15] = 1;
        oringemap[12][16] = 1;
        oringemap[12][20] = 1;
        oringemap[12][21] = 1;
        oringemap[13][15] = 1;
        oringemap[13][16] = 1;
        oringemap[14][15] = 1;
        oringemap[14][16] = 1;
        oringemap[13][20] = 1;
        oringemap[13][21] = 1;
        oringemap[15][9] = 1;
        oringemap[15][10] = 1;
        oringemap[15][15] = 1;
        oringemap[15][16] = 1;
        oringemap[15][24] = 1;
        oringemap[15][25] = 1;
        oringemap[16][9] = 1;
        oringemap[16][10] = 1;
        oringemap[16][16] = 1;
        oringemap[16][17] = 1;
        oringemap[16][24] = 1;
        oringemap[16][25] = 1;

    }
}

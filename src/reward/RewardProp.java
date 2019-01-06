package reward;

import Arsenal.*;
import Weapon.Grenade;
import Weapon.Mine;
import view.SinglePersonModel;
import view.startGame;

import javax.imageio.ImageIO;
import javax.imageio.ImageReader;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

/**
 * 击杀AI掉落道具
 */
public class RewardProp extends JLabel
{
    private int type;
    private static String [] rewardName=new String[]{"MedicalPackage","AKM","M4A1","AWM","Barret","Mine","Grenade"};
    private static URL [] rewardUrl=new URL[rewardName.length];
    static
    {
        for(int i=0;i<rewardName.length;i++)
        {
            URL url=RewardProp.class.getResource("/images/rewardProp/"+rewardName[i]+".png");
            rewardUrl[i]=url;
        }
    }
    public RewardProp(int type,Point point)
    {
        try {
            this.type = type;
            this.setSize(80, 45);
            //给奖励道具设置图标
            InputStream inputStream = startGame.class.getResourceAsStream("/images/rewardProp/" + rewardName[type] + ".png");
            BufferedImage bufferedImage = ImageIO.read(inputStream);
            ImageIcon icon=new ImageIcon();
            icon.setImage(bufferedImage.getScaledInstance(2 * SinglePersonModel.CELL, 2 * SinglePersonModel.CELL, Image.SCALE_DEFAULT));
            this.setIcon(icon);
            this.setLocation(point);
        }
        catch (IOException ioe)
        {
            ioe.printStackTrace();
        }


    }
    public int getType(){return type;}

    //返回奖励道具
    public JLabel getReward()
    {
        switch (type)
        {
            case RewardType.MedicalPackage:
                return new MedicalPackage();
            case RewardType.AKM:
                return new AKM();
            case RewardType.M4A1:
                return new M4A1();
            case RewardType.AWM:
                return new AWM();
            case RewardType.Barret:
                return new Barret();
            case RewardType.Mine:
                return new Mine();
            case RewardType.Grenade:
                return new Grenade();
                default:
                    return null;
        }
    }
}

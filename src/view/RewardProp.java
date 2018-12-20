package view;

import Arsenal.*;

import javax.swing.*;
import java.awt.*;
import java.net.URL;

//击杀怪物掉落的奖励道具
public class RewardProp extends JLabel
{
    public static final int totalTypeNum=6;
    private int type;
    RewardProp(int type,Point point)
    {
        this.type=type;
        this.setSize(GameFrame.CELL,GameFrame.CELL);
        String rewardPropName=null;
        switch (type)
        {
            case RewardType.MedicalPackage:
                rewardPropName="MedicalPackage";
                break;
            case RewardType.P92:
                rewardPropName="P92";
                break;
            case RewardType.AKM:
                rewardPropName="AKM";
                break;
            case RewardType.M4A1:
                rewardPropName="M4A1";
                break;
            case RewardType.AWM:
               rewardPropName="AWM";
               break;
            case RewardType.Barret:
                rewardPropName="Barret";
                break;
        }
        URL url =RewardProp.class.getResource("/images/rewardProp/"+rewardPropName+".png");
        ImageIcon icon=new ImageIcon(url);
        icon.setImage(icon.getImage().getScaledInstance(2*GameFrame.CELL,2*GameFrame.CELL,Image.SCALE_DEFAULT));
        this.setIcon(icon);
        this.setLocation(point);

    }
    public int getType(){return type;}

    //返回奖励道具
    public JLabel getReward()
    {
        switch (type)
        {
            case RewardType.MedicalPackage:
                return new MedicalPackage();
            case RewardType.P92:
                return new P92();
            case RewardType.AKM:
                return new AKM();
            case RewardType.M4A1:
                return new M4A1();
            case RewardType.AWM:
                return new AWM();
            case RewardType.Barret:
                return new Barret();
                default:
                    return null;
        }
    }
}

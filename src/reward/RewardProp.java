package reward;

import Arsenal.*;
import Weapon.Grenade;
import Weapon.Mine;
import view.singlePersonModel;

import javax.swing.*;
import java.awt.*;
import java.net.URL;

/**
 * 击杀AI掉落道具
 */
public class RewardProp extends JLabel
{
    private int type;
    public RewardProp(int type,Point point)
    {
        this.type=type;
        this.setSize(80,45);
        String rewardPropName=null;
        switch (type)
        {
            case RewardType.MedicalPackage:
                rewardPropName="MedicalPackage";
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
            case RewardType.Mine:
                rewardPropName="Mine";
                break;
            case RewardType.Grenade:
                rewardPropName="Grenade";
                break;
        }
        //给奖励道具设置图标
        URL url =RewardProp.class.getResource("/images/rewardProp/"+rewardPropName+".png");
        ImageIcon icon=new ImageIcon(url);
        icon.setImage(icon.getImage().getScaledInstance(2* singlePersonModel.CELL,2* singlePersonModel.CELL,Image.SCALE_DEFAULT));
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

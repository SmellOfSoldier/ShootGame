package view;

import javax.swing.*;
import java.util.Random;

//医疗包
public class MedicalPackage extends JLabel
{
    private static Random random=new Random();
    private int healthPoint;
    MedicalPackage()
    {
        healthPoint=random.nextInt(101);
    }
    public int getHealthPoint(){return healthPoint;}
}

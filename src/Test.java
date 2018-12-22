import person.Person;

import javax.swing.*;

public class Test
{
    public static void main(String[] args) {
        JFrame jFrame=new JFrame();
        jFrame.setSize(200,200);
        JFrame jFrame1=new JFrame();
        jFrame1.setSize(200,200);
        jFrame.setLocationRelativeTo(null);
        jFrame1.setLocationRelativeTo(jFrame);
        jFrame.setVisible(true);
        jFrame1.setVisible(true);
    }
}

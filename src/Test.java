import person.Person;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;
import java.util.Vector;

public class Test
{
    public static void main(String[] args) {

        JFrame jFrame=new JFrame();
        DefaultListModel<String> defaultListModel=new DefaultListModel<String>();
        JList<String> clientJList=new JList<String>(defaultListModel);
        jFrame.setSize(200,800);
        jFrame.setLocationRelativeTo(null);
        defaultListModel.addElement("老大124124124214");
        defaultListModel.addElement("老三");
        defaultListModel.addElement("老二");
        JButton button=new JButton("踢出房间");
        button.setSize(40,40);
        button.setLocation(100,40);

        clientJList.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);

                List<String> list=clientJList.getSelectedValuesList();

                for(String s:list)
                {
                    System.out.println(s);
                }
            }
        });

        JScrollPane jsp=new JScrollPane(clientJList);
        jsp.setSize(200,200);
        JPanel jPanel=new JPanel();
        jPanel.setSize(200,800);
        jPanel.setLayout(null);
        jPanel.add(button);
        jPanel.add(jsp);
        jFrame.add(jPanel);
        jFrame.setVisible(true);

    }
}

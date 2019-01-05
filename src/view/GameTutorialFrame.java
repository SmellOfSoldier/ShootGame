package view;

import com.sun.xml.internal.ws.runtime.config.TubelineFeatureReader;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class GameTutorialFrame {
    private JFrame TutoriaFrame;
    private JTextArea Mes;
    GameTutorialFrame()
    {
        TutoriaFrame=new JFrame("教程");
        Mes=new JTextArea();
        TutoriaFrame.setLocationRelativeTo(null);
        TutoriaFrame.setSize(520,200);
        TutoriaFrame.setVisible(true);
        TutoriaFrame.setLayout(new BorderLayout());
        TutoriaFrame.setResizable(false);
        Mes.setSize(520,200);
        Mes.setFont(new Font(Font.DIALOG,1,20));
        Mes.setEditable(false);
        Mes.setText("                                  游戏帮助\n" +
                "1.单人模式直接可以玩耍\n" +
                "2.掉落武器可以使用F键拾取\n" +
                "3.地雷可被手雷引爆，地雷对其他玩家不可见\n" +
                "4.多人模式需要房主创建房间，其他玩家双击房间进入\n" +
                "5.WASD控制上下左右鼠标左键使用武器1234切换武器");
        TutoriaFrame.add(Mes,BorderLayout.CENTER);
        TutoriaFrame.repaint();
        TutoriaFrame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                super.windowClosing(e);
                TutoriaFrame.dispose();
            }
        });
    }
}

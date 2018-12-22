import person.Person;

import javax.swing.*;
import java.applet.Applet;
import java.applet.AudioClip;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.net.URL;
import java.util.List;
import java.util.Vector;

public class Test
{
    static URL url=Test.class.getResource("/musics/reload/AWM.wav");
    static AudioClip audioClip= Applet.newAudioClip(url);
    public static void main(String[] args) {

        JFrame jFrame=new JFrame();
        JButton play=new JButton("播放");
        JButton stop=new JButton("停止");
        play.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                audioClip.play();
            }
        });
        stop.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                audioClip.stop();
            }
        });
        jFrame.add(play);
        jFrame.add(stop,BorderLayout.SOUTH);
        jFrame.setVisible(true);
    }
}

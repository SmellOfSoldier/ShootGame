package utils;

import java.applet.Applet;
import java.applet.AudioClip;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.URL;
import javax.swing.Timer;

public class MusicPlayer
{
    private static int bgmTime=206000;
    private static URL shotUrl=null;        //开火的声音路径
    private static URL bgmUrl=null;         //bgm的路径
    private static URL dieUrl=null;         //人物死亡声音的路径
    private static URL systemPromotUrl=null;    //系统提示声音的路径
    private static AudioClip bgmPlayer=null;
    private static Timer bgmThread=new Timer(bgmTime, new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e)
        {
            bgmPlayer.play();
        }
    });
    static
    {
        bgmUrl=MusicPlayer.class.getResource("/musics/bgm/bgm.wav");
        bgmPlayer=Applet.newAudioClip(bgmUrl);
        shotUrl=MusicPlayer.class.getResource("/musics/shot/ak47.wav");
    }
    public static void playShotMusic()
    {
        Applet.newAudioClip(shotUrl).play();
    }
    public static void playDieMusic(String name)
    {
        dieUrl=MusicPlayer.class.getResource("/musics/die/"+name+".wav");
        Applet.newAudioClip(dieUrl).play();
    }
    public static void playBgm()
    {
        bgmPlayer.play();
        bgmThread.start();
    }
    public static void stopBgm()
    {
        bgmPlayer.stop();
        bgmThread.stop();
    }
}

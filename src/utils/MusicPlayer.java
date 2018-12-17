package utils;

import java.applet.Applet;
import java.applet.AudioClip;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.URL;
import javax.swing.Timer;
import Weapon.*;

public class MusicPlayer
{
    private static int bgmTime=206000;
    private static URL shotUrl=null;        //开火的声音路径
    private static URL bgmUrl=null;         //bgm的路径
    private static URL dieUrl=null;         //人物死亡声音的路径
    private static URL systemPromotUrl=null;    //系统提示声音的路径
    private static URL bulletHitWallUrl=MusicPlayer.class.getResource("/musics/other/bulletHitWall.wav");   //子弹撞墙的声音路径
    private static URL bulletLandUrl=MusicPlayer.class.getResource("/musics/other/bulletLand.wav"); //子弹壳落地的声音
    private static URL bulletUseOutUrl=MusicPlayer.class.getResource("/musics/other/bulletUseOut.wav");    //子弹落用完的使用
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
    }
    public static void playShotMusic(String gunName)
    {
        shotUrl=MusicPlayer.class.getResource("/musics/shot/"+gunName+".wav");
        Applet.newAudioClip(shotUrl).play();
    }
    public static void playDieMusic(String name)
    {
        dieUrl=MusicPlayer.class.getResource("/musics/die/"+name+".wav");
        Applet.newAudioClip(dieUrl).play();
    }
    public static void playBgm()        //播放背景音乐
    {
        bgmPlayer.play();
        bgmThread.start();
    }
    public static void stopBgm()    //停止播放背景音乐
    {
        bgmPlayer.stop();
        bgmThread.stop();
    }
    public static void playReloadMusic(int weaponType)    //播放换子弹的声音
    {
        URL url=null;
        switch (weaponType)
        {
            case WeaponType.automaticRifle:
                url=MusicPlayer.class.getResource("/musics/other/automaticRifleReload.wav");
                break;
            case WeaponType.sniperRifle:
                url=MusicPlayer.class.getResource("/music/other/sniperRifleReload.wav");
                break;
        }
        Applet.newAudioClip(url).play();
    }
    public static void playBulletLandMuisc()        //播放子弹壳落地的声音
    {
        Applet.newAudioClip(bulletLandUrl).play();
    }
    public static void playBulletHitWallMusic()     //播放子弹撞墙的声音
    {
        Applet.newAudioClip(bulletHitWallUrl).play();
    }
    public static void playBulletUseOutMusic()  //播放子弹用完的声音
    {
        Applet.newAudioClip(bulletUseOutUrl).play();
    }
}

package utils;

import java.applet.Applet;
import java.applet.AudioClip;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.URL;
import java.util.Random;
import javax.swing.Timer;
import Weapon.*;

public class MusicPlayer
{
    private static Random random=new Random();
    private static String []actionMusicFile=new String[]{"action01.wav","action02.wav","action03.wav","action04.wav"};
    private static int bgmTime=120000;
    private static URL shotUrl=null;        //开火的声音路径
    private static URL bgmUrl=null;         //bgm的路径
    private static URL dieUrl=null;         //人物死亡声音的路径
    private static URL systemPromotUrl=null;    //系统提示声音的路径
    private static URL bulletHitWallUrl=MusicPlayer.class.getResource("/musics/other/bulletHitWall.wav");   //子弹撞墙的音效文件路径
    private static URL bulletLandUrl=MusicPlayer.class.getResource("/musics/other/bulletLand.wav"); //子弹壳落地的音效文件路径
    private static URL bulletUseOutUrl=MusicPlayer.class.getResource("/musics/other/bulletUseOut.wav");    //子弹落用完的音效路径
    private static URL peekRewardPropUrl=MusicPlayer.class.getResource("/musics/other/peekRewardProp.wav"); //拾起道具音效文件路径
    private static AudioClip bgmPlayer=null;                    //bgm播放器
    private static AudioClip continueousShotPlayer=null;
    private static AudioClip shotPlayer=null;                   //开火音效播放器
    private static AudioClip peekRewardPropPlayer=null;         //拾起道具音效播放器
    private static AudioClip actionPlayer=null;                 //播放战斗音乐


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
        peekRewardPropPlayer=Applet.newAudioClip(peekRewardPropUrl);
    }
    public static void playShotMusic(String gunName)
    {
        shotUrl=MusicPlayer.class.getResource("/musics/shot/"+gunName+".wav");
        if(shotPlayer!=null)
        {
            shotPlayer.stop();
        }
       shotPlayer= Applet.newAudioClip(shotUrl);
        shotPlayer.play();
    }
    public static void playContinueousShotMusic(String name)
    {
        URL url=MusicPlayer.class.getResource("/musics/shot/AKM30.wav");
        continueousShotPlayer=Applet.newAudioClip(url);
        continueousShotPlayer.play();

    }
    public static void playDieMusic(String name)
    {
        dieUrl=MusicPlayer.class.getResource("/musics/die/"+name+".wav");
        Applet.newAudioClip(dieUrl).play();
    }
    public static void stopContinueousShotMusic()
    {
       //continueousShotPlayer.stop();
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
    public static void playReloadMusic(String name)    //播放换子弹的声音
    {
        URL url=MusicPlayer.class.getResource("/musics/reload/"+name+".wav");

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
    public static void playChangeWeaponMusic(String weaponName)    //播放切换武器的声音
    {
        URL url=MusicPlayer.class.getResource("/musics/changeWeapon/"+weaponName+".wav");
        Applet.newAudioClip(url).play();
    }
    public static void playPeekRewardPropMusic()               //播放拾起道具的音效
    {
        peekRewardPropPlayer.play();
    }
    public static void playActionMusic()                //播放战斗音乐
    {
        URL url=MusicPlayer.class.getResource("/musics/bgm/battle/"+actionMusicFile[random.nextInt(actionMusicFile.length)]);
        actionPlayer=Applet.newAudioClip(url);
        actionPlayer.loop();
    }
    public static void stopActionMusic()                //停止播放战斗音乐
    {
        actionPlayer.stop();
    }
    public static void playBoomMusic(String name)           //播放爆炸音乐
    {
        URL url=MusicPlayer.class.getResource("/musics/boom/"+name+".wav");
         Applet.newAudioClip(url).play();
    }
}

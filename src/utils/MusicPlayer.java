package utils;

import java.applet.Applet;
import java.applet.AudioClip;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.URL;
import java.util.HashMap;
import java.util.Random;
import javax.swing.Timer;

public class MusicPlayer
{
    private static Random random=new Random();
    private static String []actionMusicFile=new String[]{"action01.wav","action02.wav","action03.wav","action04.wav"};
    private static String []weaponName=new String[]{"AKM","M4A1","AWM","Barret","Mine","Grenade"};
    private static int bgmTime=120000;
    private static URL bgmUrl=null;         //bgm的路径
    private static URL systemPromotUrl=null;    //系统提示声音的路径
    private static URL bulletHitWallUrl=MusicPlayer.class.getResource("/musics/other/bulletHitWall.wav");   //子弹撞墙的音效文件路径
    private static URL bulletLandUrl=MusicPlayer.class.getResource("/musics/other/bulletLand.wav"); //子弹壳落地的音效文件路径
    private static URL bulletUseOutUrl=MusicPlayer.class.getResource("/musics/other/bulletUseOut.wav");    //子弹落用完的音效路径
    private static URL peekRewardPropUrl=MusicPlayer.class.getResource("/musics/other/peekRewardProp.wav"); //拾起道具音效文件路径
    private static AudioClip bgmPlayer=null;                    //bgm播放器
    private static AudioClip peekRewardPropPlayer=null;         //拾起道具音效播放器
    private static HashMap<String,AudioClip> weaponAudioClipMap=new HashMap<>();      //存放武器开火音效的AudioClip
    private static AudioClip currentWeaponAudioClip;            //目前正在播放的武器音效AudioClip
    private static HashMap<String,AudioClip> changeWeaponAudioClipMap=new HashMap<>();  //存放切换武器音效的AudioClip
    private static HashMap<String,AudioClip> reloadAudioClipMap=new HashMap<>();        //存放武器上膛的音效的AudioClip
    private static AudioClip[] actionMusicAudioClips =new AudioClip[actionMusicFile.length];   //存放战斗音乐的AudioClip
    private static AudioClip currentActionMusicAudioClip=null;     //当前正在使用的战斗音乐AudioClip
    private static AudioClip beenHitMusicPlayer=null;            //人物被击中的声音播放器
    private static AudioClip dieMusicPlayer=null;                   //人物死亡的音效


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

        //初始化武器开火的AudioClipMap
        for(String name:weaponName)
        {
            URL url=MusicPlayer.class.getResource("/musics/shot/"+name+".wav");
            AudioClip audioClip=Applet.newAudioClip(url);
            weaponAudioClipMap.put(name,audioClip);
        }

        //初始化切换武器的AudioClipMap
        for(String name:weaponName)
        {
            URL url=MusicPlayer.class.getResource("/musics/changeWeapon/"+name+".wav");
            AudioClip audioClip= Applet.newAudioClip(url);
            changeWeaponAudioClipMap.put(name,audioClip);
        }
        //初始化武器上膛的AudioClipMap
        for(String name:weaponName)
        {
            URL url=MusicPlayer.class.getResource("/musics/reload/"+name+".wav");
            AudioClip audioClip=Applet.newAudioClip(url);
            reloadAudioClipMap.put(name,audioClip);
        }
        //初始化战斗音乐的AudioClip
        for(int i=0;i<actionMusicFile.length;i++)
        {
            URL url=MusicPlayer.class.getResource("/musics/bgm/action/"+actionMusicFile[i]);
            actionMusicAudioClips[i]=Applet.newAudioClip(url);
        }
        //初始化人物声音AudioClip
        {
            URL url = MusicPlayer.class.getResource("/musics/person/beenHit.wav");
            beenHitMusicPlayer = Applet.newAudioClip(url);
            url=MusicPlayer.class.getResource("/musics/person/die.wav");
            dieMusicPlayer=Applet.newAudioClip(url);
        }
    }
    //播放间断性攻击武器的声音
    public static void playDiscontinueAttackMusic(String gunName)
    {
        currentWeaponAudioClip=weaponAudioClipMap.get(gunName);
        currentWeaponAudioClip.play();
    }
    //播放连续性攻击的武器的音效
    public static void playContinueAttackMusic(String gunName)
    {
        currentWeaponAudioClip=weaponAudioClipMap.get(gunName);
        currentWeaponAudioClip.play();
    }
    //播放人物死亡的音效
    public static void playDieMusic()
    {
        dieMusicPlayer.play();
    }
    //播放人物被子弹击中的音效
    public static void playBeenHitMusic()
    {
        beenHitMusicPlayer.play();
    }
    //停止连续型攻击武器的声音
    public static void stopContinueAttackMusic()
    {
       currentWeaponAudioClip.stop();
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
    public static void playReloadMusic(String weaponName)    //播放换子弹的声音
    {
        reloadAudioClipMap.get(weaponName).play();
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
        changeWeaponAudioClipMap.get(weaponName).play();
    }
    public static void playPeekRewardPropMusic()               //播放拾起道具的音效
    {
        peekRewardPropPlayer.play();
    }
    public static void playActionMusic()                //播放战斗音乐
    {
        int index=random.nextInt(actionMusicAudioClips.length);
       currentActionMusicAudioClip= actionMusicAudioClips[index];
       currentActionMusicAudioClip.loop();
    }
    public static void stopActionMusic()                //停止播放战斗音乐
    {
        currentActionMusicAudioClip.stop();
    }
    public static void playBoomMusic(String name)           //播放爆炸音乐
    {
        URL url=MusicPlayer.class.getResource("/musics/boom/"+name+".wav");
         Applet.newAudioClip(url).play();
    }
}

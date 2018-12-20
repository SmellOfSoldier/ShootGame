package utils;

import javax.sound.sampled.*;
import java.io.File;
import java.io.IOException;

public class GameAudioSystem {
    private String sample=null;
    readAudio ReadAudio=new readAudio();
    AudioInputStream audioInputStream=null;
    File sampleFile=null;
    SourceDataLine auline=null;
    AudioFormat format;
    //初始化游戏总音频流通道
    public GameAudioSystem(String samplePath){
        this.sample=samplePath;
        try {
            audioInputStream= AudioSystem.getAudioInputStream(new File(sample));
        } catch (UnsupportedAudioFileException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        AudioFormat format= audioInputStream.getFormat();
        DataLine.Info info=new DataLine.Info(SourceDataLine.class,format);
        try {
            //建立音频流
            auline=(SourceDataLine)AudioSystem.getLine(info);
            auline.open(format);
            auline.start();
        } catch (LineUnavailableException e) {
            e.printStackTrace();
        }
    }
    //播放从内存音频数据
    public  class playShotAudio extends Thread{
        String gunName;
        byte[] gunAudio=null;
        int offset=0;//记录gunAudio枪声读取到的位置
        int nByteRead=0;//记录abData每次读入的数据数目
        int lenofaudio=0;//gunAudio的长度
        int i=0;
        //初始化枪械名字和引入内存中声音数组数据地址
        public playShotAudio(String gunName,byte[] gunAudio){
            this.gunName=gunName;
            this.gunAudio=gunAudio;
            lenofaudio=gunAudio.length;
        }
        public void run(){
            byte[] abData=new byte[512];
                //读入abDate中
                for( ;i<512&&offset<lenofaudio;i++,offset++,nByteRead++) {
                    abData[i]=gunAudio[offset];
                }
                auline.write(abData,0,nByteRead);
                i=0;
                offset++;
                nByteRead=0;
        }
    }
    //关闭音频流通道
    public void stopAudioSystem(){
        auline.drain();
        auline.stop();
    }
}
class test{
    public static void main(String[] args) {
        GameAudioSystem test=new GameAudioSystem(audioPath.AKMSHOT);
        GameAudioSystem.playShotAudio playShotAudio=test.new playShotAudio("AKM",test.ReadAudio.getAKMAUDIO());
    }
}

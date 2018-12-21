package utils;

import com.sun.scenario.effect.impl.sw.sse.SSEBlend_SRC_OUTPeer;

import java.io.File;
import java.io.IOException;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.SourceDataLine;
import javax.sound.sampled.UnsupportedAudioFileException;

/*
*BY lijie
*   AudioInputStreamTest尝试
 */

public class AudioInputStreamTest {
    public static void main(String[] args) {
        // TODO Auto-generated method stub
        AePlayWave aePlayWave=new AePlayWave("src/musics/shot/AKM.wav","src/musics/shot/P92.wav");
        Thread t=new Thread(aePlayWave);
        t.start();
    }
}
class AePlayWave extends Thread
{
    String filename;
    String filename2;
    AudioInputStream audioInputStream=null;
    AudioInputStream audioInputStream2=null;
    File SoundFile;
    File SoundFile2;
    SourceDataLine auline=null;
    public AePlayWave(String filename,String filename2)
    {
        this.filename=filename;
        this.filename2=filename2;
    }
    public void run()
    {
        SoundFile=new File(filename);
        SoundFile2=new File(filename2);
        try {
            audioInputStream=AudioSystem.getAudioInputStream(SoundFile);
            audioInputStream2=AudioSystem.getAudioInputStream(SoundFile2);
        }
        catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return;
        }
        AudioFormat format= audioInputStream.getFormat();
        AudioFormat format2= audioInputStream2.getFormat();
        DataLine.Info info=new DataLine.Info(SourceDataLine.class,format);
        DataLine.Info info2=new DataLine.Info(SourceDataLine.class,format2);

        try {
            auline=(SourceDataLine)AudioSystem.getLine(info);
            auline.open(format);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return;
        }
        auline.start();
        int nByteRead=0;
        int nByteRead2=0;
        byte[] abData=new byte[51200];
        byte[] abData2=new byte[51200];
        try {
            while(nByteRead!=-1)
            {
                nByteRead=audioInputStream.read(abData,0,abData.length);
                nByteRead2=audioInputStream2.read(abData,0,abData2.length);

                if(nByteRead>=0)
                {
                    auline.write(abData, 0, nByteRead);
                    auline.write(abData, 0, nByteRead2);
                }
            }
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        finally
        {
            auline.drain();
            auline.close();
        }

    }
}


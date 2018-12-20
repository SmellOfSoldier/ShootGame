package utils;

import Arsenal.AKM;

import javax.print.DocFlavor;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.UnsupportedAudioFileException;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
class readAudio {
    static byte[] AKMAUDIO=new byte[512000001];
    static ArrayList<Byte> AKMLIST=new ArrayList<>();
    static {
        try {
            AudioInputStream akmInputStream= AudioSystem.getAudioInputStream(new File(audioPath.AKMSHOT));
            int nByte=0;
            int i=0;
            while(nByte!=-1) {
                byte[] getdata = new byte[1];
                nByte=akmInputStream.read(getdata);
                if(nByte!=-1) AKMAUDIO[i++]=getdata[0];
            }
        } catch (UnsupportedAudioFileException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public byte[] getAKMAUDIO(){
        return AKMAUDIO;
    }
}

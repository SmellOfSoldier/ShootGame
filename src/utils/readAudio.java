package utils;

import javax.print.DocFlavor;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.UnsupportedAudioFileException;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
class readAudio {
    static byte[] AKMAUDIO;
    static {
        try {
            AudioInputStream akmInputStream= AudioSystem.getAudioInputStream(new File(audioPath.AKMSHOT));
            akmInputStream.read(AKMAUDIO);
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

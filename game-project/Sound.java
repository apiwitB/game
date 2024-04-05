import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import java.io.File;
import java.io.IOException;


public class Sound {
    private Clip clip;

    public Sound(String filePath) {
        loadSound(filePath);
    }

    public void themeSong(){
        loadSound("E:/game-project/audio/theme.wav");
    }

    public void GameOversong(){
        loadSound("E:/game-project/audio/game-over.wav");
    }

    private void loadSound(String filePath) {
        try {
            File audioFile = new File(filePath);
            AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(audioFile);
            clip = AudioSystem.getClip();
            clip.open(audioInputStream);
        } catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
            e.printStackTrace();
        }
    }
    

    public void play() {
        if (clip != null) {
            clip.setFramePosition(0); // เริ่มเล่นเสียงตั้งแต่ต้น
            clip.start();
        }
    }

    public void stop() {
        if (clip != null) {
            clip.stop();
        }
    }

    public void loop() {
        if (clip != null) {
            clip.loop(Clip.LOOP_CONTINUOUSLY); // เล่นเสียงวนไปเรื่อยๆ
        }
    }
    
}

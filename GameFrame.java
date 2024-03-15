import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.FloatControl;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import javax.swing.JFrame;
import java.io.IOException;
import java.net.URL;


public class GameFrame extends JFrame{
    GameUI gameUI;  // Add this line

    GameFrame(){
        this.add(new GamePanel());
        this.setTitle("Snake");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setResizable(false);
        this.pack();
        this.setVisible(true);
        this.setLocationRelativeTo(null);

        playSound();
    } 

        // Add this method to play sound
    private void playSound() {
        try {
            URL url = this.getClass().getClassLoader().getResource("Hatsune-Miku-sings-_Ievan-Polkka_.wav");
            AudioInputStream audioIn = AudioSystem.getAudioInputStream(url);
            Clip clip = AudioSystem.getClip();
            clip.open(audioIn);

            FloatControl volumeControl = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
            float range = volumeControl.getMaximum() - volumeControl.getMinimum();
            float gain = (range * 0.7f) + volumeControl.getMinimum(); // 70% volume
            volumeControl.setValue(gain);

            clip.loop(Clip.LOOP_CONTINUOUSLY);
        } catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
            e.printStackTrace();
        }
    }

    
}

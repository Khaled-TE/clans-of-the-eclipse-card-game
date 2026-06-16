package game.utils;

import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

public class SoundManager {

    private static MediaPlayer mediaPlayer;
    
    private static double musicVolume = 0.5;
    private static double sfxVolume = 0.5;
    
    private static boolean muted = false;

    public static void playMusic(String filename) {
        try {
            String path = SoundManager.class.getResource("/sounds/" + filename).toExternalForm();
            Media media = new Media(path);
            
            if (mediaPlayer != null) {
                mediaPlayer.stop();
            }
            
            mediaPlayer = new MediaPlayer(media);
            mediaPlayer.setCycleCount(MediaPlayer.INDEFINITE);
            mediaPlayer.setVolume(muted ? 0 : musicVolume);
            mediaPlayer.play();
        } catch (Exception e) {
            System.out.println("Sound file not found: " + filename);
        }
    }

    public static void playSFX(String filename) {
        if (muted) return;
        
        try {
            String path = SoundManager.class.getResource("/sounds/" + filename).toExternalForm();
            Media media = new Media(path);
            MediaPlayer sfx = new MediaPlayer(media);
            sfx.setVolume(sfxVolume);
            sfx.play();
     
            sfx.setOnEndOfMedia(sfx::dispose);
        } catch (Exception e) {
            System.out.println("SFX not found: " + filename);
        }
    }

    public static void setMusicVolume(double v) {
        musicVolume = v;
        if (mediaPlayer != null && !muted) {
            mediaPlayer.setVolume(musicVolume);
        }
    }

    public static void setSFXVolume(double v) {
        sfxVolume = v;
    }

    public static void toggleMute() {
        muted = !muted;
        if (mediaPlayer != null) {
            mediaPlayer.setVolume(muted ? 0 : musicVolume);
        }
    }

    public static void stopMusic() {
        if (mediaPlayer != null) {
            mediaPlayer.stop();
        }
    }

    public static boolean isMuted() { return muted; }
    public static double getMusicVolume() { return musicVolume; }
    public static double getSFXVolume() { return sfxVolume; }
}
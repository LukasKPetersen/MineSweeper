package gui;

import java.io.File;

import javafx.scene.media.AudioClip;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

public class MineSweeperAudio {
	private static final int INDEFINITE = MediaPlayer.INDEFINITE;
	private Media soundTrack;
	private AudioClip explosion;
	private Media gameOver;
	private MediaPlayer player;
	
	public MineSweeperAudio() {
		String pathSoundTrack = "SoundFX/SweepingThemMines.wav",
				pathBombSound = "SoundFX/BombSound.aif",
				pathGameOver = "SoundFX/GameOver.wav";
		soundTrack = new Media(new File(pathSoundTrack).toURI().toString());
		explosion = new AudioClip(new File(pathBombSound).toURI().toString());
		gameOver = new Media(new File(pathGameOver).toURI().toString());
	}
	
	public void playSoundTrack() {
		player = new MediaPlayer(soundTrack);
		player.setCycleCount(INDEFINITE);
		player.play();
	}
	
	public void stopSoundTrack() {
		player.stop();
	}
	
	public void bombHit() {
		stopSoundTrack();
		explosion.setCycleCount(1);
		explosion.play();
		try {
			Thread.sleep(3000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		player = new MediaPlayer(gameOver);
		player.setCycleCount(1);
		player.play();
	}
	
	public void muteUnmute() {
		int vol = explosion.getVolume() != 0 ? 0 : 100;
		player.setVolume(vol);
		explosion.setVolume(vol);
	}
	
	public void musicOnOff() { //Leaves the sound effects on
		int vol = player.getVolume() != 0 ? 0 : 100;
		player.setVolume(vol);
	}

}

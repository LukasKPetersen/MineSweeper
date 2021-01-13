package gui;

import java.io.File;

import javafx.scene.media.AudioClip;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

public class MineSweeperAudio {
	private static final int INDEFINITE = MediaPlayer.INDEFINITE;
	private Media soundTrack;
	private Media gameOver;
	private Media youWin;
	private AudioClip startUpBleep;
	private AudioClip clickDown;
	private AudioClip clickUp;
	private MediaPlayer player;
	
	public MineSweeperAudio() {
		String pathSoundTrack = "SoundFX/SweepingThemMines.wav",
				pathGameOver = "SoundFX/GameOver.wav",
				pathYouWin = "SoundFX/YouWin.wav",
				pathStartUpBleep = "SoundFX/StartUpBleep.aif",
				pathClickDown = "SoundFX/Click_down.aif",
				pathClickUp = "SoundFX/Click_Up.aif";
		soundTrack = new Media(new File(pathSoundTrack).toURI().toString());
		gameOver = new Media(new File(pathGameOver).toURI().toString());
		youWin = new Media(new File(pathYouWin).toURI().toString());
		startUpBleep = new AudioClip(new File(pathStartUpBleep).toURI().toString());
		clickDown = new AudioClip(new File(pathClickDown).toURI().toString());
		clickUp = new AudioClip(new File(pathClickUp).toURI().toString());
	}
	
	public void playSoundTrack() {
		startUpBleep.setCycleCount(1);
		startUpBleep.play();
		//Initate short wait
		try {
			Thread.sleep(800);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		player = new MediaPlayer(soundTrack);
		player.setCycleCount(INDEFINITE);
		player.play();
	}
	
	public void stopSoundTrack() {
		player.stop();
	}
	
	public void gameWon() {
		stopSoundTrack();
		player.setCycleCount(1);
		player = new MediaPlayer(youWin);
		player.play();
	}
	
	public void bombHit() {
		stopSoundTrack();
		player = new MediaPlayer(gameOver);
		player.setCycleCount(1);
		player.play();
	}
	
	public void mousePressed() {
		clickDown.setCycleCount(1);
		clickDown.play();
	}
	
	public void mouseReleased() {
		clickUp.setCycleCount(1);
		clickUp.play();
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

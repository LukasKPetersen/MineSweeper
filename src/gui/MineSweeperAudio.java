package gui;

import java.io.File;

import javafx.scene.media.AudioClip;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

public class MineSweeperAudio {
	private static final int INDEFINITE = MediaPlayer.INDEFINITE;
	private Media soundTrack;
	private Media youWin;
	private Media youLose;
	private AudioClip startUpBleep;
	private AudioClip clickDown;
	private AudioClip clickUp;
	private MediaPlayer playerST;
	private MediaPlayer playerFX;
	
	public MineSweeperAudio() {
		String pathSoundTrack = "SoundFX/SweepingThemMines.wav",
				pathYouWin = "SoundFX/YouWin.wav",
				pathYouLose = "SoundFX/YouLose.wav",
				pathStartUpBleep = "SoundFX/StartUpBleep.aif",
				pathClickDown = "SoundFX/Click_down.aif",
				pathClickUp = "SoundFX/Click_Up.aif";
		soundTrack = new Media(new File(pathSoundTrack).toURI().toString());
		youWin = new Media(new File(pathYouWin).toURI().toString());
		youLose = new Media(new File(pathYouLose).toURI().toString());
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
		playerST = new MediaPlayer(soundTrack);
		playerST.setCycleCount(INDEFINITE);
		playerST.play();
	}
	
	public void stopSoundTrack() {
		playerST.stop();
	}
	
	public void gameWon() {
		stopSoundTrack();
		playerFX = new MediaPlayer(youWin);
		playerFX.setCycleCount(1);
		playerFX.play();
	}
	
	public void gameLost() {
		stopSoundTrack();
		playerFX = new MediaPlayer(youLose);
		playerFX.setCycleCount(1);
		playerFX.play();
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
		int vol = playerFX.getVolume() != 0 ? 0 : 100;
		playerST.setVolume(vol);
		playerFX.setVolume(vol);
	}
	
	public void musicOnOff() { //Leaves the sound effects on
		int vol = playerST.getVolume() != 0 ? 0 : 100;
		playerST.setVolume(vol);
	}

}

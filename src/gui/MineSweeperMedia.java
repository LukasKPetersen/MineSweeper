package src.gui;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import javafx.scene.image.Image;
import javafx.scene.media.AudioClip;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.text.Font;

public class MineSweeperMedia {
	private static final int INDEFINITE = MediaPlayer.INDEFINITE;
	private int vol;
	private Media soundTrack;
	private Media youWin;
	private Media youLose;
	private AudioClip startUpBleep;
	private AudioClip clickDown;
	private AudioClip clickUp;
	private MediaPlayer playerST;
	private MediaPlayer playerFX;
	private Image happySmileyImage;
	private Image winSmileyImage;
	private Image deadSmileyImage;
	private Image tenseSmileyImage;
	private Image bombImage;
	private Image buttonImage;
	private Image flagImage;
	private Image pressedBombImage;
	private Image pressedButtonImage;
	private Font digitalFont;
	
	public MineSweeperMedia() throws FileNotFoundException {
		//Constructs sound media
		vol = 100;
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
		
		startUpBleep.setCycleCount(1);
		clickDown.setCycleCount(1);
		clickUp.setCycleCount(1);
		
		//Constructs visual media
		bombImage = new Image(new FileInputStream("Pictures/Bomb.png"));
		buttonImage = new Image(new FileInputStream("Pictures/Button.png"));
		flagImage = new Image(new FileInputStream("Pictures/Flag.png"));
		pressedButtonImage = new Image(new FileInputStream("Pictures/PressedButton.png"));
		pressedBombImage = new Image(new FileInputStream("Pictures/PressedBomb.png"));
		happySmileyImage = new Image(new FileInputStream("Pictures/HappySmiley.png"));
		winSmileyImage = new Image(new FileInputStream("Pictures/WinSmiley.png"));
		deadSmileyImage = new Image(new FileInputStream("Pictures/DeadSmiley.png"));
		tenseSmileyImage = new Image(new FileInputStream("Pictures/TenseSmiley.png"));
		this.digitalFont = Font.loadFont("file:Fonts/Digital.ttf", 50);
	}
	
	//***Audio methods***
	
	public void playSoundTrack() {
		startUpBleep.play();
		playerFX = new MediaPlayer(youWin);
		playerST = new MediaPlayer(soundTrack);
		playerST.setCycleCount(INDEFINITE);
		playerST.play();
	}
	
	public void stopSoundTrack() {
		playerST.stop();
	}
	
	public void startSoundTrack() {
		playerST.play();
	}
	
	public void gameWon() {
		stopSoundTrack();
		playerFX = new MediaPlayer(youWin);
		playerFX.setCycleCount(1);
		playerFX.setVolume(vol);
		playerFX.play();
	}
	
	public void gameLost() {
		stopSoundTrack();
		playerFX = new MediaPlayer(youLose);
		playerFX.setCycleCount(1);
		playerFX.setVolume(vol);
		playerFX.play();
	}
	
	public void mousePressed() {
		clickDown.play();
	}
	
	public void mouseReleased() {
		clickUp.play();
	}
	
	public void muteUnmute() {
		vol = vol != 0 ? 0 : 100;
		playerST.setVolume(vol);
		playerFX.setVolume(vol);
		startUpBleep.setVolume(vol);
		clickDown.setVolume(vol);
		clickUp.setVolume(vol);
	}
	
	public void musicOnOff() { //Leaves the sound effects on
		if(vol != 0) {
			int volST = playerST.getVolume() != 0 ? 0 : 100;
			playerST.setVolume(volST);
		}
	}
	
	//***Image methods***
	
	public Image getButtonImage() {
		return buttonImage;
	}
	public Image getBombImage() {
		return bombImage;
	}
	
	public Image getHappySmileyImage() {
		return happySmileyImage;
	}


	public Image getWinSmileyImage() {
		return winSmileyImage;
	}


	public Image getDeadSmileyImage() {
		return deadSmileyImage;
	}


	public Image getTenseSmileyImage() {
		return tenseSmileyImage;
	}

	public Image getFlagImage() {
		return flagImage;
	}

	public Image getPressedBombImage() {
		return pressedBombImage;
	}
	
	public Image getPressedButtonImage() {
		return pressedButtonImage;
	}

	public Font getDigitalFont() {
		return digitalFont;
	}

}

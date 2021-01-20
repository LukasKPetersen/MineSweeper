package application;
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
	private int volST;
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
	private Image settingsImage;
	
	public MineSweeperMedia() throws FileNotFoundException {
		//Constructs sound media
		vol =60;
		volST = 60;
		String pathSoundTrack = "SoundFX/SweepingThemMines.wav",
				pathYouWin = "SoundFX/YouWin.wav",
				pathYouLose = "SoundFX/YouLose.wav",
				pathStartUpBleep = "SoundFX/StartUpBleep.aif",
				pathClickDown = "SoundFX/Click_down.aif",
				pathClickUp = "SoundFX/Click_Up.aif";
		soundTrack = new Media(ClassLoader.getSystemResource(pathSoundTrack).toString());
		youWin = new Media(ClassLoader.getSystemResource(pathYouWin).toString());
		youLose = new Media(ClassLoader.getSystemResource(pathYouLose).toString());
		startUpBleep = new AudioClip(ClassLoader.getSystemResource(pathStartUpBleep).toString());
		clickDown = new AudioClip(ClassLoader.getSystemResource(pathClickDown).toString());
		clickUp = new AudioClip(ClassLoader.getSystemResource(pathClickUp).toString());
		
		startUpBleep.setCycleCount(1);
		clickDown.setCycleCount(1);
		clickUp.setCycleCount(1);
		
		//Constructs visual media
		
		bombImage = new Image(ClassLoader.getSystemResource("Pictures/Bomb.png").toString());
		buttonImage = new Image(ClassLoader.getSystemResource("Pictures/Button.png").toString());
		flagImage = new Image(ClassLoader.getSystemResource("Pictures/Flag.png").toString());
		pressedButtonImage = new Image(ClassLoader.getSystemResource("Pictures/PressedButton.png").toString());
		pressedBombImage = new Image(ClassLoader.getSystemResource("Pictures/PressedBomb.png").toString());
		happySmileyImage = new Image(ClassLoader.getSystemResource("Pictures/HappySmiley.png").toString());
		winSmileyImage = new Image(ClassLoader.getSystemResource("Pictures/WinSmiley.png").toString());
		deadSmileyImage = new Image(ClassLoader.getSystemResource("Pictures/DeadSmiley.png").toString());
		tenseSmileyImage = new Image(ClassLoader.getSystemResource("Pictures/TenseSmiley.png").toString());
		settingsImage = new Image(ClassLoader.getSystemResource("Pictures/Settings.png").toString());
		this.digitalFont = Font.loadFont(ClassLoader.getSystemResource("Fonts/Digital.ttf").toString(), 50);
	}
	
	//***Audio methods***
	public void playSoundTrack() {
		startUpBleep.play();
		if (playerST != null) {
			playerST.stop();
		}
		
		playerFX = new MediaPlayer(youWin);
		playerST = new MediaPlayer(soundTrack);
		playerST.setCycleCount(INDEFINITE);
		playerST.setVolume(vol);
		playerST.play();
	}
	
	public void stopSoundTrack() {
		playerST.stop();
	}
	
	public void startSoundTrack() {
		playerFX.stop();
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
		vol = vol != 0 ? 0 : 60;
		
		playerFX.setVolume(vol);
		startUpBleep.setVolume(vol);
		clickDown.setVolume(vol);
		clickUp.setVolume(vol);
		if (volST!=0) {
			playerST.setVolume(vol);
		}
		
	}
	
	public void musicOnOff() { //Leaves the sound effects on
		if(vol != 0) {
			volST = playerST.getVolume() != 0 ? 0 : 60;
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

	public Image getSettingsImage() {
		return settingsImage;
	}

}

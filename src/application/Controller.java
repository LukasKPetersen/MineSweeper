package application;

import javafx.event.*;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;

public class Controller {

	private EventHandler<MouseEvent> eventMouse;
	private int posMousePressedX;
	private int posMousePressedY;
	private boolean smileyPressed;
	private Model model;
	private View view;
	private boolean pressedSettingsButton = true;

	private int height;
	private int length;

	// private MenuController menu;

	public void soundOffButton(ActionEvent event) {
		view.setMenuScene();
	}

	public void Button1(ActionEvent event) {
		System.out.print("ss");
	}

	public Controller(final Model Model, final View view, int tilesize, int headerHeight, int borderThickness,
			int len, int heig) {
		this.height = heig;
		this.length = len;
		this.model = Model;
		this.view = view;

		this.setEventMouseAction(new EventHandler<MouseEvent>() {
			public void handle(MouseEvent event) {
				// gets coordinates of mouseclick
				double x = event.getX();
				double y = event.getY();

				
				// Unpresses pressedsmiley
				if (smileyPressed) {
					view.smileyFaceSetter("");
					smileyPressed = false;
				}
				if (x > (length / 4.0 + 20) && x < (length / 4.0 + 60) && y > (headerHeight / 2.0 - 10)
						&& y < (headerHeight / 2.0 + 30)) {
					if (event.getButton().toString() == "PRIMARY") {
						if (event.getEventType().toString() == "MOUSE_PRESSED") {
							view.drawSettingsButton(true);
							pressedSettingsButton = true;
						}
						if (event.getEventType().toString() == "MOUSE_RELEASED") {
							view.drawSettingsButton(false);
							view.setMenuScene();
							pressedSettingsButton = false;
						}
					}
				}

				// If coordinates are inside the smileybutton
				if (x > (length / 2.0 - 20) && x < (length / 2.0 + 20) && y > (headerHeight / 2.0 - 10)
						&& y < (headerHeight / 2.0 + 30)) {
					if (event.getButton().toString() == "PRIMARY") {
						// Press smiley button with left click interaction
						if (event.getEventType().toString() == "MOUSE_PRESSED") {
							smileyPressed = true;
							view.smileyFaceSetter("Pressed");
						}
						// release smileybutton with left click interaction
						if (event.getEventType().toString() == "MOUSE_RELEASED") {
							view.reset();
							model.reset();
						}
					}
				}
				// if the ycoordinate of click is within in the center and not header
				if (y > headerHeight) {
					y -= headerHeight;
					// Checks wether click is in within the borders of the minesweeper board
					if (x > borderThickness && x < length - borderThickness && y > borderThickness
							&& y < height - borderThickness) {

						// Converts from coordinates to coordinates in tiles
						x -= borderThickness;
						y -= borderThickness;
						int xCoorTiles = 0;
						int yCoorTiles = 0;
						while (x - tilesize > 0) {
							x -= tilesize;
							xCoorTiles++;
						}
						while (y - tilesize > 0) {
							y -= tilesize;
							yCoorTiles++;
						}

						if (event.getButton().toString() == "PRIMARY") {
							// if leftclick is pressed then make sound, save pressed position and animate
							// where pressed button is, and sets smiley to tense.
							if (event.getEventType().toString() == "MOUSE_PRESSED") {
								view.mousePressSound();
								posMousePressedY = yCoorTiles;
								posMousePressedX = xCoorTiles;
								Model.setPressedButton(posMousePressedY, posMousePressedX, true);
								if (!Model.isGameOver()) {
									view.smileyFaceSetter("TenseSmiley");
								}
							}
							// if leftclick is released play mousereleasesound, determine wether its
							// falseclick
							// updates model, and sets smileyface to happy.
							if (event.getEventType().toString() == "MOUSE_RELEASED") {
								view.mouseReleaseSound();
								if (!falseClick(yCoorTiles, xCoorTiles)) {
									model.update(yCoorTiles, xCoorTiles);
								}
								if (!Model.isGameOver()) {
									view.smileyFaceSetter("HappySmiley");
								}
							}
						}

						if (event.getButton().toString() == "SECONDARY") {
							// if rightclick pressed save tilecoordinates
							if (event.getEventType().toString() == "MOUSE_PRESSED") {
								posMousePressedY = yCoorTiles;
								posMousePressedX = xCoorTiles;
							}
							// if rightclick released check if falseclick, sets flag int model.
							if (event.getEventType().toString() == "MOUSE_RELEASED") {
								if (!falseClick(yCoorTiles, xCoorTiles)) {
									model.setFlag(yCoorTiles, xCoorTiles);
								}
							}
						}

					}
				}
				// unpressed putton if mouseaction is unrelease mouse, leftclick,
				// and last button pressed wasnt smiley
				if (event.getEventType().toString() == "MOUSE_RELEASED" && event.getButton().toString() == "PRIMARY"
						&& !smileyPressed) {
					Model.setPressedButton(posMousePressedY, posMousePressedX, false);
					view.smileyFaceSetter("HappySmiley");
					view.drawSettingsButton(false);
				}

			}
		});
	}

	// checks if the position of mousepressed is the same as position of mouse
	// released.
	public boolean falseClick(int y, int x) {
		if (posMousePressedY == y && posMousePressedX == x) {
			return false;
		} else {
			return true;
		}
	}

	public void setNewDimensions(int height, int length) {
		this.height = height;
		this.length = length;

	}

	// getters
	public EventHandler<MouseEvent> getEventHandler() {
		return eventMouse;
	}

	public EventHandler<MouseEvent> getEventMouse() {
		return eventMouse;
	}

	public int getPosMousePressedX() {
		return posMousePressedX;
	}

	public int getPosMousePressedY() {
		return posMousePressedY;
	}

 // setters 
	
	public void setEventMouseAction(EventHandler<MouseEvent> eventHandler) {
		this.eventMouse = eventHandler;
	}

	public void setEventMouse(EventHandler<MouseEvent> eventMouse) {
		this.eventMouse = eventMouse;
	}

	public void setPosMousePressedX(int posMousePressedX) {
		this.posMousePressedX = posMousePressedX;
	}

	public void setPosMousePressedY(int posMousePressedY) {
		this.posMousePressedY = posMousePressedY;
	}

}

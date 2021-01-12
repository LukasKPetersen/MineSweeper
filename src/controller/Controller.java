package src.controller;

import src.gui.View;
import javafx.event.*;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import src.model.Model;

public class Controller {

	private EventHandler<MouseEvent> eventMouse;
	private EventHandler<ActionEvent> eventHandler;
	private int posMousePressedX;
	private int posMousePressedY;
	private boolean smileyPressed;

	@SuppressWarnings("unused")
	private Model model;
	@SuppressWarnings("unused")
	private View view;

	public Controller(final Model Model, final View view, int tilesize, int headerHeight, int borderThickness,
			int length, int height) {
		this.model = Model;
		this.view = view;
		this.setEventHandler(new EventHandler<ActionEvent>() {
			public void handle(ActionEvent evt) {

				Button x = (Button) evt.getSource();

				if (x.getId().equals("SmileyButton")) {
					// view.reset();
					// model.reset();
				}

			}
		});

		this.setEventMouseAction(new EventHandler<MouseEvent>() {
			public void handle(MouseEvent event) {
				double x = event.getX();
				double y = event.getY();
				System.out.println(x + "," + y);
				
				if (smileyPressed) {
					view.smileyFaceSetter("HappySmiley");
					smileyPressed = false;
				}
				if (x>(length/2.0-20) && x <(length/2.0+20) && 
						y >(headerHeight/2.0-10) && y < (headerHeight/2.0+30)) {
						System.out.println("suuces");
						if (event.getButton().toString() == "PRIMARY") {
							if (event.getEventType().toString() == "MOUSE_PRESSED") {
								smileyPressed = true;
								view.smileyFaceSetter("Pressed");
							}
							if (event.getEventType().toString() == "MOUSE_RELEASED") {
								if (smileyPressed) {
									view.reset();
									model.reset();
								}
							}
						}
					}
		
				if (y > headerHeight) {
					y -= headerHeight;
					if (x > borderThickness && x < length - borderThickness) {
						if (y > borderThickness && y < height - borderThickness) {
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
								if (event.getEventType().toString() == "MOUSE_PRESSED") {
									view.mousePressSound();
									posMousePressedY = yCoorTiles;
									posMousePressedX = xCoorTiles;
									Model.setPressedButton(posMousePressedY, posMousePressedX);
									if (!Model.isGameOver()) {
										view.smileyFaceSetter("TenseSmiley");
									}
								}
								if (event.getEventType().toString() == "MOUSE_RELEASED") {
									view.mouseReleaseSound();
									if (!falseClick(yCoorTiles, xCoorTiles)) {
										model.update(yCoorTiles, xCoorTiles);
									}
									if (!Model.isGameOver()) {
										view.smileyFaceSetter("HappySmiley");
									}
									Model.setPressedButton(posMousePressedY, posMousePressedX);
								}
							}
							if (event.getButton().toString() == "SECONDARY") {
								if (event.getEventType().toString() == "MOUSE_PRESSED") {
									posMousePressedY = yCoorTiles;
									posMousePressedX = xCoorTiles;
								}
								if (event.getEventType().toString() == "MOUSE_RELEASED") {
									if (!falseClick(yCoorTiles, xCoorTiles)) {
										model.setFlag(yCoorTiles, xCoorTiles);
									}
								}
							}

						}
					}
				}

			}
		});
	}

	public void setEventHandler(EventHandler<ActionEvent> ActionEventHandler) {
		this.eventHandler = ActionEventHandler;
	}

	public boolean falseClick(int y, int x) {
		if (posMousePressedY == y && posMousePressedX == x) {
			return false;
		} else {
			return true;
		}
	}

	// getters and setters

	public EventHandler<MouseEvent> getEventHandler() {
		return eventMouse;
	}

	public void setEventMouseAction(EventHandler<MouseEvent> eventHandler) {
		this.eventMouse = eventHandler;
	}

	public EventHandler<MouseEvent> getEventMouse() {
		return eventMouse;
	}

	public void setEventMouse(EventHandler<MouseEvent> eventMouse) {
		this.eventMouse = eventMouse;
	}

	public int getPosMousePressedX() {
		return posMousePressedX;
	}

	public void setPosMousePressedX(int posMousePressedX) {
		this.posMousePressedX = posMousePressedX;
	}

	public int getPosMousePressedY() {
		return posMousePressedY;
	}

	public void setPosMousePressedY(int posMousePressedY) {
		this.posMousePressedY = posMousePressedY;
	}

	public EventHandler<ActionEvent> getActionEventHandler() {
		return eventHandler;
	}

}

package controller;

import src.gui.View;
import javafx.event.*;
import javafx.scene.input.MouseEvent;
import model.Model;

public class Controller {

	private EventHandler<MouseEvent> eventMouse;
	private int posMousePressedX;
	private int posMousePressedY;
	
	public Controller(final Model model, final View view, int tilesize) {
		this.setEventMouseAction(new EventHandler<MouseEvent>() {
			public void handle(MouseEvent event) {
				double x = event.getX();
				double y = event.getY();
				
				int xCoorTiles = 0;
				int yCoorTiles = 0;
				while (x-tilesize> 0) {	
					x -=tilesize;
					xCoorTiles++;
				}
				while (y-tilesize> 0) {
					y -=tilesize;
					yCoorTiles++;
				}
				
				
				if (event.getButton().toString() == "PRIMARY") {
					if (event.getEventType().toString() == "MOUSE_PRESSED") {
						posMousePressedY = yCoorTiles;
						posMousePressedX = xCoorTiles;
						model.setPressedButton(posMousePressedY,posMousePressedX);
						
					}
					if (event.getEventType().toString() == "MOUSE_RELEASED") {
						
						if (!falseClick(yCoorTiles,xCoorTiles)) {
							model.update(yCoorTiles,xCoorTiles);
						}
						model.setPressedButton(posMousePressedY,posMousePressedX);
					}
				}
				if (event.getButton().toString() == "SECONDARY") {
					if (event.getEventType().toString() == "MOUSE_PRESSED") {
						posMousePressedY = yCoorTiles;
						posMousePressedX = xCoorTiles;
					}
					if (event.getEventType().toString() == "MOUSE_RELEASED") {
						if (!falseClick(yCoorTiles,xCoorTiles)) {
							model.setFlag(yCoorTiles, xCoorTiles);
						}
					}
				}	
			}
		});
	}
	
	public boolean falseClick(int y, int x) {
		if (posMousePressedY==y && posMousePressedX==x) {
			return false;
		} else {
		return true;
		}
	}

	public EventHandler<MouseEvent> getEventHandler() {
		return eventMouse;
	}
	public void setEventMouseAction(EventHandler<MouseEvent> eventHandler) {
		this.eventMouse = eventHandler;
	}
}


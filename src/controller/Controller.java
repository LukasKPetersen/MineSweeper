package controller;

import gui.View;
import javafx.event.*;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import model.Model;

public class Controller {

	private EventHandler<MouseEvent> eventMouse;
	private int posMousePressedX;
	private int posMousePressedY;
	@SuppressWarnings("unused")
	private Model model;
	@SuppressWarnings("unused")
	private View view;

	public Controller(final Model Model, final View view, int tilesize) {
		this.model=Model;
		this.view=view;
		
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
						Model.setPressedButton(posMousePressedY,posMousePressedX);
						
					}
					if (event.getEventType().toString() == "MOUSE_RELEASED") {
						
						if (!falseClick(yCoorTiles,xCoorTiles)) {
							model.update(yCoorTiles,xCoorTiles);
						}
						Model.setPressedButton(posMousePressedY,posMousePressedX);
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

	//getters and setters
	
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
	
}


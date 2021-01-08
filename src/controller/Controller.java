
package controller;



import gui.View;
import javafx.event.*;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import model.Model;


public class Controller {

	/*
	 * @SuppressWarnings("unused");
	 * 
	 * @SuppressWarnings("unused") private CounterView counterView;
	 */
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
						Model.setPressedButton(posMousePressedY,posMousePressedX);
						if (!falseClick(yCoorTiles,xCoorTiles)) {
							model.update(yCoorTiles,xCoorTiles);
						}
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
					
					//System.out.print("set flag at: ");
				}
				//System.out.println(yCoorTiles + "," + xCoorTiles);
			}
		});

	}
	public boolean falseClick(int y, int x) {
		if (posMousePressedY==y && posMousePressedX==x) {
			//System.out.print("real click and ");
			return false;
		} else {
			//System.out.print("false click and ");
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


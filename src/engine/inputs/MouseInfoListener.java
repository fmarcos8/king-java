package engine.inputs;

import java.awt.MouseInfo;
import java.awt.PointerInfo;
import java.awt.Window;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;

import engine.core.Scene;
import engine.utils.Vector2;

public class MouseInfoListener implements java.awt.event.MouseListener, MouseMotionListener {
	static Vector2 posCursor;
	static boolean inWindow = false;
	static boolean hold = false;
	
	@Override
	public void mouseClicked(MouseEvent e) {
		System.out.println("mouseClicked");
	}

	@Override
	public void mousePressed(MouseEvent e) {
		System.out.println("mousePressed");
		hold = true;
		
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		System.out.println("mouseReleased");
		hold = false;
	}

	@Override
	public void mouseEntered(MouseEvent e) {
		inWindow = true;
		
	}

	@Override
	public void mouseExited(MouseEvent e) {
		inWindow = false;
		hold = false;
	}
	
	public static Vector2 getPoint() {
		if(!inWindow) return null;
		return posCursor;
	}

	@Override
	public void mouseDragged(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseMoved(MouseEvent e) {
		posCursor = new Vector2(e.getX(), e.getY());
	}

	public static void getInfo() {
		if(inWindow) {
			Scene.setInfo("mousePos", posCursor.toString());		
			Scene.setInfo("mouseHold", hold);	
		}
		
	}


}

package engine.inputs;

import engine.components.CharacterController;
import engine.core.GameObject;
import engine.core.Scene;
import engine.utils.*;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;

import static engine.utils.Constants.Component.SHOW_COLLIDER_TRUE;
import static engine.utils.Constants.Game.*;
import static engine.utils.Constants.Game.GAME_SCALE;

public class KeyInputListener extends KeyAdapter implements KeyListener {
    private Scene currentScene;
    
    public KeyInputListener(Scene currentScene) {
        this.currentScene = currentScene;
    }

    @Override
    public void keyPressed(KeyEvent e) {
    	CharacterController.add(e.getKeyCode());
//    	poolKeys.add(e.getKeyCode());
//        for (int i = 0; i < currentScene.objects.size(); i++) {
//            GameObject object = currentScene.objects.get(i);
//            if (object.getType() == ObjectType.Player) {
//                if (e.getKeyCode() == KeyEvent.VK_LEFT) {
//                    object.setMoving(true);
//                    object.setLeft(true);
//                }
//                if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
//                    object.setMoving(true);
//                    object.setRight(true);
//                }
//                if (e.getKeyCode() == KeyEvent.VK_UP) {
//                    object.setMoving(true);
//                    object.setUp(true);
//                }
//                if (e.getKeyCode() == KeyEvent.VK_DOWN) {
//                    object.setMoving(true);
//                    object.setDown(true);
//                }
//                if (e.getKeyCode() == KeyEvent.VK_C) {
//                    object.setAttacking(true);
//                }
//                if (e.getKeyCode() == KeyEvent.VK_X) {
//                    object.setJump(true);
//                }
//            }
//        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
    	CharacterController.remove(e.getKeyCode());
//    	poolKeys.remove(e.getKeyCode());
//        for (int i = 0; i < currentScene.objects.size(); i++) {
//            GameObject object = currentScene.objects.get(i);
//            if (object.getType() == ObjectType.Player) {
//                if (e.getKeyCode() == KeyEvent.VK_LEFT) {
//                    object.setMoving(false);
//                    object.setLeft(false);
//                }
//                if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
//                    object.setMoving(false);
//                    object.setRight(false);
//                }
//                if (e.getKeyCode() == KeyEvent.VK_UP) {
//                    object.setMoving(false);
//                    object.setUp(false);
//                }
//                if (e.getKeyCode() == KeyEvent.VK_DOWN) {
//                    object.setMoving(false);
//                    object.setDown(false);
//                }
//                if (e.getKeyCode() == KeyEvent.VK_X) {
//                    object.setJump(false);
//                }
//            }
//        }
    }
}

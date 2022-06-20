package engine.components;

import static engine.utils.Constants.Game.GAME_SCALE;
import static engine.utils.Constants.Game.GRAVITY;
import static engine.utils.Constants.Game.JUMP_FORCE;
import static engine.utils.Functions.CanMoveHere;
import static engine.utils.Functions.GetEntityXPosNextToWall;
import static engine.utils.Functions.GetEntityYPosUnderRoofOrAboveFloor;
import static engine.utils.Functions.IsEntityOnFloor;

import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.HashMap;

import engine.core.Component;
import engine.core.ConfigManager;
import engine.core.GameObject;
import engine.core.Scene;
import engine.core.SceneManager;
import engine.utils.Vector2;

import static engine.utils.Constants.Game.*;

public class CharacterController extends Component {

	private static HashMap<Integer, Boolean> keyPool = new HashMap<Integer, Boolean>();
	private static int count = 0;
	
	private float fallSpeedAfterCollision = 0.5f * GAME_SCALE;
	private GameObject gameObject;
	private int index;
	private int keys[];
	private boolean up = false,
	        down = false,
	        left = false,
	        right = false,
	        moving = false,
	        attacking = false,
	        jump = false;
	
	public float speed = 3.0f;
	public static enum LookSide {
    	Left, Right
    };
    public LookSide lookSide = LookSide.Right;
	
	public CharacterController(GameObject go) {
		gameObject = go;
		index = count++;

    	ConfigManager cm = new ConfigManager("controller");
    	keys = new int[6];
    	keys[0] = Integer.parseInt(cm.getValue("player(index:"+index+").keys.left"));
    	keys[1] = Integer.parseInt(cm.getValue("player(index:"+index+").keys.right"));
    	keys[2] = Integer.parseInt(cm.getValue("player(index:"+index+").keys.up"));
    	keys[3] = Integer.parseInt(cm.getValue("player(index:"+index+").keys.down"));
    	keys[4] = Integer.parseInt(cm.getValue("player(index:"+index+").keys.jump"));
    	keys[5] = Integer.parseInt(cm.getValue("player(index:"+index+").keys.attack"));
	}
	
	public static void add(Integer i) {
//		System.out.println("add:"+i);
		keyPool.put(i, true);
	}
	
	public static void remove(Integer i) {
//		System.out.println("remove:"+i);
		keyPool.remove(i);
	}
	
	private void move(int i) {
		if(i == 0 || i == 1) {
			moving = true;
		}
		
		if(i == 0) {
			left = true;
//			gameObject.getTransform().position.x -= 3.0f;
		} else if(i == 1) {
			right = true;
//			gameObject.getTransform().position.x += 3.0f;
		} else if(i == 1) {
			right = true;
//			gameObject.getTransform().position.x += 3.0f;
		} else if(i == 4) {
			jump = true;
//			gameObject.getTransform().position.x += 3.0f;
		} else if(i == 5) {
			attacking = true;
//			gameObject.getTransform().position.x += 3.0f;
		} 
	}
	
	private void stop(int i) {
		if(i == 0) {
			left = false;
//			gameObject.getTransform().position.x -= 3.0f;
		} else if(i == 1) {
			right = false;
//			gameObject.getTransform().position.x += 3.0f;
		} else if(i == 4) {
			jump = false;
//			gameObject.getTransform().position.x += 3.0f;
		} else if(i == 5) {
			attacking = false;
//			gameObject.getTransform().position.x += 3.0f;
		}
	}
	
	private void jump() {
		RigidBody rb = gameObject.getComponent(RigidBody.class);
		if(rb != null) {
			if (rb != null && rb.inAir)
				return;
			
			rb.inAir = true;	
			rb.airSpeed = JUMP_FORCE;
		}
	}
	
	@Override
    public void update() {
		for(int i = 0; i < keys.length; i++) {
			if(keyPool.getOrDefault(keys[i], false)) {
				move(i);
			} else {
				stop(i);
			}
		}
		if(!left && !right) moving = false;
		
		RigidBody rb = gameObject.getComponent(RigidBody.class);
		
		if (jump)
            jump();

        if (!left && !right && !up && !down && (rb != null && !rb.inAir))
            return;

        Vector2 dir = new Vector2(0,0);

        if (left) {
        	dir.x -= speed;   
        	lookSide = LookSide.Left;
        }

        if (right) {
        	dir.x += speed;        
        	lookSide = LookSide.Right;
        }

        if (rb != null && !rb.inAir) {
        	BoxCollider2D bc = gameObject.getComponent(BoxCollider2D.class);
            if (bc != null && !IsEntityOnFloor(bc.getHitBox(), SceneManager.currentSceneData)) {
            	rb.inAir = true;
            }
        }

        updatePos(dir);
    }
	
	private void updatePos(Vector2 dir) {
		BoxCollider2D bc = gameObject.getComponent(BoxCollider2D.class);
		
		if(bc != null) {
			if (CanMoveHere(gameObject.getTransform().position.x + dir.x, gameObject.getTransform().position.y, bc.getHitBox().width, bc.getHitBox().height, SceneManager.currentSceneData)) {
				gameObject.getTransform().position.sum(dir);
			} else {
				gameObject.getTransform().position.x = GetEntityXPosNextToWall(bc.getHitBox(), dir.x);
			}			
		} else {
			gameObject.getTransform().position.sum(dir);
		}
//		gameObject.getTransform().position
	}
	
	public boolean isUp() {
        return up;
    }

    public boolean isDown() {
        return down;
    }

    public boolean isLeft() {
        return left;
    }

    public boolean isRight() {
        return right;
    }

    public boolean isMoving() {
        return moving;
    }

    public boolean isAttacking() {
        return attacking;
    }

    public boolean isJump() {
        return jump;
    }

}
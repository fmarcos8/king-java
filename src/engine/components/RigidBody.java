package engine.components;

import static engine.utils.Constants.Game.GAME_SCALE;
import static engine.utils.Constants.Game.GRAVITY;
import static engine.utils.Functions.CanMoveHere;
import static engine.utils.Functions.GetEntityXPosNextToWall;
import static engine.utils.Functions.GetEntityYPosUnderRoofOrAboveFloor;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.geom.Rectangle2D;

import engine.core.Component;
import engine.core.GameObject;
import engine.core.Scene;
import engine.core.SceneManager;
import engine.utils.Cheats;
import engine.utils.Vector2;

public class RigidBody extends Component{

	private GameObject gameObject;
	public float airSpeed = 0.0f;
	private float fallSpeedAfterCollision = 0.5f * GAME_SCALE;
	public boolean inAir;
	private Vector2 force = new Vector2(0,0);
	private Vector2 moviment = new Vector2(0,0);
	
	public RigidBody(GameObject go) {
		gameObject = go;
		inAir = true;
	}
	
	public RigidBody(GameObject go, boolean ia) {
		gameObject = go;
		inAir = ia;
	}
	
	@Override
    public void update() {
		Vector2 dir = new Vector2(0,0);
		dir.sum(force);
		dir.sum(moviment);
		force.y = 0f;
		if (inAir) {
			if(!Cheats.CheatActivated("flymode")) {
				dir.y += airSpeed;
				airSpeed += GRAVITY;
			}
			updatePosY(dir);				
        } else {
        	airSpeed = 0;
        }
		updatePosX(dir);
		if(Cheats.CheatActivated("flymode")) {
			updatePosY(dir);
		}
		updateForce();
		moviment = new Vector2(0,0);
    }
	
	private void updateForce() {
		if(this.force.x > 0.1f) {
			this.force.x -= 0.1f;
		} else if(this.force.x < -0.1f){
			this.force.x += 0.1f;
		} else {
			this.force = new Vector2(0,0);
		}
	}
	
	public void addForce(Vector2 force) {
		this.force.sum(force);
		inAir = true;
	}
	
	public void addMoviment(Vector2 moviment) {
		this.moviment = moviment;
		if(!Cheats.CheatActivated("flymode") && moviment.y != 0) {
			inAir = true;			
		}
		Scene.setInfo("moviment", moviment.toString());
	}
	
	private void updatePosY(Vector2 dir) {
		BoxCollider2D boxCollider = gameObject.getComponent(BoxCollider2D.class);

        if (boxCollider == null || CanMoveHere(gameObject.getTransform().position.x, gameObject.getTransform().position.y + dir.y, boxCollider.getHitBox().width, boxCollider.getHitBox().height, SceneManager.currentSceneData)) {
        	gameObject.getTransform().position.y += dir.y;
        	boxCollider.down = false;
			boxCollider.up = false;
        } else {
        	gameObject.getTransform().position.y = GetEntityYPosUnderRoofOrAboveFloor(boxCollider.getHitBox(), dir.y);
        	if(dir.y > 0) {
        		boxCollider.down = true;
        		boxCollider.up = false;
        		inAir = false;
        	}else {
				boxCollider.up = true;
				boxCollider.down = false;
			}
        }
	}
	
	private void updatePosX(Vector2 dir){
		BoxCollider2D boxCollider = gameObject.getComponent(BoxCollider2D.class);

		if(dir.x != 0) {
			if (!Cheats.CheatActivated("flymode") && (boxCollider == null || CanMoveHere(gameObject.getTransform().position.x, gameObject.getTransform().position.y + dir.y + 1, boxCollider.getHitBox().width, boxCollider.getHitBox().height, SceneManager.currentSceneData))) {
				inAir = true;
			}
			if (boxCollider == null || CanMoveHere(gameObject.getTransform().position.x + dir.x, gameObject.getTransform().position.y, boxCollider.getHitBox().width, boxCollider.getHitBox().height, SceneManager.currentSceneData)) {
				gameObject.getTransform().position.x += dir.x;
				boxCollider.right = false;
				boxCollider.left = false;
			} else {
				gameObject.getTransform().position.x = GetEntityXPosNextToWall(boxCollider.getHitBox(), dir.x);
				if(dir.x > 0) {
					boxCollider.right = true;
					boxCollider.left = false;
				}
				else {
					boxCollider.left = true;
					boxCollider.right = false;
				}
			}			
		}
	}
}

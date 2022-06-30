package engine.components;

import static engine.utils.Constants.Game.GAME_SCALE;
import static engine.utils.Constants.Game.GRAVITY;
import static engine.utils.Functions.CanMoveHere;
import static engine.utils.Functions.GetEntityXPosNextToWall;
import static engine.utils.Functions.GetEntityYPosUnderRoofOrAboveFloor;

import java.awt.geom.Rectangle2D;

import engine.core.Component;
import engine.core.GameObject;
import engine.core.SceneManager;
import engine.utils.Vector2;

public class RigidBody extends Component{

	private GameObject gameObject;
	public float airSpeed = 0.0f;
	private float fallSpeedAfterCollision = 0.5f * GAME_SCALE;
	public boolean inAir;
	private Vector2 force = new Vector2(0,0);
	
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
		BoxCollider2D boxCollider = gameObject.getComponent(BoxCollider2D.class);
		Vector2 dir = new Vector2(0,0);
		dir.sum(force);
		force.y = 0f;
		if (inAir) {
            if (boxCollider == null || CanMoveHere(gameObject.getTransform().position.x, gameObject.getTransform().position.y + airSpeed, boxCollider.getHitBox().width, boxCollider.getHitBox().height, SceneManager.currentSceneData)) {
            	dir.y += airSpeed;
                airSpeed += GRAVITY;
                updatePos(dir);
            } else {
            	gameObject.getTransform().position.y = GetEntityYPosUnderRoofOrAboveFloor(boxCollider.getHitBox(), airSpeed);
                if (airSpeed > 0) {
                    inAir = false;
                    airSpeed = 0;
                } else
                    airSpeed = fallSpeedAfterCollision;
                
                updatePos(dir);
            }
        } else {
        	airSpeed = 0;
        }
		updateForce();
    }
	
	private void updateForce() {
		if(this.force.x > 0f) {
			this.force.x -= 0.1f;
		} else if(this.force.x < 0f){
			this.force.x += 0.1f;
		}
	}
	
	public void addForce(Vector2 force) {
		this.force = force;
		inAir = true;
	}
	
	private void updatePos(Vector2 dir) {
		BoxCollider2D boxCollider = gameObject.getComponent(BoxCollider2D.class);

        if (boxCollider == null || CanMoveHere(gameObject.getTransform().position.x + dir.x, gameObject.getTransform().position.y, boxCollider.getHitBox().width, boxCollider.getHitBox().height, SceneManager.currentSceneData)) {
        	gameObject.getTransform().position.sum(dir);
        } else {
        	gameObject.getTransform().position.x = GetEntityXPosNextToWall(boxCollider.getHitBox(), dir.x);
        }
	}
}

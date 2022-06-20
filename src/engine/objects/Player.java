package engine.objects;

import engine.components.BoxCollider2D;
import engine.components.CharacterController;
import engine.components.RigidBody;
import engine.components.Sprite;
import engine.core.Component;
import engine.core.GameObject;
import engine.core.Scene;
import engine.core.SceneManager;
import engine.utils.ObjectType;
import engine.utils.Size;
import engine.utils.Transform;
import engine.utils.Vector2;

import java.awt.*;
import java.awt.geom.Rectangle2D;

import static engine.utils.Constants.Component.SHOW_COLLIDER_TRUE;
import static engine.utils.Constants.Game.*;
import static engine.utils.Constants.PlayerConstants.*;
import static engine.utils.Functions.*;

public class Player extends GameObject {
    private final float SPEED = 3.0f;
    private int currentAnimation = ANIM_IDLE;
    private float xDrawOffset = 21 * GAME_SCALE;
    private float yDrawOffset = 17 * GAME_SCALE;
    private float airSpeed = 0.0f;
    private float fallSpeedAfterCollision = 0.5f * GAME_SCALE;
    
    public GameObject AreaDamage;

    public Player(Transform transform, Size size) {
        super(transform, size);
        transform.position.sub(cameraScene);
        this.type = ObjectType.Player;

        this.addComponent(new Sprite(SPRITE_PATH, WIDTH, HEIGHT));
        this.addComponent(new BoxCollider2D((int)(21 * GAME_SCALE), (int)(27 * GAME_SCALE), SHOW_COLLIDER_TRUE));
        this.addComponent(new CharacterController(this));
        this.addComponent(new RigidBody(this));
    }
    
    public void start() {
        AreaDamage = new GameObject(new Transform(new Vector2(transform.position.x + 45, transform.position.y-20), 1.0f), size);
        AreaDamage.addComponent(new BoxCollider2D((int)(21 * GAME_SCALE)+25, (int)(27 * GAME_SCALE)+40, SHOW_COLLIDER_TRUE));
        AreaDamage.activated = false;
        Instantiate(AreaDamage, ObjectType.Damage_Player);
    }
    
    private void setAnimation() {
        int startAnim = currentAnimation;

        if (getComponent(CharacterController.class).isMoving()) {
            currentAnimation = ANIM_RUN;
        } else {
            currentAnimation = ANIM_IDLE;
        }

        if (getComponent(RigidBody.class) != null && getComponent(RigidBody.class).inAir) {
            if (airSpeed < 0) {
                currentAnimation = ANIM_JUMP;
            } else {
                currentAnimation = ANIM_FALL;
            }
        }

        if (getComponent(CharacterController.class).isAttacking()) {
            currentAnimation = ANIM_ATTACK;
        }

        if (startAnim != currentAnimation)
            resetAnimTick();
    }

    private void resetAnimTick() {
        getComponent(Sprite.class).animTick = 0;
        getComponent(Sprite.class).animIndex = 0;
    }

    private void updateAnimation() {
        getComponent(Sprite.class).animTick++;
        if (getComponent(Sprite.class).animTick >= getComponent(Sprite.class).animSpeed) {
            getComponent(Sprite.class).animTick = 0;
            getComponent(Sprite.class).animIndex++;
            if (getComponent(Sprite.class).animIndex == GetAnimationFrameCount(currentAnimation)) {
                getComponent(Sprite.class).animIndex = 0;
//                attacking = false;
            }
        }
    }

    @Override
    public void update() {
    	super.update();
        updateAnimation();
        setAnimation();
        CharacterController cc = getComponent(CharacterController.class);
        if(cc.lookSide == CharacterController.LookSide.Right) {
        	AreaDamage.getTransform().position = new Vector2(transform.position.x + 45, transform.position.y-20);        	
        } else {
        	AreaDamage.getTransform().position = new Vector2(transform.position.x - 70, transform.position.y-20); 
        }
        if(cc.isAttacking()) {
        	AreaDamage.activated = true;
        } else {
        	AreaDamage.activated = false;
        }
        
        if(transform.position.x > GAME_WIDTH/2) {
			int MAX = SceneManager.currentSceneData[0].length * TILES_SIZE;
			if(Scene.CameraScene.x < MAX - GAME_WIDTH) {
				Scene.CameraScene.x = transform.position.x+Scene.CameraScene.x - GAME_WIDTH/2;				
				transform.position.x = GAME_WIDTH/2;
			}
		} else if(Scene.CameraScene.x > 0){
			Scene.CameraScene.x = transform.position.x+Scene.CameraScene.x - GAME_WIDTH/2;
			transform.position.x = GAME_WIDTH/2;
		}
    }

    @Override
    public void render(Graphics g) {
        int currentFrame = getComponent(Sprite.class).animIndex;

        if(getComponent(CharacterController.class).lookSide == CharacterController.LookSide.Right) {
        	g.drawImage(
        		getComponent(Sprite.class).getAnimations()[currentAnimation][currentFrame],
        		(int)(transform.position.x - xDrawOffset),
        		(int)(transform.position.y - yDrawOffset),
        		(int)(size.width * GAME_SCALE),
        		(int)(size.height * GAME_SCALE),
        		null
        	);        	
        } else {
        	g.drawImage(
        		getComponent(Sprite.class).getAnimations()[currentAnimation][currentFrame],
        		(int)(transform.position.x - xDrawOffset)+(int)(size.width * GAME_SCALE)-30,
        		(int)(transform.position.y - yDrawOffset),
        		(int)((size.width) * (GAME_SCALE))*-1,
        		(int)(size.height * GAME_SCALE),
        		null
        	); 
        }
        
        super.render(g);
    }
}

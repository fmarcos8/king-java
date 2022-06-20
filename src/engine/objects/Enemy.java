package engine.objects;

import engine.components.BoxCollider2D;
import engine.components.CharacterController;
import engine.components.RigidBody;
import engine.components.Sprite;
import engine.core.GameObject;
import engine.core.Scene;
import engine.core.SceneManager;
import engine.utils.ObjectType;
import engine.utils.Size;
import engine.utils.Transform;
import engine.utils.Vector2;

import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.util.List;

import static engine.utils.Constants.Component.SHOW_COLLIDER_TRUE;
import static engine.utils.Constants.EnemyConstants.*;
import static engine.utils.Constants.Game.GAME_SCALE;
import static engine.utils.Constants.Game.GRAVITY;
import static engine.utils.Constants.PlayerConstants.ANIM_ATTACK;
import static engine.utils.Constants.PlayerConstants.ANIM_FALL;
import static engine.utils.Constants.PlayerConstants.ANIM_IDLE;
import static engine.utils.Constants.PlayerConstants.ANIM_JUMP;
import static engine.utils.Constants.PlayerConstants.ANIM_RUN;
import static engine.utils.Functions.CanMoveHere;

public class Enemy extends GameObject {
    private int currentAnimation = ANIM_IDLE;
    private float xDrawOffset = 10 * GAME_SCALE;
    private float yDrawOffset = 10 * GAME_SCALE;
    private float airSpeed = 0.0f;
    private int atacatedAnimationCount = 0;
    private Vector2 lastModCam = new Vector2(0,0);

    public Enemy(Transform transform, Size size) {
        super(transform, size);
        transform.position.sub(cameraScene);
        this.type = ObjectType.Enemy;

        this.addComponent(new Sprite(PIG_SPRITE_PATH, WIDTH, HEIGHT));
        this.addComponent(new BoxCollider2D((int)(18 * GAME_SCALE), (int)(17 * GAME_SCALE), SHOW_COLLIDER_TRUE));
        this.addComponent(new RigidBody(this));
        this.addComponent(new CharacterController(this));
    }

    @Override
    public void update() {
        updateAnimation();
        setAnimation();
        updatePosition();
        List<GameObject> aux = parentScene.getObjects(ObjectType.Damage_Player);
        for(int i = 0; i < aux.size(); i++) {
        	GameObject go = aux.get(i);
        	BoxCollider2D bce = getComponent(BoxCollider2D.class);
        	if(go.activated) {
        		BoxCollider2D bcd = go.getComponent(BoxCollider2D.class);
        		if(bcd != null && bce != null) {
        			if(BoxCollider2D.intersection(bcd, bce)) {
        				bce.intersected = true;
        				atacatedAnimationCount = 3;
        			} else {
        				bce.intersected = false;
        			}
        		}
        	} else {
        		bce.intersected = false;
        	}
        }
        super.update();
    }

    private void updatePosition() {
    	if(Scene.CameraScene.x != lastModCam.x) {
    		transform.position.x +=  lastModCam.x - Scene.CameraScene.x;
    		lastModCam.x = Scene.CameraScene.x;    		
    	}
//        if (getComponent(RigidBody.class).inAir) {
//            if (CanMoveHere(transform.position.x, transform.position.y + airSpeed, getComponent(BoxCollider2D.class).getHitBox().width, getComponent(BoxCollider2D.class).getHitBox().height, SceneManager.currentSceneData)) {
//                transform.position.y += airSpeed;
//                airSpeed += GRAVITY;
//            }
//        }
        
    }
    
    private void setAnimation() {
        int startAnim = currentAnimation;

        if (atacatedAnimationCount>0) {
            currentAnimation = ANIM_DAMAGE;
        } else {
        	currentAnimation = ANIM_IDLE;
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
                if(atacatedAnimationCount>0) {
                	atacatedAnimationCount--;
                }
            }
        }
    }

    @Override
    public void render(Graphics g) {
        int currentFrame = getComponent(Sprite.class).animIndex;
        
        try {
        	g.drawImage(getComponent(Sprite.class).getAnimations()[this.currentAnimation][currentFrame],
        			(int)(transform.position.x - xDrawOffset),
        			(int)(transform.position.y - yDrawOffset),
        			(int)(size.width * GAME_SCALE),
        			(int)(size.height * GAME_SCALE),
        			null
        			);			
		} catch (Exception e) {
			System.out.println(this.currentAnimation);
			System.out.println(currentFrame);
			System.exit(-1);
		}

        super.render(g);
    }
}

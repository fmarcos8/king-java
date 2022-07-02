package engine.objects;

import engine.components.BoxCollider2D;
import engine.components.CharacterController;
import engine.components.RigidBody;
import engine.components.Sprite;
import engine.components.CharacterController.LookSide;
import engine.core.GameObject;
import engine.core.Scene;
import engine.core.SceneManager;
import engine.utils.ObjectType;
import engine.utils.Size;
import engine.utils.Transform;
import engine.utils.Vector2;

import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

import static engine.utils.Constants.Component.SHOW_COLLIDER_TRUE;
import static engine.utils.Constants.EnemyConstants.*;
import static engine.utils.Constants.Game.GAME_SCALE;
import static engine.utils.Constants.Game.GRAVITY;
import static engine.utils.Functions.CanMoveHere;
import static engine.utils.Functions.GetEntityXPosNextToWall;

public class Enemy extends GameObject {
    private int currentAnimation = ANIM_IDLE;
    private float xDrawOffset = 10 * GAME_SCALE;
    private float yDrawOffset = 10 * GAME_SCALE;
    private float airSpeed = 0.0f;
    private int atacatedAnimationCount = 0;
    private Vector2 lastModCam = new Vector2(0,0);
    private LookSide lookSide = LookSide.Left;
    private int life = 100;

    private boolean isMove = false;
    private int movimentCount = 0;
    private int stopCount = 200;
    private int[] attackMode;
    
    public GameObject[] AreaDamage = new GameObject[2];
    
    public Enemy(Transform transform, Size size) {
        super(transform, size);
        attackMode = new int[] {0,0};
        transform.position.sub(cameraScene);
        this.type = ObjectType.Enemy;

        this.addComponent(new Sprite(PIG_SPRITE_PATH, WIDTH, HEIGHT));
        this.addComponent(new BoxCollider2D((int)(18 * GAME_SCALE), (int)(17 * GAME_SCALE), SHOW_COLLIDER_TRUE));
        this.addComponent(new RigidBody(this));
    }
    
    public void start() {
    	AreaDamage[0] = new GameObject(new Transform(new Vector2(transform.position.x - 110, transform.position.y), 1.0f), size);
        AreaDamage[0].addComponent(new BoxCollider2D((int)(21 * GAME_SCALE)+70, (int)(27 * GAME_SCALE)-20, SHOW_COLLIDER_TRUE));
        AreaDamage[0].activated = false;
        Instantiate(AreaDamage[0], ObjectType.Damage_Enemy_Left);
        
        AreaDamage[1] = new GameObject(new Transform(new Vector2(transform.position.x + 34, transform.position.y), 1.0f), size);
        AreaDamage[1].addComponent(new BoxCollider2D((int)(21 * GAME_SCALE)+70, (int)(27 * GAME_SCALE)-20, SHOW_COLLIDER_TRUE));
        AreaDamage[1].activated = false;
        Instantiate(AreaDamage[1], ObjectType.Damage_Enemy_Right);
    }
    
    public void movimentRand() {
    	if(stopCount == 0) {
    		Random gerador = new Random();
    		int x = gerador.nextInt(11)-5;
    		x = x * 50;
    		if(x!=0) {
    			isMove = true;
    			if(x>0) {
    				lookSide = lookSide.Right;
    			}
    			if(x<0) {
    				lookSide = lookSide.Left;
    			}
    			movimentCount = x;
    		}
    		stopCount = gerador.nextInt(200)+200;
    	} else {
    		isMove = false;
    		stopCount-= 1;
    	}
    }

    @Override
    public void update() {
        updateAnimation();
        setAnimation();
        List<GameObject> aux = parentScene.getObjects(
	        new ObjectType[]{
	        	ObjectType.Damage_Player_Left, 
	        	ObjectType.Damage_Player_Right	
	        }
        );
        BoxCollider2D bce = getComponent(BoxCollider2D.class);
        boolean hasDamage = false;
        for(int i = 0; i < aux.size(); i++) {
        	GameObject go = aux.get(i);
        	if(go.activated) {
        		BoxCollider2D bcd = go.getComponent(BoxCollider2D.class);
        		if(bcd != null && bce != null) {
        			if(BoxCollider2D.intersection(bcd, bce) && !bce.intersected) {
        				bce.intersected = true;
        				life--;
        				movimentCount = 0;
        				hasDamage = true;
        				atacatedAnimationCount = 4;
        				RigidBody rb = getComponent(RigidBody.class);
        				if(go.getType() == ObjectType.Damage_Player_Left) {
        					rb.addForce(new Vector2(-5,-2));
        					lookSide = lookSide.Left;
        				}
        				if(go.getType() == ObjectType.Damage_Player_Right) {
        					rb.addForce(new Vector2(5,-2));
        					lookSide = lookSide.Right;
        				}
        			}
        		}
        	}
        }
        Scene.setInfo("atacatedAnimationCount enemy", atacatedAnimationCount);
        
        bce.intersected = !(!hasDamage && atacatedAnimationCount == 0);
        
        GameObject player = parentScene.getObject(ObjectType.Player);
        if(player != null && !hasDamage) {
        	BoxCollider2D bcp = player.getComponent(BoxCollider2D.class);
        	for (GameObject go : AreaDamage ) {
        		BoxCollider2D bcgo = go.getComponent(BoxCollider2D.class);
        		if(BoxCollider2D.intersection(bcp, bcgo) && attackMode[0]==0) {
        			RigidBody rb = getComponent(RigidBody.class);
        			if(go.getType() == ObjectType.Damage_Enemy_Left) {
        				lookSide = lookSide.Left;
        				rb.addForce(new Vector2(-5,-5));
        				bcgo.width = 20;
        				go.activated = true;
        			}
        			if(go.getType() == ObjectType.Damage_Enemy_Right) {
        				lookSide = lookSide.Right;
        				rb.addForce(new Vector2(5,-5));
        				bcgo.width = 20;
        				go.activated = true;
        			}
        			attackMode[0] = 150;
        			attackMode[1] = 50;
        			movimentCount = 0;
        			stopCount = 100;
        		}	
        		if(attackMode[1] == 0) {
        			go.activated = false;
        			bcgo.width = (int)(21 * GAME_SCALE)+70;
                }
			}
        }

        AreaDamage[1].getTransform().position = new Vector2(transform.position.x + 34, transform.position.y);        
        if(AreaDamage[0].getComponent(BoxCollider2D.class).width == 20) {
        	AreaDamage[0].getTransform().position = new Vector2(transform.position.x - 18, transform.position.y);         	
        } else {
        	AreaDamage[0].getTransform().position = new Vector2(transform.position.x - 110, transform.position.y);         	
        }
        
        

        updatePosition();
        super.update();
        Scene.setInfo("life enemy", life);
        
//        if(isMove) {
//        	currentAnimation = ANIM_MOVE;
//        } else {
//        	currentAnimation = ANIM_IDLE;
//        }
//        if(bce.intersected) {
//        	currentAnimation = ANIM_DAMAGE;
//        }
    }

    private void updatePosition() {
    	Vector2 dir = new Vector2(0,0);
    	if(movimentCount != 0) {
    		if(movimentCount>0) {
    			dir.x += 1;
    			movimentCount--;
    		}
    		if(movimentCount<0) {
    			dir.x -= 1;
    			movimentCount++;
    		}
    	} else {
    		movimentRand();
    	}
    	RigidBody rb = getComponent(RigidBody.class);
    	rb.addMoviment(dir);
    	if(Scene.CameraScene.x != lastModCam.x) {
    		transform.position.x +=  lastModCam.x - Scene.CameraScene.x;
    		lastModCam.x = Scene.CameraScene.x;    		
    	}
    	if(Scene.CameraScene.y != lastModCam.y) {
    		transform.position.y +=  lastModCam.y - Scene.CameraScene.y;
    		lastModCam.y = Scene.CameraScene.y;    		
    	}
    }
    
    private void setAnimation() {
        int startAnim = currentAnimation;

        if (atacatedAnimationCount>0) {
            currentAnimation = ANIM_DAMAGE;
        } else if(isMove) {
        	currentAnimation = ANIM_MOVE;
        } else if(attackMode[1] > 0) {
        	currentAnimation = ANIM_ATTACK;     
        } else {
        	currentAnimation = ANIM_IDLE;
        }
        
//        RigidBody rb = getComponent(RigidBody.class);
//        if(rb.left || rb.right) {
//        	currentAnimation = ANIM_IDLE;
//        }
        
        if(attackMode[0] > 0) attackMode[0]--;
        if(attackMode[1] > 0) attackMode[1]--;

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
        	if(lookSide == LookSide.Left) {
        		g.drawImage(getComponent(Sprite.class).getAnimations()[this.currentAnimation][currentFrame],
        				(int)(transform.position.x - xDrawOffset),
        				(int)(transform.position.y - yDrawOffset),
        				(int)(size.width * GAME_SCALE),
        				(int)(size.height * GAME_SCALE),
        				null
        				);	        		
        	} else {
        		g.drawImage(getComponent(Sprite.class).getAnimations()[this.currentAnimation][currentFrame],
        				(int)(transform.position.x - xDrawOffset)+(int)(size.width * GAME_SCALE)+10,
        				(int)(transform.position.y - yDrawOffset),
        				(int)(size.width * GAME_SCALE)*-1,
        				(int)(size.height * GAME_SCALE),
        				null
        				);	
        	}
		} catch (Exception e) {
			System.out.println(this.currentAnimation);
			System.out.println(currentFrame);
			System.exit(-1);
		}

        super.render(g);
    }
}

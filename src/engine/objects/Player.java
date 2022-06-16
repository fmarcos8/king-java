package engine.objects;

import engine.components.BoxCollider2D;
import engine.components.Sprite;
import engine.core.GameObject;
import engine.utils.ObjectType;
import engine.utils.Size;
import engine.utils.Transform;

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

    public Player(Transform transform, Size size, int[][] sceneData) {
        super(transform, size, sceneData);
        this.type = ObjectType.Player;
        inAir = true;

        this.addComponent(new Sprite(SPRITE_PATH, WIDTH, HEIGHT));
        this.addComponent(new BoxCollider2D((int)(21 * GAME_SCALE), (int)(27 * GAME_SCALE), SHOW_COLLIDER_TRUE));
    }

    private void setAnimation() {
        int startAnim = currentAnimation;

        if (moving) {
            currentAnimation = ANIM_RUN;
        } else {
            currentAnimation = ANIM_IDLE;
        }

        if (inAir) {
            if (airSpeed < 0) {
                currentAnimation = ANIM_JUMP;
            } else {
                currentAnimation = ANIM_FALL;
            }
        }

        if (attacking) {
            currentAnimation = ANIM_ATTACK;
        }

        if (startAnim != currentAnimation)
            resetAnimTick();
    }

    private void resetAnimTick() {
        getComponent(Sprite.class).animTick = 0;
        getComponent(Sprite.class).animIndex = 0;
    }

    private void movement() {
        if (jump)
            jump();

        if (!left && !right && !up && !down && !inAir)
            return;

        float xSpeed = 0;

        if (left)
            xSpeed -= SPEED;

        if (right)
            xSpeed += SPEED;

        if (!inAir) {
            if (!IsEntityOnFloor(getComponent(BoxCollider2D.class).getHitBox(), sceneData)) {
                inAir = true;
            }
        }

        if (inAir) {
            if (CanMoveHere(transform.position.x, transform.position.y + airSpeed, getComponent(BoxCollider2D.class).getHitBox().width, getComponent(BoxCollider2D.class).getHitBox().height, sceneData)) {
                transform.position.y += airSpeed;
                airSpeed += GRAVITY;
                updateXPos(xSpeed);
            } else {
                transform.position.y = GetEntityYPosUnderRoofOrAboveFloor(getComponent(BoxCollider2D.class).getHitBox(), airSpeed);
                if (airSpeed > 0) {
                    inAir = false;
                    airSpeed = 0;
                } else
                    airSpeed = fallSpeedAfterCollision;

                updateXPos(xSpeed);
            }
        } else {
            updateXPos(xSpeed);
        }
    }

    private void jump() {
        if (inAir)
            return;

        inAir = true;
        airSpeed = JUMP_FORCE;
    }

    private void updateXPos(float xSpeed) {
        float x = transform.position.x;
        float y = transform.position.y;
        Rectangle2D.Float hitBox = getComponent(BoxCollider2D.class).getHitBox();

        if (CanMoveHere(x + xSpeed, y, hitBox.width, hitBox.height, sceneData)) {
            transform.position.x += xSpeed;
        } else {
            transform.position.x = GetEntityXPosNextToWall(hitBox, xSpeed);
        }
    }

    private void updateAnimation() {
        getComponent(Sprite.class).animTick++;
        if (getComponent(Sprite.class).animTick >= getComponent(Sprite.class).animSpeed) {
            getComponent(Sprite.class).animTick = 0;
            getComponent(Sprite.class).animIndex++;
            if (getComponent(Sprite.class).animIndex == GetAnimationFrameCount(currentAnimation)) {
                getComponent(Sprite.class).animIndex = 0;
                attacking = false;
            }
        }
    }

    @Override
    public void update() {
        updateAnimation();
        setAnimation();
        movement();
    }

    @Override
    public void render(Graphics g) {
        int currentFrame = getComponent(Sprite.class).animIndex;

        g.drawImage(
            getComponent(Sprite.class).getAnimations()[currentAnimation][currentFrame],
            (int)(transform.position.x - xDrawOffset),
            (int)(transform.position.y - yDrawOffset),
            (int)(size.width * GAME_SCALE),
            (int)(size.height * GAME_SCALE),
            null
        );

        if (getComponent(BoxCollider2D.class).show) {
            Rectangle2D.Float collider = getComponent(BoxCollider2D.class).getHitBox();
            Graphics2D g2d = (Graphics2D) g;
            g2d.setColor(Color.GREEN);
            g2d.drawRect((int)collider.x, (int)collider.y, (int)collider.width, (int)collider.height);
        }
    }
}

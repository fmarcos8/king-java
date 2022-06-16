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
import static engine.utils.Constants.EnemyConstants.*;
import static engine.utils.Constants.Game.GAME_SCALE;
import static engine.utils.Constants.Game.GRAVITY;
import static engine.utils.Constants.PlayerConstants.GetAnimationFrameCount;
import static engine.utils.Functions.CanMoveHere;

public class Enemy extends GameObject {
    private int currentAnimation = ANIM_IDLE;
    private float xDrawOffset = 10 * GAME_SCALE;
    private float yDrawOffset = 10 * GAME_SCALE;
    private float airSpeed = 0.0f;

    public Enemy(Transform transform, Size size, int[][] sceneData) {
        super(transform, size, sceneData);
        this.type = ObjectType.Enemy;
        this.inAir = true;

        this.addComponent(new Sprite(PIG_SPRITE_PATH, WIDTH, HEIGHT));
        this.addComponent(new BoxCollider2D((int)(18 * GAME_SCALE), (int)(17 * GAME_SCALE), SHOW_COLLIDER_TRUE));
    }

    @Override
    public void update() {
        updateAnimation();
        updatePosition();
    }

    private void updatePosition() {
        if (inAir) {
            if (CanMoveHere(transform.position.x, transform.position.y + airSpeed, getComponent(BoxCollider2D.class).getHitBox().width, getComponent(BoxCollider2D.class).getHitBox().height, sceneData)) {
                transform.position.y += airSpeed;
                airSpeed += GRAVITY;
            }
        }
    }

    private void updateAnimation() {
        getComponent(Sprite.class).animTick++;
        if (getComponent(Sprite.class).animTick >= getComponent(Sprite.class).animSpeed) {
            getComponent(Sprite.class).animTick = 0;
            getComponent(Sprite.class).animIndex++;
            if (getComponent(Sprite.class).animIndex == GetAnimationFrameCount(currentAnimation)) {
                getComponent(Sprite.class).animIndex = 0;
            }
        }
    }

    @Override
    public void render(Graphics g) {
        int currentFrame = getComponent(Sprite.class).animIndex;

        g.drawImage(getComponent(Sprite.class).getAnimations()[this.currentAnimation][currentFrame],
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

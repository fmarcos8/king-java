package engine.core;

import engine.utils.ObjectType;
import engine.utils.Size;
import engine.utils.Transform;

import java.awt.Graphics;
import java.util.ArrayList;
import java.util.List;

public class GameObject {
    protected ObjectType type;
    protected Transform transform;
    protected Size size;
    protected List<Component> components;
    protected int[][] sceneData;
    protected boolean up = false,
        down = false,
        left = false,
        right = false,
        moving = false,
        attacking = false,
        jump = false,
        inAir = false;

    public GameObject(Transform transform, Size size, int[][] sceneData) {
        this.transform = transform;
        this.size = size;
        this.sceneData = sceneData;
        this.components = new ArrayList<>();
    }

    public <T extends  Component> T getComponent(Class<T> componentClass) {
        for (Component component: components) {
            if (componentClass.isAssignableFrom(component.getClass())) {
                try {
                    return componentClass.cast(component);
                } catch (ClassCastException e) {
                    e.printStackTrace();
                    System.exit(-1);
                }
            }
        }
        return null;
    }

    public void addComponent(Component c) {
        c.parent = this;
        components.add(c);
    }

    public void update() {
        for (int i = 0; i < components.size(); i++ ) {
            Component c = components.get(i);
            c.update();
        }
    }

    public void render(Graphics g) {
        for (int i = 0; i < components.size(); i++ ) {
            Component c = components.get(i);
            c.render(g);
        }
    }

    public ObjectType getType() {
        return type;
    }

    public Transform getTransform() {
        return transform;
    }

    public Size getSize() {
        return size;
    }

    public boolean isUp() {
        return up;
    }

    public void setUp(boolean up) {
        this.up = up;
    }

    public boolean isDown() {
        return down;
    }

    public void setDown(boolean down) {
        this.down = down;
    }

    public boolean isLeft() {
        return left;
    }

    public void setLeft(boolean left) {
        this.left = left;
    }

    public boolean isRight() {
        return right;
    }

    public void setRight(boolean right) {
        this.right = right;
    }

    public boolean isMoving() {
        return moving;
    }

    public void setMoving(boolean moving) {
        this.moving = moving;
    }

    public boolean isAttacking() {
        return attacking;
    }

    public void setAttacking(boolean attacking) {
        this.attacking = attacking;
    }

    public boolean isJump() {
        return jump;
    }

    public void setJump(boolean jump) {
        this.jump = jump;
    }
}

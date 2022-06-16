package engine.components;

import engine.core.Component;

import java.awt.geom.Rectangle2D;

public class BoxCollider2D extends Component {
    public boolean show;
    public int width;
    public int height;
    private Rectangle2D.Float hitBox;

    public BoxCollider2D(boolean show) {
        this.show = show;
    }

    public BoxCollider2D(int width, int height, boolean show) {
        this.show = show;
        this.width = width;
        this.height = height;
    }

    @Override
    public void update() {

    }

    public Rectangle2D.Float getHitBox() {
        return new Rectangle2D.Float(
            parent.getTransform().position.x,
            parent.getTransform().position.y,
            width,
            height
        );
    }
}

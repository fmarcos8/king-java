package engine.components;

import engine.core.Component;
import engine.core.Scene;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;

public class BoxCollider2D extends Component {
    public boolean show;
    public int width;
    public int height;
    private Rectangle2D.Float hitBox;
    public boolean intersected = false;

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
    
    public void render(Graphics g) {
    	if (Scene.ShowDebug) {
            Graphics2D g2d = (Graphics2D) g;
            if(intersected) {
            	g2d.setColor(Color.GREEN);            	
            } else {
            	g2d.setColor(Color.RED);
            }
            g2d.drawRect((int)getHitBox().x, (int)getHitBox().y, (int)getHitBox().width, (int)getHitBox().height);
        }
    }
    
    public static boolean intersection(BoxCollider2D b1, BoxCollider2D b2) {
    	return b1.getHitBox().getBounds2D().intersects(b2.getHitBox().getBounds2D());
    }
}

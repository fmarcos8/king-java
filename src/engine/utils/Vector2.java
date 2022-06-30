package engine.utils;

public class Vector2 {
    public float x, y;

    public Vector2() {
        x = 0;
        y = 0;
    }

    public Vector2(float x, float y) {
        this.x = x;
        this.y = y;
    }
    
    public void sum(Vector2 that) {
    	this.x += that.x;
    	this.y += that.y;
    }
    
    public void sub(Vector2 that) {
    	this.x -= that.x;
    	this.y -= that.y;
    }
    
    public String toString() {
    	return "X:"+x+",Y:"+y;
    }
}

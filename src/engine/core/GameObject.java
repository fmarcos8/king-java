package engine.core;

import engine.utils.ObjectType;
import engine.utils.Size;
import engine.utils.Transform;
import engine.utils.Vector2;

import java.awt.Graphics;
import java.util.ArrayList;
import java.util.List;

public class GameObject {
    protected ObjectType type;
    protected Transform transform;
    protected Size size;
    protected List<Component> components;
    protected Scene parentScene;
    protected Vector2 cameraScene = Scene.CameraScene;
    
    public boolean activated = true;

    public GameObject(Transform transform, Size size) {
        this.transform = transform;
        this.size = size;
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
    
    public <T extends  Component> T getComponent(Class<T> componentClass, int index) {
        int i = 0;
    	for (Component component: components) {
            if (componentClass.isAssignableFrom(component.getClass()) && i == index) {
                try {
                    return componentClass.cast(component);
                } catch (ClassCastException e) {
                    e.printStackTrace();
                    System.exit(-1);
                }

            	i++;
            }
        }
        return null;
    }

    public void addComponent(Component c) {
        c.parent = this;
        components.add(c);
    }

    public void update() {
    	if(activated)
	        for (int i = 0; i < components.size(); i++ ) {
	            Component c = components.get(i);
	            if(c.activate)
	            	c.update();
	        }
    }

    public void render(Graphics g) {
    	if(activated)
	        for (int i = 0; i < components.size(); i++ ) {
	            Component c = components.get(i);
	            if(c.activate)
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
    
    public void setSize(Size size) {
    	this.size = size;
    }

    public void setScene(Scene s) {
    	parentScene = s;
    }

    public GameObject Instantiate(GameObject go) {
    	parentScene.addObject(go);
    	return go;
    }
    
    public GameObject Instantiate(GameObject go, ObjectType ot) {
    	go.type = ot;
    	parentScene.addObject(go);
    	return go;
    }
    
    public void start() {
    	
    }
}

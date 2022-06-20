package engine.core;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

import engine.utils.ObjectType;
import engine.utils.Vector2;

public abstract class Scene {
    public SceneManager sceneManager;
    protected int[][] sceneData;
    public List<GameObject> objects;
    public String fileImageScenePath;
    
    public static Vector2 CameraScene = new Vector2(0,0);

    public Scene() {
        //this.fileImageScenePath = fileImageScenePath;
        this.objects = new ArrayList<>();
        //this.sceneData = LoadSave.GetLevelData(sceneImagePath);
    }

    public void init(){}

    public void addObject(GameObject object) {
    	object.setScene(this);
        objects.add(object);
        object.start();
    }

    public List<GameObject> getObjects() {
        return objects;
    }
    
    public List<GameObject> getObjects(ObjectType ot) {
    	List<GameObject> aux = new ArrayList<GameObject>();
    	for(int i = 0; i < objects.size(); i++) {
    		GameObject go = objects.get(i);
    		if(ot == go.type) aux.add(go);
    	}
        return aux;
    }

    public abstract void update();
    public abstract void render(Graphics g);
}

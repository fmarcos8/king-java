package engine.core;

import java.awt.*;
import java.util.ArrayList;
import java.util.Dictionary;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import engine.utils.Constants.Game;
import engine.utils.ObjectType;
import engine.utils.Vector2;

public abstract class Scene {
    public SceneManager sceneManager;
    protected int[][] sceneData;
    public List<GameObject> objects;
    public String fileImageScenePath;
    public static boolean ShowDebug = true;
    
    public static Vector2 CameraScene = new Vector2(0,0);
    
    private static HashMap<String, String> info = new HashMap<String, String>(); 

    public Scene() {
        //this.fileImageScenePath = fileImageScenePath;
        this.objects = new ArrayList<>();
        setInfo("Camera Position",CameraScene.toString());
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
    
    public List<GameObject> getObjects(ObjectType[] ot) {
    	List<GameObject> aux = new ArrayList<GameObject>();
    	for(int i = 0; i < objects.size(); i++) {
    		GameObject go = objects.get(i);
    		for(int j = 0; j < ot.length; j++) {
    			if(ot[j] == go.type) aux.add(go);    			
    		}
    	}
        return aux;
    }

    public abstract void update();
    
    public void render(Graphics g) {
    	if(ShowDebug) {
    		int top = 15;
    		g.setColor(new Color(0, 0, 0, 50));
    		g.fillRect(0, 0, 300, Game.GAME_HEIGHT);
    		Font bigFont = new Font("Sans", Font.BOLD, 13);
    		g.setFont(bigFont);
    		for (Map.Entry<String,String> pair : info.entrySet()) {
    			String t = pair.getKey();
    			String v = pair.getValue();
    			g.setColor(Color.white);
    			g.drawString(t+" : "+v, 2,top);
    			
    			top+=20;
    		}    		
    	}
    }
    
    public static void setInfo(String key, String value) {
    	info.put(key, value);
    }
    
    public static void setInfo(String key, int value) {
    	info.put(key, String.valueOf(value));
    }
    
    public static void setInfo(String key, boolean value) {
    	info.put(key, String.valueOf(value));
    }
}

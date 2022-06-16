package engine.core;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public abstract class Scene {
    public SceneManager sceneManager;
    protected int[][] sceneData;
    public List<GameObject> objects;
    public String fileImageScenePath;

    public Scene() {
        //this.fileImageScenePath = fileImageScenePath;
        this.objects = new ArrayList<>();
        //this.sceneData = LoadSave.GetLevelData(sceneImagePath);
    }

    public void init(){}

    public void addObject(GameObject object) {
        objects.add(object);
    }

    public List<GameObject> getObjects() {
        return objects;
    }

    public abstract void update();
    public abstract void render(Graphics g);
}

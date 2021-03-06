package engine.core;

import engine.utils.Constants;
import engine.utils.Functions;

import java.awt.*;
import java.awt.image.BufferedImage;

import static engine.utils.Constants.Game.*;

public class SceneManager {
    public BufferedImage[] tiles;
    private BufferedImage tilesSprite;
    public int[][] currentSceneData;

    public Scene scene;
    public String sceneImageFile;

    public SceneManager(String sceneImageFile) {
        this.sceneImageFile = sceneImageFile;
        this.tilesSprite = Functions.LoadImage(Constants.Scenes.TILES_SPRITE_PATH);
        importLevelData();
        getLevelData();
    }

    public void getLevelData() {
        currentSceneData = Functions.GetSceneData(sceneImageFile);
    }

    private void importLevelData() {
        tiles = new BufferedImage[96];
        for (int y = 0; y < 8; y++) {
            for (int x = 0; x < 12; x++) {
                int idx = y * 12 + x;
                tiles[idx] = tilesSprite.getSubimage(x * TILES_DEFAULT_SIZE, y * TILES_DEFAULT_SIZE, TILES_DEFAULT_SIZE, TILES_DEFAULT_SIZE);
            }
        }
    }

    public void update(double dt) {

    }

    public void render(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;
        for (int y = 0; y < COUNT_TILES_IN_HEIGHT; y++) { //height
            for (int x = 0; x < currentSceneData[0].length; x++) { //width
                int idx = currentSceneData[y][x];
                g2d.drawImage(tiles[idx], TILES_SIZE * x,TILES_SIZE * y, TILES_SIZE, TILES_SIZE,null);
            }
        }
    }
}

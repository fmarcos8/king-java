package engine.utils;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;

import static engine.utils.Constants.Game.GAME_HEIGHT;
import static engine.utils.Constants.Game.TILES_SIZE;

public class Functions {
    public static BufferedImage LoadImage(String filePath) {
        BufferedImage img = null;
        InputStream is = Functions.class.getResourceAsStream("/"+filePath);
        try {
            img = ImageIO.read(is);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                is.close();
            } catch (IOException e){
                e.printStackTrace();
                System.exit(-1);
            }
        }

        return img;
    }

    public static int[][] GetSceneData(String sceneImageFile) {
        BufferedImage levelImage = LoadImage(sceneImageFile);
        int[][] currentSceneData = new int[levelImage.getHeight()][levelImage.getWidth()];


        for (int y = 0; y < levelImage.getHeight(); y++) { //height
            for (int x = 0; x < levelImage.getWidth(); x++) { //width
                Color color = new Color(levelImage.getRGB(x, y));
                int value = color.getRed();
                if (value > 96) {
                    value = 0;
                }
                currentSceneData[y][x] = value;
            }
        }

        return currentSceneData;
    }

    public static boolean CanMoveHere(float x, float y, float width, float height, int[][] sceneData) {
        if (!IsSolid(x, y, sceneData)) {
            if (!IsSolid(x+width, y+height, sceneData)) {
                if (!IsSolid(x + width, y, sceneData)) {
                    if (!IsSolid(x, y+height, sceneData)) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    private static boolean IsSolid(float x, float y, int[][] sceneData) {
        int maxWidth = sceneData[0].length * TILES_SIZE;

        if (x < 0 || x >= maxWidth) {
            return true;
        }
        if (y < 0 || y >= GAME_HEIGHT) {
            return true;
        }

        float xIndex = x / TILES_SIZE;
        float yIndex = y / TILES_SIZE;

        int value = sceneData[(int)yIndex][(int)xIndex];

        return value != 13 && value != 11 && value != 48 && value != 49 && value != 50 && value != 52 && value != 60 && value != 61 && value != 62 && value != 53 && value != 72 && value != 73 && value != 74;
    }

    public static float GetEntityXPosNextToWall(Rectangle2D.Float hitBox, float xSpeed) {
        int currentTile = (int)(hitBox.x / TILES_SIZE);
        if (xSpeed > 0) {
            int tileXPos = currentTile * TILES_SIZE;
            int xOffset = (int)(TILES_SIZE - hitBox.width);
            return tileXPos + xOffset - 1;
        } else {
            return currentTile * TILES_SIZE;
        }
    }

    public static float GetEntityYPosUnderRoofOrAboveFloor(Rectangle2D.Float hitBox, float airSpeed) {
        int currentTile = (int)(hitBox.y / TILES_SIZE);
        if (airSpeed > 0) {
            int tileYPos = currentTile * TILES_SIZE;
            int yOffset = (int)(TILES_SIZE - hitBox.height);
            return tileYPos + yOffset - 1;
        } else {
            return currentTile * TILES_SIZE;
        }
    }

    public static boolean IsEntityOnFloor(Rectangle2D.Float hitBox, int[][] sceneData) {
        if (!IsSolid(hitBox.x, hitBox.y + hitBox.height + 1, sceneData)) {
            if (!IsSolid(hitBox.x + hitBox.width, hitBox.y + hitBox.height + 1, sceneData)) {
                return false;
            }
        }
        return true;
    }
}

package engine.utils;

import javax.imageio.ImageIO;

import engine.core.Scene;

import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;

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
    
    static int getMapTiles(String s){
    	String[] map = new String[] {
    		"ururbbubg",
    		"urubbbugu",
    		"urubbrgbu",
    		"ururbrubu",
    		"ugugbbubr",
    		"ugubbgrbu",
    		"rbubbgrbu",
    		"rbrbbbugu",
    		"gbrbbbrbr",
    		"rbgbbbrbr",
    		"rbrbbbrbr",
    		"ububbbubu",
    		"uburbgubu",
    		"ggggggggg",
    		"ubugbrubu",
    		"uburbrubu",
    		"ubrgbbugu",
    		"rbubbgugu",
    		"ugubbbrbr",
    		"ubrgbbupr",
    		"rbrbbbgbr",
    		"rbrbbbrbg",
    		"gbrbbbrbg",
    		"rbgbbbgbr",
    		"ubgrbburu",
    		"ugubbburu",
    		"gbubbruru",
    		"uburbruru",
    		"urubbbgbr",
    		"urubbbrbg",
    		"ubgrbbubr",
    		"gbubbrrbu",
    		"urubbbrbr",
    		"rbubbrrbu",
    		"ururbbubr",
    		"urubbrrbu",
    		"ururbburu",
    		"urubbburu",
    		"urubbruru",
    		"ururbruru",
    		"ubrbbburu",
    		"rbubbburu",
    		"ubrrbbubg",
    		"rbubbrgbu",
    		"ubrrbbubr",
    		"rbrbbburu",
    		"ubrrbburu",
    		"rbubbruru"
    		
    	};
    	
    	for(int i = 0; i < map.length; i++) {
    		String sAux = map[i];
    		boolean itIs = true;
    		for(int j = 0; j < s.length(); j++) {
    			char c1 = sAux.charAt(j), c2 = s.charAt(j);
    			if(c1 != 'u') {
    				if(c1 != c2) {
    					itIs = false;
    					break;
    				}
    			}
    		}
    		if(itIs) return i;
    	}
    	
    	return 13;
    }

    public static int[][] GetSceneData(String sceneImageFile) {
        BufferedImage levelImage = LoadImage(sceneImageFile);
        int[][] currentSceneData = new int[levelImage.getHeight()][levelImage.getWidth()];
        


        for (int y = 0; y < levelImage.getHeight(); y++) { //height
            for (int x = 0; x < levelImage.getWidth(); x++) { //width

//            	Color color = new Color(levelImage.getRGB(x, y));
            	
                String s = "";
                for(int i=-1;i<=1;i++) {
                	for(int j=-1;j<=1;j++) {
                		if(x+j < 0 || x+j >= levelImage.getWidth() || y+i < 0 || y+i >= levelImage.getHeight()) s += "g";
                		else {
                			Color color = new Color(levelImage.getRGB(x+j, y+i));
                            int r = color.getRed(), g = color.getGreen(), b = color.getBlue();
                            if(r == 255) s += "r";
                            else if(g == 255) s += "g";
                            else if(b == 255) s += "b";
                		}
                	}
                }
                int value = getMapTiles(s);
                if(value != 13) {

                	System.out.println(s);
                }
//            	int value = color.getRed();
//                if (value > 96) {
//                    value = 0;
//                }
                
                
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
    	x += Scene.CameraScene.x;
    	y += Scene.CameraScene.y;
        int maxWidth = sceneData[0].length * TILES_SIZE;
        int maxHeight = sceneData.length * TILES_SIZE;
        
        if (x < 0 || x >= maxWidth) {
            return true;
        }
        if (y < 0 || y >= maxHeight) {
            return true;
        }

        float xIndex = x / TILES_SIZE;
        float yIndex = y / TILES_SIZE;

        int value = sceneData[(int)yIndex][(int)xIndex];

        return value < 47 && value != 13 ;
    }

    public static float GetEntityXPosNextToWall(Rectangle2D.Float hitBox, float xSpeed) {
        int currentTile = (int)((hitBox.x + Scene.CameraScene.x) / TILES_SIZE);
        if (xSpeed > 0) {
            int tileXPos = (int) (currentTile * TILES_SIZE);
            int xOffset = (int)(TILES_SIZE - hitBox.width);
            return tileXPos + xOffset - 1 - Scene.CameraScene.x;
        } else {
            return currentTile * TILES_SIZE - Scene.CameraScene.x;
        }
    }

    public static float GetEntityYPosUnderRoofOrAboveFloor(Rectangle2D.Float hitBox, float airSpeed) {
        int currentTile = (int)((hitBox.y + Scene.CameraScene.y) / TILES_SIZE);
        if (airSpeed > 0) {
            int tileYPos = currentTile * TILES_SIZE;
            int yOffset = (int)(TILES_SIZE - hitBox.height);
            return tileYPos + yOffset - 1 - Scene.CameraScene.y;
        } else {
            return currentTile * TILES_SIZE - Scene.CameraScene.y;
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

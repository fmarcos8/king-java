package engine.utils;

import java.awt.image.BufferedImage;

public class Tile {
	BufferedImage image;
	enum TileType{
		wall, back, none
	}
	TileType type;
	String map;
	Vector2 pos;
	
	public Tile(BufferedImage image, TileType type, String map, Vector2 pos) {
		
	}
}

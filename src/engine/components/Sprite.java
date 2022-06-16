package engine.components;

import engine.core.Component;
import engine.utils.Functions;

import java.awt.*;
import java.awt.image.BufferedImage;

public class Sprite extends Component {
    private BufferedImage sprite;
    private BufferedImage subSprite;
    private BufferedImage[][] animations;
    public int animTick;
    public int animIndex;
    public int animSpeed = 10;

    private String filePath;
    private int subWidth;
    private int subHeight;

    public Sprite(String filePath, int subWidth, int subHeight) {
        this.filePath = filePath;
        this.subWidth = subWidth;
        this.subHeight = subHeight;
        this.sprite = Functions.LoadImage(filePath);

        loadAnimations();
    }

    private void loadAnimations() {
        int cols = this.sprite.getHeight() / this.subHeight;
        int rows = this.sprite.getWidth() / this.subWidth;

        animations = new BufferedImage[cols][rows];

        for (int y = 0; y < animations.length; y++) {
            for (int x = 0; x < animations[y].length; x++) {
                animations[y][x] = this.sprite.getSubimage(x * this.subWidth, y * this.subHeight, this.subWidth, this.subHeight);
            }
        }
    }

    public BufferedImage[][] getAnimations() {
        return animations;
    }
}

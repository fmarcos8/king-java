package engine.scenes;

import engine.components.BoxCollider2D;
import engine.components.Sprite;
import engine.core.GameObject;
import engine.core.Scene;
import engine.core.SceneManager;
import engine.inputs.MouseInfoListener;
import engine.objects.Enemy;
import engine.objects.Player;
import engine.utils.*;

import java.awt.Graphics;

import static engine.utils.Constants.*;
import static engine.utils.Constants.Component.SHOW_COLLIDER_FALSE;
import static engine.utils.Constants.Component.SHOW_COLLIDER_TRUE;
import static engine.utils.Constants.Game.*;
import static engine.utils.Constants.Game.GAME_SCALE;

public class DebugScene extends Scene {
    public DebugScene() {
        init();
    }

    @Override
    public void init() {
        sceneManager = new SceneManager(Scenes.DEBUG_SCENE_PATH_NEW);

        Player player = new Player(new Transform(new Vector2(400, GAME_HEIGHT >> 1), 1.0f), new Size(PlayerConstants.WIDTH,PlayerConstants.HEIGHT));
        addObject(player);

        Enemy pig = new Enemy(new Transform(new Vector2(2000, 500), GAME_SCALE), new Size(34, 28));
        addObject(pig);
    }

    @Override
    public void update() {
        super.update();
    }

    @Override
    public void render(Graphics g) {
        super.render(g);
    }
}

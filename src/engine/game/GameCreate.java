package engine.game;

import engine.core.Scene;
import engine.core.Window;
import engine.inputs.KeyInputListener;
import engine.inputs.MouseInfoListener;
import engine.scenes.DebugScene;

import java.awt.*;
import java.awt.image.BufferStrategy;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;

import static engine.utils.Constants.Game.*;

public class GameCreate implements Runnable {
    private boolean running;
    private Window window;
    private GraphicsEnvironment graphicsEnvironment;
    private Font atariFont;

    public Scene currentScene;

    private KeyInputListener keyInputListener;

    private int frames = 0;

    public GameCreate() {
        InputStream fontFile = getClass().getResourceAsStream("/assets/font/AtariClassicChunky.ttf");
        atariFont = new Font("Consolas", Font.PLAIN, (int)(15 * GAME_SCALE));
            //atariFont = Font.createFont(Font.TRUETYPE_FONT, fontFile);
            //graphicsEnvironment = GraphicsEnvironment.getLocalGraphicsEnvironment();
            //graphicsEnvironment.registerFont(atariFont);
        window = new Window(SCREEN_TITLE, GAME_WIDTH, GAME_HEIGHT);
        currentScene = new DebugScene();
        keyInputListener = new KeyInputListener(currentScene);
        window.addKeyListener(keyInputListener);
        window.addMouseListener(new MouseInfoListener());
        window.addMouseMotionListener(new MouseInfoListener());
    }

    public synchronized void start() {
        if (running)
            return;

        running = true;

        Thread thread = new Thread(this);
        thread.start();
    }

    public void update() {
        currentScene.update();
        MouseInfoListener.getInfo();
    }

    public void render() {
        BufferStrategy bs = window.getBufferStrategy();
        if (bs == null) {
            window.createBufferStrategy(3);
            return;
        }

        Graphics g = bs.getDrawGraphics();
        g.setColor(Color.GRAY);
        g.fillRect(0,0, window.getWidth(), window.getHeight());

        currentScene.render(g);
        /*g.setFont(atariFont);
        g.drawString(String.format("FPS: %d", frames), 50, 50);*/

        //Graphics2D g2d = (Graphics2D) g;

        g.dispose();
        bs.show();
    }

    @Override
    public void run() {
        double timePerFrame = 1000000000.0d / FPS_SET;
        double timePerUpdate = 1000000000.0d / UPS_SET;

        long previousTime = System.nanoTime();
        int updates = 0;
        long lastCheck = System.currentTimeMillis();

        double deltaTime = 0;
        double deltaFrame = 0;

        while (running) {
            long currentTime = System.nanoTime();

            deltaTime += (currentTime - previousTime) / timePerUpdate;
            deltaFrame += (currentTime - previousTime) / timePerFrame;
            previousTime = currentTime;

            if (deltaTime >= 1) {
                update();
                updates++;
                deltaTime--;
            }

            if (deltaFrame >= 1) {
                render();
                frames++;
                deltaFrame--;
            }

            if (System.currentTimeMillis() - lastCheck >= 1000) {
                lastCheck = System.currentTimeMillis();
                window.getFrame().setTitle(SCREEN_TITLE+" - "+String.format("FPS: %d | UPS: %d", frames, updates));
                frames = 0;
                updates = 0;
            }
        }
    }
}

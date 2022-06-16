package engine.core;

import javax.swing.JFrame;
import java.awt.Dimension;
import java.awt.Canvas;

public class Window extends Canvas {
    private String title;
    private int width, height;
    private JFrame frame;

    public Window(String title, int width, int height) {
        this.title = title;
        this.width = width;
        this.height = height;

        setPreferredSize(new Dimension(width, height));
        setMaximumSize(new Dimension(width, height));
        setMinimumSize(new Dimension(width, height));

        frame = new JFrame(title);
        frame.add(this);
        frame.pack();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
        frame.setLocationRelativeTo(null);
        frame.setResizable(false);

        requestFocus();
    }

    public JFrame getFrame() {
        return frame;
    }
}

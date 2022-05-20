package main.java.drawingtool;

import main.java.drawingtool.auxiliary.GUI;

import javax.swing.JFrame;
import java.io.Serial;

public class DrawingTool extends JFrame {
    @Serial
    private static final long serialVersionUID = 1L;

    public static void main(String[] args) {
        new DrawingTool();
    }

    public DrawingTool() {
        setTitle("Drawing field");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        GUI gof = new GUI(this, 16);
        gof.initialize(this.getContentPane());
        this.setSize(1024, 768);
        this.setVisible(true);
    }
}

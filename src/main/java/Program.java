package main.java;

import main.java.gui.*;

import javax.swing.JFrame;
import java.io.Serial;

public class Program extends JFrame {
    @Serial
    private static final long serialVersionUID = 1L;

    public static void main(String[] args) {
        new Program();
    }

    public Program() {
        setTitle("Drawing field");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);


        this.setSize(960, 790); //756
        GUI gof = new GUI(this, 20);
        gof.initialize(this.getContentPane(), 46, 33);//46x33
        this.setVisible(true);
    }
}

package main.java.gui;

import main.java.drawingtool.auxiliary.Subsoil;
import main.java.drawingtool.auxiliary.Point;

import javax.swing.*;
import javax.swing.event.MouseInputListener;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.Serial;

import static java.lang.Math.max;
import static java.lang.Math.min;

public class Board extends JComponent{
    @Serial
    private static final long serialVersionUID = 1L;
    Point[][] points;
    private final int size;
    final String mapSource = "src/main/resources/table.txt";

    public Board(int length, int height, int squareSize) {
        size = squareSize;
        setBackground(Color.WHITE);
        setOpaque(true);
        try {
            initialize((length / size) + 1, (height / size) + 1);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    public void clear() {
        for (Point[] pp : points)
            for (Point p : pp) {
                p.clear();
            }
        this.repaint();
    }

    private void initialize(int length, int height) throws IOException {
        points = new Point[length][height];
        for (int x = 0; x < points.length; ++x)
            for (int y = 0; y < points[x].length; ++y)
                points[x][y] = new Point();

        FileReader filereader = new FileReader(mapSource);
        BufferedReader bufferedreader = new BufferedReader(filereader);
        String line = bufferedreader.readLine();
        while (line != null) {
            int x = 0, y = 0, idx=0;
            while(idx<line.length()){
                if(line.charAt(idx)==']') {
                    y += 1;
                    x=-1;
                }
                if(line.charAt(idx)==',')
                    x+=1;
                if(line.charAt(idx)>='0' && line.charAt(idx)<='9' &&(y<points.length && x<points[0].length)) {
                    points[y][x].type = Subsoil.fromInt(line.charAt(idx)-48);
                }
                idx++;
            }
            line = bufferedreader.readLine();
        }
    }

    protected void paintComponent(Graphics g) {
        if (isOpaque()) {
            g.setColor(getBackground());
            g.fillRect(0, 0, this.getWidth(), this.getHeight());
        }
        g.setColor(Color.GRAY);
        drawNetting(g, size);
    }

    private void drawNetting(Graphics g, int gridSpace) {
        Insets insets = getInsets();
        int firstX = insets.left;
        int firstY = insets.top;
        int lastX = this.getWidth() - insets.right;
        int lastY = this.getHeight() - insets.bottom;

        int x = firstX;
        while (x < lastX) {
            g.drawLine(x, firstY, x, lastY);
            x += gridSpace;
        }

        int y = firstY;
        while (y < lastY) {
            g.drawLine(firstX, y, lastX, y);
            y += gridSpace;
        }

        for (x = 1; x < points.length-1; ++x) {
            for (y = 1; y < points[x].length-1; ++y) {
                switch (points[x][y].type) {
                    case empty -> g.setColor(new Color(1.0f, 1.0f, 1.0f, 1.0f));
                    case street -> g.setColor(new Color(0.3f, 0.3f, 0.3f, 0.7f));
                    case pavement -> g.setColor(new Color(0.2f, 0.8f, 0.2f, 0.7f));
                    case crossing -> g.setColor(new Color(0.6f, 0.6f, 0.6f, 0.7f));
                    case unavailable -> g.setColor(new Color(0.0f, 0.0f, 0.0f, 1.0f));
                    case underground -> g.setColor(new Color(0.7f, 0.0f, 0.7f, 0.7f));
                    case underground_street -> g.setColor(new Color(0.3f, 0.3f, 0.3f, 0.4f));
                    case underground_unavailable -> g.setColor(new Color(0.0f, 0.0f, 0.0f, 0.4f));
                    case underground_pavement -> g.setColor(new Color(0.2f, 0.8f, 0.2f, 0.4f));
                    default -> g.setColor(new Color(1.0f, 0.0f, 0.0f, 0.7f));
                }
                g.fillRect((x * size) + 1, (y * size) + 1, (size - 1), (size - 1));
            }
        }

    }
    public void iteration(){
        // TODO
        System.out.println("Something happens in simulation.");
    }

}

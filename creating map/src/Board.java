import java.awt.Color;
import java.awt.Graphics;
import java.awt.Insets;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Random;

import javax.swing.JComponent;
import javax.swing.event.MouseInputListener;

public class Board extends JComponent implements MouseInputListener, ComponentListener {
    private static final long serialVersionUID = 1L;
    private Point[][] points;
    private int size = 10;
    public int editType=0;

    public Board(int length, int height) {
        addMouseListener(this);
        addComponentListener(this);
        addMouseMotionListener(this);
        setBackground(Color.WHITE);
        setOpaque(true);
    }

    public void iteration() {
        for (int x = 1; x < points.length - 1; ++x)
            for (int y = 1; y < points[x].length - 1; ++y)
                points[x][y].blocked=false;
        for (int x = 1; x < points.length - 1; ++x)
            for (int y = 1; y < points[x].length - 1; ++y)
                points[x][y].move();
        this.repaint();
    }

    public void clear() {
        for (int x = 0; x < points.length; ++x)
            for (int y = 0; y < points[x].length; ++y) {
                points[x][y].clear();
            }
        calculateField();
        this.repaint();
    }

    private void initialize(int length, int height) {
        points = new Point[length][height];

        int Moore = 1;

        for (int x = 0; x < points.length; ++x)
            for (int y = 0; y < points[x].length; ++y)
                points[x][y] = new Point();

        for (int x = 1; x < points.length-1; ++x) {
            for (int y = 1; y < points[x].length-1; ++y) {
                if(Moore==1){
                    points[x][y].addNeighbor(points[x-1][y]);
                    points[x][y].addNeighbor(points[x][y-1]);
                    points[x][y].addNeighbor(points[x][y+1]);
                    points[x][y].addNeighbor(points[x+1][y]);
                    points[x][y].addNeighbor(points[x-1][y-1]);
                    points[x][y].addNeighbor(points[x-1][y+1]);
                    points[x][y].addNeighbor(points[x+1][y-1]);
                    points[x][y].addNeighbor(points[x+1][y+1]);
                }
                else{
                    points[x][y].addNeighbor(points[x-1][y]);
                    points[x][y].addNeighbor(points[x][y-1]);
                    points[x][y].addNeighbor(points[x][y+1]);
                    points[x][y].addNeighbor(points[x+1][y]);
                }
            }
        }

        int board = 1; //0 - nic, 1 - stadion, 2 - idk
        if(board == 1){
            int walls = 4;
            int ppl1 = 70;
            int ppl2 = 95;
            for (int x = 1; x < points.length-1; ++x) {         //walls
                for (int y = 1; y < points[x].length-1; ++y) {
                    //borders
                    if(x==1 || x==points.length-2)
                        points[x][y].type=1;
                    if(y==1 || y==points[x].length-2)
                        points[x][y].type=1;
                    //rows
                    if(y%walls==0 && x>5 && (x<(points.length-1)/2-5 || x>(points.length-1)/2+5) && x<points.length-6)
                        points[x][y].type=1;
                    //entrance
                    if((y>(points[x].length-1)/2-8 && y<(points[x].length-1)/2+10) &&
                            (x==(points.length-1)/2-3 || x==(points.length-1)/2+3))
                        points[x][y].type=1;
                    if(y==(points[x].length-1)/2-8+1 && (x>(points.length-1)/2-3 && x<(points.length-1)/2+3))
                        points[x][y].type=1;
                    if(y==(points[x].length-1)/2-8+2 && (x>(points.length-1)/2-3 && x<(points.length-1)/2+3))
                        points[x][y].type=2;
                }
            }

            for (int x = 1; x < points.length-1; ++x) {         //people
                for (int y = 1; y < points[x].length-1; ++y) {
                    if(points[x][y].type==0){
                        Random rand = new Random(); //instance of random class
                        int upperbound = 100;
                        if(x>5 && (x<(points.length-1)/2-5 || x>(points.length-1)/2+5) && x<points.length-6) {//rows
                            if(rand.nextInt(upperbound)>ppl1) {
                                points[x][y].isPedestrian=true;
                            }
                        }
                        else{
                            if(rand.nextInt(upperbound)>ppl2) {
                                points[x][y].isPedestrian=true;
                            }
                        }
                    }
                }
            }
        }
        if(board == 2){

        }
    }

    private void calculateField(){
        ArrayList<Point> toCheck = new ArrayList<Point>();
        for (int x = 0; x < points.length; ++x) {
            for (int y = 0; y < points[x].length; ++y) {
                if (points[x][y].type == 2) {
                    points[x][y].staticField = 0;
                    toCheck.addAll(points[x][y].neighbors);
                }
            }
        }
        while(!toCheck.isEmpty()){
            Point tmp = toCheck.remove(0);
            if(tmp.calcStaticField()){
                toCheck.addAll(tmp.neighbors);
            }
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
                if(points[x][y].type==0){
                    double staticField = points[x][y].staticField;
                    float intensity = (float)staticField/100;
                    if (intensity > 1.0) {
                        intensity = 1.0f;
                    }
                    g.setColor(new Color(intensity, intensity,intensity ));
                }
                else if (points[x][y].type==1){
                    g.setColor(new Color(1.0f, 0.0f, 0.0f, 0.7f));
                }
                else if (points[x][y].type==2){
                    g.setColor(new Color(0.0f, 1.0f, 0.0f, 0.7f));
                }
                if (points[x][y].isPedestrian){
                    g.setColor(new Color(0.0f, 0.0f, 1.0f, 0.7f));
                }
                g.fillRect((x * size) + 1, (y * size) + 1, (size - 1), (size - 1));
            }
        }

    }

    public void mouseClicked(MouseEvent e) {
        int x = e.getX() / size;
        int y = e.getY() / size;
        if ((x < points.length) && (x > 0) && (y < points[x].length) && (y > 0)) {
            if(editType==3){
                points[x][y].isPedestrian=true;
            }
            else{
                points[x][y].type= editType;
            }
            this.repaint();
        }
    }

    public void componentResized(ComponentEvent e) {
        int dlugosc = (this.getWidth() / size) + 1;
        int wysokosc = (this.getHeight() / size) + 1;
        initialize(dlugosc, wysokosc);
    }

    public void mouseDragged(MouseEvent e) {
        int x = e.getX() / size;
        int y = e.getY() / size;
        if ((x < points.length) && (x > 0) && (y < points[x].length) && (y > 0)) {
            if(editType==3){
                points[x][y].isPedestrian=true;
            }
            else{
                points[x][y].type= editType;
            }
            this.repaint();
        }
    }

    public void mouseExited(MouseEvent e) {
    }

    public void mouseEntered(MouseEvent e) {
    }

    public void componentShown(ComponentEvent e) {
    }

    public void componentMoved(ComponentEvent e) {
    }

    public void mouseReleased(MouseEvent e) {
    }

    public void mouseMoved(MouseEvent e) {
    }

    public void componentHidden(ComponentEvent e) {
    }

    public void mousePressed(MouseEvent e) {
    }

}

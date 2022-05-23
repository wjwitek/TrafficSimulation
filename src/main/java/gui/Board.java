package main.java.gui;

import javax.swing.*;
import javax.swing.event.MouseInputListener;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.Serial;
import java.util.ArrayList;

import static java.lang.Math.max;
import static java.lang.Math.min;

public class Board extends JComponent implements MouseInputListener {
    @Serial
    private static final long serialVersionUID = 1L;
    Point[][] points;
    private final int size;
    int unreachable;
    public Subsoil editType = Subsoil.empty;
    public int rectangleMode = 0, length, height;
    private Cords rectangleCorner;
    public boolean mode = false;
//    private StaticField staticField = new StaticField();
    public boolean showStaticField;
    Cords startingPointSF;

    public Board(int length, int height, int squareSize, String mapSource) {
        showStaticField = false;
        size = squareSize;
        this.length = length;
        this.height = height;
        unreachable = length * height + 10;
        startingPointSF = new Cords(1, 32);
        addMouseListener(this);
        addMouseMotionListener(this);
        setBackground(Color.WHITE);
        setOpaque(true);
        try {
            initialize(mapSource);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        calculateStaticFields();
//        staticField.initialize(this);
//        System.out.printf("field: %d\n", points[1][1].fields.get(new Cords(3, 3)));
//        System.out.printf("%d\n",StaticField.getField(new PairCords(3, 3, 5, 5))[1][1]);

    }

    public void clear() {
        for (Point[] pp : points)
            for (Point p : pp) {
                p.clear();
            }
        this.repaint();
    }

    public void initialize(String mapSource) throws IOException {
        points = new Point[this.length][this.height];
        for (int x = 0; x < points.length; ++x)
            for (int y = 0; y < points[x].length; ++y)
                points[x][y] = new Point(x, y, this);

        for (int x = 1; x < points.length-1; ++x) {
            for (int y = 1; y < points[x].length-1; ++y) {
                points[x][y].addNeighbor(points[x-1][y]);
                points[x][y].addNeighbor(points[x][y-1]);
                points[x][y].addNeighbor(points[x][y+1]);
                points[x][y].addNeighbor(points[x+1][y]);
                points[x][y].addNeighbor(points[x-1][y-1]);
                points[x][y].addNeighbor(points[x-1][y+1]);
                points[x][y].addNeighbor(points[x+1][y-1]);
                points[x][y].addNeighbor(points[x+1][y+1]);
            }
        }

        FileReader filereader = new FileReader(mapSource);
        BufferedReader bufferedreader = new BufferedReader(filereader);
        String line = bufferedreader.readLine();
        while (line != null) {
            int x = 0, y = 0, idx=0;
            while(idx< line.length()){
                StringBuilder S = new StringBuilder();
                while(line.charAt(idx)>='0' && line.charAt(idx)<='9' &&(y<points.length-1 && x<points[0].length-1)){
                    S.append(line.charAt(idx));
                    idx++;
                }
                if(!S.toString().equals("")){
                    points[y][x].type = Subsoil.fromInt(Integer.parseInt(S.toString()));
                }
                if(line.charAt(idx)==']') {
                    y += 1;
                    x=-1;
                }
                if(line.charAt(idx)==',')
                    x+=1;
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

    private void calculateStaticFields(){
        for(int x=0;x< length;x++){
            for(int y=0;y<height;y++){
                calculateStaticField(new Cords(x, y));
            }
        }
    }

    private void calculateStaticField(Cords cords){
//        Cords begin = cords.begin, end = cords.end;
        Integer[][] field = new Integer[length][length];
        for(int x=0;x< length;x++)
            for(int y=0;y<height;y++)
                field[x][y]=unreachable;
        field[cords.x][cords.y] = 0;

        if(points[cords.x][cords.y].type==Subsoil.pavement) {
            ArrayList<Point> toCheck = new ArrayList<>();
            toCheck.add(points[cords.x][cords.y]);
            while (!toCheck.isEmpty()) {
                Point current = toCheck.remove(0);
                boolean updated = false;
                if(field[current.x][current.y]==0)
                    updated = true;

                for (Point tmp : current.neighbors) {
                    if (    tmp.type == Subsoil.pavement ||
                            tmp.type == Subsoil.crossing ||
                            tmp.type == Subsoil.underground||
                            tmp.type == Subsoil.underground_pavement ||
                            tmp.type == Subsoil.underground_street ||
                            tmp.type == Subsoil.underground_unavailable ||
                            tmp.type == Subsoil.lights) {
                        if (field[tmp.x][tmp.y] + 1 < field[current.x][current.y]) {
                            field[current.x][current.y] = field[tmp.x][tmp.y] + 1;
                            updated = true;
                        }
                    }
                }
                if (updated) {
                    for (Point tmp : current.neighbors) {
                        if (    tmp.type == Subsoil.pavement ||
                                tmp.type == Subsoil.crossing ||
                                tmp.type == Subsoil.underground||
                                tmp.type == Subsoil.underground_pavement ||
                                tmp.type == Subsoil.underground_street ||
                                tmp.type == Subsoil.underground_unavailable ||
                                tmp.type == Subsoil.lights) {
                            toCheck.add(tmp);
                        }
                    }
                }
            }
        }
        for(int x= 1;x< length-1;x++) {
            for (int y = 1; y < height-1; y++) {
                points[x][y].addField(cords, field[x][y]);
            }
        }
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
//                g.setColor(Subsoil.getColor(points[x][y].type));
                g.setColor(points[x][y].getColor(startingPointSF, showStaticField));
                g.fillRect((x * size) + 1, (y * size) + 1, (size - 1), (size - 1));
            }
        }

    }

    public void mouseClicked(MouseEvent e) {
        if (mode){
            int x = e.getX() / size;
            int y = e.getY() / size;
            if ((x < points.length) && (x > 0) && (y < points[x].length) && (y > 0)) {
                if(rectangleMode==2){
                    rectangleCorner = new Cords(x,y);
                    rectangleMode--;
                }else if(rectangleMode==1){
                    Cords rectangleCorner2 = new Cords(x, y);
                    rectangleMode++;
                    for(int i = min(rectangleCorner.x, rectangleCorner2.x); i<=max(rectangleCorner.x, rectangleCorner2.x); i++){
                        for(int j = min(rectangleCorner.y, rectangleCorner2.y); j<=max(rectangleCorner.y, rectangleCorner2.y); j++){
                            points[i][j].type=editType;
                        }
                    }
                }
                points[x][y].type= editType;
                this.repaint();
            }
        }
    }

    public void mouseDragged(MouseEvent e) {
        if (mode){
            int x = e.getX() / size;
            int y = e.getY() / size;
            if ((x < points.length) && (x > 0) && (y < points[x].length) && (y > 0)) {
                points[x][y].type= editType;
                this.repaint();
            }
        }
    }

    public void mouseExited(MouseEvent e) {}
    public void mouseEntered(MouseEvent e) {}
    public void mouseReleased(MouseEvent e) {}
    public void mouseMoved(MouseEvent e) {}
    public void mousePressed(MouseEvent e) {}

    public void iteration(){
        // TODO
        System.out.println("Something happens in simulation.");
    }
}

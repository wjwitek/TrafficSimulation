package main.java.gui;

import main.java.Program;
import main.java.model.Car;
import main.java.model.Simulation;

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
    public Point[][] points;
    private final int size;
    int unreachable;
    public Subsoil editType = Subsoil.empty;
    public int rectangleMode = 0, length, height;
    private Coords rectangleCorner;
    public boolean mode = false, modeSF = false;
    public boolean showStaticField;
    Coords startingPointSF;
    private Simulation simulation;
    private String mapSource;

    public Board(int length, int height, int squareSize, String mapSource) {
        simulation = new Simulation(this);
        showStaticField = false;
        size = squareSize;
        this.length = length;
        this.height = height;
        unreachable = length * height + 10;
        startingPointSF = new Coords(1, 32);
        addMouseListener(this);
        addMouseMotionListener(this);
        setBackground(Color.WHITE);
        setOpaque(true);
        this.mapSource = mapSource;
        try {
            initialize(mapSource);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        calculateStaticFields();
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

        for (int x = 1; x < points.length - 1; ++x) {
            for (int y = 1; y < points[x].length - 1; ++y) {
                for (int dx = -1; dx <= 1; ++dx) {
                    for (int dy = -1; dy <= 1; ++dy) {
                        if (!(dx == 0 && dy == 0) && dx * dy == 0)
                            points[x][y].addNeighbor(points[x + dx][y + dy]);
                    }
                }
            }
        }

        FileReader filereader = new FileReader(mapSource);
        BufferedReader bufferedreader = new BufferedReader(filereader);
        String line = bufferedreader.readLine();
        while (line != null) {
            int x = 0, y = 0, idx = 0;
            while (idx < line.length()) {
                StringBuilder S = new StringBuilder();
                while (line.charAt(idx) >= '0' && line.charAt(idx) <= '9' && (y < points.length - 1 && x < points[0].length - 1)) {
                    S.append(line.charAt(idx));
                    idx++;
                }
                if (!S.toString().equals("")) {
                    points[y][x].type = Subsoil.fromInt(Integer.parseInt(S.toString()));
                }
                if (line.charAt(idx) == ']') {
                    y += 1;
                    x = -1;
                }
                if (line.charAt(idx) == ',')
                    x += 1;
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

    private void calculateStaticFields() {
        for (int x = 0; x < length; x++) {
            for (int y = 0; y < height; y++) {
                calculateStaticField(new Coords(x, y));
            }
        }
    }

    private void calculateStaticField(Coords coords) {
        Integer[][] field = new Integer[length][length];
        for (int x = 0; x < length; x++)
            for (int y = 0; y < height; y++)
                field[x][y] = unreachable;
        field[coords.x][coords.y] = 0;

        if (points[coords.x][coords.y].type == Subsoil.pavement) {
            ArrayList<Point> toCheck = new ArrayList<>();
            toCheck.add(points[coords.x][coords.y]);
            while (!toCheck.isEmpty()) {
                Point current = toCheck.remove(0);
                boolean updated = field[current.x][current.y] == 0;

                for (Point tmp : current.neighbors) {
                    if (current.type == Subsoil.pavement ||
                            current.type == Subsoil.crossing ||
                            current.type == Subsoil.crossingE ||
                            current.type == Subsoil.crossingN ||
                            current.type == Subsoil.crossingS ||
                            current.type == Subsoil.crossingW ||
                            current.type == Subsoil.underground ||
                            current.type == Subsoil.underground_pavement ||
                            current.type == Subsoil.underground_street ||
                            current.type == Subsoil.underground_streetS ||
                            current.type == Subsoil.underground_streetN ||
                            current.type == Subsoil.underground_unavailable ||
                            current.type == Subsoil.lights_pedestrians_red ||
                            current.type == Subsoil.lights_pedestrians_green) {
                        if (field[tmp.x][tmp.y] + 1 < field[current.x][current.y]) {
                            field[current.x][current.y] = field[tmp.x][tmp.y] + 1;
                            updated = true;
                        }
                    }
                }
                if (updated) {
                    for (Point tmp : current.neighbors) {
                        if (tmp.type == Subsoil.pavement ||
                                tmp.type == Subsoil.crossing ||
                                tmp.type == Subsoil.crossingE ||
                                tmp.type == Subsoil.crossingN ||
                                tmp.type == Subsoil.crossingS ||
                                tmp.type == Subsoil.crossingW ||
                                tmp.type == Subsoil.underground ||
                                tmp.type == Subsoil.underground_pavement ||
                                tmp.type == Subsoil.underground_street ||
                                tmp.type == Subsoil.underground_streetS ||
                                tmp.type == Subsoil.underground_streetN ||
                                tmp.type == Subsoil.underground_unavailable ||
                                tmp.type == Subsoil.lights_pedestrians_red ||
                                tmp.type == Subsoil.lights_pedestrians_green) {
                            toCheck.add(tmp);
                        }
                    }
                }
            }
        } else if (points[coords.x][coords.y].type == Subsoil.street ||
                points[coords.x][coords.y].type == Subsoil.streetN ||
                points[coords.x][coords.y].type == Subsoil.streetE ||
                points[coords.x][coords.y].type == Subsoil.streetS ||
                points[coords.x][coords.y].type == Subsoil.streetW) {
            ArrayList<Point> toCheck = new ArrayList<>();
            toCheck.add(points[coords.x][coords.y]);
            while (!toCheck.isEmpty()) {
                Point current = toCheck.remove(0);
                boolean updated = field[current.x][current.y] == 0;
                for (Point tmp : current.neighbors) {
                    if (current.type == Subsoil.street ||
//                            current.type == Subsoil.crossing ||
                            current.type == Subsoil.crossingE && tmp.x > current.x ||
                            current.type == Subsoil.crossingN && tmp.y < current.y ||
                            current.type == Subsoil.crossingS && tmp.y > current.y ||
                            current.type == Subsoil.crossingW && tmp.x < current.x ||
                            current.type == Subsoil.streetE && tmp.x > current.x ||
                            current.type == Subsoil.streetN && tmp.y < current.y ||
                            current.type == Subsoil.streetS && tmp.y > current.y ||
                            current.type == Subsoil.streetW && tmp.x < current.x ||
//                            current.type == Subsoil.underground_street ||
                            current.type == Subsoil.underground_streetS && tmp.y > current.y ||
                            current.type == Subsoil.underground_streetN && tmp.y < current.y ||
                            current.type == Subsoil.lights_cars_green ||
                            current.type == Subsoil.lights_cars_red) {
                        if (field[tmp.x][tmp.y] + 1 < field[current.x][current.y]) {
                            field[current.x][current.y] = field[tmp.x][tmp.y] + 1;
                            updated = true;
                        }
                    }
                }
                if (updated) {
                    for (Point tmp : current.neighbors) {
                        if (tmp.type == Subsoil.street ||
                                tmp.type == Subsoil.crossing ||
                                tmp.type == Subsoil.crossingE ||
                                tmp.type == Subsoil.crossingN ||
                                tmp.type == Subsoil.crossingS ||
                                tmp.type == Subsoil.crossingW ||
                                tmp.type == Subsoil.streetE ||
                                tmp.type == Subsoil.streetN ||
                                tmp.type == Subsoil.streetS ||
                                tmp.type == Subsoil.streetW ||
//                                tmp.type == Subsoil.streetE && tmp.x < current.x ||
//                                tmp.type == Subsoil.streetN && tmp.y < current.y ||
//                                tmp.type == Subsoil.streetS && tmp.y > current.y ||
//                                tmp.type == Subsoil.streetW && tmp.x > current.x ||
                                tmp.type == Subsoil.underground_street ||
                                tmp.type == Subsoil.underground_streetS ||
                                tmp.type == Subsoil.underground_streetN ||
                                tmp.type == Subsoil.lights_cars_green ||
                                tmp.type == Subsoil.lights_cars_red) {
                            toCheck.add(tmp);
                        }
                    }
                }
            }
        }
        float maxField = 0;
        for (int x = 1; x < length - 1; x++) {
            for (int y = 1; y < height - 1; y++) {
                points[x][y].addField(coords, field[x][y]);
                if (field[x][y] != unreachable)
                    maxField = max(maxField, field[x][y]);
            }
        }
        for (int x = 1; x < length - 1; x++) {
            for (int y = 1; y < height - 1; y++) {
                points[x][y].addMaxField(coords, maxField);
            }
        }
    }

    private void drawNetting(Graphics g, int gridSpace) {
        Insets insets = getInsets();
        int firstX = insets.left + gridSpace;
        int firstY = insets.top + gridSpace;
        int lastX = firstX + (length - 2) * size;
        int lastY = firstY + (height - 2) * size;

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

        for (x = 1; x < points.length - 1; ++x) {
            for (y = 1; y < points[x].length - 1; ++y) {
                g.setColor(points[x][y].getColor(startingPointSF, showStaticField));
                g.fillRect((x * size) + 1, (y * size) + 1, (size - 1), (size - 1));
                if (points[x][y].hasPedestrian != 0) {
                    g.setColor(new Color(250,250,0));
                    if(points[x][y].hasPedestrian <= 6) {
                        int n = points[x][y].hasPedestrian;
                        g.fillRect((x * size) + 1 + 2 + 6 - n, (y * size) + 1 + 2 + 6 - n,
                                (size - 1 - 4 - 12 + n * 2), (size - 1 - 4 - 12 + n * 2));
                    }
                    else
                        g.fillRect((x * size) + 3, (y * size) + 3, (size - 5), (size - 5));
                }
            }
        }
        for(Car c : simulation.cars){ //TODO color of cars
            int _x = c.currentPosition.x;
            int _y = c.currentPosition.y;
            if(Program.debug){
                if (c.destination.x == 1) {
                    g.setColor(new Color(108, 137, 255));
                } else if (c.destination.y == 1) {
                    g.setColor(new Color(83, 208, 170));
                } else if (c.destination.x == 46) {
                    g.setColor(new Color(12, 159, 62));
                } else if (c.destination.y == 33) {
                    g.setColor(new Color(142, 17, 204));
                }
            }else {
                g.setColor(new Color(16, 57, 241));
            }
            g.fillRect((_x * size) + 3, (_y * size) + 3, (size - 5), (size - 5));
        }
    }

    public void mouseClicked(MouseEvent e) {
        int x = e.getX() / size;
        int y = e.getY() / size;
        if (mode) {
//            int x = e.getX() / size;
//            int y = e.getY() / size;
            if ((x < points.length) && (x > 0) && (y < points[x].length) && (y > 0)) {
                if (rectangleMode == 2) {
                    rectangleCorner = new Coords(x, y);
                    rectangleMode--;
                } else if (rectangleMode == 1) {
                    Coords rectangleCorner2 = new Coords(x, y);
                    rectangleMode++;
                    for (int i = min(rectangleCorner.x, rectangleCorner2.x); i <= max(rectangleCorner.x, rectangleCorner2.x); i++) {
                        for (int j = min(rectangleCorner.y, rectangleCorner2.y); j <= max(rectangleCorner.y, rectangleCorner2.y); j++) {
                            points[i][j].type = editType;
                        }
                    }
                }
                points[x][y].type = editType;
                this.repaint();
            }
        } else if (modeSF) {
            startingPointSF.change(x, y);
            showStaticField = !showStaticField;
            repaint();
        }
    }

    public void mouseDragged(MouseEvent e) {
        if (mode) {
            int x = e.getX() / size;
            int y = e.getY() / size;
            if ((x < points.length) && (x > 0) && (y < points[x].length) && (y > 0)) {
                points[x][y].type = editType;
                this.repaint();
            }
        }
    }

    public void mouseExited(MouseEvent e) {
    }

    public void mouseEntered(MouseEvent e) {
    }

    public void mouseReleased(MouseEvent e) {
    }

    public void mouseMoved(MouseEvent e) {
    }

    public void mousePressed(MouseEvent e) {
    }

    public void iteration(int iteration_num) {
        simulation.iteration(iteration_num);
        this.repaint();
    }
  
    public void restart() {
        simulation.cars.clear();
        simulation.pedestrians.clear();
        try {
            initialize(mapSource);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        calculateStaticFields();
        this.repaint();
    }


    public Point getPointByCoords(Coords coords){
        return points[coords.x][coords.y];
    }
}

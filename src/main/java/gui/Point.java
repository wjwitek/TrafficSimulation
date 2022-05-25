package main.java.gui;

import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;

public class Point {
    public Subsoil type;
    public ArrayList<Point> neighbors = new ArrayList<>();
    public int x, y;
    final HashMap<Coords, Integer> fields = new HashMap<>();
    Board board;
    public boolean hasCar;

    public Point(int x, int y, Board board){
        this.x = x;
        this.y = y;
        type = Subsoil.empty;
        this.board = board;
    }

    public void addNeighbor(Point nei) {neighbors.add(nei);}
    public void addField(Coords coords, Integer value){fields.put(coords, value);}

    public Color getColor(Coords coords, boolean staticField){
        if(staticField) {
            int distance = fields.get(coords);
            if(distance==board.unreachable)
                return new Color(0.2f, 0.2f, 0.2f, 0.7f);
            return new Color(0.3f, 0.3f, 1f, 1-(fields.get(coords))/(float)80);
        }
        return Subsoil.getColor(type);
    }

    public void clear(){
        type = Subsoil.empty;
    }
}
package main.java.gui;

import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;

public class Point {
    public Subsoil type;
    public ArrayList<Point> neighbors = new ArrayList<>();
    public int x, y;
    final HashMap<Cords, Integer> fields = new HashMap<>();
    Board board;

    public Point(int x, int y, Board board){
        this.x = x;
        this.y = y;
        type = Subsoil.empty;
        this.board = board;
    }

    public void addNeighbor(Point nei) {neighbors.add(nei);}
    public void addField(Cords cords, Integer value){fields.put(cords, value);}

    public Color getColor(Cords cords, boolean staticField){
        if(staticField) {
            int distance = fields.get(cords);
            if(distance==board.unreachable)
                return new Color(0.2f, 0.2f, 0.2f, 0.7f);
            return new Color(0.3f, 0.3f, 1f, 1-(fields.get(cords))/(float)80);
        }
        return Subsoil.getColor(type);
    }

    public void clear(){
        type = Subsoil.empty;
    }
}
package main.java.gui;

import java.util.ArrayList;

public class Point {
    public Subsoil type;
    public ArrayList<Point> neighbors;
    public int x, y;

    public Point(int x, int y) {
        this.x = x;
        this.y = y;
        type = Subsoil.empty;
    }

    public void addNeighbor(Point nei) {
        neighbors.add(nei);
    }

    public void clear(){
        type = Subsoil.empty;
    }
}
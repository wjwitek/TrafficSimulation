package main.java.gui;

import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;

public class Point {
    public Subsoil type;
    public ArrayList<Point> neighbors = new ArrayList<>();
    public int x, y;
    public final HashMap<Coords, Integer> fields = new HashMap<>();
    public final HashMap<Coords, Float> maxField = new HashMap<>();
    Board board;
    public boolean hasCar;
    public int hasPedestrian = 0;

    public Point(int x, int y, Board board){
        this.x = x;
        this.y = y;
        type = Subsoil.empty;
        this.board = board;
    }

    public void addNeighbor(Point nei) {neighbors.add(nei);}
    public void addField(Coords coords, Integer value){fields.put(coords, value);}
    public void addMaxField(Coords coords, Float value){maxField.put(coords, value);}

    public Color getColor(Coords coords, boolean staticField){
        if(staticField) {
            int distance = fields.get(coords);
            if(distance==board.unreachable)
                return new Color(0.2f, 0.2f, 0.2f, 0.7f);
            if(board.points[coords.x][coords.y].type==Subsoil.pavement)
                return new Color(0.3f, 0.3f, 1f, 1-(distance)/maxField.get(coords));
            if(board.points[coords.x][coords.y].type==Subsoil.street||
                    board.points[coords.x][coords.y].type==Subsoil.streetN||
                    board.points[coords.x][coords.y].type==Subsoil.streetE||
                    board.points[coords.x][coords.y].type==Subsoil.streetS||
                    board.points[coords.x][coords.y].type==Subsoil.streetW)
                return new Color(1f, 0.3f, 0.3f, 1-(distance)/maxField.get(coords));
        }
        return Subsoil.getColor(type);
    }

    public void clear(){
        type = Subsoil.empty;
    }

    public Point getLowestStaticFieldNeighbour(Coords destination){
        ArrayList<Point> bests = new ArrayList<>();
        bests.add(neighbors.get(0));
//        Point best = neighbors.get(0);
        for (int i=1; i<neighbors.size(); i++){
            if (!(Subsoil.driveable(bests.get(0).type)) && Subsoil.driveable(neighbors.get(i).type)){
                bests.clear();
                bests.add(neighbors.get(i));
            }
            else if (Subsoil.driveable(bests.get(0).type) && Subsoil.driveable(neighbors.get(i).type)){
                if (bests.get(0).fields.get(destination) > neighbors.get(i).fields.get(destination)){
                    bests.clear();
                    bests.add(neighbors.get(i));
                }
                else if (Objects.equals(bests.get(0).fields.get(destination), neighbors.get(i).fields.get(destination))){
                    bests.add(neighbors.get(i));
                }
            }
        }
        if(!(Subsoil.driveable(bests.get(0).type)))
            throw new RuntimeException("Point" + this.x + ", " + this.y + "do not have valid neighbour!\n");
        Point firstNeighbor = bests.get(0);
        for(int i=bests.size()-1;i>=0;i--){
            if(bests.get(i).hasCar)
                bests.remove(i);
        }
        if(bests.isEmpty())
            return firstNeighbor;
        return bests.get(getRandomNumber(0,bests.size()));
    }

    public int getRandomNumber(int min, int max) {
        return (int) ((Math.random() * (max - min)) + min);
    }
}
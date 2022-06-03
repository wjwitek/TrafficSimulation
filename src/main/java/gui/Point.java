package main.java.gui;

import java.awt.*;
import java.util.*;

public class Point implements Comparable<Point> {
    public Subsoil type;
    public ArrayList<Point> neighbors = new ArrayList<>();
    public int x, y;
    public final HashMap<Coords, Integer> fields = new HashMap<>();
    public final HashMap<Coords, Float> maxField = new HashMap<>();
    Board board;
    public boolean hasCar;
    public int hasPedestrian = 0;
    private final TreeSet<Point> crossingNeighbours = new TreeSet<>();

    public Point(int x, int y, Board board){
        this.x = x;
        this.y = y;
        type = Subsoil.empty;
        this.board = board;
    }

    protected void getCrossingNeighbours(){
        // find left upper corner
        Point leftUpper = this;
        boolean changed = true;
        while (changed) {
            changed = false;
            if (Subsoil.crossing(board.points[leftUpper.x - 1][leftUpper.y].type)){
                leftUpper = board.points[leftUpper.x - 1][leftUpper.y];
                changed = true;
            }
            if (Subsoil.crossing(board.points[leftUpper.x][leftUpper.y - 1].type)){
                leftUpper = board.points[leftUpper.x][leftUpper.y - 1];
                changed = true;
            }
        }
        changed = true;
        // find right down corner
        Point rightDown = this;
        while (changed) {
            changed = false;
            if (Subsoil.crossing(board.points[rightDown.x + 1][rightDown.y].type)) {
                rightDown = board.points[rightDown.x + 1][rightDown.y];
                changed = true;
            }
            if (Subsoil.crossing(board.points[rightDown.x][rightDown.y + 1].type)) {
                rightDown = board.points[rightDown.x][rightDown.y + 1];
                changed = true;
            }
        }
        // add crossing Points
        for (int x=leftUpper.x; x<rightDown.x+1; x++){
            crossingNeighbours.addAll(Arrays.asList(board.points[x]).subList(leftUpper.y, rightDown.y + 1));
        }
    }

    public boolean pedestrianOnCrossing(){
        if (!(Subsoil.crossing(type))){
            return false;
        }
        for (Point crossingNeighbour : crossingNeighbours) {
            if (crossingNeighbour.hasPedestrian > 0) {
                return true;
            }
        }
        return false;
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

    @Override
    public int compareTo(Point o) {
        if (o.x == x && o.y == y){
            return 0;
        }
        else if (o.x > x){
            return 1;
        }
        return -1;
    }
}
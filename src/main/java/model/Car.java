package main.java.model;

import main.java.gui.Board;
import main.java.gui.Coords;
import main.java.gui.Point;
import main.java.gui.Subsoil;

import java.awt.*;
import java.util.ArrayList;

public class Car {
    public boolean moved = false;
    public Coords currentPosition;
    //private ArrayList<Point> pathTaken = new ArrayList<>();
    protected Board map;
    private final int maxVelocity;
    private int velocity;
    private int acceleration = 1; // TODO change to function of velocity
    public Coords destination;
    private ArrayList<Point> nextPath;

    public Car(Board newMap, int startingVelocity, Coords startingPosition, int maxVelocity){
        map = newMap;
        map.points[startingPosition.x][startingPosition.y].hasCar = true;
        this.maxVelocity = maxVelocity;
        if (startingVelocity > maxVelocity){
            throw new ArithmeticException("Starting velocity cannot be bigger than maximum velocity.");
        }
        velocity = startingVelocity;
        currentPosition = startingPosition;
    }

    public boolean iteration(){
        // get path of movement
        ArrayList<Point> path = preparePath();
        // calculate new velocity
        newVelocity(path);
        // make moves
        move(path);

        if (outOfBounds()){
            // signals to the simulation that car should be deleted
            return true;
        }
        map.points[currentPosition.x][currentPosition.y].hasCar = true;
        return false;
    }

    protected void prepareIteration(){
        // get path of movement
        nextPath = preparePath();
        // calculate new velocity
        newVelocity(nextPath);
    }

    protected boolean finalizeIteration(){
        move(nextPath);

        if (outOfBounds()){
            // signals to the simulation that car should be deleted
            return true;
        }
        map.points[currentPosition.x][currentPosition.y].hasCar = true;
        return false;
    }

    private boolean outOfBounds(){
        if (currentPosition.x < 0 || currentPosition.x >= map.points.length ||
                (currentPosition.x == destination.x && currentPosition.y == destination.y)){
            return true;
        }
        return currentPosition.y < 0 || currentPosition.y >= map.points[0].length;
    }

    public Color getColor(){
        return Color.CYAN;
    }

    private ArrayList<Point> preparePath(){
        ArrayList<Point> path = new ArrayList<>();
        Point currentPoint = map.getPointByCoords(currentPosition);
        for (int i=0; i<maxVelocity; i++){
            Point nextPoint = currentPoint.getLowestStaticFieldNeighbour(destination);
            if (nextPoint.hasCar || nextPoint.type == Subsoil.lights_cars_red || (nextPoint.hasPedestrian > 0 && Subsoil.underground(nextPoint.type))){
                break;
            }
            path.add(nextPoint);
            currentPoint = nextPoint;
        }
        return path;
    }

    private void newVelocity(ArrayList<Point> path){
        int vPossible;
        if (path.size() == 0){
            vPossible = 0;
        }
        else{
            double temp = Math.sqrt(2.0 * (double) path.size() * 3);
            vPossible = Math.max(1, Math.min(path.size(), (int) temp));
        }
        velocity = Math.min(vPossible, velocity + acceleration);
    }

    private void move(ArrayList<Point> path){
        if (velocity != 0){
            Point newPoint = path.get(velocity - 1);
            newPoint.hasCar = true;
            map.points[currentPosition.x][currentPosition.y].hasCar = false;
            currentPosition = new Coords(newPoint.x, newPoint.y);
        }
    }
}

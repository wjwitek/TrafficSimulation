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
    protected Board map;
    private final int maxVelocity;
    private int velocity;
    private int acceleration = 1; // TODO change to function of velocity
    public Coords destination;
    private ArrayList<Point> nextPath;

    /**
     * Class constructor specifying starting values.
     * @param newMap board where simulation is taking place
     * @param startingVelocity velocity before first move
     * @param startingPosition coords before first move
     * @param maxVelocity maximum possible velocity of car (speed limit)
     */
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

    /**
     * First stage of car's iteration - calculates path and new velocity of car.
     */
    protected void prepareIteration(){
        // get path of movement
        nextPath = preparePath();
        // calculate new velocity
        newVelocity(nextPath);
    }

    /**
     * Second stage of car's iteration - actually move car.
     * @return true if car exited map
     */
    protected boolean finalizeIteration(){
        move(nextPath);

        if (outOfBounds()){
            // signals to the simulation that car should be deleted
            return true;
        }
        map.points[currentPosition.x][currentPosition.y].hasCar = true;
        return false;
    }

    /**
     * Check if car reached its destination.
     * @return true if car left map or reached destination
     */
    private boolean outOfBounds(){
        if (currentPosition.x < 0 || currentPosition.x >= map.points.length ||
                (currentPosition.x == destination.x && currentPosition.y == destination.y)){
            return true;
        }
        return currentPosition.y < 0 || currentPosition.y >= map.points[0].length;
    }

    /**
     * Calculate array of points that car moves through.
     * @return path
     */
    private ArrayList<Point> preparePath(){
        ArrayList<Point> path = new ArrayList<>();
        Point currentPoint = map.getPointByCoords(currentPosition);
        for (int i=0; i<maxVelocity; i++){
            Point nextPoint = currentPoint.getLowestStaticFieldNeighbour(destination);
            if (nextPoint.hasCar || nextPoint.type == Subsoil.lights_cars_red || resolveCrossing(map.getPointByCoords(currentPosition), nextPoint)){
                break;
            }
            path.add(nextPoint);
            currentPoint = nextPoint;
        }
        return path;
    }

    /**
     * Calculate velocity of car during this iteration.
     * @param path path that car is going to take
     */
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

    /**
     * Change car's current position and corresponding points' attributes
     * @param path path that car is taking
     */
    private void move(ArrayList<Point> path){
        if (velocity != 0){
            Point newPoint = path.get(velocity - 1);
            newPoint.hasCar = true;
            map.points[currentPosition.x][currentPosition.y].hasCar = false;
            currentPosition = new Coords(newPoint.x, newPoint.y);
        }
    }

    /**
     * Check if car should wait before crossing
     * @param currentPosition where car is now
     * @param nextPoint where car wants to go
     * @return return true if car can't move to nextPoint
     */
    private boolean resolveCrossing(Point currentPosition, Point nextPoint){
        if (!(Subsoil.crossing(currentPosition.type))){
            return nextPoint.pedestrianOnCrossing();
        }
        return nextPoint.hasPedestrian > 0;
    }
}

package main.java.model;

import main.java.gui.Board;
import main.java.gui.Coords;
import main.java.gui.Point;

import java.awt.*;
import java.util.ArrayList;

public class Pedestrian {
    public boolean moved = false;
    public Coords currentPosition;
    private Coords vectorOfMovement; // [left-right, up-down]
    private Board map;
    private final int maxVelocity = 2;
    private int maxDistanceFromEnd;
    public boolean changeLeft = false;
    public boolean changeRight = false;
    private int velocity;
    private final Coords destinationPosition;
    public Pedestrian(Board newMap, int startingVelocity, Coords directionOfMovement, Coords startingPosition, Coords endPosition) {
        map = newMap;
        map.points[startingPosition.x][startingPosition.y].hasPedestrian = true;
        if (startingVelocity > maxVelocity) {
            throw new ArithmeticException("Starting velocity cannot be bigger than maximum velocity.");
        }
        velocity = startingVelocity;
        vectorOfMovement = directionOfMovement;
        currentPosition = startingPosition;
        destinationPosition = endPosition;
    }


    public boolean move() {
        if (currentPosition.equals(destinationPosition)) return true;

        ArrayList<Point> neighborsPedestrian = map.points[currentPosition.x][currentPosition.y].neighbors;
        Coords nextPosition = currentPosition;
        int minNeighboursStaticFiled = map.points[currentPosition.x][currentPosition.y].fields.get(destinationPosition);
        for (Point point : neighborsPedestrian) {
            if (map.points[point.x][point.y].fields.containsKey(destinationPosition) &&
                    minNeighboursStaticFiled > map.points[point.x][point.y].fields.get(destinationPosition) &&
                    map.points[point.x][point.y].fields.get(destinationPosition)!= map.unreachable ) {
                minNeighboursStaticFiled = map.points[point.x][point.y].fields.get(destinationPosition);
                nextPosition =new Coords(point.x, point.y);
            }
            map.points[currentPosition.x][currentPosition.y].hasPedestrian = false;
            currentPosition.change(nextPosition.x, nextPosition.y);
        }

        if (outOfBounds()) {
            // signals to the simulation that car should be deleted
            return true;
        }
        map.points[currentPosition.x][currentPosition.y].hasPedestrian = true;
        return false;
    }

    private boolean outOfBounds() {
        if (currentPosition.x < 0 || currentPosition.x >= map.points.length) {
            return true;
        }
        return currentPosition.y < 0 || currentPosition.y >= map.points[0].length;
    }

    public Color getColor() {
        return Color.ORANGE;
    }
}

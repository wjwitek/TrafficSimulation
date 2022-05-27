package main.java.model;

import main.java.gui.Board;
import main.java.gui.Coords;

import java.awt.*;
import java.util.ArrayList;

public class Car {
    public boolean moved = false;
    public Coords currentPosition;
    private Coords vectorOfMovement; // [left-right, up-down]
    private Board map;
    private final int maxVelocity = 4;
    private int maxPossibleVelocity;
    private int nextVelocity;
    private int maxDistanceFromEnd;
    public boolean changeLeft = false;
    public boolean changeRight = false;
    private int velocity;

    public Car(Board newMap, int startingVelocity, Coords directionOfMovement, Coords startingPosition){
        map = newMap;
        map.points[startingPosition.x][startingPosition.y].hasCar = true;
        if (startingVelocity > maxVelocity){
            throw new ArithmeticException("Starting velocity cannot be bigger than maximum velocity.");
        }
        velocity = startingVelocity;
        vectorOfMovement = directionOfMovement;
        currentPosition = startingPosition;
    }


    public boolean move(){
        map.points[currentPosition.x][currentPosition.y].hasCar = false;
        currentPosition = vectorOfMovement.multiplyByConstant(velocity).add(currentPosition);
        if (outOfBounds()){
            // signals to the simulation that car should be deleted
            return true;
        }
        map.points[currentPosition.x][currentPosition.y].hasCar = true;
        return false;
    }

    private boolean outOfBounds(){
        if (currentPosition.x < 0 || currentPosition.x >= map.points.length){
            return true;
        }
        return currentPosition.y < 0 || currentPosition.y >= map.points[0].length;
    }

    public Color getColor(){
        return Color.CYAN;
    }
}

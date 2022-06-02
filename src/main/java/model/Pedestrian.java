package main.java.model;

import main.java.gui.Board;
import main.java.gui.Coords;
import main.java.gui.Point;
import main.java.gui.Subsoil;

import java.awt.*;
import java.util.ArrayList;

public class Pedestrian {
    public boolean moved = false;
    public Coords currentPosition;
//    private Coords vectorOfMovement; // [left-right, up-down]
    private Board map;
    private final int maxVelocity = 2;
    private int maxDistanceFromEnd;
    public boolean changeLeft = false;
    public boolean changeRight = false;
    private int velocity;

    private int can_go = 1;
    private final Coords destinationPosition;

    public Pedestrian(Board newMap, int startingVelocity, Coords startingPosition, Coords endPosition) {
        map = newMap;
        map.points[startingPosition.x][startingPosition.y].hasPedestrian += 1;
        if (startingVelocity > maxVelocity) {
            throw new ArithmeticException("Starting velocity cannot be bigger than maximum velocity.");
        }
        velocity = startingVelocity;
//        vectorOfMovement = directionOfMovement;
        currentPosition = startingPosition;
        destinationPosition = endPosition;
    }


    public boolean move() {
        if (currentPosition.equals(destinationPosition)) return true;

        ArrayList<Point> neighborsPedestrian = map.points[currentPosition.x][currentPosition.y].neighbors;
        ArrayList<Coords> allNextPosition = new ArrayList<>();
        Coords nextPosition = currentPosition;
        boolean onStreet = false;
        map.points[currentPosition.x][currentPosition.y].hasPedestrian -= 1;
        int minNeighboursStaticFiled = map.points[currentPosition.x][currentPosition.y].fields.get(destinationPosition);
        for (Point point : neighborsPedestrian) {
            if (map.points[point.x][point.y].fields.containsKey(destinationPosition) &&
                    ((map.points[point.x][point.y].type != Subsoil.lights_pedestrians_red) &&
                    !(can_go == 1 && Subsoil.lights_pedestrians_green_blinking == map.points[point.x][point.y].type)) || isOnStreetInFrontOfLights(point)) {
                if (minNeighboursStaticFiled > map.points[point.x][point.y].fields.get(destinationPosition)) {
                    minNeighboursStaticFiled = map.points[point.x][point.y].fields.get(destinationPosition);
                    allNextPosition = new ArrayList<>();
                    allNextPosition.add(new Coords(point.x, point.y));
                    onStreet = isOnStreetInFrontOfLights(point);
                } else if (minNeighboursStaticFiled == map.points[point.x][point.y].fields.get(destinationPosition)) {
                    allNextPosition.add(new Coords(point.x, point.y));
                }
            }
        }
        if (!allNextPosition.isEmpty()) {
            nextPosition = allNextPosition.get(getRandomNumber(0, allNextPosition.size()));
        }

        currentPosition.change(nextPosition.x, nextPosition.y);
        if (map.points[currentPosition.x][currentPosition.y].type == Subsoil.lights_pedestrians_green || onStreet) can_go = (can_go + 1) % 2;
        if (map.points[currentPosition.x][currentPosition.y].type == Subsoil.lights_pedestrians_green_blinking) can_go = 1;

        if (outOfBounds()) {
            // signals to the simulation that car should be deleted
            return true;
        }
        map.points[currentPosition.x][currentPosition.y].hasPedestrian += 1;
        return false;
    }


    private boolean isOnStreetInFrontOfLights(Point point){
        return map.points[currentPosition.x][currentPosition.y].type != Subsoil.pavement
                && (map.points[point.x][point.y].type == Subsoil.lights_pedestrians_red ||map.points[point.x][point.y].type == Subsoil.lights_pedestrians_green_blinking);
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

    public int getRandomNumber(int min, int max) {
        return (int) ((Math.random() * (max - min)) + min);
    }
}

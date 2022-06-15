package main.java.model.spawn;

import main.java.gui.Board;
import main.java.gui.Coords;

import java.util.ArrayList;

public abstract class SpawnPoint {
    protected Coords coords;
    protected int spawn_probability;
    protected Board map;
    protected int maxVelocity;
    protected ArrayList<Coords> possibleDestination;

    public SpawnPoint(Coords newCoords, int prob, Board newMap, ArrayList<Coords> destinationArray){
        coords = newCoords;
        spawn_probability = prob;
        map = newMap;
        possibleDestination = destinationArray;
        for(Coords dest : possibleDestination){
            if(map.points[coords.x][coords.y].fields.get(new Coords(dest.x, dest.y))==map.length * map.height + 10){
                throw new RuntimeException("Error with spawn point!\n");
            }
        }
    }

    public Object get(){
        return null;
    }

    public static int getRandomNumber(int min, int max) {
        return (int) ((Math.random() * (max - min)) + min);
    }
}

package main.java.model.spawn;

import main.java.gui.Board;
import main.java.gui.Coords;

import java.util.ArrayList;

public class SpawnPoint {
    protected Coords coords;
    protected int spawn_probability;
    protected Board map;
    protected int maxVelocity;
    protected ArrayList<Coords> possibleDestination = new ArrayList<>();

    public SpawnPoint(Coords newCoords, int prob, Board newMap){
        coords = newCoords;
        spawn_probability = prob;
        map = newMap;
    }

    public Object get(){
        return null;
    }

    public int getRandomNumber(int min, int max) {
        return (int) ((Math.random() * (max - min)) + min);
    }
}

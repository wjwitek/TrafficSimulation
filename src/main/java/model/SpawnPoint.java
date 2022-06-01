package main.java.model;

import main.java.gui.Board;
import main.java.gui.Coords;
import main.java.gui.Point;
import org.json.JSONArray;
import org.json.JSONObject;

public class SpawnPoint {
    private Coords coords;
    private int probability;
    private Board map;
    private int maxVelocity = 4;

    public SpawnPoint(Coords startCoords, int prob, Board newMap){
        coords = startCoords;
        probability = prob;
        map = newMap;
    }

    public SpawnPoint(JSONObject info, Board newMap){
        JSONArray startCoords = info.getJSONArray("coords");
        coords = new Coords(startCoords.getInt(0), startCoords.getInt(1));
        probability = info.getInt("probability");
        map = newMap;
    }

    public Car getCar(){
        if (getRandomNumber(0, 101) < probability && !(map.getPointByCoords(coords).hasCar)){
            Car newCar = new Car(map, getRandomNumber(1, maxVelocity + 1), coords.copy());
            // TODO change to random generation
            newCar.destination = new Coords(46, 23);
            return newCar;
        }
        return null;
    }

    public int getRandomNumber(int min, int max) {
        return (int) ((Math.random() * (max - min)) + min);
    }
}

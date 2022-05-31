package main.java.model;

import main.java.gui.Board;
import main.java.gui.Coords;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class SpawnPoint {
    private Coords coords;
    private int probability;
    private Board map;
    private int maxVelocityCars = 4;

    private int maxVelocityPedestrians = 2;
    private Coords vectorOfMovement;

    private ArrayList<Coords> destinationPointsPedestrian;


    public SpawnPoint(Coords startCoords, int prob, Board newMap, Coords movementDirection){
        coords = startCoords;
        probability = prob;
        map = newMap;
        vectorOfMovement = movementDirection;
    }

    public SpawnPoint(JSONObject info, Board newMap){
        JSONArray startCoords = info.getJSONArray("coords");
        coords = new Coords(startCoords.getInt(0), startCoords.getInt(1));
        probability = info.getInt("probability");
        map = newMap;
        JSONArray startVector = info.getJSONArray("direction");
        vectorOfMovement = new Coords(startVector.getInt(0), startVector.getInt(1));
    }

    public SpawnPoint(JSONObject info, Board newMap,ArrayList<Coords> destinationArray){
        JSONArray startCoords = info.getJSONArray("coords");
        coords = new Coords(startCoords.getInt(0), startCoords.getInt(1));
        probability = info.getInt("probability");
        map = newMap;
        JSONArray startVector = info.getJSONArray("direction");
        vectorOfMovement = new Coords(startVector.getInt(0), startVector.getInt(1));
        destinationPointsPedestrian= destinationArray;
    }


    public Car getCar(){
        if (getRandomNumber(0, 101) < probability){
            return new Car(map, getRandomNumber(1, maxVelocityCars + 1), vectorOfMovement.copy(), coords.copy());
        }
        return null;
    }

    public Pedestrian getPedestrian(){
        if (getRandomNumber(0, 101) < probability){
            Coords dest = destinationPointsPedestrian.get(getRandomNumber(0,destinationPointsPedestrian.size()));
            return new Pedestrian(map, getRandomNumber(1, maxVelocityPedestrians + 1), vectorOfMovement.copy(), coords.copy(),dest.copy());
        }
        return null;
    }

    public int getRandomNumber(int min, int max) {
        return (int) ((Math.random() * (max - min)) + min);
    }
}

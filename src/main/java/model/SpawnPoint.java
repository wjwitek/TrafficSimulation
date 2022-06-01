package main.java.model;

import main.java.gui.Board;
import main.java.gui.Coords;
import main.java.gui.Point;
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
    private int maxVelocity = 4;
    private ArrayList<Coords> possibleDestination = new ArrayList<>();


    public SpawnPoint(Coords startCoords, int prob, Board newMap, Coords movementDirection){
        coords = startCoords;
        probability = prob;
        map = newMap;
    }
    

    public SpawnPoint(Coords startCoords, int prob, Board newMap){
        coords = startCoords;
        probability = prob;
        map = newMap;
    }

    public SpawnPoint(JSONObject info, Board newMap){
        JSONArray startCoords = info.getJSONArray("coords");
        coords = new Coords(startCoords.getInt(0), startCoords.getInt(1));
        probability = info.getInt("probability");
        JSONArray destinationCoords = info.getJSONArray("destination");
        for (int i=0; i<destinationCoords.length(); i += 2){
            possibleDestination.add(new Coords(destinationCoords.getInt(i), destinationCoords.getInt(i + 1)));
            System.out.println(destinationCoords.getInt(i) + " " + destinationCoords.getInt(i + 1));
        }
        map = newMap;
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
      if (getRandomNumber(0, 101) < probability && !(map.getPointByCoords(coords).hasCar)){
          Car newCar = new Car(map, getRandomNumber(1, maxVelocity + 1), coords.copy());
          // TODO change to random generation
          newCar.destination = possibleDestination.get(0);
          return newCar;
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

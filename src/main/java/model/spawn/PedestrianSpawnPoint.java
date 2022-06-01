package main.java.model.spawn;

import main.java.gui.Board;
import main.java.gui.Coords;
import main.java.model.Pedestrian;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class PedestrianSpawnPoint extends SpawnPoint {
    public Coords vectorOfMovement = new Coords(1, 1);

    public PedestrianSpawnPoint(Coords newCoords, int prob, Board newMap){
        super(newCoords, prob, newMap);
        maxVelocity = 2;
    }

    public PedestrianSpawnPoint(JSONObject info, Board newMap, ArrayList<Coords> destinationArray){
        super(new Coords(0, 0), 0, null);
        JSONArray startCoords = info.getJSONArray("coords");
        coords = new Coords(startCoords.getInt(0), startCoords.getInt(1));
        spawn_probability = info.getInt("probability");
        map = newMap;
        JSONArray startVector = info.getJSONArray("direction");
        possibleDestination = destinationArray;
        maxVelocity = 2;
    }

    @Override
    public Pedestrian get(){
        if (getRandomNumber(0, 101) < spawn_probability){
            Coords dest = possibleDestination.get(getRandomNumber(0, possibleDestination.size()));
            return new Pedestrian(map, getRandomNumber(1, maxVelocity + 1), vectorOfMovement.copy(), coords.copy(),dest.copy());
        }
        return null;
    }
}

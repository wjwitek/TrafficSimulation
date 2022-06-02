package main.java.model.spawn;

import main.java.gui.Board;
import main.java.gui.Coords;
import main.java.model.Pedestrian;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class PedestrianSpawnPoint extends SpawnPoint {

    public PedestrianSpawnPoint(Coords coords, int spawn_probability, Board newMap, ArrayList<Coords> destinationArray){
        super(coords, spawn_probability, newMap, destinationArray);
        maxVelocity = 2;
    }

    @Override
    public Pedestrian get(){
        if (getRandomNumber(0, 101) < spawn_probability){
            Coords dest = possibleDestination.get(getRandomNumber(0, possibleDestination.size()));
            while(dest==coords){
                dest = possibleDestination.get(getRandomNumber(0, possibleDestination.size()));
            }
            return new Pedestrian(map, getRandomNumber(0, 101)<95?1:2, coords.copy(), dest.copy());
        }
        return null;
    }
}

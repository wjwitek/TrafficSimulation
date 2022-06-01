package main.java.model.spawn;

import main.java.gui.Board;
import main.java.gui.Coords;
import main.java.model.Car;
import main.java.model.spawn.SpawnPoint;
import org.json.JSONArray;
import org.json.JSONObject;

public class CarSpawnPoint extends SpawnPoint {
    public CarSpawnPoint(JSONObject info, Board newMap){
        super(new Coords(0, 0), 0, null);
        maxVelocity = 4;
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

    public Car get(){
      if (getRandomNumber(0, 101) < probability && !(map.getPointByCoords(coords).hasCar)){
          Car newCar = new Car(map, getRandomNumber(1, maxVelocity + 1), coords.copy());
          // TODO change to random generation
          newCar.destination = possibleDestination.get(0);
          return newCar;
       }
        return null;
    }
}

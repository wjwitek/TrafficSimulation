package main.java.model.spawn;

import main.java.gui.Board;
import main.java.gui.Coords;
import main.java.model.Car;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class CarSpawnPoint extends SpawnPoint {
    private final ArrayList<Integer> possibleDestinationProbability = new ArrayList<>();
    private ArrayList<Car> queue = new ArrayList<>();
    public CarSpawnPoint(JSONObject info, Board newMap){
        super(new Coords(0, 0), 0, newMap, new ArrayList<>());
        maxVelocity = 4; //TODO changeable
        JSONArray startCoords = info.getJSONArray("coords");
        coords = new Coords(startCoords.getInt(0), startCoords.getInt(1));
        spawn_probability = info.getInt("spawn_probability");
        JSONArray destinationCoords = info.getJSONArray("destination");
        JSONArray destinationCoordsProbability = info.getJSONArray("destination_probability");
        for (int i=0; i<destinationCoords.length(); i += 2){
            possibleDestination.add(new Coords(destinationCoords.getInt(i), destinationCoords.getInt(i + 1)));
            possibleDestinationProbability.add(destinationCoordsProbability.getInt(i/2));
//            System.out.println(destinationCoords.getInt(i) + " " + destinationCoords.getInt(i + 1) + "; "
//                    + destinationCoordsProbability.getInt(i/2));
        }
//        map = newMap;
    }

    public Car get(){
      if (getRandomNumber(0, 101) < spawn_probability){
          Car newCar = new Car(map, getRandomNumber(1, maxVelocity + 1), coords.copy(), maxVelocity);
          // TODO change to random generation
          int destination_no = getRandomNumber(0, 101);
          int idx = 0;
          while(destination_no>possibleDestinationProbability.get(idx)){
              destination_no-=possibleDestinationProbability.get(idx);
              idx++;
              if(idx>=possibleDestinationProbability.size()){
                  throw new RuntimeException("Invalid JSON with destination probability!\n");
              }
          }
          newCar.destination = possibleDestination.get(idx);
          if (map.getPointByCoords(coords).hasCar){
              queue.add(newCar);
              return null;
          }
          return newCar;
       }
      else if (queue.size() != 0){
          Car newCar = queue.get(0);
          queue.remove(0);
          return newCar;
      }
      return null;
    }
}

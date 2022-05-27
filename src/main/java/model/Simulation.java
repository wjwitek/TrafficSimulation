package main.java.model;

import main.java.gui.Board;
import main.java.gui.Coords;
import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.InputStream;
import java.util.ArrayList;

public class Simulation {
    private final String spawnSource = "/simulation_config.json";
    private final int lightCycleLength = 120; // TODO move to simulation_config.json
    public ArrayList<Car> cars = new ArrayList<>();
    public ArrayList<SpawnPoint> spawnPoints = new ArrayList<>();
    public ArrayList<TrafficLight> trafficLights = new ArrayList<>();
    private Board map;

    public Simulation(Board newMap){
        map = newMap;
        // read spawn points from config
        InputStream spawnStream = Simulation.class.getResourceAsStream(spawnSource);
        if (spawnStream == null){
            throw new NullPointerException("Cannot find resource file " + spawnSource);
        }
        JSONTokener tokenizer = new JSONTokener(spawnStream);
        JSONObject spawns = new JSONObject(tokenizer);
        JSONArray spawnPointsConfigs = spawns.getJSONArray("spawn_points");
        for (int i=0; i<spawnPointsConfigs.length(); i++){
            JSONObject info = (JSONObject) spawnPointsConfigs.get(i);
            spawnPoints.add(new SpawnPoint(info, map));
        }
        // read traffic light from config
        JSONArray trafficLightsConfig = spawns.getJSONArray("traffic_lights");
        for (int i=0; i<trafficLightsConfig.length(); i++){
            JSONObject info = (JSONObject) trafficLightsConfig.get(i);
            trafficLights.add(new TrafficLight(info, map));
        }
    }

    public void iteration(int iteration_num){
        newCars();
        map.repaint();
        // change color lights
        for (TrafficLight light: trafficLights){
            light.changeLight(iteration_num % lightCycleLength);
        }
        // move and delete cars
        ArrayList<Car> carsToDelete = new ArrayList<>();
        for (Car car: cars){
            if (car.move()){
                carsToDelete.add(car);
            }
        }
        for (Car car: carsToDelete){
            cars.remove(car);
        }
    }

    public void newCars(){
        for (SpawnPoint spawn: spawnPoints){
            Car newCar = spawn.getCar();
            if (newCar != null){
                cars.add(newCar);
            }
        }
    }
}

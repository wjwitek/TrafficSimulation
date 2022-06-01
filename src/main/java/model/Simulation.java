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
    public ArrayList<Pedestrian> pedestrians = new ArrayList<>();
    public ArrayList<SpawnPoint> spawnPointsCars = new ArrayList<>();
    public ArrayList<SpawnPoint> spawnPointsPedestrians = new ArrayList<>();
    public ArrayList<TrafficLight> trafficLights = new ArrayList<>();
    public ArrayList<TrafficLight> trafficLightsPedestrian = new ArrayList<>();

    private ArrayList<Coords> destinationPedestrian = new ArrayList<>();
    private Board map;

    public Simulation(Board newMap) {
        map = newMap;
        // read spawn points from config
        InputStream spawnStream = Simulation.class.getResourceAsStream(spawnSource);
        if (spawnStream == null) {
            throw new NullPointerException("Cannot find resource file " + spawnSource);
        }
        JSONTokener tokenizer = new JSONTokener(spawnStream);
        JSONObject spawns = new JSONObject(tokenizer);
        JSONArray spawnPointsCarsConfigs = spawns.getJSONArray("spawn_points_cars");
        for (int i = 0; i < spawnPointsCarsConfigs.length(); i++) {
            JSONObject info = (JSONObject) spawnPointsCarsConfigs.get(i);
            spawnPointsCars.add(new SpawnPoint(info, map));
        }


        JSONArray spawnPointsPedestrianDestinationsConfigs = spawns.getJSONArray("destination_points_pedestrians");
        JSONObject info1 = (JSONObject) spawnPointsPedestrianDestinationsConfigs.get(0);
        JSONArray jsonArray = info1.getJSONArray("coords");
        for (int i = 0; i < jsonArray.length(); i++) {
            JSONArray jsonArray1 = jsonArray.getJSONArray(i);
            Coords coords1 = new Coords(jsonArray1.getInt(0), jsonArray1.getInt(1));
            destinationPedestrian.add(coords1);
        }

        JSONArray spawnPointsPedestriansConfigs = spawns.getJSONArray("spawn_points_pedestrians");
        for (int i = 0; i < spawnPointsPedestriansConfigs.length(); i++) {
            JSONObject info = (JSONObject) spawnPointsPedestriansConfigs.get(i);
            spawnPointsPedestrians.add(new SpawnPoint(info, map, destinationPedestrian));
        }


        // read traffic light from config
        JSONArray trafficLightsConfig = spawns.getJSONArray("traffic_lights");
        for (int i = 0; i < trafficLightsConfig.length(); i++) {
            JSONObject info = (JSONObject) trafficLightsConfig.get(i);
            trafficLights.add(new TrafficLight(info, map));
        }

        JSONArray trafficLightsPedestriansConfig = spawns.getJSONArray("traffic_lights_pedestrians");
        for (int i = 0; i < trafficLightsPedestriansConfig.length(); i++) {
            JSONObject info = (JSONObject) trafficLightsPedestriansConfig.get(i);
            trafficLightsPedestrian.add(new TrafficLight(info, map));
        }
    }

    public void iteration(int iteration_num) {
        newCars();
        newPedestrian();
        map.repaint();
        // change color lights
        for (TrafficLight light : trafficLights) {
            light.changeLight(iteration_num % lightCycleLength);
        }
        for (TrafficLight light : trafficLightsPedestrian) {
            light.changeLight(iteration_num % lightCycleLength);
        }
        // move and delete cars
        ArrayList<Car> carsToDelete = new ArrayList<>();
        for (Car car: cars){
            if (car.iteration()){
                carsToDelete.add(car);
            }
        }
        for (Car car: carsToDelete){
            map.points[car.currentPosition.x][car.currentPosition.y].hasCar = false;
            cars.remove(car);
        }

        ArrayList<Pedestrian> pedestriansToDelete = new ArrayList<>();
        for (Pedestrian pedestrian : pedestrians) {
            if (pedestrian.move()) {
                pedestriansToDelete.add(pedestrian);
            }
        }
        for (Pedestrian pedestrian : pedestriansToDelete) {
            pedestrians.remove(pedestrian);
        }
    }

    public void newCars() {
        for (SpawnPoint spawn : spawnPointsCars) {
            Car newCar = spawn.getCar();
            if (newCar != null) {
                cars.add(newCar);
            }
        }
    }

    public void newPedestrian() {
        for (SpawnPoint spawn : spawnPointsPedestrians) {
            Pedestrian pedestrian = spawn.getPedestrian();
            if (pedestrian != null) {
                pedestrians.add(pedestrian);
            }
        }
    }
}

package main.java.model;

import main.java.gui.Board;
import main.java.gui.Coords;
import main.java.model.spawn.CarSpawnPoint;
import main.java.model.spawn.PedestrianSpawnPoint;
import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.InputStream;
import java.util.ArrayList;

import static main.java.model.spawn.SpawnPoint.getRandomNumber;

public class Simulation {
    private final String spawnSource = "/simulation_config.json";
    private final int lightCycleLength = 120; // TODO move to simulation_config.json
    public ArrayList<Car> cars = new ArrayList<>();
    public ArrayList<Pedestrian> pedestrians = new ArrayList<>();
    public ArrayList<CarSpawnPoint> spawnPointsCars = new ArrayList<>();
    public ArrayList<PedestrianSpawnPoint> spawnPointsPedestrians = new ArrayList<>();
    public ArrayList<TrafficLight> trafficLights = new ArrayList<>();
    public ArrayList<TrafficLight> trafficLightsPedestrian = new ArrayList<>();

    private ArrayList<Coords> pointPedestrian = new ArrayList<>();
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
            spawnPointsCars.add(new CarSpawnPoint(info, map));
        }


        JSONArray spawnPointsPedestriansConfigs = spawns.getJSONArray("pedestrians_points");
        JSONObject info1 = (JSONObject) spawnPointsPedestriansConfigs.get(0);
        JSONArray jsonArray = info1.getJSONArray("coords");
        for (int i = 0; i < jsonArray.length(); i++) {
            JSONArray jsonArray1 = jsonArray.getJSONArray(i);
            Coords coords1 = new Coords(jsonArray1.getInt(0), jsonArray1.getInt(1));
            pointPedestrian.add(coords1);
        }
        int probability = info1.getInt("spawn_probability");
        for (int i = 0; i < jsonArray.length(); i++) {
            spawnPointsPedestrians.add(new PedestrianSpawnPoint(
                    pointPedestrian.get(getRandomNumber(0, pointPedestrian.size())),
                    probability, map, pointPedestrian));
        }

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

        // move and delete pedestrians
        ArrayList<Pedestrian> pedestriansToDelete = new ArrayList<>();
        for (Pedestrian pedestrian : pedestrians) {
            if (pedestrian.move()) {
                pedestriansToDelete.add(pedestrian);
            }
        }
        for (Pedestrian pedestrian : pedestriansToDelete) {
            map.points[pedestrian.currentPosition.x][pedestrian.currentPosition.y].hasPedestrian--;
            pedestrians.remove(pedestrian);
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
    }

    public void newCars() {
        for (CarSpawnPoint spawn : spawnPointsCars) {
            Car newCar = spawn.get();
            if (newCar != null) {
                cars.add(newCar);
            }
        }
    }

    public void newPedestrian() {
        for (PedestrianSpawnPoint spawn : spawnPointsPedestrians) {
            Pedestrian pedestrian = spawn.get();
            if (pedestrian != null) {
                pedestrians.add(pedestrian);
            }
        }
    }
}

package main.java.model;

import main.java.gui.Board;
import main.java.gui.Coords;

import java.util.ArrayList;

public class Simulation {
    public ArrayList<Car> cars = new ArrayList<>();
    private Board map;

    public Simulation(Board newMap){
        map = newMap;
        // starting set of cars
        cars.add(new Car(map, 2, new Coords(0, 1), new Coords(9, 2)));
    }

    public void iteration(){
        System.out.println("Simulation time");
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
}

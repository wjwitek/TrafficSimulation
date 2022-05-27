package main.java.model;

import main.java.gui.Board;
import main.java.gui.Coords;
import main.java.gui.Subsoil;
import org.json.JSONArray;
import org.json.JSONObject;
import java.util.ArrayList;

public class TrafficLight {
    private ArrayList<Subsoil> sequence = new ArrayList<>();
    private Coords coords;
    private Board map;

    public TrafficLight(JSONObject info, Board newMap){
        JSONArray coordsJSON = info.getJSONArray("coords");
        JSONArray sequenceJSON = info.getJSONArray("sequence");
        coords = new Coords(coordsJSON.getInt(0), coordsJSON.getInt(1));
        for (int i=0; i<sequenceJSON.length(); i++){
            for (int j=0; j<5; j++){
                sequence.add(Subsoil.fromInt(sequenceJSON.getInt(i)));
            }
        }
        map = newMap;
    }

    public void changeLight(int iteration){
        map.points[coords.x][coords.y].type = sequence.get(iteration); // / 5, cause our light sequence are changing every 5 seconds, not 1
    }
}

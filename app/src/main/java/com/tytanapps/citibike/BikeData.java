package com.tytanapps.citibike;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * @author Tyler Carberry
 */
public class BikeData {

    long last_updated;
    int ttl;

    Map<String, ArrayList<Station>> data;

    public List<Station> getStationsList() {
        return data.get("stations");
    }

    public Map<Integer, Station> getStationsMap() {

        HashMap<Integer, Station> stations = new HashMap<>();

        List<Station> stationList = getStationsList();
        Iterator<Station> iter = stationList.iterator();
        while (iter.hasNext()) {
            Station stat = iter.next();
            stations.put(stat.station_id, stat);
        }

        return stations;
    }


}

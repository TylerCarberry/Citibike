package com.tytanapps.citibike;

import android.os.AsyncTask;

import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Map;

public class BikeTask extends AsyncTask<Void, Void, BikeData> {

    protected BikeData doInBackground(Void... voids) {
        String data = readUrl("https://gbfs.citibikenyc.com/gbfs/en/station_status.json");
        Gson gson = new Gson();
        BikeData bikeData = gson.fromJson(data, BikeData.class);

        String stationData = readUrl("https://gbfs.citibikenyc.com/gbfs/en/station_information.json");
        BikeData stationBikeData = gson.fromJson(stationData, BikeData.class);


        Map<Integer, Station> stations = bikeData.getStationsMap();
        for(int station_id : stations.keySet()) {
            Station bikeStation = stations.get(station_id);
            Station infoStation = stationBikeData.getStationsMap().get(station_id);

            mergeStations(bikeStation, infoStation);
        }

        return bikeData;
    }

    private static void mergeStations(Station bikeStation, Station infoStation) {
        bikeStation.short_name = infoStation.short_name;
        bikeStation.name = infoStation.name;
        bikeStation.lat = infoStation.lat;
        bikeStation.lon = infoStation.lon;
        bikeStation.region_id = infoStation.region_id;

        bikeStation.capacity = infoStation.capacity;
        bikeStation.rental_methods = infoStation.rental_methods;

    }

    private static String readUrl(String urlString) {
        BufferedReader reader = null;
        try {
            URL url = new URL(urlString);
            reader = new BufferedReader(new InputStreamReader(url.openStream()));
            StringBuffer buffer = new StringBuffer();
            int read;
            char[] chars = new char[1024];
            while ((read = reader.read(chars)) != -1)
                buffer.append(chars, 0, read);

            return buffer.toString();
        } catch (MalformedURLException e) {

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (reader != null)
                try {
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
        }

        return "";
    }


}

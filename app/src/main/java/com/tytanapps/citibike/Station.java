package com.tytanapps.citibike;

import java.util.List;

/**
 * @author Tyler Carberry
 */
public class Station {

    int station_id;
    String name;
    String short_name;
    double lat;
    double lon;
    int region_id;
    List<String> rental_methods;
    int capacity;
    boolean eightd_has_key_dispenser;

    int num_bikes_available;
    int num_bikes_disabled;
    int num_docks_available;
    int num_docks_disabled;



}

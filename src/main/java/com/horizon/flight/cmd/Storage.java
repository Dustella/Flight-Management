package com.horizon.flight.cmd;

import java.util.HashMap;
import java.util.Map;

import com.horizon.flight.entities.AddOnService;
import com.horizon.flight.entities.AirlineCompany;
import com.horizon.flight.entities.Passenger;

public class Storage {

    private static Storage instance;
    private final Map<String, AirlineCompany> airlineCompany;
    private final Map<String, Passenger> passengers;
    private final Map<String, AddOnService> addons;

    private Storage() {
        airlineCompany = new HashMap<>();
        passengers = new HashMap<>();
        addons = new HashMap<>();
    }

    public static synchronized Storage getInstance() {
        if (instance == null) {
            instance = new Storage();
        }
        return instance;
    }

    public void addAirlineCompany(String id, AirlineCompany AirlineCompany) {
        airlineCompany.put(id, AirlineCompany);
    }

    public Map<String, AirlineCompany> getAllAirlineCompanies() {
        return airlineCompany;
    }

    public AirlineCompany getAirlineCompany(String id) {
        return airlineCompany.get(id);
    }

    public void addPassenger(String id, Passenger passenger) {
        passengers.put(id, passenger);
    }

    public Passenger getPassenger(String id) {
        return passengers.get(id);
    }

    public void addAddon(String id, AddOnService addon) {
        addons.put(id, addon);
    }

    public AddOnService getAddon(String id) {
        return addons.get(id);
    }
}

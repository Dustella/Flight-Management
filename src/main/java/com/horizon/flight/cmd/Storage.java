package com.horizon.flight.cmd;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

import com.horizon.flight.entities.AddOnService;
import com.horizon.flight.entities.AirlineCompany;
import com.horizon.flight.entities.Flight;
import com.horizon.flight.entities.Passenger;

public class Storage implements Serializable {
    private static Storage instance;
    private boolean hasInitBefore = false;
    private Map<String, AirlineCompany> airlineCompany;
    private Map<String, Passenger> passengers;
    private Map<String, AddOnService> addons; // 使用AddOnService

    public Storage() {
        if (hasInitBefore) {
            return;
        }

        airlineCompany = new HashMap<>();
        passengers = new HashMap<>();
        addons = new HashMap<>(); // 初始化addons Map

        // Adding dummy airline companies
        AirlineCompany company1 = new AirlineCompany("Horizon Airlines");
        AirlineCompany company2 = new AirlineCompany("Sky High Airlines");
        airlineCompany.put("HA", company1);
        airlineCompany.put("SHA", company2);

        // Adding dummy passengers
        Passenger passenger1 = new Passenger("John Doe");
        Passenger passenger2 = new Passenger("Jane Smith");
        passengers.put("P1", passenger1);
        passengers.put("P2", passenger2);

        // Adding dummy add-on services
        AddOnService addon1 = new AddOnService("Extra Baggage", 50.0);
        AddOnService addon2 = new AddOnService("Priority Boarding", 30.0);
        addons.put("A1", addon1);
        addons.put("A2", addon2);

        // Adding dummy flights
        Flight flight1 = new Flight("HA123", LocalDateTime.of(2025, 10, 10, 10, 0),
                LocalDateTime.of(2025, 10, 10, 14, 0), "HK", "SHH", 150);
        Flight flight2 = new Flight("SHA456", LocalDateTime.of(2025, 11, 15, 16, 0),
                LocalDateTime.of(2025, 11, 15, 20, 0), "SHH", "HK", 200);

        company1.addFlight(flight1);
        company2.addFlight(flight2);

        hasInitBefore = true;
    }

    public static synchronized Storage getInstance() {
        if (instance == null) {
            instance = new Storage();
        }
        return instance;
    }

    public static void setInstance(Storage storage) {
        instance = storage;
    }

    public static void readStorageFromFile(String path)
            throws IOException, ClassNotFoundException {
        // Read storage from file using path

        try (ObjectInputStream in = new ObjectInputStream(new FileInputStream(path))) {
            instance = (Storage) in.readObject();
        } catch (IOException | ClassNotFoundException e) {
            throw e;
        }

    }

    public static void dumpStorageToFile(String path) throws IOException {
        // Write storage to file using path
        try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(path))) {
            out.writeObject(instance);
        } catch (IOException e) {
            throw e;
        }
    }

    public Flight getFlight(String flightNumber) {
        for (AirlineCompany company : airlineCompany.values()) {
            Flight flight = company.findFlightByNumber(flightNumber);
            if (flight != null) {
                return flight;
            }
        }
        return null;
    }

    public void addAirlineCompany(String id, AirlineCompany airlineCompany) {
        this.airlineCompany.put(id, airlineCompany);
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

    public Map<String, Passenger> getPassengers() {
        return passengers;
    }

    public void addAddon(String id, AddOnService addon) {
        addons.put(id, addon);
    }

    public AddOnService getAddon(String id) {
        return addons.get(id);
    }
}
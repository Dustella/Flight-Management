package com.horizon.flight.entities;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class Flight implements Serializable {
    private final String flightNumber;
    private LocalDateTime departureTime;
    private LocalDateTime arrivalTime;
    private final String departureAirport;
    private final String arrivalAirport;
    private final int capacity;
    private final List<Passenger> passengers;
    private boolean isOpenForReservation;
    private final List<AddOnService> services; // 用于存储航班提供的增值服务

    public Flight(String flightNumber, LocalDateTime departureTime, LocalDateTime arrivalTime, String departureAirport,
            String arrivalAirport, int capacity) {
        this.flightNumber = flightNumber;
        this.departureTime = departureTime;
        this.arrivalTime = arrivalTime;
        this.departureAirport = departureAirport;
        this.arrivalAirport = arrivalAirport;
        this.capacity = capacity;
        this.passengers = new ArrayList<>();
        this.isOpenForReservation = true;
        this.services = new ArrayList<>(); // 初始化为空列表
    }

    // 获取航班号
    public String getFlightNumber() {
        return flightNumber;
    }

    // 获取出发时间
    public LocalDateTime getDepartureTime() {
        return departureTime;
    }

    // 获取到达时间
    public LocalDateTime getArrivalTime() {
        return arrivalTime;
    }

    // 获取出发机场
    public String getDepartureAirport() {
        return departureAirport;
    }

    public String getArrivalAirport() {
        return arrivalAirport;
    }

    public int getCapacity() {
        return capacity;
    }

    public List<Passenger> getPassengers() {
        return passengers;
    }

    // 获取航班是否开放预订
    public boolean isOpenForReservation() {
        return isOpenForReservation;
    }

    // Set the open for reservation status
    public void setOpenForReservation(boolean openForReservation) {
        isOpenForReservation = openForReservation;
    }

    // Add a passenger to the flight, if the flight is full or not open for
    // reservation, throw an exception
    public boolean addPassenger(Passenger p) {
        if (isFull() || !isOpenForReservation) {
            throw new IllegalStateException("main.Flight is full or not open for reservation.");
        }

        passengers.add(p);
        return true;

    }

    // Remove a passenger from the flight
    public boolean removePassenger(Passenger p) {
        return passengers.remove(p);
    }

    // 取消机票
    public boolean cancelTicket(Passenger passenger) {
        return passengers.remove(passenger);
    }

    // 订票
    public boolean bookTicket(Passenger passenger) {
        if (passengers.size() < capacity && !passengers.contains(passenger)) {
            passengers.add(passenger);
            return true;
        }
        return false;
    }

    // 预订增值服务
    public boolean bookService(AddOnService service) {
        if (services.contains(service)) {
            return false; // 服务已经被预订
        }
        services.add(service);
        return true;
    }

    // 判断航班是否已满员
    public boolean isFull() {
        return passengers.size() == capacity;
    }

    // Check if the flight has a time conflict with another flight
    public boolean hasTimeConflict(LocalDateTime otherDepartureTime, LocalDateTime otherArrivalTime) {
        return (otherDepartureTime.isBefore(this.arrivalTime) && otherArrivalTime.isAfter(this.departureTime))
                || (otherDepartureTime.isEqual(this.departureTime) || otherArrivalTime.isEqual(this.arrivalTime));
    }

    // Get the number of available seats on the flight
    public int getAvailableSeats() {
        return capacity - passengers.size();
    }

    public boolean checkAvailability() {
        return getAvailableSeats() > 0;
    }

    public void setDepartureTime(LocalDateTime newDepartureTime) {
        // departure time should not be in the past, and arrival time should be after
        // departure time
        if (newDepartureTime.isBefore(LocalDateTime.now()) || newDepartureTime.isAfter(arrivalTime)) {
            throw new IllegalArgumentException("Invalid departure time.");
        }

        this.departureTime = newDepartureTime;
    }

    public void setArrivalTime(LocalDateTime newArrivalTime) {
        // arrival time should be after departure time, and not in the past
        if (newArrivalTime.isBefore(departureTime) || newArrivalTime.isBefore(LocalDateTime.now())) {
            throw new IllegalArgumentException("Invalid arrival time.");
        }

        this.arrivalTime = newArrivalTime;
    }

    @Override
    public String toString() {
        return "Flight{" +
                "flightNumber='" + flightNumber + '\'' +
                ", departureTime=" + departureTime +
                ", arrivalTime=" + arrivalTime +
                ", departureAirport='" + departureAirport + '\'' +
                ", arrivalAirport='" + arrivalAirport + '\'' +
                ", capacity=" + capacity +
                ", passengers=" + passengers.size() +
                ", isOpenForReservation=" + isOpenForReservation +
                ", services=" + services +
                '}';
    }
}
package com.horizon.flight;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class Flight {
    private String flightNumber;
    private LocalDateTime departureTime;
    private LocalDateTime arrivalTime;
    private String departureAirport;
    private String arrivalAirport;
    private int capacity;
    private List<Passenger> passengers;
    private boolean isOpenForReservation;

    public Flight(String flightNumber, LocalDateTime departureTime, LocalDateTime arrivalTime, String departureAirport, String arrivalAirport, int capacity) {
        this.flightNumber = flightNumber;
        this.departureTime = departureTime;
        this.arrivalTime = arrivalTime;
        this.departureAirport = departureAirport;
        this.arrivalAirport = arrivalAirport;
        this.capacity = capacity;
        this.passengers = new ArrayList<>();
        this.isOpenForReservation = true;
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

    // 获取到达机场
    public String getArrivalAirport() {
        return arrivalAirport;
    }

    // 获取航班容量
    public int getCapacity() {
        return capacity;
    }

    // 获取已预订该航班的乘客列表
    public List<Passenger> getPassengers() {
        return passengers;
    }

    // 判断航班是否开放预订
    public boolean isOpenForReservation() {
        return isOpenForReservation;
    }

    // 设置航班是否开放预订
    public void setOpenForReservation(boolean openForReservation) {
        isOpenForReservation = openForReservation;
    }

    // 添加乘客到航班（检查容量和预订状态）
    public boolean addPassenger(Passenger p) {
        if (isFull() || !isOpenForReservation) {
            throw new IllegalStateException("main.Flight is full or not open for reservation.");
        }

        passengers.add(p);
        return true;

    }

    // 从航班移除乘客
    public boolean removePassenger(Passenger p) {
        return passengers.remove(p);
    }

    // 判断航班是否已满员
    public boolean isFull() {
        return passengers.size() == capacity;
    }

    // 检查给定时间范围是否与本航班时间冲突
    public boolean hasTimeConflict(LocalDateTime otherDepartureTime, LocalDateTime otherArrivalTime) {
        return (otherDepartureTime.isBefore(this.arrivalTime) && otherArrivalTime.isAfter(this.departureTime))
                || (otherDepartureTime.isEqual(this.departureTime) || otherArrivalTime.isEqual(this.arrivalTime));
    }

    // 获取航班剩余可预订座位数
    public int getAvailableSeats() {
        return capacity - passengers.size();
    }

    public boolean checkAvailability() {
        return getAvailableSeats() > 0;
    }

    // 设置出发时间的方法
    public void setDepartureTime(LocalDateTime newDepartureTime) {
//      departure time should not be in the past, and arrival time should be after departure time
        if (newDepartureTime.isBefore(LocalDateTime.now()) || newDepartureTime.isAfter(arrivalTime)) {
            throw new IllegalArgumentException("Invalid departure time.");
        }

        this.departureTime = newDepartureTime;
    }

    // 设置到达时间的方法
    public void setArrivalTime(LocalDateTime newArrivalTime) {
//        arrival time should be after departure time, and not in the past
        if (newArrivalTime.isBefore(departureTime) || newArrivalTime.isBefore(LocalDateTime.now())) {
            throw new IllegalArgumentException("Invalid arrival time.");
        }

        this.arrivalTime = newArrivalTime;
    }
}
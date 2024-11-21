package com.horizon.flight;


import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class FlightReservationSystemTest {
    public static void main(String[] args) {
        // 创建航空公司
        AirlineCompany airline = new AirlineCompany("China Eastern Airlines");

        // 创建航班
        LocalDateTime departureTime = LocalDateTime.now().plusHours(2);
        LocalDateTime arrivalTime = departureTime.plusHours(3);
        Flight flight1 = new Flight("MU123", departureTime, arrivalTime, "Shanghai", "Beijing", 100);
        airline.addFlight(flight1);

        // 创建乘客
        Passenger passenger1 = new Passenger("John Doe");
        Passenger passenger2 = new Passenger("Jane Smith");

        // 创建可选附加服务列表
        List<AddOnService> addOnServices = new ArrayList<>();
        addOnServices.add(new AddOnService("Speedy Boarding", 10));
        addOnServices.add(new AddOnService("Seat Selection", 20));

        // 乘客进行预订，确保传入有效的Flight对象
        TicketCategory category = TicketCategory.ECONOMY;
        if (flight1!= null) {
            passenger1.makeReservation(flight1, category, addOnServices);
        }

        // 航空公司延迟航班
        airline.delayFlight(flight1, 30);

        // 尝试修改预订（演示错误情况）
        Flight newFlight = new Flight("MU456", LocalDateTime.now().plusHours(1), LocalDateTime.now().plusHours(4), "Shanghai", "Guangzhou", 100);
        TicketCategory newCategory = TicketCategory.BUSINESS_CLASS;
        List<AddOnService> newAddOnServices = new ArrayList<>();
        if (passenger1.getReservations().size() > 0) {
            Reservation reservationToModify = passenger1.getReservations().get(0);
            boolean modifyResult = passenger1.modifyReservation(reservationToModify, newFlight, newCategory, newAddOnServices, 50);
            if (!modifyResult) {
                System.out.println("Failed to modify reservation.");
            }
        }

        // 正确修改预订（演示正常情况）
        newFlight = new Flight("MU123", flight1.getDepartureTime().plusHours(1), flight1.getArrivalTime().plusHours(1), "Shanghai", "Beijing", 100);
        if (passenger1.getReservations().size() > 0) {
            Reservation reservationToModify = passenger1.getReservations().get(0);
            boolean modifyResult = passenger1.modifyReservation(reservationToModify, newFlight, newCategory, newAddOnServices, 50);
            if (modifyResult) {
                System.out.println("main.Reservation modified successfully.");
            }
        }

        // 航空公司检查接近满员的航班（假设阈值为80%）
        List<Flight> nearFullFlights = airline.checkCapacityUtilization(80);
        for (Flight f : nearFullFlights) {
            System.out.println("main.Flight " + f.getFlightNumber() + " from " + f.getDepartureAirport() + " to " + f.getArrivalAirport() + " is near full.");
        }

        // 航空公司取消航班
        airline.cancelFlight(flight1);

        // 尝试取消不存在的预订（演示错误情况），创建虚拟Flight对象来构造Reservation
        Flight dummyFlight = new Flight("DummyFlightNumber", LocalDateTime.now(), LocalDateTime.now().plusHours(1), "DummyDeparture", "DummyArrival", 1);
        Reservation nonExistentReservation = new Reservation(dummyFlight, category, addOnServices);
        boolean cancelResult = passenger1.cancelReservation(nonExistentReservation, 10);
        if (!cancelResult) {
            System.out.println("Failed to cancel non-existent reservation.");
        }

        // 乘客加入忠诚度计划
        passenger1.joinLoyaltyScheme();
    }
}
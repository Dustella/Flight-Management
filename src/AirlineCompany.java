import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class AirlineCompany {
    private String name;
    private List<Flight> flights;

    public AirlineCompany(String name) {
        this.name = name;
        this.flights = new ArrayList<>();
    }

    // 获取航空公司名称
    public String getName() {
        return name;
    }

    // 获取航空公司运营的所有航班列表
    public List<Flight> getFlights() {
        return flights;
    }

    // 添加航班到航空公司的运营列表
    public void addFlight(Flight f) {
        if (f == null) {
            throw new IllegalArgumentException("Flight cannot be null when adding to AirlineCompany.");
        }
        flights.add(f);
    }

    // 从航空公司运营列表取消航班（同时移除所有相关乘客预订）
    public void cancelFlight(Flight f) {
        if (f == null) {
            throw new IllegalArgumentException("Flight cannot be null when cancelling from AirlineCompany.");
        }

        for (Passenger passenger : f.getPassengers()) {
            passenger.cancelReservationByFlight(f);
        }
        flights.remove(f);
    }

    // 延迟航班（更新出发和到达时间，并通知乘客）
    public void delayFlight(Flight f, int minutes) {
        LocalDateTime newDepartureTime = f.getDepartureTime().plusMinutes(minutes);
        LocalDateTime newArrivalTime = f.getArrivalTime().plusMinutes(minutes);
        f.setDepartureTime(newDepartureTime);
        f.setArrivalTime(newArrivalTime);

        // 通知乘客航班延迟，这里简单打印消息，实际可替换为合适的通知机制
        for (Passenger passenger : f.getPassengers()) {
            System.out.println("Dear " + passenger.getName() + ", your flight " + f.getFlightNumber() + " from " + f.getDepartureAirport() + " to " + f.getArrivalAirport() + " has been delayed by " + minutes + " minutes.");
        }
    }

    // 根据给定的容量利用率阈值，查找接近满员的航班列表
    public List<Flight> checkCapacityUtilization(int thresholdPercentage) {
//        do checks for input percentage
        if (thresholdPercentage < 0 || thresholdPercentage > 100) {
            throw new IllegalArgumentException("Invalid threshold percentage.");
        }

        List<Flight> nearFullFlights = new ArrayList<>();

        for (Flight flight : flights) {
            double utilization = (double) flight.getPassengers().size() / flight.getCapacity();
            if (utilization >= (thresholdPercentage / 100.0)) {
                nearFullFlights.add(flight);
            }
        }
        return nearFullFlights;
    }

    // 根据航班号查找航班
    public Flight findFlightByNumber(String flightNumber) {
        for (Flight flight : flights) {
            if (flight.getFlightNumber().equals(flightNumber)) {
                return flight;
            }
        }
        return null;
    }
}
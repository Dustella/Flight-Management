import java.util.List;

public class Reservation {
    private Flight flight;
    private TicketCategory ticketCategory;
    private List<AddOnService> addOnServices;

    public Reservation(Flight flight, TicketCategory ticketCategory, List<AddOnService> addOnServices) {
        if (flight == null) {
            throw new IllegalArgumentException("Flight cannot be null when creating a Reservation.");
        }
        this.flight = flight;
        this.ticketCategory = ticketCategory;
        this.addOnServices = addOnServices;
    }

    // 获取预订对应的航班
    public Flight getFlight() {
        return flight;
    }

    // 获取预订的机票类别
    public TicketCategory getTicketCategory() {
        return ticketCategory;
    }

    // 获取预订的附加服务列表
    public List<AddOnService> getAddOns() {
        return addOnServices;
    }

    // 计算预订的总费用（机票基础价格 + 附加服务价格）
    public double calculateTotalCost() {
        double basePrice = ticketCategory.getBasePrice();
        double addOnCost = 0;
        for (AddOnService addOnService : addOnServices) {
            addOnCost += addOnService.getPrice();
        }
        return basePrice + addOnCost;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass()!= o.getClass()) return false;
        Reservation that = (Reservation) o;
        return flight.equals(that.flight) &&
                ticketCategory == that.ticketCategory &&
                addOnServices.equals(that.addOnServices);
    }

    @Override
    public int hashCode() {
        int result = flight.hashCode();
        result = 31 * result + ticketCategory.hashCode();
        result = 31 * result + addOnServices.hashCode();
        return result;
    }
}
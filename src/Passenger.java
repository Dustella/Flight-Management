import java.util.ArrayList;
import java.util.List;

public class Passenger {
//
    public  static Integer passengerCount = 0;
    private String name;
    private List<Reservation> reservations;
    private  Integer ID;
    private boolean isLoyaltyMember;

    public Passenger(String name) {
        this.name = name;
        passengerCount+=1;
        this.ID = passengerCount;
        this.reservations = new ArrayList<>();
        this.isLoyaltyMember = false;
    }

    // 获取乘客姓名
    public String getName() {
        return name;
    }

    // 获取乘客的所有预订列表
    public List<Reservation> getReservations() {
        return reservations;
    }

    // 判断乘客是否是忠诚度会员
    public boolean isLoyaltyMember() {
        return isLoyaltyMember;
    }

    // 乘客加入忠诚度计划
    public void joinLoyaltyScheme() {
        isLoyaltyMember = true;
    }

    // 乘客预订航班（检查航班是否可预订、时间冲突等）
    public boolean makeReservation(Flight f, TicketCategory category, List<AddOnService> addOnServices) {
        if (!f.isOpenForReservation()) {
            System.out.println("Flight is not open for reservation.");
            return false;
        }
        for (Reservation reservation : reservations) {
            if (f.hasTimeConflict(reservation.getFlight().getDepartureTime(), reservation.getFlight().getArrivalTime())) {
                System.out.println("Time conflict with existing reservation.");
                return false;
            }
        }
        Reservation newReservation = new Reservation(f, category, addOnServices); // 确保这里传入的 f 不为 null
        reservations.add(newReservation);
        return f.addPassenger(this);
    }

    // 乘客取消指定的预订（处理相关逻辑，如从航班移除等）
    public boolean cancelReservation(Reservation r, double fee) {
        if (reservations.remove(r)) {
            r.getFlight().removePassenger(this);
            // TODO: we can add logic to deduct fee from account balance, but we will skip it for now
            return true;
        }
        return false;
    }

    // 根据航班对象取消对应的所有预订（用于航班取消时调用）
    public void cancelReservationByFlight(Flight f) {
        List<Reservation> toRemove = new ArrayList<>();
        for (Reservation reservation : reservations) {
            if (reservation.getFlight().equals(f)) {
                toRemove.add(reservation);
            }
        }
        for (Reservation reservation : toRemove) {
            reservations.remove(reservation);
        }
    }

//    modify current reservation, remove passenger from current flight, add passenger to new flight
    public boolean modifyReservation(Reservation r, Flight newFlight, TicketCategory newCategory, List<AddOnService> newAddOnServices, double fee) {
        int index = reservations.indexOf(r);
        if (index == -1) {
            System.out.println("Reservation not found.");
            return false;
        }

        // 先从原航班移除乘客
        r.getFlight().removePassenger(this);

        // 创建新的 Reservation 对象，确保属性都正确设置
        Reservation updatedReservation = new Reservation(newFlight, newCategory, newAddOnServices);

        // 替换原预订列表中的元素，这里使用正确获取到的索引
        reservations.set(index, updatedReservation);

        // 将乘客添加到新航班（这里添加了一些额外的逻辑判断，如果添加失败可以返回 false 并进行相应处理）
        if (!newFlight.addPassenger(this)) {
            System.out.println("Failed to add passenger to the new flight.");
            return false;
        }

        return true;
    }

    public Integer getID() {
        return ID;
    }

}
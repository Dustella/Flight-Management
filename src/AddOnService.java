public class AddOnService {
    private String name;
    private double price;

    public AddOnService(String name, double price) {
        this.name = name;
        this.price = price;
    }

    // 获取附加服务名称
    public String getName() {
        return name;
    }

    // 获取附加服务价格
    public double getPrice() {
        return price;
    }

    // 获取附加服务的描述信息
    public String getDescription() {
        return "Add-on: " + name + ", Price: " + price;
    }
}
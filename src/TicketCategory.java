public enum TicketCategory {
    ECONOMY(100),
    PREMIUM_ECONOMY(200),
    BUSINESS_CLASS(300),
    FIRST_CLASS(500);

    private double basePrice;

    TicketCategory(double basePrice) {
        this.basePrice = basePrice;
    }

    // 获取机票类别的基础价格
    public double getBasePrice() {
        return basePrice;
    }
}
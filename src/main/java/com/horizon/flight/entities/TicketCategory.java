package com.horizon.flight.entities;

public enum TicketCategory {
    ECONOMY(100),
    PREMIUM_ECONOMY(200),
    BUSINESS_CLASS(300),
    FIRST_CLASS(500);

    private final double basePrice;

    TicketCategory(double basePrice) {
        this.basePrice = basePrice;
    }

    // Get Base Price of the Ticket Category
    // Base price is the price of the ticket without any add-on services
    public double getBasePrice() {
        return basePrice;
    }
}
public enum RoomCategory {
    STANDARD(1500.0),
    DELUXE(2800.0),
    SUITE(5000.0);

    private final double pricePerNight;

    RoomCategory(double pricePerNight) {
        this.pricePerNight = pricePerNight;
    }

    public double getPricePerNight() {
        return pricePerNight;
    }
}
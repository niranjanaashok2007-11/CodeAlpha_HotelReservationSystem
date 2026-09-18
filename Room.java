public class Room {
    private int roomNumber;
    private RoomCategory category;
    private boolean available;

    public Room(int roomNumber, RoomCategory category) {
        this.roomNumber = roomNumber;
        this.category = category;
        this.available = true;
    }

    public int getRoomNumber() {
        return roomNumber;
    }

    public RoomCategory getCategory() {
        return category;
    }

    public boolean isAvailable() {
        return available;
    }

    public void setAvailable(boolean available) {
        this.available = available;
    }

    @Override
    public String toString() {
        return String.format("Room %-4d | %-8s | Rs.%-8.2f/night | %s",
                roomNumber, category, category.getPricePerNight(),
                available ? "Available" : "Booked");
    }
}
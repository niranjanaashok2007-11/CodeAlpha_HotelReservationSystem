public class Reservation {
    private int reservationId;
    private String guestName;
    private Room room;
    private int nights;
    private double totalAmount;
    private boolean paid;

    public Reservation(int reservationId, String guestName, Room room, int nights) {
        this.reservationId = reservationId;
        this.guestName = guestName;
        this.room = room;
        this.nights = nights;
        this.totalAmount = room.getCategory().getPricePerNight() * nights;
        this.paid = false;
    }

    public int getReservationId() {
        return reservationId;
    }

    public String getGuestName() {
        return guestName;
    }

    public Room getRoom() {
        return room;
    }

    public int getNights() {
        return nights;
    }

    public double getTotalAmount() {
        return totalAmount;
    }

    public boolean isPaid() {
        return paid;
    }

    public void markAsPaid() {
        this.paid = true;
    }

    public String getBookingDetails() {
        return String.format(
            "Reservation #%d%n" +
            "  Guest       : %s%n" +
            "  Room        : %d (%s)%n" +
            "  Nights      : %d%n" +
            "  Total       : Rs.%.2f%n" +
            "  Payment     : %s",
            reservationId, guestName, room.getRoomNumber(), room.getCategory(),
            nights, totalAmount, paid ? "PAID" : "PENDING"
        );
    }

    // Format used for saving to file: id,guestName,roomNumber,nights,totalAmount,paid
    public String toFileFormat() {
        return reservationId + "," + guestName + "," + room.getRoomNumber() + ","
                + nights + "," + totalAmount + "," + paid;
    }
}
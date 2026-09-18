import java.util.List;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        Hotel hotel = new Hotel();
        boolean running = true;

        while (running) {
            System.out.println("\n===== Hotel Reservation System =====");
            System.out.println("1. View All Rooms");
            System.out.println("2. Search Available Rooms by Category");
            System.out.println("3. Book a Room");
            System.out.println("4. Cancel a Reservation");
            System.out.println("5. Make Payment for a Reservation");
            System.out.println("6. View Booking Details");
            System.out.println("7. View All Reservations");
            System.out.println("8. Exit");
            System.out.print("Choose an option: ");

            int choice;
            try {
                choice = Integer.parseInt(sc.nextLine().trim());
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Enter a number.");
                continue;
            }

            switch (choice) {
                case 1:
                    viewAllRooms(hotel);
                    break;

                case 2:
                    searchRooms(sc, hotel);
                    break;

                case 3:
                    bookRoom(sc, hotel);
                    break;

                case 4:
                    System.out.print("Enter reservation ID to cancel: ");
                    try {
                        int cancelId = Integer.parseInt(sc.nextLine().trim());
                        if (hotel.cancelReservation(cancelId)) {
                            System.out.println("Reservation cancelled successfully.");
                        } else {
                            System.out.println("Reservation not found.");
                        }
                    } catch (NumberFormatException e) {
                        System.out.println("Invalid ID.");
                    }
                    break;

                case 5:
                    System.out.print("Enter reservation ID to pay for: ");
                    try {
                        int payId = Integer.parseInt(sc.nextLine().trim());
                        if (hotel.processPayment(payId)) {
                            System.out.println("Payment successful.");
                        } else {
                            System.out.println("Reservation not found or already paid.");
                        }
                    } catch (NumberFormatException e) {
                        System.out.println("Invalid ID.");
                    }
                    break;

                case 6:
                    System.out.print("Enter reservation ID: ");
                    try {
                        int viewId = Integer.parseInt(sc.nextLine().trim());
                        var reservation = hotel.findReservation(viewId);
                        if (reservation != null) {
                            System.out.println("\n" + reservation.getBookingDetails());
                        } else {
                            System.out.println("Reservation not found.");
                        }
                    } catch (NumberFormatException e) {
                        System.out.println("Invalid ID.");
                    }
                    break;

                case 7:
                    viewAllReservations(hotel);
                    break;

                case 8:
                    running = false;
                    System.out.println("Exiting. Goodbye!");
                    break;

                default:
                    System.out.println("Invalid option. Choose 1-8.");
            }
        }
        sc.close();
    }

    private static void viewAllRooms(Hotel hotel) {
        System.out.println("\n----- All Rooms -----");
        for (var room : hotel.getAllRooms()) {
            System.out.println(room);
        }
    }

    private static void searchRooms(Scanner sc, Hotel hotel) {
        System.out.println("Category: 1. Standard  2. Deluxe  3. Suite  4. Any");
        System.out.print("Choose category: ");
        RoomCategory category = null;
        String catChoice = sc.nextLine().trim();
        switch (catChoice) {
            case "1": category = RoomCategory.STANDARD; break;
            case "2": category = RoomCategory.DELUXE; break;
            case "3": category = RoomCategory.SUITE; break;
            case "4": category = null; break;
            default:
                System.out.println("Invalid choice, showing all.");
        }
        List<Room> available = hotel.searchAvailableRooms(category);
        if (available.isEmpty()) {
            System.out.println("No available rooms in this category.");
        } else {
            System.out.println("\n----- Available Rooms -----");
            for (Room r : available) {
                System.out.println(r);
            }
        }
    }

    private static void bookRoom(Scanner sc, Hotel hotel) {
        System.out.print("Enter guest name: ");
        String name = sc.nextLine().trim();
        System.out.print("Enter room number: ");
        int roomNumber;
        try {
            roomNumber = Integer.parseInt(sc.nextLine().trim());
        } catch (NumberFormatException e) {
            System.out.println("Invalid room number.");
            return;
        }
        System.out.print("Enter number of nights: ");
        int nights;
        try {
            nights = Integer.parseInt(sc.nextLine().trim());
            if (nights <= 0) {
                System.out.println("Nights must be positive.");
                return;
            }
        } catch (NumberFormatException e) {
            System.out.println("Invalid number of nights.");
            return;
        }

        Reservation reservation = hotel.bookRoom(name, roomNumber, nights);
        if (reservation == null) {
            System.out.println("Room not available or does not exist.");
        } else {
            System.out.println("\nBooking confirmed!");
            System.out.println(reservation.getBookingDetails());
            System.out.println("(Use option 5 to simulate payment for this reservation.)");
        }
    }

    private static void viewAllReservations(Hotel hotel) {
        List<Reservation> reservations = hotel.getAllReservations();
        if (reservations.isEmpty()) {
            System.out.println("No reservations yet.");
            return;
        }
        System.out.println("\n----- All Reservations -----");
        for (Reservation r : reservations) {
            System.out.println(r.getBookingDetails());
            System.out.println("---");
        }
    }
}
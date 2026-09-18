import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class Hotel {
    private List<Room> rooms;
    private List<Reservation> reservations;
    private int nextReservationId;

    private static final String ROOMS_FILE = "rooms.txt";
    private static final String RESERVATIONS_FILE = "reservations.txt";

    public Hotel() {
        rooms = new ArrayList<>();
        reservations = new ArrayList<>();
        nextReservationId = 1;
        loadRooms();
        loadReservations();
    }

    // ---------- Setup ----------

    private void initializeDefaultRooms() {
        int roomNum = 101;
        for (int i = 0; i < 3; i++) rooms.add(new Room(roomNum++, RoomCategory.STANDARD));
        roomNum = 201;
        for (int i = 0; i < 3; i++) rooms.add(new Room(roomNum++, RoomCategory.DELUXE));
        roomNum = 301;
        for (int i = 0; i < 2; i++) rooms.add(new Room(roomNum++, RoomCategory.SUITE));
    }

    // ---------- Search & Booking ----------

    public List<Room> searchAvailableRooms(RoomCategory category) {
        List<Room> result = new ArrayList<>();
        for (Room r : rooms) {
            if (r.isAvailable() && (category == null || r.getCategory() == category)) {
                result.add(r);
            }
        }
        return result;
    }

    public Reservation bookRoom(String guestName, int roomNumber, int nights) {
        Room room = findRoom(roomNumber);
        if (room == null || !room.isAvailable()) {
            return null;
        }
        room.setAvailable(false);
        Reservation reservation = new Reservation(nextReservationId++, guestName, room, nights);
        reservations.add(reservation);
        saveRooms();
        saveReservations();
        return reservation;
    }

    public boolean cancelReservation(int reservationId) {
        Reservation toRemove = null;
        for (Reservation r : reservations) {
            if (r.getReservationId() == reservationId) {
                toRemove = r;
                break;
            }
        }
        if (toRemove == null) return false;
        toRemove.getRoom().setAvailable(true);
        reservations.remove(toRemove);
        saveRooms();
        saveReservations();
        return true;
    }

    public boolean processPayment(int reservationId) {
        Reservation r = findReservation(reservationId);
        if (r == null || r.isPaid()) return false;
        r.markAsPaid();
        saveReservations();
        return true;
    }

    public Reservation findReservation(int reservationId) {
        for (Reservation r : reservations) {
            if (r.getReservationId() == reservationId) return r;
        }
        return null;
    }

    private Room findRoom(int roomNumber) {
        for (Room r : rooms) {
            if (r.getRoomNumber() == roomNumber) return r;
        }
        return null;
    }

    public List<Room> getAllRooms() {
        return rooms;
    }

    public List<Reservation> getAllReservations() {
        return reservations;
    }

    // ---------- File I/O ----------

    private void loadRooms() {
        File file = new File(ROOMS_FILE);
        if (!file.exists()) {
            initializeDefaultRooms();
            saveRooms();
            return;
        }
        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = br.readLine()) != null) {
                if (line.isBlank()) continue;
                String[] parts = line.split(",");
                int roomNumber = Integer.parseInt(parts[0]);
                RoomCategory category = RoomCategory.valueOf(parts[1]);
                boolean available = Boolean.parseBoolean(parts[2]);
                Room room = new Room(roomNumber, category);
                room.setAvailable(available);
                rooms.add(room);
            }
        } catch (IOException e) {
            System.out.println("Could not load rooms file, initializing defaults.");
            rooms.clear();
            initializeDefaultRooms();
        }
    }

    private void saveRooms() {
        try (PrintWriter pw = new PrintWriter(new FileWriter(ROOMS_FILE))) {
            for (Room r : rooms) {
                pw.println(r.getRoomNumber() + "," + r.getCategory() + "," + r.isAvailable());
            }
        } catch (IOException e) {
            System.out.println("Error saving rooms: " + e.getMessage());
        }
    }

    private void loadReservations() {
        File file = new File(RESERVATIONS_FILE);
        if (!file.exists()) return;
        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            int maxId = 0;
            while ((line = br.readLine()) != null) {
                if (line.isBlank()) continue;
                String[] parts = line.split(",");
                int id = Integer.parseInt(parts[0]);
                String guestName = parts[1];
                int roomNumber = Integer.parseInt(parts[2]);
                int nights = Integer.parseInt(parts[3]);
                boolean paid = Boolean.parseBoolean(parts[5]);

                Room room = findRoom(roomNumber);
                if (room == null) continue;
                Reservation res = new Reservation(id, guestName, room, nights);
                if (paid) res.markAsPaid();
                reservations.add(res);
                if (id > maxId) maxId = id;
            }
            nextReservationId = maxId + 1;
        } catch (IOException e) {
            System.out.println("Could not load reservations file.");
        }
    }

    private void saveReservations() {
        try (PrintWriter pw = new PrintWriter(new FileWriter(RESERVATIONS_FILE))) {
            for (Reservation r : reservations) {
                pw.println(r.toFileFormat());
            }
        } catch (IOException e) {
            System.out.println("Error saving reservations: " + e.getMessage());
        }
    }
}
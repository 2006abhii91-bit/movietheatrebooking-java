//main class
import java.util.*;

abstract class Seat {
    protected String seatId;
    protected boolean isBooked;

    public Seat(String seatId) {
        this.seatId = seatId;
        this.isBooked = false;
    }

    public boolean isBooked() {
        return isBooked;
    }

    public void bookSeat() {
        isBooked = true;
    }

    public String getSeatId() {
        return seatId;
    }

    abstract double getPrice();
}





//class 2
class RegularSeat extends Seat {
    public RegularSeat(String seatId) {
        super(seatId);
    }

    @Override
    double getPrice() {
        return 150;
    }
}





//class 3
class PremiumSeat extends Seat {
    public PremiumSeat(String seatId) {
        super(seatId);
    }

    @Override
    double getPrice() {
        return 300;
    }
}





//main class
public class MovieTheatreMatrixBooking {

    static Seat[][] seats = new Seat[5][10];
    static char[] rows = {'A', 'B', 'C', 'D', 'E'};

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        // Initialize seats
        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 10; j++) {
                String seatId = rows[i] + String.valueOf(j + 1);

                // A, B, C -> Regular | D, E -> Premium
                if (i < 3) {
                    seats[i][j] = new RegularSeat(seatId);
                } else {
                    seats[i][j] = new PremiumSeat(seatId);
                }
            }
        }

        // Seat category info
        System.out.println("🎬 SEAT CATEGORY DETAILS");
        System.out.println("Rows A, B, C → Regular Seats (₹150.0)");
        System.out.println("Rows D, E     → Premium Seats (₹300.0)");

        // Show seats before booking
        System.out.println("\n🎬 SEAT MATRIX (Before Booking)");
        displaySeats();

        // Ask seat count first
        System.out.print("\nEnter number of seats required: ");
        int requiredSeats = sc.nextInt();
        sc.nextLine(); // consume newline

        // Seat selection input
        System.out.print("Enter seat numbers (comma separated, e.g., A5,A6): ");
        String input = sc.nextLine().toUpperCase();
        String[] selectedSeats = input.split(",");

        if (selectedSeats.length != requiredSeats) {
            System.out.println("❌ Number of seats entered does not match required seats.");
            sc.close();
            return;
        }

        // Booking process
        Map<Double, List<String>> bookedByPrice = new LinkedHashMap<>();
        double totalAmount = 0;

        for (String seat : selectedSeats) {
            seat = seat.trim();

            int rowIndex = seat.charAt(0) - 'A';
            int colIndex;

            try {
                colIndex = Integer.parseInt(seat.substring(1)) - 1;
            } catch (NumberFormatException e) {
                System.out.println("❌ Invalid seat format: " + seat);
                continue;
            }

            if (rowIndex < 0 || rowIndex >= 5 || colIndex < 0 || colIndex >= 10) {
                System.out.println("❌ Seat does not exist: " + seat);
                continue;
            }

            Seat selectedSeat = seats[rowIndex][colIndex];

            if (selectedSeat.isBooked()) {
                System.out.println("⚠️ Seat already booked: " + seat);
            } else {
                selectedSeat.bookSeat();
                double price = selectedSeat.getPrice();
                totalAmount += price;

                bookedByPrice
                        .computeIfAbsent(price, k -> new ArrayList<>())
                        .add(seat);
            }
        }

        // Booking summary
        System.out.println("\n🎟️ BOOKING SUMMARY");
        for (Map.Entry<Double, List<String>> entry : bookedByPrice.entrySet()) {
            System.out.println(
                    "Seat booked: " +
                    String.join(", ", entry.getValue()) +
                    " (₹" + entry.getKey() + ")"
            );
        }

        // Show seats after booking
        System.out.println("\n🎬 SEAT MATRIX (After Booking)");
        displaySeats();

        System.out.println("\n💰 Total Amount Payable: ₹" + totalAmount);
        sc.close();
    }

    // Display seat matrix
    static void displaySeats() {
        System.out.print("    ");
        for (int i = 1; i <= 10; i++) {
            System.out.printf("%3d", i);
        }
        System.out.println();

        for (int i = 0; i < 5; i++) {
            System.out.print(rows[i] + " | ");
            for (int j = 0; j < 10; j++) {
                System.out.print(seats[i][j].isBooked() ? "  X" : "  O");
            }
            System.out.println();
        }
    }
}


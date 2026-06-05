package view.component;

import model.Seat;
import java.util.ArrayList;

public class SeatUtils {

    // BookingHistoryView용 (좌석 ID만)
    public static String getSeatText(ArrayList<Seat> seats) {
        if (seats == null || seats.isEmpty()) return "-";
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < seats.size(); i++) {
            if (i > 0) sb.append(", ");
            sb.append(seats.get(i).getSeatId());
        }
        return sb.toString();
    }

    // BookingConfirmView용 ((일반 n석) 포함)
    public static String getSeatTextWithCount(ArrayList<Seat> seats) {
        if (seats == null || seats.isEmpty()) return "-";
        String base = getSeatText(seats);
        return base + " (일반 " + seats.size() + "석)";
    }
}
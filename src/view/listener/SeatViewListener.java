package view.listener;

import java.util.List;

public interface SeatViewListener {
    void onNextButtonClicked(List<String> selectedSeats);
    void onHistoryButtonClicked();
} 
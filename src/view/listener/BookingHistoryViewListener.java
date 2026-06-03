package view.listener;

import model.Reservation;

public interface BookingHistoryViewListener {
    void onBack();
    void onReservationSelected(Reservation reservation);
}
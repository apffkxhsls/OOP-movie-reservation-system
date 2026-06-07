package controller1;

import model.Reservation;
import model.ReservationRepository;
import controller2.PaymentController;
import controller2.ReservationController;
import view.BookingConfirmView;
import view.BookingHistoryView;
import view.MainView;

import java.util.ArrayList;

// MainController 내부의 new 연산자를 대신 실행해주는 조립 객체 (Factory 패턴)
public class ControllerFactory {
    private final ReservationRepository repository;

    public ControllerFactory(ReservationRepository repository) {
        this.repository = repository;
    }

    public UIProvider createUIProvider() {
        return new SwingUIProvider();
    }

    public MainView createMainView() {
        return new MainView();
    }

    public ReservationController createReservationController() {
        return new ReservationController(repository);
    }

    public PaymentController createPaymentController(ReservationController resController) {
        return new PaymentController(resController);
    }

    public BookingHistoryView createHistoryView(ArrayList<Reservation> reservations) {
        return new BookingHistoryView(reservations);
    }

    public BookingConfirmView createConfirmView() {
        return new BookingConfirmView();
    }
}
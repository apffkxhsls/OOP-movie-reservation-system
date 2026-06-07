package controller1;

import java.util.ArrayList;
import java.util.List;
import javax.swing.JOptionPane;

import model.Movie;
import model.ShowInfo;
import model.DummyData;
import model.Reservation;
import model.ReservationRepository;
import model.Seat;
import view.MainView;
import view.BookingConfirmView;
import view.BookingHistoryView;
import view.listener.MainViewListener;
import view.listener.BookingConfirmViewListener;
import view.listener.BookingHistoryViewListener;
import controller2.PaymentController;
import controller2.ReservationController;

public class MainController implements MainViewListener {

    private Movie selectedMovie;
    private ShowInfo selectedShowInfo;

    private final ReservationRepository repository;
    private final MainView mainView;
    private final SeatController seatController;

    public MainController(ReservationRepository repository) {
        this.repository = repository;

        this.mainView = new MainView();
        this.mainView.setListener(this);

        this.seatController = new SeatController(
                this::openPaymentView,
                this::openHistoryView);
    }

    public void start() {
        this.mainView.setVisible(true);
    }

    @Override
    public void onMovieSelected(Movie movie) {
        this.selectedMovie = movie;

        ShowInfo matchedShowInfo = null;
        for (ShowInfo info : DummyData.getShowInfos()) {
            if (info.getMovie().getTitle().equals(movie.getTitle())) {
                matchedShowInfo = info;
                break;
            }
        }

        if (matchedShowInfo == null) {
            JOptionPane.showMessageDialog(mainView, "해당 영화의 상영 정보를 찾지 못했습니다.", "알림", JOptionPane.WARNING_MESSAGE);
            matchedShowInfo = DummyData.getShowInfos().get(0);
        }

        this.selectedShowInfo = matchedShowInfo;
        openSeatView();
    }

    @Override
    public void onHistoryButtonClicked() {
        openHistoryView();
    }

    public void openSeatView() {
        if (this.selectedMovie == null || this.selectedShowInfo == null) {
            JOptionPane.showMessageDialog(mainView, "영화와 상영 정보에 오류가 발생했습니다.", "오류", JOptionPane.ERROR_MESSAGE);
            return;
        }

        this.mainView.setVisible(false);
        // 수정됨: this.selectedMovie 파라미터 제거
        this.seatController.startSeatSelection(this.selectedShowInfo);
    }

    public void openHistoryView() {
        this.mainView.setVisible(false);

        // 타 파트의 BookingHistoryView는 생성 시 데이터를 강제하므로 여기서 동적 생성
        ReservationController resController = new ReservationController(this.repository);
        ArrayList<Reservation> reservations = resController.getReservationList();

        BookingHistoryView historyView = new BookingHistoryView(reservations);
        historyView.setBookingHistoryViewListener(new BookingHistoryViewListener() {
            @Override
            public void onBack() {
                historyView.dispose();
                goMainView();
            }

            @Override
            public void onReservationSelected(Reservation reservation) {
            }
        });

        historyView.setVisible(true);
    }

    public void goMainView() {
        this.selectedMovie = null;
        this.selectedShowInfo = null;
        this.mainView.setVisible(true);
    }

    public void openPaymentView(ShowInfo showInfo, List<Seat> selectedSeats) {
        ReservationController resController = new ReservationController(this.repository);
        PaymentController paymentController = new PaymentController(resController);

        Reservation reservation = paymentController.processPayment(showInfo, new ArrayList<>(selectedSeats));

        if (reservation == null) {
            JOptionPane.showMessageDialog(mainView, "결제 진행 중 오류가 발생했습니다. 메인 화면으로 돌아갑니다.", "결제 실패",
                    JOptionPane.ERROR_MESSAGE);
            goMainView();
            return;
        }

        BookingConfirmView confirmView = new BookingConfirmView();
        confirmView.setReservation(reservation);
        confirmView.setBookingConfirmViewListener(new BookingConfirmViewListener() {
            @Override
            public void onBack() {
                confirmView.dispose();
                seatController.startSeatSelection(selectedShowInfo);
            }

            @Override
            public void onNext() {
                confirmView.dispose();
                openHistoryView();
            }

            @Override
            public void onHistory() {
                confirmView.dispose();
                openHistoryView();
            }
        });

        confirmView.setVisible(true);
    }
}
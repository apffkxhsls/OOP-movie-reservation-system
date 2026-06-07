package controller1;

import java.util.ArrayList;
import java.util.List;

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

    private ShowInfo selectedShowInfo;

    private final MainView mainView;
    private final SeatController seatController;
    private final ControllerFactory factory; // Factory 패턴
    private final UIProvider ui; // Adapter 패턴

    // 제약조건: Main.java에서 호출하는 생성자 시그니처 유지
    public MainController(ReservationRepository repository) {
        // 객체 생성 로직을 팩토리로 위임 (DIP 준수)
        this.factory = new ControllerFactory(repository);
        this.ui = factory.createUIProvider();

        this.mainView = factory.createMainView();
        this.mainView.setListener(this);

        this.seatController = new SeatController(
                this.ui,
                this::openPaymentView,
                this::openHistoryView);
    }

    public void start() {
        this.mainView.setVisible(true);
    }

    @Override
    public void onMovieSelected(Movie movie) {
        ShowInfo matchedShowInfo = null;
        for (ShowInfo info : DummyData.getShowInfos()) {
            if (info.getMovie().getTitle().equals(movie.getTitle())) {
                matchedShowInfo = info;
                break;
            }
        }

        if (matchedShowInfo == null) {
            ui.showWarningMessage("해당 영화의 상영 정보를 찾지 못했습니다.", "알림");
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
        if (this.selectedShowInfo == null) {
            ui.showErrorMessage("상영 정보에 오류가 발생했습니다.", "오류");
            return;
        }

        this.mainView.setVisible(false);
        this.seatController.startSeatSelection(this.selectedShowInfo);
    }

    public void openHistoryView() {
        this.mainView.setVisible(false);

        // 팩토리를 통해 Controller2 파트의 컨트롤러와 View 안전하게 생성
        ReservationController resController = factory.createReservationController();
        ArrayList<Reservation> reservations = resController.getReservationList();

        BookingHistoryView historyView = factory.createHistoryView(reservations);
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
        this.selectedShowInfo = null;
        this.mainView.setVisible(true);
    }

    public void openPaymentView(ShowInfo showInfo, List<Seat> selectedSeats) {
        ReservationController resController = factory.createReservationController();
        PaymentController paymentController = factory.createPaymentController(resController);

        Reservation reservation = paymentController.processPayment(showInfo, new ArrayList<>(selectedSeats));

        if (reservation == null) {
            ui.showErrorMessage("결제 진행 중 오류가 발생했습니다. 메인 화면으로 돌아갑니다.", "결제 실패");
            goMainView();
            return;
        }

        BookingConfirmView confirmView = factory.createConfirmView();
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
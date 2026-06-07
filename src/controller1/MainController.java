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

// MainViewListener 구현을 통해 메인 화면에서의 사용자 입력을 처리하고, 다른 뷰(View)와의 화면 전환을 유기적으로 조율하는 역할을 하는 핵심 컨트롤러
public class MainController implements MainViewListener {

    // 사용자가 메인 화면의 목록에서 최종적으로 선택한 특정 영화의 상영 정보 캐싱
    private ShowInfo selectedShowInfo;

    // 화면 제어, 좌석 선택 위임, 팩토리 객체 등 시스템 흐름 제어에 필요한 핵심 컴포넌트 참조
    private final MainView mainView;
    private final SeatController seatController;
    private final ControllerFactory factory;
    private final UIProvider ui;

    // 생성자: 팩토리를 활용하여 필요한 뷰와 하위 컨트롤러들을 생성하고, 이벤트 리스너(콜백)를 등록하여 초기 환경을 설정
    public MainController(ReservationRepository repository) {
        this.factory = new ControllerFactory(repository);
        this.ui = factory.createUIProvider();

        this.mainView = factory.createMainView();
        this.mainView.setListener(this);

        this.seatController = new SeatController(
                this.ui,
                this::openPaymentView,
                this::openHistoryView);
    }

    // 초기화된 메인 뷰(MainView)를 사용자 화면에 표시하여 프로그램의 시작을 알림
    public void start() {
        this.mainView.setVisible(true);
    }

    // 사용자가 영화를 선택했을 때 DummyData 등을 검색하여 해당 영화의 구체적인 상영 정보를 매핑하고 좌석 선택 화면으로 이동
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

    // 메인 뷰에서 '예매 내역' 버튼 클릭 이벤트를 감지하여 예매 내역 조회 화면으로 전환하도록 지시
    @Override
    public void onHistoryButtonClicked() {
        openHistoryView();
    }

    // 선택된 상영 정보가 유효한지 검증한 후, 메인 화면을 일시적으로 숨기고 좌석 선택을 담당하는 컨트롤러를 호출
    public void openSeatView() {
        if (this.selectedShowInfo == null) {
            ui.showErrorMessage("상영 정보에 오류가 발생했습니다.", "오류");
            return;
        }

        this.mainView.setVisible(false);
        this.seatController.startSeatSelection(this.selectedShowInfo);
    }

    // ReservationController를 통해 현재까지의 예매 목록 데이터를 가져오고, 팩토리를 통해 BookingHistoryView를 생성하여 화면에 출력
    public void openHistoryView() {
        this.mainView.setVisible(false);

        ReservationController resController = factory.createReservationController();
        ArrayList<Reservation> reservations = resController.getReservationList();

        BookingHistoryView historyView = factory.createHistoryView(reservations);
        historyView.setBookingHistoryViewListener(new BookingHistoryViewListener() {
            // 사용자가 '뒤로 가기'를 클릭했을 때 내역 화면을 닫고 메인 화면으로 복귀
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

    // 진행 중이던 상태(선택된 상영 정보)를 초기화하고 메인 뷰를 다시 사용자 화면 최상단에 표시
    public void goMainView() {
        this.selectedShowInfo = null;
        this.mainView.setVisible(true);
    }

    // 좌석 선택 완료 후 PaymentController를 호출하여 결제를 진행하고, 성공 시 예매 확인 뷰(ConfirmView)를 띄우거나 실패 시 에러 처리를 수행
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
            // 결제 확인 창에서 이전 단계인 좌석 선택 화면으로 복귀 요청 시 처리
            @Override
            public void onBack() {
                confirmView.dispose();
                seatController.startSeatSelection(selectedShowInfo);
            }

            // 예매 완료 후 다음 단계로 넘어가 예매 내역 화면으로 이동 요청 시 처리
            @Override
            public void onNext() {
                confirmView.dispose();
                openHistoryView();
            }

            // '예매 내역 보기' 명시적 버튼 클릭에 대한 화면 이동 처리
            @Override
            public void onHistory() {
                confirmView.dispose();
                openHistoryView();
            }
        });

        confirmView.setVisible(true);
    }
}
package controller1;

import model.Reservation;
import model.ReservationRepository;
import controller2.PaymentController;
import controller2.ReservationController;
import view.BookingConfirmView;
import view.BookingHistoryView;
import view.MainView;

import java.util.ArrayList;

// 팩토리 패턴(Factory Pattern)을 통해 시스템 구동에 필요한 다양한 컨트롤러와 뷰 객체들을 일관되게 생성하고 의존성을 주입하는 역할을 하는 클래스
public class ControllerFactory {
    // 생성되는 컨트롤러들에 공통으로 주입되어 데이터 접근을 돕는 예매 데이터 저장소 참조
    private final ReservationRepository repository;

    // 시스템 시작 시 저장소 객체를 전달받아 팩토리 내부 상태를 초기화
    public ControllerFactory(ReservationRepository repository) {
        this.repository = repository;
    }

    // SwingUIProvider 인스턴스 생성을 통해 프로그램 전반에서 사용할 UI 알림 제공자 객체 반환
    public UIProvider createUIProvider() {
        return new SwingUIProvider();
    }

    // 사용자에게 보여줄 초기 화면인 MainView 객체를 생성 및 반환
    public MainView createMainView() {
        return new MainView();
    }

    // 예매 관련 비즈니스 로직(저장, 조회 등) 처리를 담당하는 ReservationController 객체를 저장소 의존성과 함께 생성
    public ReservationController createReservationController() {
        return new ReservationController(repository);
    }

    // 예매된 정보와 선택된 좌석을 바탕으로 결제 진행 처리를 담당하는 PaymentController 객체를 생성
    public PaymentController createPaymentController(ReservationController resController) {
        return new PaymentController(resController);
    }

    // 데이터베이스(저장소)에서 불러온 사용자의 과거 예매 내역 리스트를 화면에 표시하기 위한 BookingHistoryView 객체 생성
    public BookingHistoryView createHistoryView(ArrayList<Reservation> reservations) {
        return new BookingHistoryView(reservations);
    }

    // 최종 결제 완료 후 사용자에게 예매 내역 확인증(영수증 역할)을 띄워주는 BookingConfirmView 객체 생성
    public BookingConfirmView createConfirmView() {
        return new BookingConfirmView();
    }
}
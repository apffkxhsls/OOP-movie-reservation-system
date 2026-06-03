package controller1;

import java.util.ArrayList;
import java.util.List;
import model.Movie;
import model.ShowInfo;
import model.Seat;

/**
 * 상영관의 좌석 배치도를 표시하고, 좌석 선택의 유효성 검증 및
 * 임시 할당을 제어하는 좌석 전담 컨트롤러 클래스입니다.
 */
public class SeatController {

    // 1. 상태 저장용 필드 (캡슐화 적용)
    private Movie currentMovie;
    private ShowInfo currentShowInfo;
    private List<Seat> tempSelectedSeats; // 사용자가 '현재 단계'에서 선택 중인 임시 좌석 리스트

    // 2. 연동할 타 파트 객체 정의 (의존성 주입 예정)
    // private MainController mainController; // 뒤로 가기 처리를 위한 메인 컨트롤러 참조
    // private SeatView seatView; // 좌석 화면 관리를 위한 뷰

    /**
     * SeatController의 생성자입니다.
     * 선택된 좌석 목록을 빈 리스트로 초기화합니다.
     */
    public SeatController() {
        this.tempSelectedSeats = new ArrayList<>();
    }

    /**
     * MainController로부터 제어권을 넘겨받아 좌석 선택 프로세스를 시작하는 진입점 메서드입니다.
     * * @param movie 사용자가 선택한 영화 객체
     * 
     * @param showInfo 사용자가 선택한 상영 정보 객체
     */
    public void startSeatSelection(Movie movie, ShowInfo showInfo) {
        this.currentMovie = movie;
        this.currentShowInfo = showInfo;
        this.tempSelectedSeats.clear(); // 새로운 선택을 위해 기존 임시 선택 내역 초기화

        System.out.println("\n[시스템] " + currentMovie.getTitle() + " 좌석 선택 세션을 시작합니다.");

        // 1. 현재 상영관의 좌석 배치도 출력
        displaySeatLayout();

        // 2. TODO: SeatView를 통해 사용자에게 좌석 입력(예: A1, B3)을 받는 루프 구동 예정
    }

    /**
     * 현재 상영관(Theater)의 2D 좌석 배열(Seat[][])을 가져와
     * 콘솔 화면에 가시적인 배치도로 시각화하는 메서드입니다.
     */
    public void displaySeatLayout() {
        System.out.println("\n===== SCREEN =====");

        // TODO: currentShowInfo.getTheater().getSeats()를 활용하여
        // 2차원 배열을 순회하며 [A1] [A2] (예약됨: [XX]) 형태로 출력하는 로직 구현 예정
        System.out.println("[임시] 상영관 좌석 배치도가 이곳에 출력됩니다.");
        System.out.println("==================");
    }

    /**
     * 사용자가 선택한 좌석 번호(예: "A1")를 받아 유효성을 검증하고 임시 선택 목록에 추가합니다.
     * * @param seatName 사용자가 입력한 좌석 명칭
     * 
     * @return 선택 성공 여부 (true: 성공, false: 중복 또는 예매 불가)
     */
    public boolean selectSeat(String seatName) {
        // 1. TODO: 상영관 2D 배열에서 해당 이름의 Seat 객체 찾기
        // 2. TODO: Seat.isReserved()를 통해 이미 예매된 좌석인지 방어 코드 작성
        // 3. TODO: 이미 tempSelectedSeats에 포함되어 있는 중복 선택인지 검증

        System.out.println("[시스템] 좌석 " + seatName + "이(가) 임시 선택되었습니다.");
        // 예외 통과 시 tempSelectedSeats.add(foundSeat);
        return true;
    }

    /**
     * 사용자가 좌석 선택을 완료하고 결제 단계로 승인 요청을 보낼 때 실행되는 메서드입니다.
     * 유효성 검증 완료 후 Control 2 파트(PaymentController)로 데이터를 이관합니다.
     */
    public void confirmSeatSelection() {
        if (tempSelectedSeats.isEmpty()) {
            System.out.println("[오류] 최소 한 개 이상의 좌석을 선택하셔야 합니다.");
            return;
        }

        System.out.println("[시스템] 좌석 확정 완료. 결제 및 예매 확정 단계로 이동합니다.");
        // TODO: Control 2 파트의 PaymentController를 호출하며 데이터 전달 예정
        // paymentController.startPayment(currentMovie, currentShowInfo,
        // tempSelectedSeats);
    }

    /**
     * 사용자가 좌석 선택 중 취소하거나 뒤로 가기를 눌렀을 때,
     * 임시 상태를 비우고 MainController의 메인 화면으로 돌아가는 제어 메서드입니다.
     */
    public void cancelAndGoBack() {
        this.currentMovie = null;
        this.currentShowInfo = null;
        this.tempSelectedSeats.clear();
        System.out.println("[시스템] 좌석 선택이 취소되었습니다. 메인 메뉴로 복귀합니다.");

        // mainController.goMainView(); // 메인 화면으로 복귀 호출
    }
}
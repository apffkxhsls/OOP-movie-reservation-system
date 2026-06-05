package controller1;

import java.util.ArrayList;
import java.util.List;
import model.Movie;
import model.Seat;
import model.ShowInfo;
import view.SeatView;
import view.listener.SeatViewListener;

/**
 * 상영관의 좌석 배치도를 표시하고, 좌석 선택의 유효성 검증 및
 * 임시 할당을 제어하는 좌석 전담 컨트롤러 클래스입니다.
 * Java Swing GUI인 SeatView의 이벤트를 수신하기 위해 SeatViewListener를 구현합니다.
 */
public class SeatController implements SeatViewListener {

    // 1. 상태 저장용 필드 (캡슐화 원칙 적용)
    private Runnable returnToMainAction; // 화면 복귀를 위한 필드

    private Movie currentMovie;
    private ShowInfo currentShowInfo;
    private List<Seat> tempSelectedSeats; // 사용자가 '현재 단계'에서 선택 중인 임시 좌석 리스트

    // 2. 연동할 UI 뷰 객체 정의
    private SeatView seatView; // 좌석 화면 관리를 위한 Swing 뷰 객체

    /**
     * SeatController의 생성자입니다.
     * 
     * @param mainController 취소/뒤로 가기 시 메인 화면으로 돌아가기 위해 상위 컨트롤러를 주입받습니다.
     */
    public SeatController(Runnable returnToMainAction) {
        this.returnToMainAction = returnToMainAction;
        this.tempSelectedSeats = new ArrayList<>();
    }

    /**
     * MainController로부터 제어권을 넘겨받아 좌석 선택 프로세스를 시작하는 진입점 메서드입니다.
     * Java Swing 기반의 SeatView 창을 띄우고 제어 로직(리스너)을 바인딩합니다.
     * * @param movie 사용자가 선택한 영화 객체
     * 
     * @param showInfo 사용자가 선택한 상영 정보 객체
     */
    public void startSeatSelection(Movie movie, ShowInfo showInfo) {
        this.currentMovie = movie;
        this.currentShowInfo = showInfo;
        this.tempSelectedSeats.clear(); // 새로운 트랜잭션을 위해 기존 선택 내역 초기화

        System.out.println("\n[시스템] " + currentMovie.getTitle() + " 좌석 선택 세션을 시작합니다.");

        // Java Swing GUI 뷰 객체 인스턴스 생성 및 의존성 연결
        this.seatView = new SeatView(this.currentShowInfo);
        this.seatView.setListener(this); // 중요: View에 Controller 자신(this)을 리스너로 등록
        this.seatView.setVisible(true); // Swing 윈도우 창을 화면에 표시

        // 디버깅 및 무결성 검증을 위한 콘솔 시각화 레이아웃 출력 병행
        displaySeatLayout();
    }

    /**
     * 현재 상영관(Theater)의 2D 좌석 배열(Seat[][]) 상태를 가져와
     * 콘솔 화면에 동기화된 가시적 배치도로 시각화 출력하는 메서드입니다.
     */
    private void displaySeatLayout() {
        System.out.println("\n===== SCREEN =====");

        Seat[][] seats = currentShowInfo.getTheater().getSeats();
        int rows = currentShowInfo.getTheater().getRows();
        int cols = currentShowInfo.getTheater().getCols();

        for (int i = 0; i < rows; i++) {
            char rowChar = (char) ('A' + i);
            System.out.print(rowChar + " ");
            for (int j = 0; j < cols; j++) {
                Seat seat = seats[i][j];
                if (seat.isReserved()) {
                    System.out.print("[XX] "); // 이미 선점되어 예매 불가한 좌석 표시
                } else {
                    System.out.print("[" + seat.getSeatId() + "] "); // 선택 가능한 활성 좌석 표시
                }
            }
            System.out.println();
        }
        System.out.println("==================");
    }

    /**
     * 사용자가 선택한 좌석 이름(예: "A1")을 도메인 모델 내에서 찾아 유효성을 검증하고,
     * 이상이 없을 시 컨트롤러의 임시 선택 목록에 안전하게 추가하는 도메인 제어 메서드입니다.
     * * @param seatName 사용자가 입력하거나 GUI에서 전달한 좌석 명칭
     * 
     * @return 선택 성공 여부 (true: 성공, false: 중복 또는 예매 불가 상태 발견)
     */
    public boolean selectSeat(String seatName) {
        Seat[][] seats = currentShowInfo.getTheater().getSeats();
        int rows = currentShowInfo.getTheater().getRows();
        int cols = currentShowInfo.getTheater().getCols();

        // 도메인 내부 2차원 배열을 순회하며 매핑되는 Seat 인스턴스 탐색
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                Seat seat = seats[i][j];
                if (seat.getSeatId().equals(seatName)) {
                    // 방어적 코드: 도메인 모델 내부의 무결성 상태값 최종 검증
                    if (seat.isReserved()) {
                        System.out.println("[오류] 이미 다른 사용자에 의해 예매된 좌석입니다: " + seatName);
                        return false;
                    }
                    // 멱등성 검증: 임시 리스트에 동일 객체가 중복 저장되는 것을 사전에 방지
                    if (tempSelectedSeats.contains(seat)) {
                        System.out.println("[오류] 현재 세션에서 이미 선택하신 좌석입니다: " + seatName);
                        return false;
                    }

                    tempSelectedSeats.add(seat);
                    System.out.println("[시스템] 좌석 " + seatName + "이(가) 성공적으로 검증되어 임시 할당되었습니다.");
                    return true;
                }
            }
        }

        System.out.println("[오류] 상영관 구조 정보에 존재하지 않는 좌석 명칭입니다: " + seatName);
        return false;
    }

    /**
     * 사용자가 모든 좌석 지정을 완료하고 '다음 단계' 결제 승인 프로세스로 이관을 요청할 때
     * 실행되는 최종 확정 메서드입니다.
     */
    public void confirmSeatSelection() {
        if (tempSelectedSeats.isEmpty()) {
            System.out.println("[오류] 최소 한 개 이상의 좌석을 선택하셔야 결제 단계로 진행 가능합니다.");
            return;
        }

        System.out.println("[시스템] 좌석 무결성 검증 통과 완료. 결제 및 예매 확정 단계로 제어권을 위임합니다.");

        // [기획서 9p 명시 사항] 기획서 파트 2(PaymentController)와의 데이터 전달 접점 연결
        // PaymentController paymentController = new PaymentController();
        // paymentController.processPayment(currentShowInfo, tempSelectedSeats);

        if (this.seatView != null) {
            this.seatView.setVisible(false);
            this.seatView.dispose();
        }
    }

    /**
     * 사용자가 예매 내역 조회 화면으로 이동하거나 창을 이탈하는 경우,
     * 컨트롤러 상태값들을 안전하게 초기화(Clean-up)하고 메인 화면으로 돌아가는 복귀 제어 메서드입니다.
     */
    public void cancelAndGoBack() {
        this.currentMovie = null;
        this.currentShowInfo = null;
        this.tempSelectedSeats.clear(); // 임시 선택 내역 청소 (메모리 릭 방지)

        if (this.seatView != null) {
            this.seatView.setVisible(false);
            this.seatView.dispose(); // Swing 리소스 완전 해제
        }

        System.out.println("[시스템] 좌석 선택 세션이 완전히 취소되었습니다. 메인 대시보드로 복귀합니다.");

        // [수정됨] 주입받은 MainController를 통해 메인 화면으로 복귀합니다!
        if (this.returnToMainAction != null) {
            this.returnToMainAction.run();
        }
    }

    /**
     * [SeatViewListener 상속 구현]
     * SeatView 내부의 다음 단계 JButton 컴포넌트 클릭 이벤트 발생 시 호출되는 스윙 연동 콜백 메서드입니다.
     * 뷰에서 전달한 문자열 좌석 정보들을 실제 도메인 모델 데이터로 정밀 변환 및 유효성을 실시간 검토합니다.
     */
    @Override
    public void onNextButtonClicked(List<String> selectedSeats) {
        this.tempSelectedSeats.clear(); // 재검증을 위한 기존 리스트 초기화

        // 1. 실패한 좌석들을 모아둘 리스트 생성
        List<String> failedSeats = new ArrayList<>();

        for (String seatName : selectedSeats) {
            // 2. break 없이 모든 좌석을 끝까지 검사
            if (!selectSeat(seatName)) {
                failedSeats.add(seatName); // 검증 실패 시 리스트에 좌석 이름 추가
            }
        }

        // 3. 실패한 좌석이 비어있다면(=모두 성공했다면) 결제 진행
        if (failedSeats.isEmpty()) {
            confirmSeatSelection();
        } else {
            // 4. 실패한 좌석이 하나라도 있다면 모아서 한 번에 출력 (UX 개선)
            String failedListStr = String.join(", ", failedSeats);
            System.out.println("[오류] 다음 좌석은 예매가 불가능합니다: [" + failedListStr + "]");
            System.out.println("[시스템] 좌석을 다시 선택해 주세요.");

            this.tempSelectedSeats.clear(); // 불완전한 상태 전이 차단을 위해 전체 초기화
        }
    }

    /**
     * [SeatViewListener 상속 구현]
     * SeatView 우측 상단의 '예매내역 조회' 버튼 컴포넌트가 클릭되었을 때 자동으로 가로채어
     * 컨트롤러의 취소 및 뒤로 가기 프로세스로 즉시 라우팅하는 스윙 연동 콜백 메서드입니다.
     */
    @Override
    public void onHistoryButtonClicked() {
        cancelAndGoBack();
    }
}
package controller1;

import java.util.List;
import model.Movie;
import model.ShowInfo;
import model.DummyData;
import model.ReservationRepository;

// [추가된 임포트] View 관련 클래스들을 가져옵니다.
import view.MainView;
import view.listener.MainViewListener;

/**
 * 영화 예매 시스템의 최초 진입점과 화면 전환 흐름을 제어하는 메인 컨트롤러
 * [수정 포인트 1] MainViewListener 인터페이스를 구현(implements)하여
 * View에서 발생하는 버튼 클릭 이벤트를 직접 수신합니다.
 */
public class MainController implements MainViewListener {

    // 1. 상태 저장용 필드 (캡슐화 적용)
    private Movie selectedMovie;
    private ShowInfo selectedShowInfo;

    // 2. 연동될 타 파트
    private final ReservationRepository repository;
    private MainView mainView; // [주석 해제] 메인 화면 객체
    private SeatController seatController; // [주석 해제] 좌석 제어 객체

    /**
     * MainController 생성자입니다.
     */
    public MainController(ReservationRepository repository) {
        this.repository = repository;
        // 시스템 시작 시 SeatController도 함께 준비해 둡니다.
        this.seatController = new SeatController();
    }

    /**
     * 프로그램의 실질적인 시작(Entry Point)을 담당하는 메서드입니다.
     */
    public void start() {
        System.out.println("[시스템] 영화 예매 시스템 컨트롤러 및 GUI 화면 가동");

        // 1. 메인 화면(View) 객체 생성
        this.mainView = new MainView();

        // 2. Controller 자신(this)을 View의 리스너로 등록하여 신호를 받겠다고 선언
        this.mainView.setListener(this);

        // 3. 화면을 모니터에 노출
        this.mainView.setVisible(true);
    }

    // =======================================================
    // [수정 포인트 2] MainViewListener 인터페이스 구현부 (오버라이딩)
    // =======================================================

    /**
     * MainView에서 '예매하기' 버튼이 클릭되었을 때 호출됩니다.
     */
    @Override
    public void onMovieSelected(Movie movie) {
        System.out.println("[시스템] 사용자가 '" + movie.getTitle() + "' 영화를 선택했습니다.");
        selectMovie(movie);

        // [주의] 현재 기획상 '상영 시간 선택 화면'이 따로 없이 바로 SeatView로 넘어가야 합니다.
        // SeatView는 생성될 때 'ShowInfo'를 필요로 하므로, 임시로 DummyData의 첫 번째 상영 정보를 매핑해 줍니다.
        ShowInfo tempShowInfo = DummyData.getShowInfos().get(0);
        selectShowInfo(tempShowInfo);

        // 좌석 선택 화면으로 이동
        openSeatView();
    }

    /**
     * MainView에서 '예매내역 조회' 아이콘이 클릭되었을 때 호출됩니다.
     */
    @Override
    public void onHistoryButtonClicked() {
        openHistoryView();
    }

    // =======================================================
    // 기존 흐름 제어 로직 보완
    // =======================================================

    public void selectMovie(Movie movie) {
        this.selectedMovie = movie;
    }

    public void selectShowInfo(ShowInfo showInfo) {
        this.selectedShowInfo = showInfo;
    }

    public void openSeatView() {
        if (this.selectedMovie == null || this.selectedShowInfo == null) {
            System.out.println("[오류] 영화와 상영 정보를 확인할 수 없습니다.");
            return;
        }

        System.out.println("[시스템] 유효성 검증 완료. 메인 화면을 숨기고 좌석 선택 화면을 엽니다.");

        // 메인 화면을 잠시 숨깁니다.
        if (this.mainView != null) {
            this.mainView.setVisible(false);
        }

        // 제어권을 SeatController로 넘깁니다.
        seatController.startSeatSelection(this.selectedMovie, this.selectedShowInfo);
    }

    public void openHistoryView() {
        System.out.println("[시스템] 예매 내역 조회 화면으로 이동합니다.");
        // TODO: BookingHistoryView 생성 및 활성화
    }

    public void goMainView() {
        this.selectedMovie = null;
        this.selectedShowInfo = null;
        System.out.println("[시스템] 메인 화면으로 복귀합니다.");

        // 숨겨두었던 메인 화면을 다시 띄웁니다.
        if (this.mainView != null) {
            this.mainView.setVisible(true);
        }
    }
}
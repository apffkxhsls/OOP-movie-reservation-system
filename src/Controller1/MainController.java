package Controller1;

import java.util.List;
import model.Movie;
import model.ShowInfo;
import model.DummyData;
import model.ReservationRepository; // 연동될 데이터 저장소 임포트

/**
 * 영화 예매 시스템의 최초 진입점과 화면 전환 흐름을 제어하는 메인 컨트롤러
 */
public class MainController {

    // 1. 상태 저장용 필드 (캡슐화 적용 - 외부에서 직접 접근 불가)
    private Movie selectedMovie;
    private ShowInfo selectedShowInfo;

    // 2. 연동될 타 파트
    private final ReservationRepository repository;
    // private MainView mainView;
    // private SeatController seatController;

    /**
     * MainController 생성자입니다.
     * [수정 포인트] Main.java에서 시스템 시작 시 넘겨주는 ReservationRepository를
     * 주입받을 수 있도록 의존성 주입(DI) 구조를 반영했습니다.
     */
    public MainController(ReservationRepository repository) {
        this.repository = repository;
    }

    /**
     * [수정 포인트] 프로그램의 실질적인 시작(Entry Point)을 담당하는 메서드입니다.
     * Main.java에서 controller.start() 호출 시 실행됩니다.
     */
    public void start() {
        System.out.println("\n=== 영화 예매 시스템 컨트롤러 가동 ===");
        loadMovieList();
        // TODO: 추후 MainView를 호출하여 메인 화면을 띄우는 로직 구현
    }

    /**
     * Model 단으로부터 전체 영화 목록을 로드하여 View에 전달하는 메서드입니다.
     */
    public void loadMovieList() {
        // DummyData로부터 영화 데이터를 안전하게 불러옵니다.
        List<Movie> movies = DummyData.getMovies();

        System.out.println("[현재 상영 중인 영화 목록]");
        for (Movie movie : movies) {
            System.out.println("- " + movie.getTitle() + " (" + movie.getGenre() + ")");
        }
    }

    /**
     * 사용자가 화면에서 선택한 영화 정보를 내부 상태로 보관합니다.
     */
    public void selectMovie(Movie movie) {
        this.selectedMovie = movie;
    }

    /**
     * 사용자가 화면에서 선택한 상영 시간 및 상영관 정보를 내부 상태로 보관합니다.
     */
    public void selectShowInfo(ShowInfo showInfo) {
        this.selectedShowInfo = showInfo;
    }

    /**
     * 좌석 선택 단계로 진입하며, 유효성 검사 후 흐름을 SeatController로 이관합니다.
     */
    public void openSeatView() {
        // [방어적 프로그래밍] 데이터 누락 시 흐름 차단
        if (this.selectedMovie == null || this.selectedShowInfo == null) {
            System.out.println("[오류] 영화와 상영 시간을 모두 선택해 주세요.");
            return;
        }

        System.out.println("[시스템] 유효성 검증 완료. 좌석 선택 화면으로 이동합니다.");
        // seatController.startSeatSelection(this.selectedMovie, this.selectedShowInfo);
    }

    /**
     * 예매 내역 조회 화면(BookingHistoryView)으로 진입하는 흐름을 제어합니다.
     */
    public void openHistoryView() {
        System.out.println("[시스템] 예매 내역 조회 화면으로 이동합니다.");
        // TODO: BookingHistoryView 객체 생성 및 화면 활성화 처리 예정
    }

    /**
     * 프로세스 도중 뒤로 가기나 취소가 발생했을 때, 선택 상태를 초기화하고 메인으로 복귀합니다.
     */
    public void goMainView() {
        this.selectedMovie = null;
        this.selectedShowInfo = null;
        System.out.println("[시스템] 선택 내역을 초기화하고 메인 화면으로 복귀합니다.");
        // TODO: mainView를 화면에 다시 노출시키는 로직 구현 예정
    }
}
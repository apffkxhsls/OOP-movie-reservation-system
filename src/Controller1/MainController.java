package Controller1;

import java.util.List;
import model.Movie;
import model.ShowInfo;

/**
 * 영화 예매 시스템의 최초 진입점과 화면 전환 흐름을 제어하는 메인 컨트롤러입니다.
 * 기획서의 Control 1 파트 요구사항에 맞춰 선택 정보를 임시 제어합니다.
 */
public class MainController {

    // 1. 상태 저장용 필드 (캡슐화 적용)
    private Movie selectedMovie;
    private ShowInfo selectedShowInfo;

    // 2. 연동될 타 파트 객체들 (의존성 주입 예정)
    // private MainView mainView;
    // private SeatController seatController;

    public MainController() {
        // 생성자를 통해 MainView, SeatController 등의 의존성을 주입받고 초기화할 예정입니다.
    }

    /**
     * Model 단으로부터 전체 영화 목록을 로드하여 View에 전달하는 메서드입니다.
     */
    public void loadMovieList() {
        // TODO: Model 파트의 DummyData 또는 실제 파일 데이터를 불러와 MainView로 전달
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
        // [예외 처리] 영화 또는 상영 시간이 선택되지 않은 상태의 방어 코드
        if (this.selectedMovie == null || this.selectedShowInfo == null) {
            System.out.println("[오류 메시지] 영화와 상영 시간을 모두 선택해 주세요.");
            // 실제 구현 시에는 View에 팝업 알림 메시지를 띄우도록 위임할 예정입니다.
            return;
        }

        // 유효성 통과 시 SeatController를 호출하여 선택 데이터를 전송하고 화면을 전환
        // seatController.startSeatSelection(this.selectedMovie, this.selectedShowInfo);
    }

    /**
     * 예매 내역 조회 화면(BookingHistoryView)으로 진입하는 흐름을 제어합니다.
     */
    public void openHistoryView() {
        // TODO: BookingHistoryView 객체 생성 및 화면 활성화 처리 예정
    }

    /**
     * 프로세스 도중 뒤로 가기나 취소가 발생했을 때, 선택 상태를 초기화하고 메인으로 복귀합니다.
     */
    public void goMainView() {
        this.selectedMovie = null;
        this.selectedShowInfo = null;
        // TODO: mainView를 화면에 다시 노출시키는 로직 구현 예정
    }
}
package controller1;

import java.util.ArrayList;
import model.Movie;
import model.ShowInfo;
import model.DummyData;
import model.Reservation;
import model.ReservationRepository;

import view.MainView;
import view.BookingHistoryView;
import view.listener.MainViewListener;
import view.listener.BookingHistoryViewListener;

import controller2.ReservationController;

/**
 * 영화 예매 시스템의 최초 진입점(Entry Point)과 전반적인 화면 전환 흐름을 제어하는 메인 컨트롤러입니다.
 * MainViewListener 인터페이스를 구현하여 View(MainView)에서 발생하는 사용자 이벤트를 직접 수신하고 처리합니다.
 */
public class MainController implements MainViewListener {

    // 1. 상태 저장용 필드 (캡슐화 원칙 적용: private 선언으로 외부 접근 차단)
    private Movie selectedMovie;
    private ShowInfo selectedShowInfo;

    // 2. 연동될 타 파트 및 View 객체
    private final ReservationRepository repository;
    private MainView mainView;
    private SeatController seatController;

    /**
     * MainController 생성자입니다.
     * 시스템 가동 시 필요한 저장소(Repository)를 주입받고, 하위 컨트롤러를 초기화합니다.
     * * @param repository 예매 내역을 관리하는 데이터 저장소 객체
     */
    public MainController(ReservationRepository repository) {
        this.repository = repository;

        // [OOP 설계 포인트] SeatController가 나중에 "뒤로 가기"를 눌렀을 때
        this.seatController = new SeatController(this::goMainView);
    }

    /**
     * 프로그램의 실질적인 시작을 담당하는 라이프사이클 런칭 메서드입니다.
     */
    public void start() {
        System.out.println("[시스템] 영화 예매 시스템 컨트롤러 및 GUI 화면 가동");

        // 1. 메인 화면(View) 객체 생성
        this.mainView = new MainView();

        // 2. Controller 자신(this)을 View의 이벤트 리스너로 등록 (다형성 활용)
        this.mainView.setListener(this);

        // 3. 렌더링된 메인 화면을 사용자 모니터에 노출
        this.mainView.setVisible(true);
    }

    // =======================================================
    // [MainViewListener 인터페이스 오버라이딩 영역]
    // =======================================================

    /**
     * MainView에서 특정 영화 카드의 '예매하기' 버튼이 클릭되었을 때 호출되는 콜백 메서드입니다.
     * * @param movie 사용자가 선택한 영화 객체
     */
    @Override
    public void onMovieSelected(Movie movie) {
        System.out.println("[시스템] 사용자가 '" + movie.getTitle() + "' 영화를 선택했습니다.");
        selectMovie(movie);

        // [비즈니스 로직 추가]
        // 무조건 첫 번째 DummyData를 가져오는 대신, '선택한 영화'와 일치하는 상영 정보(ShowInfo)를 매핑합니다.
        ShowInfo matchedShowInfo = null;
        for (ShowInfo info : DummyData.getShowInfos()) {
            if (info.getMovie().getTitle().equals(movie.getTitle())) {
                matchedShowInfo = info;
                break; // 일치하는 상영 정보를 찾으면 즉시 탐색 종료
            }
        }

        // 만약 매핑되는 정보가 없다면 (방어적 프로그래밍), 기본값으로 첫 번째 데이터를 할당합니다.
        if (matchedShowInfo == null) {
            System.out.println("[경고] 해당 영화의 상영 정보를 찾지 못해 임시 상영 정보로 대체합니다.");
            matchedShowInfo = DummyData.getShowInfos().get(0);
        }

        selectShowInfo(matchedShowInfo);

        // 검증이 완료되었으므로 좌석 선택 화면으로 상태를 전이합니다.
        openSeatView();
    }

    /**
     * MainView 우측 상단의 '예매내역 조회' 아이콘이 클릭되었을 때 호출되는 콜백 메서드입니다.
     */
    @Override
    public void onHistoryButtonClicked() {
        openHistoryView();
    }

    // =======================================================
    // [내부 흐름 제어 로직 보완 영역]
    // =======================================================

    private void selectMovie(Movie movie) {
        this.selectedMovie = movie;
    }

    private void selectShowInfo(ShowInfo showInfo) {
        this.selectedShowInfo = showInfo;
    }

    /**
     * 메인 화면을 숨기고 좌석 선택 화면(SeatView)으로 제어권을 위임하는 전이 메서드입니다.
     */
    public void openSeatView() {
        if (this.selectedMovie == null || this.selectedShowInfo == null) {
            System.out.println("[오류] 선택된 영화와 상영 정보의 무결성이 훼손되었습니다.");
            return; // 데이터가 정상적이지 않으면 전이를 차단
        }

        System.out.println("[시스템] 데이터 유효성 검증 완료. 메인 화면을 숨기고 좌석 선택 세션으로 진입합니다.");

        // 메모리에서 삭제(dispose)하지 않고 잠시 보이지 않게만 숨김 처리 (상태 유지)
        if (this.mainView != null) {
            this.mainView.setVisible(false);
        }

        // 제어권 및 데이터를 SeatController로 이관
        this.seatController.startSeatSelection(this.selectedMovie, this.selectedShowInfo);
    }

    public void openHistoryView() {
        System.out.println("[시스템] 예매 내역 조회 화면으로 이동합니다.");

        // 1. 기존 메인 화면을 안전하게 숨김 (상태 복귀를 위해 dispose하지 않음)
        if (this.mainView != null) {
            this.mainView.setVisible(false);
        }

        // 2. Control 2 파트의 ReservationController를 인스턴스화하고 최신 예매 내역 리스트 획득
        ReservationController reservationController = new ReservationController(this.repository);
        ArrayList<Reservation> reservations = reservationController.getReservationList();

        // 3. 획득한 비즈니스 데이터를 주입하여 예매 내역 뷰(BookingHistoryView) 생성
        BookingHistoryView historyView = new BookingHistoryView(reservations);

        // 4. 인터페이스 구현(익명 클래스)을 통해 뷰 내부의 이벤트 바인딩 (에러 해결 완료!)
        historyView.setBookingHistoryViewListener(new BookingHistoryViewListener() {

            // [필수 구현 1] 뒤로 가기 버튼을 눌렀을 때의 동작
            @Override
            public void onBack() {
                historyView.dispose(); // 예매 내역 화면 창을 완전히 닫고 리소스 해제
                goMainView(); // 숨겨두었던 메인 화면 복귀 호출
            }

            // [필수 구현 2] 특정 예매 내역을 선택했을 때의 동작
            @Override
            public void onReservationSelected(Reservation reservation) {
                // 사용자가 화면에서 특정 예매 내역을 클릭했을 때 콘솔에 정보를 출력합니다.
                System.out.println("[시스템] 예매 번호 " + reservation.getReservationNumber() + " 내역이 선택되었습니다.");

                // 향후 예매 취소 기능 등을 구현할 때 Control 2의 ReservationController와
                // 연결할 수 있도록 확장성을 열어둡니다.
            }
        });

        // 5. 완성이 완료된 예매 내역 화면을 화면에 표시
        historyView.setVisible(true);
    }

    /**
     * 다른 하위 컨트롤러(SeatController 등)에서 취소/뒤로가기 버튼을 눌렀을 때,
     * 다시 메인 화면으로 복귀하도록 열어둔(Public) 인터페이스 메서드입니다.
     */
    public void goMainView() {
        // 이전 선택 기록을 초기화하여 새로운 예매를 준비 (상태 초기화)
        this.selectedMovie = null;
        this.selectedShowInfo = null;

        System.out.println("[시스템] 작업이 취소/종료되어 메인 화면으로 복귀합니다.");

        // 숨겨두었던 메인 화면을 다시 가시화
        if (this.mainView != null) {
            this.mainView.setVisible(true);
        }
    }
}

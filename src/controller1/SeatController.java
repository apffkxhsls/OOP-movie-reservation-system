package controller1;

import java.util.ArrayList;
import java.util.List;

import model.Seat;
import model.ShowInfo;
import view.SeatView;
import view.listener.SeatViewListener;
import java.util.function.BiConsumer;

// SeatViewListener 구현을 통해 좌석 선택 화면에서 발생하는 이벤트를 처리하고, 사용자 좌석 선점 논리 검증 및 선택 데이터 관리를 수행하는 컨트롤러
public class SeatController implements SeatViewListener {

    private final UIProvider ui;
    private BiConsumer<ShowInfo, List<Seat>> proceedToPaymentAction;
    private Runnable openHistoryAction;

    private SeatManager seatManager;
    private List<Seat> tempSelectedSeats;
    private SeatView seatView;

    // 생성자: 알림을 위한 UIProvider와 다른 화면(결제 화면, 내역 화면)으로 넘어가기 위한 콜백 함수(Action)들을 외부로부터
    // 주입받아 초기화
    public SeatController(UIProvider ui, BiConsumer<ShowInfo, List<Seat>> proceedToPaymentAction,
            Runnable openHistoryAction) {
        this.ui = ui;
        this.proceedToPaymentAction = proceedToPaymentAction;
        this.openHistoryAction = openHistoryAction;
        this.tempSelectedSeats = new ArrayList<>();
    }

    // 사용자가 선택한 상영 정보를 바탕으로 SeatManager와 SeatView를 새롭게 구성하고 좌석 선택 프로세스 화면을 사용자에게 띄움
    public void startSeatSelection(ShowInfo showInfo) {
        this.seatManager = new SeatManager(showInfo);
        this.tempSelectedSeats.clear();

        this.seatView = new SeatView(showInfo);
        this.seatView.setListener(this);
        this.seatView.setVisible(true);
    }

    // 동기화 블록(synchronized)을 활용해 동시성 문제를 방지하며 특정 좌석의 존재 유무 및 예매 여부를 검증하고, 문제가 없으면 임시
    // 선택 목록에 좌석을 등록
    public boolean selectSeat(String seatName) {
        synchronized (seatManager.getShowInfo()) {
            Seat seat = seatManager.findSeat(seatName);

            if (seat == null) {
                ui.showErrorMessage("존재하지 않는 좌석입니다: " + seatName, "오류");
                return false;
            }
            if (seat.isReserved()) {
                ui.showWarningMessage("이미 다른 사용자에 의해 예매된 좌석입니다: " + seatName, "예매 불가");
                return false;
            }
            if (tempSelectedSeats.contains(seat)) {
                ui.showInfoMessage("현재 세션에서 이미 선택하신 좌석입니다: " + seatName, "안내");
                return false;
            }

            tempSelectedSeats.add(seat);
            return true;
        }
    }

    // 최소 하나 이상의 좌석이 선택되었는지 검사한 후, 초기화 시 등록된 콜백(proceedToPaymentAction)을 호출하여 선택된 좌석
    // 정보와 함께 결제 프로세스로 전환
    public void confirmSeatSelection() {
        if (tempSelectedSeats.isEmpty()) {
            ui.showInfoMessage("최소 한 개 이상의 좌석을 선택하셔야 합니다.", "안내");
            return;
        }
        if (this.proceedToPaymentAction != null) {
            this.proceedToPaymentAction.accept(this.seatManager.getShowInfo(), List.copyOf(this.tempSelectedSeats));
        }
        if (this.seatView != null) {
            this.seatView.setVisible(false);
            this.seatView.dispose();
        }
    }

    // 사용자가 '다음(Next)' 버튼을 클릭했을 때 선택된 좌석 목록 전체에 대해 동시 선점 여부 등을 2차 검증하고, 검증 실패 시 에러
    // 메시지 출력 후 초기화
    @Override
    public void onNextButtonClicked(List<String> selectedSeats) {
        this.tempSelectedSeats.clear();
        List<String> failedSeats = new ArrayList<>();

        for (String seatName : selectedSeats) {
            if (!selectSeat(seatName)) {
                failedSeats.add(seatName);
            }
        }

        if (failedSeats.isEmpty()) {
            confirmSeatSelection();
        } else {
            String failedListStr = String.join(", ", failedSeats);
            ui.showErrorMessage("다음 좌석은 동시 선점되어 예매가 불가능합니다:\n[" + failedListStr + "]\n다시 선택해 주세요.", "동기화 충돌");
            this.tempSelectedSeats.clear();
        }
    }

    // 좌석 선택 중 사용자가 '내역 보기'를 클릭했을 때 현재 작업 중인 뷰를 닫고 임시 데이터를 정리한 후, 등록된
    // 콜백(openHistoryAction)을 통해 내역 화면으로 복귀
    @Override
    public void onHistoryButtonClicked() {
        this.seatManager = null;
        this.tempSelectedSeats.clear();
        if (this.seatView != null) {
            this.seatView.setVisible(false);
            this.seatView.dispose();
        }
        if (this.openHistoryAction != null) {
            this.openHistoryAction.run();
        }
    }
}
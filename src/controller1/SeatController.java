package controller1;

import java.util.ArrayList;
import java.util.List;

import model.Seat;
import model.ShowInfo;
import view.SeatView;
import view.listener.SeatViewListener;
import java.util.function.BiConsumer;

public class SeatController implements SeatViewListener {

    private final UIProvider ui; // Adapter 패턴
    private BiConsumer<ShowInfo, List<Seat>> proceedToPaymentAction;
    private Runnable openHistoryAction;

    private SeatManager seatManager; // Manager 패턴
    private List<Seat> tempSelectedSeats;
    private SeatView seatView;

    public SeatController(UIProvider ui, BiConsumer<ShowInfo, List<Seat>> proceedToPaymentAction,
            Runnable openHistoryAction) {
        this.ui = ui;
        this.proceedToPaymentAction = proceedToPaymentAction;
        this.openHistoryAction = openHistoryAction;
        this.tempSelectedSeats = new ArrayList<>();
    }

    public void startSeatSelection(ShowInfo showInfo) {
        this.seatManager = new SeatManager(showInfo);
        this.tempSelectedSeats.clear();

        this.seatView = new SeatView(showInfo); // 제약사항으로 인해 허용된 불가피한 new
        this.seatView.setListener(this);
        this.seatView.setVisible(true);
    }

    public boolean selectSeat(String seatName) {
        // [핵심 기획 반영] SeatManager를 활용한 실시간 동기화 (동일 상영관 동시 접근 차단)
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

    public void confirmSeatSelection() {
        if (tempSelectedSeats.isEmpty()) {
            ui.showInfoMessage("최소 한 개 이상의 좌석을 선택하셔야 합니다.", "안내");
            return;
        }
        if (this.proceedToPaymentAction != null) {
            // 방어적 복사(List.copyOf)를 사용하여 외부 변조 방지 (캡슐화 강화)
            this.proceedToPaymentAction.accept(this.seatManager.getShowInfo(), List.copyOf(this.tempSelectedSeats));
        }
        if (this.seatView != null) {
            this.seatView.setVisible(false);
            this.seatView.dispose();
        }
    }

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
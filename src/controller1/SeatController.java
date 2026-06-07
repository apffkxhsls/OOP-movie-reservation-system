package controller1;

import java.util.ArrayList;
import java.util.List;
import javax.swing.JOptionPane;

import model.Seat;
import model.ShowInfo;
import view.SeatView;
import view.listener.SeatViewListener;
import java.util.function.BiConsumer;

public class SeatController implements SeatViewListener {

    private BiConsumer<ShowInfo, List<Seat>> proceedToPaymentAction;
    private Runnable openHistoryAction;

    private ShowInfo currentShowInfo;
    private List<Seat> tempSelectedSeats;

    private SeatView seatView;

    public SeatController(BiConsumer<ShowInfo, List<Seat>> proceedToPaymentAction,
            Runnable openHistoryAction) {
        this.proceedToPaymentAction = proceedToPaymentAction;
        this.openHistoryAction = openHistoryAction;
        this.tempSelectedSeats = new ArrayList<>();
    }

    public void startSeatSelection(ShowInfo showInfo) {
        this.currentShowInfo = showInfo;
        this.tempSelectedSeats.clear();

        this.seatView = new SeatView(this.currentShowInfo);
        this.seatView.setListener(this);
        this.seatView.setVisible(true);
    }

    private Seat findSeat(String seatName) {
        Seat[][] seats = currentShowInfo.getTheater().getSeats();
        int rows = currentShowInfo.getTheater().getRows();
        int cols = currentShowInfo.getTheater().getCols();

        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                if (seats[i][j].getSeatId().equals(seatName)) {
                    return seats[i][j];
                }
            }
        }
        return null;
    }

    public boolean selectSeat(String seatName) {
        synchronized (currentShowInfo) {
            Seat seat = findSeat(seatName);

            if (seat == null) {
                JOptionPane.showMessageDialog(seatView, "존재하지 않는 좌석입니다: " + seatName, "오류", JOptionPane.ERROR_MESSAGE);
                return false;
            }
            if (seat.isReserved()) {
                JOptionPane.showMessageDialog(seatView, "이미 다른 사용자에 의해 예매된 좌석입니다: " + seatName, "예매 불가",
                        JOptionPane.WARNING_MESSAGE);
                return false;
            }
            if (tempSelectedSeats.contains(seat)) {
                JOptionPane.showMessageDialog(seatView, "현재 세션에서 이미 선택하신 좌석입니다: " + seatName, "안내",
                        JOptionPane.INFORMATION_MESSAGE);
                return false;
            }

            tempSelectedSeats.add(seat);
            return true;
        }
    }

    public void confirmSeatSelection() {
        if (tempSelectedSeats.isEmpty()) {
            JOptionPane.showMessageDialog(seatView, "최소 한 개 이상의 좌석을 선택하셔야 합니다.", "안내", JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        if (this.proceedToPaymentAction != null) {
            this.proceedToPaymentAction.accept(this.currentShowInfo, this.tempSelectedSeats);
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
            JOptionPane.showMessageDialog(seatView, "다음 좌석은 동시 선점되어 예매가 불가능합니다:\n[" + failedListStr + "]\n다시 선택해 주세요.",
                    "동기화 충돌", JOptionPane.ERROR_MESSAGE);
            this.tempSelectedSeats.clear();
        }
    }

    @Override
    public void onHistoryButtonClicked() {
        this.currentShowInfo = null;
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
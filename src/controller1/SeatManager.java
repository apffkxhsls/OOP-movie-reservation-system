package controller1;

import model.Seat;
import model.ShowInfo;
import model.theater.Theater;

// 2차원 배열 좌석 탐색을 캡슐화하여 디미터 법칙을 준수하게 해주는 헬퍼 객체
public class SeatManager {
    private final ShowInfo showInfo;

    public SeatManager(ShowInfo showInfo) {
        this.showInfo = showInfo;
    }

    public ShowInfo getShowInfo() {
        return showInfo;
    }

    // 컨트롤러 대신 복잡한 2중 for문 배열 탐색을 전담
    public Seat findSeat(String seatName) {
        Theater theater = showInfo.getTheater();
        Seat[][] seats = theater.getSeats();
        int rows = theater.getRows();
        int cols = theater.getCols();

        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                if (seats[i][j].getSeatId().equals(seatName)) {
                    return seats[i][j];
                }
            }
        }
        return null; // 못 찾았을 경우
    }
}
package controller1;

import model.Seat;
import model.ShowInfo;
import model.theater.Theater;

// 상영 정보(ShowInfo)를 바탕으로 해당 상영관(Theater)의 2차원 좌석 상태를 확인하고 검색하는 역할을 하는 관리 클래스
public class SeatManager {
    // 관리 대상이 되는 특정 상영 정보 참조
    private final ShowInfo showInfo;

    // 외부로부터 상영 정보 객체를 주입받아 SeatManager 내부 상태를 초기화
    public SeatManager(ShowInfo showInfo) {
        this.showInfo = showInfo;
    }

    // 현재 SeatManager가 관리 중인 상영 정보(ShowInfo) 객체를 반환
    public ShowInfo getShowInfo() {
        return showInfo;
    }

    // 전달받은 좌석 이름(예: A1, B2)을 바탕으로 전체 좌석을 탐색하여 일치하는 좌석 객체를 검색 후 반환
    public Seat findSeat(String seatName) {
        Theater theater = showInfo.getTheater();
        Seat[][] seats = theater.getSeats();
        int rows = theater.getRows();
        int cols = theater.getCols();

        // 상영관의 행(row)과 열(col)을 기준으로 전체 2차원 좌석 배열을 순회하며 일치하는 ID를 가진 좌석 탐색
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                if (seats[i][j].getSeatId().equals(seatName)) {
                    return seats[i][j];
                }
            }
        }
        return null;
    }
}
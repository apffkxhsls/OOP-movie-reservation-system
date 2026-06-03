package controller2;

import model.Movie;
import model.Reservation;
import model.Seat;
import model.ShowInfo;
import model.theater.Theater;

import java.util.ArrayList;

public class PaymentController {

    private ReservationController reservationController;
    private static final int BOOKING_FEE = 1000;

    public PaymentController(ReservationController reservationController) {
        this.reservationController = reservationController;
    }

    // 상영관 종류별 금액 계산
    public int calculateTotalPrice(ShowInfo showInfo, ArrayList<Seat> selectedSeats) {

        // 전달받은 상영, 좌석 정보가 정상인지 확인
        if (showInfo == null) {
            System.out.println("상영 정보가 없습니다.");
            return -1;
        }

        if (selectedSeats == null || selectedSeats.isEmpty()) {
            System.out.println("선택된 좌석이 없습니다.");
            return -1;
        }

        Movie movie = showInfo.getMovie();
        Theater theater = showInfo.getTheater();

        if (movie == null) {
            System.out.println("영화 정보가 없습니다.");
            return -1;
        }

        if (theater == null) {
            System.out.println("상영관 정보가 없습니다.");
            return -1;
        }

        int oneSeatPrice = theater.calculatePrice(movie.getBasePrice());

        return oneSeatPrice * selectedSeats.size();

    }

    // 결제 버튼 클릭 시 실행- 결제 정보 확인,금액 계산, 좌석 최종 여부 확인, 결제 성공 여부 확인, 컨트롤러 예매 저장 요청
    public Reservation processPayment(ShowInfo showInfo, ArrayList<Seat> selectedSeats) {

        int totalPrice = calculateTotalPrice(showInfo, selectedSeats);
        int finalPrice = totalPrice + BOOKING_FEE;
        // 금액 한번 더 확인
        if (totalPrice < 0) {
            System.out.println("결제 실패: 금액 계산 오류");
            return null;
        }

        // 1. 결제 완료 여부 확인+ 좌석 최종 예약 처리
        boolean paymentSuccess = finishPayment(finalPrice, selectedSeats);
        if (!paymentSuccess) {
            System.out.println("결제 실패: 결제 또는 좌석 예약에 실패했습니다.");
            return null;
        }

        System.out.println("총 좌석 금액: " + totalPrice + "원");
        System.out.println("예매 수수료: " + BOOKING_FEE + "원");
        System.out.println("최종 결제 금액: " + finalPrice + "원");
        System.out.println("결제가 완료되었습니다.");

        // 2. 결제 성공 후 ReservationController에 저장 요청
        Reservation reservation = reservationController.saveReservation(showInfo, selectedSeats, totalPrice,
                BOOKING_FEE);

        // 3. 저장 실패 시 좌석 다시 취소
        if (reservation == null) {
            cancelSeats(selectedSeats);
            System.out.println("예매 저장 실패: 좌석 예약을 취소합니다.");
            return null;
        }

        return reservation;
    }

    // 결제 완료 여부 확인
    private boolean finishPayment(int finalPrice, ArrayList<Seat> selectedSeats) {

        if (finalPrice <= 0) {
            return false;
        }

        // 예약 성공 좌석들 저장 리스트
        ArrayList<Seat> reservedSeats = new ArrayList<>();

        for (Seat seat : selectedSeats) {

            if (seat == null) {
                cancelSeats(reservedSeats);
                return false;
            }

            boolean success = seat.reserve();

            if (!success) {
                cancelSeats(reservedSeats);
                return false;
            }

            reservedSeats.add(seat); // 성공 reservedSeats에 추가
        }

        return true;
    }

    // 결제 실패 또는 저장 실패 시 좌석 원상복구
    private void cancelSeats(ArrayList<Seat> seats) {

        for (Seat seat : seats) {
            if (seat != null) {
                seat.cancel();
            }
        }
    }
}
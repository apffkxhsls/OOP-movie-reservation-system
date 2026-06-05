package controller2;

import model.Movie;
import model.Reservation;
import model.Seat;
import model.ShowInfo;
import model.theater.Theater;

//좌석 여러 개를 리스트로 저장하기 위해 import.
import java.util.ArrayList;


// PaymnetController클래스: 결제 금액 계산, 좌석 예약 처리, 예매 요청 담당.
public class PaymentController {

    //PaymentController가 예매 저장을 직접 하지 않고, ReservationController에게 요청하기 위해 가지고 있는 변수. 
    private ReservationController reservationController; 
    // 좌석 1장당 수수료를 상수로 저장. 
    private static final int FEE_PER_SEAT = 1000;


    //PaymentController 객체를 만들 때 ReservationController을 전달받는 생성자. 
    public PaymentController(ReservationController reservationController) {
        this.reservationController = reservationController;

    }


    // 좌석 금액 총합을 계산하는 메소드
    public int calculateTotalPrice(ShowInfo showInfo, ArrayList<Seat> selectedSeats) {
    

        // 안전장치로 검사 - 전달받은 상영, 좌석 정보가 정상인지 확인
        if (showInfo == null) {
            System.out.println("상영 정보가 없습니다.");// 콘솔에 테스트용 출력
            return -1;//계산실패
        }
        if (selectedSeats == null || selectedSeats.isEmpty()) {
            System.out.println("선택된 좌석이 없습니다.");
            return -1;
        }

        // showInfo에서 영화, 상영관 정보 가져오기
        Movie movie = showInfo.getMovie();
        Theater theater = showInfo.getTheater();

        //영화 정보가 없을 경우 검사
        if (movie == null) {
            System.out.println("영화 정보가 없습니다.");
            return -1;
        }
        //상영관 정보가 없을 경우 검사
        if (theater == null) {
            System.out.println("상영관 정보가 없습니다.");
            return -1;
        }

        //좌석 1개의 가격을 계산//
        int oneSeatPrice = theater.calculatePrice(movie.getBasePrice());

        //좌석 1개 가격에 선택 좌석 개수를 곱해 총 좌석 금액 반환
        return oneSeatPrice * selectedSeats.size();

    }


    // 결제 처리를 하는 메소드: 결제 정보 확인, 금액 계산, 좌석 최종 여부 확인, 결제 성공 여부 확인, 컨토롤러 예매 저장 요청
    public Reservation processPayment(ShowInfo showInfo, ArrayList<Seat> selectedSeats) {

        //좌석 금액 총합 계산
        int seatTotalPrice = calculateTotalPrice(showInfo, selectedSeats);

        //금액 한번 더 확인  
        if (seatTotalPrice < 0) {
            //아까 계산 실패 시 -1을 반환했기 때문에, 0보다 작으면 실패.
            System.out.println("결제 실패: 금액 계산 오류");
            return null;
        }

        //예매 수수료 총액 계산
        int bookingFee = FEE_PER_SEAT * selectedSeats.size();

        //최종 결제 금액 계산
        int finalPrice = seatTotalPrice + bookingFee;

        // 결제 완료 여부 확인과 좌석 최종 예약 처리
        boolean paymentSuccess = finishPayment(finalPrice, selectedSeats);

        //결제 성공이 아닐 경우, null return.
        if(!paymentSuccess){
            System.out.println("결제 실패: 결제 또는 좌석 예약에 실패했습니다.");
            return null;
        }

        //콘솔에 테스트 용으로 출력.
        System.out.println("총 좌석 금액: " + seatTotalPrice + "원");
        System.out.println("예매 수수료: " + bookingFee + "원");
        System.out.println("최종 결제 금액: " + finalPrice + "원");
        System.out.println("결제가 완료되었습니다.");

        // 결제 성공 후 ReservationController에 예매 저장 요청
        Reservation reservation = reservationController.saveReservation(showInfo,selectedSeats,seatTotalPrice,bookingFee);

        // 저장 실패 시 좌석 다시 취소
        if (reservation == null) {
            cancelSeats(selectedSeats); 
            System.out.println("예매 저장 실패: 좌석 예약을 취소합니다.");
            return null;
        }
        return reservation;
        //저장 성공하면 Reservaion객체 반환
    }

    // 결제 완료 여부 확인하는 private 메소드
    private boolean finishPayment(int finalPrice, ArrayList<Seat> selectedSeats) {

        //최종 금액이 0원 이하면 실패 처리
        if (finalPrice <= 0) {
            return false;
        }
        
        //예약 성공 좌석들 임시 저장하는 리스트
        ArrayList<Seat> reservedSeats = new ArrayList<>();

        //선택된 좌석을 하나씩 꺼내서 반복하는 코드
        for (int i = 0; i < selectedSeats.size(); i++) {
            Seat seat = selectedSeats.get(i);

            //좌석 객체가 null이면 이미 예매한 좌석들 취소
            if (seat == null) {
                cancelSeats(reservedSeats);
                return false;
            }

            // 좌석 예약 시도하는 코드
            boolean success = seat.reserve();
            // Seat클래스 안의 reserve()는 이미 예약된 좌석이면 false, 예약 가능하면 true 반환. 
             
            // 좌석 예약 실패하면 앞에서 성공한 좌석 전부 원상복구하고 실패 처리
            if (!success) {
                cancelSeats(reservedSeats);
                return false;
            }

            //예약 성공한 좌석들을 reservedSeats에 저장.
            reservedSeats.add(seat); 
        }

        return true;
    }

    // 좌석 목록을 받아서 예약을 취소하는 메소드.
    private void cancelSeats(ArrayList<Seat> seats) {

        // i를 0부터 시작해서 seats리스트의 크기보다 작을 때까지 반복한다.
        for (int i = 0; i < seats.size(); i++) {
            Seat seat = seats.get(i); 

            if (seat != null) {
                //null이 아닌 경우 예약 취소 실행. 실패 시 좌석 상태를 다시 비워주는 역할을 함.
                seat.cancel();
            }
        }
    }
}
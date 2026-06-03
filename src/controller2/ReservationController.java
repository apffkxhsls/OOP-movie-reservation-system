package controller2;

import model.Reservation;
import model.ReservationRepository;
import model.Seat;
import model.ShowInfo;

import java.util.ArrayList;

public class ReservationController {

    private ReservationRepository repository;

    //ReservationController 생성자. 
    public ReservationController(ReservationRepository repository) {
        this.repository = repository;
    }

    // PaymentController가 결제 성공 후 호출하는 메소드
    public Reservation saveReservation(ShowInfo showInfo, ArrayList<Seat> selectedSeats, int totalPrice, int bookingFee) {
        if (showInfo == null) {
            System.out.println("예매 저장 실패: 상영 정보가 없습니다.");
            return null;
        }

        if (selectedSeats == null || selectedSeats.isEmpty()) {
            System.out.println("예매 저장 실패: 선택된 좌석이 없습니다.");
            return null;
        }

        if (totalPrice < 0 || bookingFee < 0) {
            System.out.println("예매 저장 실패: 금액 정보가 올바르지 않습니다.");
            return null;
        }

        //예매번호 만들어주는 메소드
        String reservationNumber = createReservationNumber();


        // 전달받은 예매 정보를 바탕으로 새로운 Reservation 객체를 생성
        Reservation reservation = new Reservation(reservationNumber,showInfo,selectedSeats,totalPrice,bookingFee); 

        repository.addReservation(reservation);


        System.out.println("예매 정보가 저장되었습니다.");
        System.out.println("예매 번호: " + reservationNumber);

        return reservation;
    }

    // 예매 취소
    public void cancelReservation(Reservation reservation) {


        //예매 정보가 없는 경우 확인
        if (reservation == null) {
            System.out.println("취소할 예매 정보가 없습니다.");
            return;
        }
        //이미 취소한 예매 상태일 경우 확인
        if (reservation.getStatus().equals("취소")) {
            System.out.println("이미 취소된 예매입니다.");
            return;
        }

        // 중요:
        // ReservationController가 직접 좌석 취소 로직을 만들지 않고
        // Reservation 객체의 cancelReservation()을 호출함
        reservation.cancelReservation();


        // 취소 상태를 파일에 다시 저장
        repository.saveToFile();


        System.out.println("예매가 취소되었습니다.");
    }



    // 예매 목록 반환하는 메소드(수정같은 것은 불가)
    // BookingHistoryView에서 이 메소드 부를 수 있음. 
    public ArrayList<Reservation> getReservationList() {

        return repository.getAllReservations();

    }

    // 전체 예매 정보 파일로 저장.
    public void saveAll() {
        repository.saveToFile();
        System.out.println("전체 예매 정보가 파일로 저장되었습니다.");
    }

    // 예매 번호 생성
    private String createReservationNumber() {

        return "R" + System.currentTimeMillis();
        //일단 현재 시간을 숫자로 반환하는 java기본메소드. 
    }
}
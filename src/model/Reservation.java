package model;

import java.util.ArrayList;

public class Reservation {
    private String reservationNumber;
    private ShowInfo showInfo;
    private ArrayList<Seat> seats;
    private int totalPrice;
    private int bookingFee;     // 추가
    private String status;

    public Reservation(String reservationNumber, ShowInfo showInfo, ArrayList<Seat> seats, int totalPrice, int bookingFee) {
        this.reservationNumber = reservationNumber;
        this.showInfo = showInfo;
        this.seats = seats;
        this.totalPrice = totalPrice;
        this.bookingFee = bookingFee;
        this.status = "예정";
    }

    public String getReservationNumber() { return reservationNumber; }
    public ShowInfo getShowInfo() { return showInfo; }
    public ArrayList<Seat> getSeats() { return seats; }
    public int getTotalPrice() { return totalPrice; }
    public int getBookingFee() { return bookingFee; }
    public int getFinalPrice() { return totalPrice + bookingFee; }  // 최종 결제금액
    public String getStatus() { return status; }

    public void cancelReservation() {
        this.status = "취소";
        for (Seat seat : seats) {
            seat.cancel();
        }
    }
}
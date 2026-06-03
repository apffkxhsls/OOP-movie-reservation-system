package model;

public class Seat {
    private String seatId;
    private boolean isReserved;

    public Seat(String seatId) {
        this.seatId = seatId;
        this.isReserved = false;
    }

    public String getSeatId() { return seatId; }
    public boolean isReserved() { return isReserved; }

    public boolean reserve() {
        if (this.isReserved) {
            return false;
        }
        this.isReserved = true;
        return true;
    }

    public void cancel() {
        this.isReserved = false;
    }
}
package model.theater;

import model.Seat;

public abstract class Theater {
    private String name;      
    private Seat[][] seats;

    public Theater(String name, int rows, int cols) {
        this.name = name;
        this.seats = new Seat[rows][cols];
        initializeSeats(rows, cols);
    }

    private void initializeSeats(int rows, int cols) {
        for (int i = 0; i < rows; i++) {
            char rowChar = (char) ('A' + i);
            for (int j = 0; j < cols; j++) {
                seats[i][j] = new Seat(rowChar + String.valueOf(j + 1));
            }
        }
    }

    public String getName() { return name; }
    public Seat[][] getSeats() { return seats; }

    // 상영관 좌석 행 수 반환 (추가)
    public int getRows() {
        return seats.length;
    }
    
    // 상영관 좌석 열 수 반환 (추가)
    public int getCols() { 
        return seats[0].length;
    }

    public abstract int calculatePrice(int basePrice);
}
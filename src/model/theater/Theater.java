package model.theater;

import model.Seat;

public abstract class Theater {
    private String name;      
    protected Seat[][] seats;  

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

    public abstract int calculatePrice(int basePrice);
}
package model;

import model.theater.Theater;

public class ShowInfo {
    private Movie movie;
    private Theater theater;
    private String time;
    private int availableSeats;

    public ShowInfo(Movie movie, Theater theater, String time, int availableSeats) {
        this.movie = movie;
        this.theater = theater;
        this.time = time;
        this.availableSeats = availableSeats;
    }

    public Movie getMovie() { return movie; }
    public Theater getTheater() { return theater; }
    public String getTime() { return time; }
    public int getAvailableSeats() { return availableSeats; }

    public void decreaseSeat() {
        if (availableSeats > 0) {
            availableSeats--;
        }
    }
}
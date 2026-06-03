package model;

public class Movie {
    private String title;
    private String genre;
    private int basePrice;
    private int durationMinutes;
    private String ageRating;

    public Movie(String title, String genre, int basePrice, int durationMinutes, String ageRating) {
        this.title = title;
        this.genre = genre;
        this.basePrice = basePrice;
        this.durationMinutes = durationMinutes;
        this.ageRating = ageRating;
    }

    public String getTitle() { return title; }
    public String getGenre() { return genre; }
    public int getBasePrice() { return basePrice; }
    public int getDurationMinutes() { return durationMinutes; }
    public String getAgeRating() { return ageRating; }
    // (추가) getRunningTime() 메서드 구현
    public int getRunningTime() {
        return durationMinutes;
    }
}
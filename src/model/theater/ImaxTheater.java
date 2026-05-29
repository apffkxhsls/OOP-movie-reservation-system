package model.theater;

public class ImaxTheater extends Theater {
    public ImaxTheater(String name, int rows, int cols) {
        super(name, rows, cols);
    }

    @Override
    public int calculatePrice(int basePrice) {
        return basePrice + 5000;
    }
}
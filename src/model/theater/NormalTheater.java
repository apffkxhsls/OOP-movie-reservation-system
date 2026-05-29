package model.theater;

public class NormalTheater extends Theater {
    public NormalTheater(String name, int rows, int cols) {
        super(name, rows, cols);
    }

    @Override
    public int calculatePrice(int basePrice) {
        return basePrice;
    }
}
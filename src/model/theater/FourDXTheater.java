package model.theater;

public class FourDXTheater extends Theater {
    public FourDXTheater(String name, int rows, int cols) {
        super(name, rows, cols);
    }

    @Override
    public int calculatePrice(int basePrice) {
        return basePrice + 10000;
    }
}
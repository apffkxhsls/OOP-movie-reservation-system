import controller1.MainController;
import model.ReservationRepository;

import javax.swing.SwingUtilities;

public class Main {
    public static void main(String[] args) {
        ReservationRepository repository = new ReservationRepository();

        SwingUtilities.invokeLater(() -> {
            MainController mainController = new MainController(repository);
            mainController.start();
        });
    }
}
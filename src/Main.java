import model.DummyData;
import model.Reservation;
import model.ReservationRepository;
import model.Seat;
import model.ShowInfo;
import view.BookingConfirmView;
import view.BookingHistoryView;
import view.MainView;
import view.SeatView;

import java.util.ArrayList;

import javax.swing.SwingUtilities;

public class Main {
    public static void main(String[] args) {
        ReservationRepository repository = new ReservationRepository();

        // 더미데이터 확인 출력 (나중에 Controller/View 연결 후 제거)
        System.out.println("=== 영화 목록 ===");
        DummyData.getMovies().forEach(m ->
            System.out.println(m.getTitle() + " / " + m.getGenre() + " / " + m.getBasePrice() + "원")
        );

        System.out.println("\n=== 상영관 목록 ===");
        DummyData.getTheaters().forEach(t ->
            System.out.println(t.getName())
        );

        // Controller 연결 후 아래 더미데이터 및 각 View 직접 실행 코드 제거
        ShowInfo showInfo = DummyData.getShowInfos().get(0);

        ArrayList<Seat> seats = new ArrayList<>();
        seats.add(new Seat("C4"));
        seats.add(new Seat("C5"));

        Reservation reservation = new Reservation("R001", showInfo, seats, 28000, 2000);
        repository.addReservation(reservation); // 테스트용 더미 예매 데이터 저장 (Controller 연결 후 제거)
        
        // MainView 실행 (Controller 연결 전)
        SwingUtilities.invokeLater(() -> {
            try {
                MainView mainView = new MainView();
                mainView.setVisible(true);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });

        // SeatView 실행 (Controller 연결 전)
        SwingUtilities.invokeLater(() -> {
            try {
                SeatView seatView = new SeatView(showInfo);
                seatView.setVisible(true);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });

        // BookingConfirm 실행 (Controller 연결 전)
        SwingUtilities.invokeLater(() -> {
            try {
                BookingConfirmView view = new BookingConfirmView();
                view.setReservation(reservation);
                view.setVisible(true);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });

        // BookingHistory 실행 (Controller 연결 전)
        SwingUtilities.invokeLater(() -> {
            try {
                ArrayList<Reservation> reservations = repository.getAllReservations();
                BookingHistoryView bookingHistoryView = new BookingHistoryView(reservations);
                bookingHistoryView.setVisible(true);
            } catch (Throwable e) {
                e.printStackTrace();
            }
        });
    }
}

// TODO: MainController 완성 후 아래처럼 교체
// MainController controller = new MainController(repository);
// controller.start();
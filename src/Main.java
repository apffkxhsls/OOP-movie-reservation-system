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

        ShowInfo showInfo = DummyData.getShowInfos().get(0);

        ArrayList<Seat> seats = new ArrayList<>();
        seats.add(new Seat("C4"));
        seats.add(new Seat("C5"));

        Reservation reservation = new Reservation("R001", showInfo, seats, 28000, 2000);
        // MainView 실행
        SwingUtilities.invokeLater(() -> {
            try {
                MainView mainView = new MainView();
                mainView.setVisible(true);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
        // SeatView 실행
        SwingUtilities.invokeLater(() -> {
            try {
                SeatView seatView = new SeatView(showInfo);
                seatView.setVisible(true);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
        // BookingConfirm 실행
        SwingUtilities.invokeLater(() -> {
            try {
                BookingConfirmView view = new BookingConfirmView();
                view.setReservation(reservation);
                view.setVisible(true);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });

        // BookingHistory 실행
        SwingUtilities.invokeLater(() -> {
            BookingHistoryView bookingHistoryView = new BookingHistoryView();

            bookingHistoryView.setLocation(500, 80);
            bookingHistoryView.setAlwaysOnTop(true);
            bookingHistoryView.setVisible(true);

            ArrayList<Reservation> reservations = new ArrayList<>();
            reservations.add(reservation);

            bookingHistoryView.setReservations(reservations);
            bookingHistoryView.revalidate();
            bookingHistoryView.repaint();

            bookingHistoryView.toFront();
            bookingHistoryView.requestFocus();

            SwingUtilities.invokeLater(() -> {
                bookingHistoryView.setAlwaysOnTop(false);
            });
        });
    }

        // TODO: MainController 완성 후 아래처럼 교체
        // MainController controller = new MainController(repository);
        // controller.start();
    }

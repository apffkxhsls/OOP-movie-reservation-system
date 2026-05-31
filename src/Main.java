import model.DummyData;
import model.ReservationRepository;
import view.MainView;
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

        // 메인 화면 실행 
        SwingUtilities.invokeLater(() -> {
            MainView mainView = new MainView();
            mainView.setVisible(true);
        });

        // TODO: MainController 완성 후 아래처럼 교체
        // MainController controller = new MainController(repository);
        // controller.start();
    }
}
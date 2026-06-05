package view;

import model.Reservation;
import model.ShowInfo;
import view.component.BreadCrumbPanel;
import view.component.HeaderPanel;
import view.component.SeatUtils;
import view.listener.BookingHistoryViewListener;
import view.component.StyledButton;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class BookingHistoryView extends JFrame {
    private BookingHistoryViewListener listener;

    private static final Color NAVY = HeaderPanel.NAVY;
    private static final Color BACKGROUND = new Color(230, 230, 230);
    private static final Color WHITE      = Color.WHITE;
    private static final Color TEXT       = new Color(40, 40, 40);
    private static final Color TEXT_GRAY  = new Color(100, 100, 100);
    private static final Color STATUS_GRAY = new Color(130, 136, 155);

    private JPanel tableBodyPanel;

    public BookingHistoryView(ArrayList<Reservation> reservations) {
        setTitle("BookingHistory");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setResizable(false);

        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        try {
            add(new HeaderPanel("홈으로"), BorderLayout.NORTH);
            add(createContentPanel(), BorderLayout.CENTER);
        } catch (Exception e) {
            e.printStackTrace();
        }

        // UI 구성 완료 후 데이터 세팅
        if (reservations != null) {
            setReservations(reservations);
        }
    }

    // Listener 등록
    public void setBookingHistoryViewListener(BookingHistoryViewListener listener) {
        this.listener = listener;

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    // 전체 레이아웃    
    private JPanel createContentPanel() {
        JPanel contentPanel = new JPanel(new BorderLayout());
        contentPanel.setBackground(BACKGROUND);

        // 브레드크럼 
        contentPanel.add(new BreadCrumbPanel(3), BorderLayout.NORTH);

        JPanel bodyPanel = new JPanel();
        bodyPanel.setBackground(BACKGROUND);
        bodyPanel.setLayout(new BoxLayout(bodyPanel, BoxLayout.Y_AXIS));
        bodyPanel.setBorder(BorderFactory.createEmptyBorder(28, 80, 28, 80));

        // 타이틀
        JLabel titleLabel = new JLabel("예매 내역 조회");
        titleLabel.setFont(new Font("맑은 고딕", Font.BOLD, 24));
        titleLabel.setForeground(TEXT);
        titleLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        // 필터 버튼 (전체 / 예매완료 / 취소)
        JPanel filterPanel = createFilterPanel();
        filterPanel.setAlignmentX(Component.LEFT_ALIGNMENT);

        // 예매 내역 테이블
        JPanel tablePanel = createTablePanel();
        tablePanel.setAlignmentX(Component.LEFT_ALIGNMENT);

        // 하단 이전으로 버튼
        JButton backButton = new StyledButton("이전으로", WHITE, NAVY, 110, 38);
        backButton.addActionListener(e -> {
            if (listener != null) listener.onBack();
        });

        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 0, 0));
        bottomPanel.setBackground(BACKGROUND);
        bottomPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 60));
        bottomPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        bottomPanel.add(backButton);

        bodyPanel.add(titleLabel);
        bodyPanel.add(Box.createVerticalStrut(14));
        bodyPanel.add(filterPanel);
        bodyPanel.add(Box.createVerticalStrut(20));
        bodyPanel.add(tablePanel);
        bodyPanel.add(Box.createVerticalStrut(16));
        bodyPanel.add(bottomPanel);

        contentPanel.add(bodyPanel, BorderLayout.CENTER);
        return contentPanel;
    }

    // 필터 패널 (전체 / 예매완료 / 취소)
    private JPanel createFilterPanel() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT, 12, 0));
        panel.setBackground(BACKGROUND);
        panel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 36));

        panel.add(StyledButton.navy("전체", 80, 32));
        panel.add(StyledButton.outline("예매완료", 90, 32));
        panel.add(StyledButton.outline("취소", 80, 32));    

        return panel;
    }

    // 테이블 패널 (헤더 + 바디)
    private JPanel createTablePanel() {
        JPanel tablePanel = new JPanel(new BorderLayout());
        tablePanel.setBackground(WHITE);
        tablePanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 420));
        tablePanel.setPreferredSize(new Dimension(940, 420));
        tablePanel.setAlignmentX(Component.LEFT_ALIGNMENT);

        // 테이블 헤더
        JPanel headerPanel = new JPanel(new GridLayout(1, 5));
        headerPanel.setBackground(NAVY);
        headerPanel.setPreferredSize(new Dimension(940, 58));
        headerPanel.add(createHeaderLabel("영화 제목"));
        headerPanel.add(createHeaderLabel("상영 일시"));
        headerPanel.add(createHeaderLabel("좌석"));
        headerPanel.add(createHeaderLabel("결제 금액"));
        headerPanel.add(createHeaderLabel("상태"));

        // 테이블 바디 (동적으로 행 추가)
        tableBodyPanel = new JPanel();
        tableBodyPanel.setBackground(WHITE);
        tableBodyPanel.setLayout(new BoxLayout(tableBodyPanel, BoxLayout.Y_AXIS));

        tablePanel.add(headerPanel,    BorderLayout.NORTH);
        tablePanel.add(tableBodyPanel, BorderLayout.CENTER);

        return tablePanel;
    }

    // 데이터 세팅 (외부에서 호출)
    public void setReservations(ArrayList<Reservation> reservations) {
        if (tableBodyPanel == null) return;
        tableBodyPanel.removeAll();

        if (reservations == null || reservations.isEmpty()) {
            tableBodyPanel.add(createEmptyPanel());
        } else {
            for (Reservation reservation : reservations) {
                tableBodyPanel.add(createReservationRow(reservation));
            }
        }

        tableBodyPanel.revalidate();
        tableBodyPanel.repaint();
    }

    // 데이터 없을 때 표시
    private JPanel createEmptyPanel() {
        JPanel panel = new JPanel(new GridBagLayout()); 
        panel.setBackground(WHITE);
        panel.setPreferredSize(new Dimension(940, 362)); // 테이블 바디 높이에 맞춤
        panel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 362));

        JLabel label = new JLabel("예매 내역이 없습니다.", SwingConstants.CENTER);
        label.setFont(new Font("맑은 고딕", Font.BOLD, 15));
        label.setForeground(TEXT_GRAY);

        panel.add(label); 
        return panel;
    }

    // 예매 1건 행 생성
    private JPanel createReservationRow(Reservation reservation) {
        JPanel row = new JPanel(new GridLayout(1, 5));
        row.setBackground(WHITE);
        row.setPreferredSize(new Dimension(940, 78));
        row.setMaximumSize(new Dimension(Integer.MAX_VALUE, 78));

        String movieText = "-";
        String timeText  = "-";
        String seatText  = "-";
        String priceText = "-";

        if (reservation != null) {
            ShowInfo showInfo = reservation.getShowInfo();

            if (showInfo != null) {
                if (showInfo.getMovie() != null) {
                    movieText = "<html>" + showInfo.getMovie().getTitle() + "<br>"
                            + showInfo.getMovie().getGenre() + " / "
                            + showInfo.getMovie().getDurationMinutes() + "분</html>";
                }

                String theaterName = (showInfo.getTheater() != null)
                        ? showInfo.getTheater().getName() : "-";

                timeText = "<html>" + formatDate(showInfo.getTime()) + "<br>"
                        + formatTime(showInfo.getTime()) + " / " + theaterName + "</html>";
            }

            seatText = SeatUtils.getSeatText(reservation.getSeats());
            priceText = String.format("%,d원", reservation.getFinalPrice());
        }

        row.add(createBodyLabel(movieText));
        row.add(createBodyLabel(timeText));
        row.add(createBodyLabel(seatText));
        row.add(createBodyLabel(priceText));
        // 상태는 reservation에서 가져옴 
        row.add(createStatusPanel(reservation != null ? reservation.getStatus() : "-"));

        return row;
    }
    
    // UI 컴포넌트
    private JLabel createHeaderLabel(String text) {
        JLabel label = new JLabel(text, SwingConstants.CENTER);
        label.setFont(new Font("맑은 고딕", Font.BOLD, 16));
        label.setForeground(Color.GRAY);
        return label;
    }

    private JLabel createBodyLabel(String text) {
        JLabel label = new JLabel(text, SwingConstants.CENTER);
        label.setFont(new Font("맑은 고딕", Font.BOLD, 13));
        label.setForeground(TEXT_GRAY);
        return label;
    }

    private JPanel createStatusPanel(String text) {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(WHITE);

        JLabel badge = new JLabel(text, SwingConstants.CENTER);
        badge.setOpaque(true);
        badge.setBackground(STATUS_GRAY);
        badge.setForeground(WHITE);
        badge.setFont(new Font("맑은 고딕", Font.BOLD, 11));
        badge.setPreferredSize(new Dimension(82, 30));

        panel.add(badge);
        return panel;
    }

    // 날짜/시간 
    private String formatDate(String time) {
        if (time == null || time.length() < 10) return "-";
        return time.substring(0, 10);
    }

    private String formatTime(String time) {
        if (time == null) return "-";
        int idx = time.lastIndexOf(" ");
        if (idx < 0 || idx + 5 > time.length()) return "-";
        return time.substring(idx + 1);
    }
}
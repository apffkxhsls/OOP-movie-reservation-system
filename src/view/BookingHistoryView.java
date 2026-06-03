package view;

import model.Reservation;
import model.Seat;
import model.ShowInfo;
import view.component.BreadCrumbPanel;
import view.component.HeaderPanel;
import view.listener.BookingHistoryViewListener;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class BookingHistoryView extends JFrame {
    private BookingHistoryViewListener listener;

    private static final Color NAVY = new Color(43, 53, 88);
    private static final Color BACKGROUND = new Color(234, 235, 239);
    private static final Color WHITE = Color.WHITE;
    private static final Color TEXT = new Color(40, 40, 40);
    private static final Color TEXT_GRAY = new Color(100, 100, 100);
    private static final Color STATUS_GRAY = new Color(130, 136, 155);

    private JPanel tableBodyPanel;

    public BookingHistoryView() {
        setTitle("BookingHistory");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(1040, 680);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        try {
            add(new HeaderPanel("홈으로"), BorderLayout.NORTH);
            add(createContentPanel(), BorderLayout.CENTER);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public BookingHistoryView(ArrayList<Reservation> reservations) {
        this();
        setReservations(reservations);
    }

    public void setBookingHistoryViewListener(BookingHistoryViewListener listener) {
        this.listener = listener;
    }

    private JPanel createContentPanel() {
        JPanel contentPanel = new JPanel(new BorderLayout());
        contentPanel.setBackground(BACKGROUND);

        contentPanel.add(new BreadCrumbPanel(3), BorderLayout.NORTH);

        JPanel bodyPanel = new JPanel();
        bodyPanel.setBackground(BACKGROUND);
        bodyPanel.setLayout(new BoxLayout(bodyPanel, BoxLayout.Y_AXIS));
        bodyPanel.setBorder(BorderFactory.createEmptyBorder(28, 50, 28, 50));

        JLabel titleLabel = new JLabel("예매 내역 조회");
        titleLabel.setFont(new Font("맑은 고딕", Font.BOLD, 24));
        titleLabel.setForeground(TEXT);
        titleLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        JPanel filterPanel = createFilterPanel();
        filterPanel.setAlignmentX(Component.LEFT_ALIGNMENT);

        JPanel tablePanel = createTablePanel();
        tablePanel.setAlignmentX(Component.LEFT_ALIGNMENT);

        JButton backButton = createButton("이전으로", WHITE, NAVY, 110, 38);
        backButton.addActionListener(e -> {
            if (listener != null) {
                listener.onBack();
            }
        });

        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 0, 0));
        bottomPanel.setBackground(BACKGROUND);
        bottomPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 60));
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

    private JPanel createFilterPanel() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT, 12, 0));
        panel.setBackground(BACKGROUND);
        panel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 36));

        panel.add(createButton("전체", NAVY, WHITE, 80, 32));
        panel.add(createButton("예매완료", WHITE, NAVY, 90, 32));
        panel.add(createButton("취소", WHITE, NAVY, 80, 32));

        return panel;
    }

    private JPanel createTablePanel() {
        JPanel tablePanel = new JPanel(new BorderLayout());
        tablePanel.setBackground(WHITE);
        tablePanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 420));
        tablePanel.setPreferredSize(new Dimension(940, 420));

        JPanel headerPanel = new JPanel(new GridLayout(1, 5));
        headerPanel.setBackground(NAVY);
        headerPanel.setPreferredSize(new Dimension(940, 58));

        headerPanel.add(createHeaderLabel("영화 제목"));
        headerPanel.add(createHeaderLabel("상영 일시"));
        headerPanel.add(createHeaderLabel("좌석"));
        headerPanel.add(createHeaderLabel("결제 금액"));
        headerPanel.add(createHeaderLabel("상태"));

        tableBodyPanel = new JPanel();
        tableBodyPanel.setBackground(WHITE);
        tableBodyPanel.setLayout(new BoxLayout(tableBodyPanel, BoxLayout.Y_AXIS));

        tablePanel.add(headerPanel, BorderLayout.NORTH);
        tablePanel.add(tableBodyPanel, BorderLayout.CENTER);

        return tablePanel;
    }

    public void setReservations(ArrayList<Reservation> reservations) {
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

    private JPanel createEmptyPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(WHITE);
        panel.setPreferredSize(new Dimension(940, 100));
        panel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 100));

        JLabel label = new JLabel("예매 내역이 없습니다.", SwingConstants.CENTER);
        label.setFont(new Font("맑은 고딕", Font.BOLD, 15));
        label.setForeground(TEXT_GRAY);

        panel.add(label, BorderLayout.CENTER);
        return panel;
    }

    private JPanel createReservationRow(Reservation reservation) {
        JPanel row = new JPanel(new GridLayout(1, 5));
        row.setBackground(WHITE);
        row.setPreferredSize(new Dimension(940, 78));
        row.setMaximumSize(new Dimension(Integer.MAX_VALUE, 78));

        String movieText = "-";
        String timeText = "-";
        String seatText = "-";
        String priceText = "-";

        if (reservation != null) {
            ShowInfo showInfo = reservation.getShowInfo();

            if (showInfo != null) {
                if (showInfo.getMovie() != null) {
                    movieText = "<html>" + showInfo.getMovie().getTitle() + "<br>"
                            + showInfo.getMovie().getGenre() + " / "
                            + showInfo.getMovie().getRunningTime() + "분</html>";
                }

                String theaterName = "-";
                if (showInfo.getTheater() != null) {
                    theaterName = showInfo.getTheater().getName();
                }

                timeText = "<html>" + formatDate(showInfo.getTime()) + "<br>"
                        + formatTime(showInfo.getTime()) + " / " + theaterName + "</html>";
            }

            seatText = getSeatText(reservation.getSeats());
            priceText = String.format("%,d원", reservation.getFinalPrice());
        }

        row.add(createBodyLabel(movieText));
        row.add(createBodyLabel(timeText));
        row.add(createBodyLabel(seatText));
        row.add(createBodyLabel(priceText));
        row.add(createStatusPanel("예매 완료"));

        return row;
    }

    private JLabel createHeaderLabel(String text) {
        JLabel label = new JLabel(text, SwingConstants.CENTER);
        label.setFont(new Font("맑은 고딕", Font.BOLD, 16));
        label.setForeground(WHITE);
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

    private JButton createButton(String text, Color bg, Color fg, int width, int height) {
        JButton button = new JButton(text);
        button.setPreferredSize(new Dimension(width, height));
        button.setBackground(bg);
        button.setForeground(fg);
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setFont(new Font("맑은 고딕", Font.BOLD, 12));
        return button;
    }

    private String getSeatText(ArrayList<Seat> seats) {
        if (seats == null || seats.isEmpty()) {
            return "-";
        }

        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < seats.size(); i++) {
            if (i > 0) sb.append(", ");
            sb.append(seats.get(i).getSeatId());
        }
        return sb.toString();
    }

    private String formatDate(String time) {
        if (time == null || time.length() < 10) return "-";
        return time.substring(2, 10).replace("-", ".");
    }

    private String formatTime(String time) {
        if (time == null || time.length() < 16) return "-";
        return time.substring(11, 16);
    }
}
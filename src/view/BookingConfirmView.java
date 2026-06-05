package view;

import model.Movie;
import model.Reservation;
import model.ShowInfo;
import view.component.BreadCrumbPanel;
import view.component.HeaderPanel;
import view.listener.BookingConfirmViewListener;
import view.component.StyledButton;
import view.component.SeatUtils;

import javax.swing.*;
import java.awt.*;

public class BookingConfirmView extends JFrame {
    // 버튼 이벤트를 Controller에 전달하기 위한 Listener
    private BookingConfirmViewListener listener;

    // 예매 정보 표시 라벨
    private JLabel movieTitleValueLabel;
    private JLabel movieGenreValueLabel;
    private JLabel movieAgeValueLabel;

    private JLabel showTimeValueLabel;
    private JLabel theaterValueLabel;
    private JLabel seatValueLabel;

    private JLabel seatPriceValueLabel;
    private JLabel bookingFeeValueLabel;
    private JLabel finalPriceLabel;

    // 공통 색상
    private static final Color NAVY = HeaderPanel.NAVY;
    private static final Color BACKGROUND = new Color(230, 230, 230);

    // 화면 생성 및 UI 구현
    public BookingConfirmView() {
        setTitle("BookingConfirm");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1040, 680);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        add(new HeaderPanel("예매내역 조회"), BorderLayout.NORTH);
        add(createContentPanel(), BorderLayout.CENTER);
    }

    // Listener 등록
    public void setBookingConfirmViewListener(BookingConfirmViewListener listener) {
        this.listener = listener;
    }

    // 전체 화면 구성
    private JPanel createContentPanel() {
        JPanel contentPanel = new JPanel(new BorderLayout());
        contentPanel.setBackground(BACKGROUND);

        // 브레드크럼
        contentPanel.add(new BreadCrumbPanel(2), BorderLayout.NORTH);

        JPanel bodyPanel = new JPanel();
        bodyPanel.setBackground(BACKGROUND);
        bodyPanel.setLayout(new BoxLayout(bodyPanel, BoxLayout.Y_AXIS));
        bodyPanel.setBorder(BorderFactory.createEmptyBorder(20, 40, 8, 40));

        movieTitleValueLabel = new JLabel();
        movieGenreValueLabel = new JLabel();
        movieAgeValueLabel = new JLabel();

        showTimeValueLabel = new JLabel();
        theaterValueLabel = new JLabel();
        seatValueLabel = new JLabel();

        seatPriceValueLabel = new JLabel();
        bookingFeeValueLabel = new JLabel();

        finalPriceLabel = new JLabel("0원");

        JPanel titlePanel = createTitlePanel();

        JPanel movieCard = createInfoCard(
                "영화 정보",
                new String[]{"영화 제목", "장르 / 러닝타임", "관람등급"},
                new JLabel[]{movieTitleValueLabel, movieGenreValueLabel, movieAgeValueLabel}
        );

        JPanel showCard = createInfoCard(
                "상영 정보",
                new String[]{"상영일시", "상영관", "선택 좌석"},
                new JLabel[]{showTimeValueLabel, theaterValueLabel, seatValueLabel}
        );

        JPanel paymentCard = createInfoCard(
                "결제 정보",
                new String[]{"좌석 금액", "예매 수수료"},
                new JLabel[]{seatPriceValueLabel, bookingFeeValueLabel}
        );

        JPanel finalPricePanel = createFinalPricePanel();
        JPanel buttonPanel = createButtonPanel();

        bodyPanel.add(titlePanel);
        bodyPanel.add(Box.createVerticalStrut(10));
        bodyPanel.add(movieCard);
        bodyPanel.add(Box.createVerticalStrut(10));
        bodyPanel.add(showCard);
        bodyPanel.add(Box.createVerticalStrut(10));
        bodyPanel.add(paymentCard);
        bodyPanel.add(Box.createVerticalStrut(8));
        bodyPanel.add(finalPricePanel);
        bodyPanel.add(Box.createVerticalStrut(8));
        bodyPanel.add(buttonPanel);

        contentPanel.add(bodyPanel, BorderLayout.CENTER);

        return contentPanel;
    }

    // 왼쪽 상단 "결제" 제목 표시
    private JPanel createTitlePanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(BACKGROUND);
        panel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 34));
        panel.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel titleLabel = new JLabel("결제");
        titleLabel.setFont(new Font("맑은 고딕", Font.BOLD, 22));
        titleLabel.setHorizontalAlignment(SwingConstants.LEFT);

        panel.add(titleLabel, BorderLayout.WEST);

        return panel;
    }

    // 공통 카드 패널: 영화 정보, 상영 정보, 결제 정보
private JPanel createInfoCard(String title, String[] labels, JLabel[] valueLabels) {
    JPanel card = new JPanel(new BorderLayout());
    card.setBackground(Color.WHITE);
    card.setBorder(BorderFactory.createEmptyBorder(14, 22, 14, 22));
    card.setMaximumSize(new Dimension(Integer.MAX_VALUE, 112));
    card.setAlignmentX(Component.LEFT_ALIGNMENT);

    JLabel titleLabel = new JLabel(title);
    titleLabel.setFont(new Font("맑은 고딕", Font.BOLD, 18));

    // 왼쪽 라벨과 오른쪽 값을 행 단위로 맞추기 위한 패널
    JPanel infoPanel = new JPanel(new GridLayout(labels.length, 2, 40, 2));
    infoPanel.setOpaque(false);

    for (int i = 0; i < labels.length; i++) {
        JLabel label = new JLabel(labels[i]);
        label.setFont(new Font("맑은 고딕", Font.PLAIN, 13));
        label.setForeground(Color.GRAY);

        valueLabels[i].setFont(new Font("맑은 고딕", Font.BOLD, 14));
        valueLabels[i].setHorizontalAlignment(SwingConstants.LEFT);

        JPanel valueWrapper = new JPanel(new BorderLayout());
        valueWrapper.setOpaque(false);
        valueWrapper.setBorder(
            BorderFactory.createEmptyBorder(0, 180, 0, 0)
        );

        valueWrapper.add(valueLabels[i], BorderLayout.WEST);

        infoPanel.add(label);
        infoPanel.add(valueWrapper);
    }

    // 제목과 정보 행들을 세로로 배치
    JPanel contentPanel = new JPanel(new BorderLayout());
    contentPanel.setOpaque(false);
    contentPanel.add(titleLabel, BorderLayout.NORTH);
    contentPanel.add(infoPanel, BorderLayout.CENTER);

    card.add(contentPanel, BorderLayout.CENTER);

    return card;
}

    // 최종 금액 표시
    private JPanel createFinalPricePanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(new Color(245, 246, 250));
        panel.setBorder(BorderFactory.createEmptyBorder(8, 24, 8, 36));
        panel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 44));
        panel.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel label = new JLabel("최종 결제 금액");
        label.setFont(new Font("맑은 고딕", Font.BOLD, 18));
        label.setForeground(NAVY);

        finalPriceLabel.setFont(new Font("맑은 고딕", Font.BOLD, 20));
        finalPriceLabel.setForeground(NAVY);
        finalPriceLabel.setHorizontalAlignment(SwingConstants.RIGHT);

        panel.add(label, BorderLayout.WEST);
        panel.add(finalPriceLabel, BorderLayout.EAST);

        return panel;
    }

    // 이전으로, 다음 단계 버튼
    private JPanel createButtonPanel() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 14, 0));
        panel.setBackground(BACKGROUND);
        panel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 36));
        panel.setAlignmentX(Component.LEFT_ALIGNMENT);

        JButton backButton = StyledButton.outline("이전으로");
        JButton nextButton = StyledButton.navy("다음 단계");

        backButton.addActionListener(e -> {
            if (listener != null) listener.onBack();
        });

        nextButton.addActionListener(e -> {
            if (listener != null) listener.onNext();
        });

        panel.add(backButton);
        panel.add(nextButton);

        return panel;
    }

    // Reservation 객체의 정보를 화면에 표시
    public void setReservation(Reservation reservation) {
        ShowInfo showInfo = reservation.getShowInfo();
        Movie movie = showInfo.getMovie();

        movieTitleValueLabel.setText(movie.getTitle());
        movieGenreValueLabel.setText(movie.getGenre() + " / " + movie.getDurationMinutes() + "분");
        movieAgeValueLabel.setText(movie.getAgeRating());

        showTimeValueLabel.setText(showInfo.getTime());
        theaterValueLabel.setText(showInfo.getTheater().getName());
        seatValueLabel.setText(SeatUtils.getSeatTextWithCount(reservation.getSeats()));

        seatPriceValueLabel.setText(String.format("%,d원", reservation.getTotalPrice()));
        bookingFeeValueLabel.setText(String.format("%,d원", reservation.getBookingFee()));

        finalPriceLabel.setText(String.format("%,d원", reservation.getFinalPrice()));
    }
}
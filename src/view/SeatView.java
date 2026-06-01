package view;

import model.ShowInfo;
import view.component.BreadCrumbPanel;
import view.component.HeaderPanel;
import view.component.SeatButton;
import view.component.SeatButton.State;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class SeatView extends JFrame {

    private static final Color BG = new Color(0xEE, 0xEE, 0xEE);
    private static final Color NAVY = new Color(0x2B, 0x35, 0x58);
    private static final Color CARD_BG = Color.WHITE;
    private static final Color BOTTOM_BG = new Color(0xF7, 0xF8, 0xFC);

    private final ShowInfo showInfo;
    private final List<String> selectedSeats = new ArrayList<>();

    private JLabel selectedSeatLabel;
    private JLabel priceLabel;

    public SeatView(ShowInfo showInfo) {
        this.showInfo = showInfo;

        setTitle("Seat");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1040, 680);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        add(new HeaderPanel("예매내역 조회"), BorderLayout.NORTH);

        JPanel contentPanel = new JPanel(new BorderLayout());
        contentPanel.setBackground(BG);

        contentPanel.add(new BreadCrumbPanel(1), BorderLayout.NORTH);
        contentPanel.add(createBodyPanel(), BorderLayout.CENTER);

        add(contentPanel, BorderLayout.CENTER);
    }

    private JPanel createBodyPanel() {
        JPanel bodyPanel = new JPanel(new BorderLayout());
        bodyPanel.setBackground(BG);
        bodyPanel.setBorder(BorderFactory.createEmptyBorder(28, 24, 40, 24));

        JLabel titleLabel = new JLabel(makeTitleText());
        titleLabel.setFont(new Font("SansSerif", Font.BOLD, 13));
        titleLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 20, 0));

        JPanel cardPanel = new JPanel(new BorderLayout());
        cardPanel.setBackground(CARD_BG);
        cardPanel.setBorder(BorderFactory.createEmptyBorder(42, 16, 28, 16));

        cardPanel.add(createScreenAndSeatPanel(), BorderLayout.CENTER);
        cardPanel.add(createBottomPanel(), BorderLayout.SOUTH);

        bodyPanel.add(titleLabel, BorderLayout.NORTH);
        bodyPanel.add(cardPanel, BorderLayout.CENTER);

        return bodyPanel;
    }

    private String makeTitleText() {
        return "좌석 선택 — "
                + showInfo.getMovie().getTitle()
                + " | "
                + showInfo.getTime()
                + " | "
                + showInfo.getTheater().getName();
    }

    private JPanel createScreenAndSeatPanel() {
        JPanel wrapper = new JPanel();
        wrapper.setBackground(CARD_BG);
        wrapper.setLayout(new BoxLayout(wrapper, BoxLayout.Y_AXIS));

        JLabel screen = new JLabel("S C R E E N", SwingConstants.CENTER);
        screen.setOpaque(true);
        screen.setBackground(NAVY);
        screen.setForeground(Color.WHITE);
        screen.setFont(new Font("SansSerif", Font.PLAIN, 9));
        screen.setMaximumSize(new Dimension(Integer.MAX_VALUE, 20));

        wrapper.add(screen);
        wrapper.add(Box.createVerticalStrut(28));
        wrapper.add(createSeatGridPanel());
        wrapper.add(Box.createVerticalStrut(18));
        wrapper.add(createLegendPanel());
        wrapper.add(Box.createVerticalGlue());

        return wrapper;
    }

    private JPanel createSeatGridPanel() {
        JPanel gridWrapper = new JPanel(new GridBagLayout());
        gridWrapper.setBackground(CARD_BG);

        JPanel seatPanel = new JPanel(new GridBagLayout());
        seatPanel.setBackground(CARD_BG);

        int rows = showInfo.getTheater().getRows();
        int cols = showInfo.getTheater().getCols();

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(4, 4, 4, 4);

        for (int r = 0; r < rows; r++) {
            JLabel rowLabel = new JLabel(String.valueOf((char) ('A' + r)));
            rowLabel.setFont(new Font("SansSerif", Font.PLAIN, 10));
            rowLabel.setForeground(Color.GRAY);

            gbc.gridx = 0;
            gbc.gridy = r;
            seatPanel.add(rowLabel, gbc);

            for (int c = 0; c < cols; c++) {
                String seatId = (char) ('A' + r) + String.valueOf(c + 1);

                State state = isDummyReserved(r, c) ? State.RESERVED : State.AVAILABLE;
                SeatButton seatButton = new SeatButton(seatId, state);
                seatButton.setPreferredSize(new Dimension(16, 16));

                seatButton.addActionListener(e -> toggleSeat(seatButton));

                gbc.gridx = c + 1;
                gbc.gridy = r;

                if (cols >= 8 && c == cols / 2) {
                    gbc.insets = new Insets(4, 22, 4, 4);
                } else {
                    gbc.insets = new Insets(4, 4, 4, 4);
                }

                seatPanel.add(seatButton, gbc);
            }
        }

        gridWrapper.add(seatPanel);
        return gridWrapper;
    }

    private boolean isDummyReserved(int row, int col) {
        return (row == 0 && col == 3)
                || (row == 1 && col == 0)
                || (row == 2 && col == 1)
                || (row == 3 && col == 4)
                || (row == 5 && col == 2);
    }

    private void toggleSeat(SeatButton seatButton) {
        String seatId = seatButton.getSeatId();

        if (seatButton.getState() == State.AVAILABLE) {
            seatButton.setState(State.SELECTED);
            selectedSeats.add(seatId);
        } else if (seatButton.getState() == State.SELECTED) {
            seatButton.setState(State.AVAILABLE);
            selectedSeats.remove(seatId);
        }

        updateBottomInfo();
    }

    private JPanel createLegendPanel() {
        JPanel legendPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 16, 0));
        legendPanel.setBackground(CARD_BG);

        legendPanel.add(createLegendItem(new Color(0xC9, 0xCE, 0xDD), "선택 가능"));
        legendPanel.add(createLegendItem(Color.GRAY, "선택 불가"));
        legendPanel.add(createLegendItem(NAVY, "선택됨"));

        return legendPanel;
    }

    private JPanel createLegendItem(Color color, String text) {
        JPanel item = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 0));
        item.setBackground(CARD_BG);

        JLabel box = new JLabel();
        box.setOpaque(true);
        box.setBackground(color);
        box.setPreferredSize(new Dimension(10, 10));

        JLabel label = new JLabel(text);
        label.setFont(new Font("SansSerif", Font.PLAIN, 10));
        label.setForeground(Color.GRAY);

        item.add(box);
        item.add(label);

        return item;
    }

    private JPanel createBottomPanel() {
        JPanel bottomPanel = new JPanel(new BorderLayout());
        bottomPanel.setBackground(BOTTOM_BG);
        bottomPanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(0xDD, 0xE0, 0xEA)),
                BorderFactory.createEmptyBorder(10, 14, 10, 14)
        ));

        selectedSeatLabel = new JLabel("선택 좌석: -");
        selectedSeatLabel.setFont(new Font("SansSerif", Font.PLAIN, 12));

        priceLabel = new JLabel("금액: 0원");
        priceLabel.setFont(new Font("SansSerif", Font.BOLD, 12));

        JPanel infoPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 0));
        infoPanel.setOpaque(false);
        infoPanel.add(selectedSeatLabel);
        infoPanel.add(new JLabel("|"));
        infoPanel.add(priceLabel);

        JButton nextButton = new JButton("다음 단계");
        nextButton.setBackground(NAVY);
        nextButton.setForeground(Color.WHITE);
        nextButton.setFont(new Font("SansSerif", Font.BOLD, 12));
        nextButton.setFocusPainted(false);
        nextButton.setPreferredSize(new Dimension(88, 34));

        bottomPanel.add(infoPanel, BorderLayout.WEST);
        bottomPanel.add(nextButton, BorderLayout.EAST);

        return bottomPanel;
    }

    private void updateBottomInfo() {
        if (selectedSeats.isEmpty()) {
            selectedSeatLabel.setText("선택 좌석: -");
            priceLabel.setText("금액: 0원");
            return;
        }

        int totalPrice = selectedSeats.size() * showInfo.getMovie().getBasePrice();

        selectedSeatLabel.setText("선택 좌석: " + String.join(", ", selectedSeats));
        priceLabel.setText("금액: " + String.format("%,d원", totalPrice));
    }
}
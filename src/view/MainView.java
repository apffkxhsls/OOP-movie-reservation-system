package view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.Dimension;
import java.awt.Font;

import javax.swing.*;

import model.DummyData;
import model.Movie;
import view.component.HeaderPanel;
import view.component.StyledButton;
import view.component.BreadCrumbPanel;
import view.listener.MainViewListener;

public class MainView extends JFrame {

    private MainViewListener listener;
    private static final Color BACKGROUND = new Color(230, 230, 230);

    public MainView() {
        setTitle("Main");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1040, 680);
        setLocationRelativeTo(null);

        HeaderPanel header = new HeaderPanel("예매내역 조회");
        header.addRightButtonListener(e -> {
            if (listener != null) {
                listener.onHistoryButtonClicked();
            }
        });
        add(header, BorderLayout.NORTH);
        add(createContentPanel(), BorderLayout.CENTER); // 메서드 호출
    }

    private JPanel createContentPanel() {
        JPanel contentPanel = new JPanel(new BorderLayout());
        contentPanel.setBackground(BACKGROUND);

        // 브레드크럼 + 검색창을 하나의 topPanel로 묶기
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setBackground(new Color(0xF4, 0xF4, 0xF6));
        topPanel.add(new BreadCrumbPanel(0), BorderLayout.CENTER);

        JTextField searchField = new JTextField("영화 검색....", 14);
        searchField.setForeground(Color.GRAY);

        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 6));
        searchPanel.setBackground(new Color(0xF4, 0xF4, 0xF6));
        searchPanel.add(searchField);

        topPanel.add(searchPanel, BorderLayout.EAST);
        contentPanel.add(topPanel, BorderLayout.NORTH); // 하나의 NORTH에 묶어서 추가

        // 영화 목록 전체 영역
        JPanel bodyPanel = new JPanel(new BorderLayout());
        bodyPanel.setBorder(BorderFactory.createEmptyBorder(30, 60, 30, 60));

        // 섹션 제목
        JLabel sectionTitle = new JLabel("현재 상영 중");
        sectionTitle.setFont(new Font("SansSerif", Font.BOLD, 20));
        sectionTitle.setBorder(BorderFactory.createEmptyBorder(0, 0, 20, 0));
        bodyPanel.add(sectionTitle, BorderLayout.NORTH);

        // 영화 목록을 담는 패널
        JPanel moviePanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 25, 0));

        // 영화 카드 생성
        for (Movie movie : DummyData.getMovies()) {
            JPanel card = new JPanel(new BorderLayout());
            card.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));
            card.setPreferredSize(new Dimension(220, 300));
            card.setBackground(Color.WHITE);

            // 포스터 영역
            JLabel poster = new JLabel("POSTER", SwingConstants.CENTER);
            poster.setOpaque(true);
            poster.setBackground(Color.LIGHT_GRAY);
            poster.setPreferredSize(new Dimension(180, 150));
            card.add(poster, BorderLayout.NORTH);

            // 영화 정보 영역
            JPanel infoPanel = new JPanel();
            infoPanel.setLayout(new BoxLayout(infoPanel, BoxLayout.Y_AXIS));
            infoPanel.setBackground(Color.WHITE);

            JLabel title = new JLabel(movie.getTitle(), SwingConstants.CENTER);
            title.setFont(new Font("SansSerif", Font.BOLD, 14));
            title.setAlignmentX(CENTER_ALIGNMENT);

            JLabel info = new JLabel(
                "<html><center>"
                + movie.getGenre() + " / " + movie.getDurationMinutes() + "분<br>"
                + "관람등급: " + movie.getAgeRating()
                + "</center></html>",
                SwingConstants.CENTER
            );
            info.setFont(new Font("SansSerif", Font.PLAIN, 12));
            info.setAlignmentX(Component.CENTER_ALIGNMENT);

            infoPanel.add(Box.createVerticalStrut(12));
            infoPanel.add(title);
            infoPanel.add(Box.createVerticalStrut(8));
            infoPanel.add(info);
            infoPanel.add(Box.createVerticalGlue());

            card.add(infoPanel, BorderLayout.CENTER);

            // 예매 버튼
            JButton bookButton = StyledButton.navy("예매하기");
            bookButton.addActionListener(e -> {
                if (listener != null) {
                    listener.onMovieSelected(movie);
                }
            });
            card.add(bookButton, BorderLayout.SOUTH);

            moviePanel.add(card);
        }

        bodyPanel.add(moviePanel, BorderLayout.CENTER);
        contentPanel.add(bodyPanel, BorderLayout.CENTER);

        return contentPanel; // 반환
    }

    public void setListener(MainViewListener listener) {
        this.listener = listener;
    }
}
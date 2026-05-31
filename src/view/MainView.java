package view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Dimension;
import java.awt.Font;

import javax.swing.*;

import model.DummyData;
import model.Movie;
import view.component.HeaderPanel;
import view.component.BreadCrumbPanel;

public class MainView extends JFrame {
    
    public MainView() {
        setTitle("Main");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1040, 680);
        setLocationRelativeTo(null);

        HeaderPanel header = new HeaderPanel("예매내역 조회");
        add(header, BorderLayout.NORTH);

        // 헤더 아래에 들어갈 전체 영역
        JPanel contentPanel = new JPanel(new BorderLayout());

        // 브레드크럼 + 검색창 영역
        JPanel topPanel = new JPanel(new BorderLayout());
        
        BreadCrumbPanel breadCrumb = new BreadCrumbPanel(0);
        topPanel.add(breadCrumb, BorderLayout.CENTER);

        // 검색창
        JTextField searchField = new JTextField("영화 검색....", 14);
        searchField.setForeground(Color.GRAY);

        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 6));
        searchPanel.setBackground(new Color(0xF4, 0xF4, 0xF6));
        searchPanel.add(searchField);

        topPanel.add(searchPanel, BorderLayout.EAST);
        contentPanel.add(topPanel, BorderLayout.NORTH);
        
        // 영화 목록을 담는 패널
        JPanel moviePanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 25, 30));
        moviePanel.setBorder(BorderFactory.createEmptyBorder(30, 60, 30, 40));

        // 영화 카드 생성
        for (Movie movie : DummyData.getMovies()) {
            JPanel card = new JPanel(new BorderLayout());
            card.setBorder(BorderFactory.createLineBorder(Color.GRAY));
            card.setPreferredSize(new Dimension(220, 280));

            // 포스터 영역
            JLabel poster = new JLabel("POSTER", SwingConstants.CENTER);
            poster.setOpaque(true);
            poster.setBackground(Color.LIGHT_GRAY);
            poster.setPreferredSize(new Dimension(180, 150));
            card.add(poster, BorderLayout.CENTER);

            // 영화 제목
            JLabel title = new JLabel(movie.getTitle(), SwingConstants.CENTER);
            title.setFont(new Font("SansSerif", Font.BOLD, 14));
            card.add(title, BorderLayout.SOUTH);

            moviePanel.add(card);
        }

        contentPanel.add(moviePanel, BorderLayout.CENTER);
        add(contentPanel, BorderLayout.CENTER);
    }

}
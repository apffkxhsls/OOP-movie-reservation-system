package view;

import java.awt.BorderLayout;

import javax.swing.*;

import view.component.HeaderPanel;

public class MainView extends JFrame {
    
    public MainView() {
        setTitle("Main");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1040, 680);
        setLocationRelativeTo(null);

        HeaderPanel header = new HeaderPanel("예매내역 조회");
        add(header, BorderLayout.NORTH);
    }

}
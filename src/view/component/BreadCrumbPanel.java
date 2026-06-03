package view.component;

import javax.swing.*;
import java.awt.*;

// 상단 화면 이동 경로 표시 패널
public class BreadCrumbPanel extends JPanel {

    private static final Color BG = new Color(0xF4, 0xF4, 0xF6);
    private static final Color ACTIVE = new Color(0x1A, 0x1A, 0x2E);
    private static final Color INACTIVE = new Color(0x99, 0x99, 0xAA);
    
    // 글씨
    private static final Font ACTIVE_FONT = new Font("SansSerif", Font.BOLD, 13);
    private static final Font INACTIVE_FONT = new Font("SansSerif", Font.PLAIN, 13);

    private static final String[] STEPS = {"영화 선택", "좌석 선택", "결제", "예매 내역 조회"};
    
    // activeStep: 0=영화선택, 1=좌석선택, 2=결제, 3=예매내역조회
    public BreadCrumbPanel(int activeStep) {
        setBackground(BG);
        setPreferredSize(new Dimension(0, 40));
        setLayout(new FlowLayout(FlowLayout.LEFT, 0, 11));
        setBorder(BorderFactory.createEmptyBorder(0, 32, 0, 0));

        for (int i = 0; i < STEPS.length; i++) {
            JLabel label = new JLabel(STEPS[i]);

            // 현재 단계는 굵은 글씨, 나머지는 일반 글씨
            label.setFont(i == activeStep ? ACTIVE_FONT : INACTIVE_FONT);
            label.setForeground(i == activeStep ? ACTIVE : INACTIVE);
            add(label);

            if (i < STEPS.length - 1) {
                JLabel separator = new JLabel(" > ");
                separator.setFont(INACTIVE_FONT);
                separator.setForeground(INACTIVE);
                add(separator);
            }
        }
    }
<<<<<<< HEAD
}
=======
}
>>>>>>> c1cc2c1d24d87f29906ad274be79dd772d4062e5

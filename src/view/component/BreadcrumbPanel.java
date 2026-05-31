package view.component;

import javax.swing.*;
import java.awt.*;

// 상단 breadcrumb 네비게이션 바
public class BreadCrumbPanel extends JPanel {

    private static final Color BG       = new Color(0xF4, 0xF4, 0xF6);
    private static final Color ACTIVE   = new Color(0x1A, 0x1A, 0x2E);
    private static final Color INACTIVE = new Color(0x99, 0x99, 0xAA);
    private static final Font  FONT     = new Font("SansSerif", Font.PLAIN, 12);

    private static final String[] STEPS = {"영화 선택", "좌석 선택", "결제", "예매 내역 조회"};
    
    // activeStep: 0=영화선택, 1=좌석선택, 2=결제, 3=예매내역조회
    public BreadCrumbPanel(int activeStep) {
        setBackground(BG);
        setPreferredSize(new Dimension(0, 36));
        setLayout(new FlowLayout(FlowLayout.LEFT, 0, 0));
        setBorder(BorderFactory.createEmptyBorder(0, 24, 0, 0));

        for (int i = 0; i < STEPS.length; i++) {
            JLabel lbl = new JLabel(STEPS[i]);
            lbl.setFont(FONT);
            lbl.setForeground(i == activeStep ? ACTIVE : INACTIVE);
            add(lbl);
            if (i < STEPS.length - 1) {
                JLabel sep = new JLabel(" > ");
                sep.setFont(FONT);
                sep.setForeground(INACTIVE);
                add(sep);
            }
        }
    }
}

package view.component;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

// 모든 화면 공통 상단 헤더
// rightButtonText가 null이면 우측 버튼 미표시
public class HeaderPanel extends JPanel {
    public static final Color NAVY      = new Color(0x2B, 0x35, 0x58);
    public static final Color BTN_BG    = new Color(0xFF, 0xFF, 0xFF);
    public static final Color BTN_FG    = new Color(0x2B, 0x35, 0x58);
    public static final Font  LOGO_FONT = new Font("SansSerif", Font.BOLD, 22);
    public static final Font  BTN_FONT  = new Font("SansSerif", Font.PLAIN, 13);

    private final JButton rightButton;

    public HeaderPanel(String rightButtonText) {
        setBackground(NAVY);
        setPreferredSize(new Dimension(0, 64));
        setLayout(new BorderLayout());
        setBorder(BorderFactory.createEmptyBorder(12, 24, 0, 24));

        JLabel logo = new JLabel("SongBox");
        logo.setFont(LOGO_FONT);
        logo.setForeground(Color.WHITE);
        add(logo, BorderLayout.WEST);

        if (rightButtonText != null && !rightButtonText.isBlank()) {
            rightButton = new JButton(rightButtonText);
            rightButton.setFont(BTN_FONT);
            rightButton.setBackground(BTN_BG);
            rightButton.setForeground(BTN_FG);
            rightButton.setFocusPainted(false);
            rightButton.setPreferredSize(new Dimension(130, 40));
            rightButton.setBorder(BorderFactory.createEmptyBorder(6, 16, 6, 16));
            rightButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
            JPanel wrap = new JPanel(new FlowLayout(FlowLayout.RIGHT, 0, 0));
            wrap.setOpaque(false);
            wrap.add(rightButton);
            add(wrap, BorderLayout.EAST);
        } else {
            rightButton = null;
        }
    }

    public void addRightButtonListener(ActionListener l) {
        if (rightButton != null) rightButton.addActionListener(l);
    }
}
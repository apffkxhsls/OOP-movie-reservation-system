package view.component;

import javax.swing.*;
import java.awt.*;

public class StyledButton extends JButton {

    // NAVY 색상은 HeaderPanel에서 참조해서 통일
    private static final Color NAVY = HeaderPanel.NAVY;

    // 기본 사이즈 (BookingConfirmView 기준 92x32)
    public StyledButton(String text, Color bg, Color fg) {
        this(text, bg, fg, 92, 32);
    }

    // 사이즈 직접 지정 (BookingHistoryView처럼 버튼마다 크기가 다를 때)
    public StyledButton(String text, Color bg, Color fg, int width, int height) {
        super(text);
        setPreferredSize(new Dimension(width, height));
        setBackground(bg);
        setForeground(fg);
        setFocusPainted(false);
        setBorderPainted(false);
        setFont(new Font("맑은 고딕", Font.BOLD, 12));
        setVerticalAlignment(SwingConstants.CENTER);
        setHorizontalAlignment(SwingConstants.CENTER);
        setMargin(new Insets(0, 0, 0, 0));
    }

    // 자주 쓰는 네이비 + 흰 글씨 조합
    public static StyledButton navy(String text) {
        return new StyledButton(text, NAVY, Color.WHITE);
    }

    public static StyledButton navy(String text, int width, int height) {
        return new StyledButton(text, NAVY, Color.WHITE, width, height);
    }

    // 자주 쓰는 흰 배경 + 네이비 글씨 조합
    public static StyledButton outline(String text) {
        return new StyledButton(text, Color.WHITE, NAVY);
    }

    public static StyledButton outline(String text, int width, int height) {
        return new StyledButton(text, Color.WHITE, NAVY, width, height);
    }
}
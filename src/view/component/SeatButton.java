package view.component;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

// 좌석 1개를 표현하는 커스텀 버튼
public class SeatButton extends JButton {
    public enum State { AVAILABLE, RESERVED, SELECTED }

    // 상태: AVAILABLE, RESERVED, SELECTED
    private static final Color C_AVAILABLE = Color.LIGHT_GRAY;
    private static final Color C_RESERVED = Color.GRAY;
    private static final Color C_SELECTED = Color.BLUE;
    private static final Color C_HOVER = Color.CYAN;

    private State state;
    private final String seatId;

    public SeatButton(String seatId, State initialState) {
        this.seatId = seatId;
        setText("");
        setPreferredSize(new Dimension(28,28));
        setFocusPainted(false);
        setBorderPainted(false);
        setContentAreaFilled(false);
        setOpaque(true);
        setState(initialState);

        addMouseListener(new MouseAdapter() {
            @Override public void mouseEntered(MouseEvent e) {
                if (state == State.AVAILABLE) setBackground(C_HOVER);
            }
            @Override public void mouseExited(MouseEvent e) {
                if (state == State.AVAILABLE) setBackground(C_AVAILABLE);
            }
        });
    }

    private void setState(State s) {
        this.state = s;
        switch (s) {
            case AVAILABLE -> { setBackground((C_AVAILABLE)); setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR)); setEnabled(true); }
            case RESERVED -> { setBackground(C_RESERVED); setCursor(Cursor.getDefaultCursor()); setEnabled(false);}
            case SELECTED -> { setBackground(C_SELECTED); setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR)); setEnabled(true);}
        }
        repaint();
    }

    public State getState() { return state; }
    public String getSeatId() { return seatId; }

    @Override protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setColor(getBackground());
        g2.fillRoundRect(0, 0, getWidth(), getHeight(), 6, 6);
        g2.dispose();
    }
}
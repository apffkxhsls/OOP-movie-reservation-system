package controller1;

import javax.swing.JOptionPane;

// 실제 Swing 기술을 사용해 화면에 팝업을 띄우는 구현체 (Adapter 패턴)
public class SwingUIProvider implements UIProvider {
    @Override
    public void showInfoMessage(String message, String title) {
        JOptionPane.showMessageDialog(null, message, title, JOptionPane.INFORMATION_MESSAGE);
    }

    @Override
    public void showWarningMessage(String message, String title) {
        JOptionPane.showMessageDialog(null, message, title, JOptionPane.WARNING_MESSAGE);
    }

    @Override
    public void showErrorMessage(String message, String title) {
        JOptionPane.showMessageDialog(null, message, title, JOptionPane.ERROR_MESSAGE);
    }
}
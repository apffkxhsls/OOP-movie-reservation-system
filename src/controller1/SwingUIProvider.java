package controller1;

import javax.swing.JOptionPane;

// JOptionPane 클래스를 활용하여 데스크톱 환경에서 Swing 기반의 알림 창(팝업)을 제공하는 역할을 하는 UI 구현체
public class SwingUIProvider implements UIProvider {

    // JOptionPane.INFORMATION_MESSAGE 옵션을 통해 사용자 화면 중앙에 정보 대화상자를 출력
    @Override
    public void showInfoMessage(String message, String title) {
        JOptionPane.showMessageDialog(null, message, title, JOptionPane.INFORMATION_MESSAGE);
    }

    // JOptionPane.WARNING_MESSAGE 옵션을 통해 사용자 화면 중앙에 경고 대화상자를 출력
    @Override
    public void showWarningMessage(String message, String title) {
        JOptionPane.showMessageDialog(null, message, title, JOptionPane.WARNING_MESSAGE);
    }

    // JOptionPane.ERROR_MESSAGE 옵션을 통해 사용자 화면 중앙에 오류 대화상자를 출력
    @Override
    public void showErrorMessage(String message, String title) {
        JOptionPane.showMessageDialog(null, message, title, JOptionPane.ERROR_MESSAGE);
    }
}
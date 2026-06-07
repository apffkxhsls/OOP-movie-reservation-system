package controller1;

// 컨트롤러가 UI 기술(Swing 등)에 종속되지 않도록 분리하는 인터페이스 (Adapter 패턴)
public interface UIProvider {
    void showInfoMessage(String message, String title);

    void showWarningMessage(String message, String title);

    void showErrorMessage(String message, String title);
}
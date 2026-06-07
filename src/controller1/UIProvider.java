package controller1;

// 다양한 메시지(정보, 경고, 오류) 출력을 통해 사용자에게 시스템 상태 알림을 제공하는 역할을 하는 UI 인터페이스
public interface UIProvider {

    // 사용자에게 일반적인 안내 및 확인 정보를 전달하기 위한 메시지 출력
    void showInfoMessage(String message, String title);

    // 사용자에게 주의가 필요한 상황(예: 중복 선택, 예매 불가 등)을 알리기 위한 경고 메시지 출력
    void showWarningMessage(String message, String title);

    // 시스템 오류나 잘못된 조작 등 정상적으로 진행할 수 없는 상태를 알리기 위한 에러 메시지 출력
    void showErrorMessage(String message, String title);
}
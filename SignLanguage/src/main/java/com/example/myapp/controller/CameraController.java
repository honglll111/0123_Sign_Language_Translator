package com.example.myapp.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CameraController {

    @GetMapping("/api/check-camera")
    public boolean checkCamera() {
        // 여기서는 간단히 연결 여부를 boolean 값으로 반환합니다.
        // 실제로는 주피터 노트북에서 데이터를 받아 확인하거나 로컬 장치 상태를 확인할 수 있습니다.
        boolean isCameraConnected = checkCameraConnection();
        return isCameraConnected;
    }

    private boolean checkCameraConnection() {
        // 실제로는 카메라 연결 상태를 확인하는 로직이 필요합니다.
        // 예시로는 항상 false로 설정하여 카메라가 연결되지 않은 상황을 시뮬레이션합니다.
        return false;
    }
}

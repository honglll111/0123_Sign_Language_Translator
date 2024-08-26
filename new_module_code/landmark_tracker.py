import time

class LandmarkTracker:
    def __init__(self):
        self.last_landmark_time = time.time()
        self.timeout_seconds = 10

    def dispose(self, results):
        current_time = time.time()

        no_landmarks_detected = (
            results.pose_landmarks is None and
            results.left_hand_landmarks is None and
            results.right_hand_landmarks is None
        )

        pose_only_detected = (
            results.pose_landmarks and
            not (results.left_hand_landmarks and results.right_hand_landmarks)
        )

        if no_landmarks_detected or pose_only_detected:
            if current_time - self.last_landmark_time > self.timeout_seconds:
                return True  # 10초 경과 시 종료
        else:
            self.last_landmark_time = current_time  # 랜드마크가 감지되면 타이머 리셋

        return False  # 계속 처리

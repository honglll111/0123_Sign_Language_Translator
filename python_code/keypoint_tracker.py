import time

class LandmarkTracker:
    def __init__(self):
        self.last_landmark_time = time.time()
        self.timeout_seconds = 7

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
                return True
        else:
            self.last_landmark_time = current_time

        return False
